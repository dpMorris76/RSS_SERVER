package com.centurylink.rss.web.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.centurylink.rss.business.service.ChannelDS;
import com.centurylink.rss.business.service.ChannelGroupDS;
import com.centurylink.rss.business.service.DataService;
import com.centurylink.rss.business.service.EmailService;
import com.centurylink.rss.business.service.JDBCUtil;
import com.centurylink.rss.business.service.RSSWritingService;
import com.centurylink.rss.business.service.StoryDS;
import com.centurylink.rss.business.service.UserDS;
import com.centurylink.rss.domain.entity.Channel;
import com.centurylink.rss.domain.entity.ChannelGroup;
import com.centurylink.rss.domain.entity.ExpiredStory;
import com.centurylink.rss.domain.entity.Permission;
import com.centurylink.rss.domain.entity.Story;
import com.centurylink.rss.domain.entity.User;
import com.centurylink.rss.schedule.controller.ChannelCrons;
import com.centurylink.rss.web.controller.util.BaseController;
import com.centurylink.rss.web.form.ContentGridForm;
import com.centurylink.rss.web.form.StoryForm;
import com.centurylink.rss.web.form.StoryReviewForm;
import com.centurylink.rss.web.service.util.UserState;

@Controller
public class ContentPageController extends BaseController {

	@Autowired
	EmailService emailsvc;

	@Autowired
	UserDS userService;
	
	@Autowired
	JDBCUtil jdbcUtil;

	@Autowired
	@Qualifier("xmlPathProperties")
	Properties filePaths;

	@Autowired
	RSSWritingService rws;

	@Autowired
	ChannelCrons channelCrons;
	
	@Autowired
	DataService dataService;
	
	@Autowired
	StoryDS storyS;
	
	@Autowired
	ChannelDS channelS;
	
	@Autowired
	ChannelGroupDS channelGroupS;
	
	private static final Logger logger = Logger.getLogger(ContentPageController.class);
	// RSS pages
	@RequestMapping("/secure/contentHome")
	public ModelAndView contentHomeView(HttpServletRequest request, ModelMap modelMap) {

		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");

		return new ModelAndView("contentHome");
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/secure/submissionForm")
	public ModelAndView submissionFormView(HttpServletRequest request, ModelMap modelMap) {
		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		UserState userState = (UserState) request.getSession().getAttribute("userState");
		User currentUser = userService.findUser(userState.getUser().getId());

		StoryForm storyForm = new StoryForm();
		modelMap.put("StoryForm", storyForm);

		Long currGroupId = currentUser.getAssocGroupId();
		List<ChannelGroup> groups = new ArrayList<ChannelGroup>();
		List<Channel> channels = new ArrayList<Channel>();
		
		if (currGroupId != null) {
			logger.debug("Finding associated Channel Group for user " + currentUser.getId() + " to post to");
			ChannelGroup assocGroup = channelGroupS.findById(currGroupId);
//			groups.add(assocGroup);
			logger.debug("Finding channels user can publish to");
//			channels.addAll(assocGroup.getAlphaBetaChannels());
			channels.addAll(assocGroup.getChannels());
		} else {
			logger.warn("No associated channel group for user " + currentUser.getId() + " to post to, returning null");
			groups = null;
		}
//		make the admin feed postable to. 
		if(currentUser.getPermissions().contains(Permission.findPermission(Permission.CHANNEL_ADMINISTRATION_ID)) 
				|| currentUser.getPermissions().contains(Permission.findPermission(Permission.CHANNEL_GROUP_ADMINISTRATION_ID)) 
				|| currentUser.getPermissions().contains(Permission.findPermission(Permission.USER_ADMINISTRATION_ID)))
		{
			// then they can post to the admin channel.
			channels.add(Channel.findById(Channel.ADMIN_CHANNEL_ID));
		}
		
		Collections.sort(channels);
		modelMap.put("Channels", channels);
		
		boolean canApprove = false;
		ChannelGroup group = null;
		if(currGroupId != null)
		{
			group = channelGroupS.findById(currGroupId);
			if (currentUser.getPermissions().contains(dataService.findPermissionByName(Permission.CONTENT_REVIEW)) && group.getContentGatekeepers().contains(currentUser)) 
			{
				canApprove = true;
			}
		}
		modelMap.addAttribute("canApprove", canApprove);

		return new ModelAndView("submissionForm");
	}

	// adding content submit leads to this.
	@RequestMapping(value = "/secure/updateStory", method = RequestMethod.POST)
	public ModelAndView submit(ModelMap modelMap, HttpServletRequest request, @ModelAttribute StoryForm storyForm,
			BindingResult result) {
		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");

		Story s = null;
		
		if (storyForm.getStoryId() == null) {
			// this means that we are making a NEW story.
			UserState userState = (UserState) request.getSession().getAttribute("userState");
			User user = userService.findUser(userState.getUser().getId());
			storyForm.setStoryAuthorEmail(user.getEmail());
			String path = filePaths.getProperty("deployUrl");
			String deployPath = path + "/RSS";
			if(dataService == null)
			{
				logger.error("DATASERVICE IS NULL");
			}
			s = storyS.saveNewFromForm(storyForm, deployPath);
			seekingApprovalStatusEmail(s);
		} else {
			// this means that we are updating a story.
			s = storyS.findById(storyForm.getStoryId());
			if(s.getApprovalStatus().equals(Story.REJECTED_STATUS))
			{ 
				// send reapproval email because we changed it
				sendReapprovalStatusEmail(s);
			}
			else if(s.getApprovalStatus().equals(Story.APPROVED_STATUS))
			{ // send normal approval email because we changed it b4 publish date
				seekingApprovalStatusEmail(s);
				s.setApprovedBy(null);
				storyS.save(s);
			}
			storyS.updateFromForm(storyForm);
		}

		if (s.getApprovalStatus().equals(Story.APPROVED_STATUS)) {
			channelCrons.writeApprovedStory(s);
		}
		return new ModelAndView("redirect:/secure/submissionGrid");
	}

	// TODO add scheduling to updateStoryAndApprove
	@RequestMapping("/secure/updateStoryAndApprove")
	public ModelAndView submitAndApproveView(HttpServletRequest request, ModelMap modelMap,
			@ModelAttribute StoryForm storyForm) {
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		
		Set<Channel> channelsToWrite = new HashSet<Channel>();
		channelsToWrite.add(channelS.findById(storyForm.getChannelId()));
		UserState userState = (UserState) request.getSession().getAttribute("userState");
		User user = userService.findUser(userState.getUser().getId());
		
		if (storyForm.getStoryId() == null) {
			storyForm.setStoryAuthorEmail(user.getEmail());
			channelCrons.writeApprovedStory(storyS.saveAndApproveNewFromForm(storyForm, user.getFullName()));
			// Story.saveAndApproveNewStoryFromForm does not return a story with an id
			// however writeApprovedStory only needs the channels set, not the id, so this works correctly.
		} else {
			Story s = storyS.findById(storyForm.getStoryId());
			channelsToWrite.addAll(s.getChannels());
			storyS.updateFromForm(storyForm);
			storyS.approveSubmission(s.getId(), user); 
			channelCrons.writeApprovedStory(s);
		}
		
		return new ModelAndView("redirect:/secure/submissionGrid");
	}

	@RequestMapping("/secure/submissionGrid")
	public ModelAndView submissionGridView(HttpServletRequest request, ModelMap modelMap) {

		UserState userState = (UserState) request.getSession().getAttribute("userState");
		User user = userService.findUser(userState.getUser().getId());
		if (user.getPermissions().contains(dataService.findPermissionByName(Permission.CHANNEL_GROUP_ADMINISTRATION))) {
			List<Channel> channels = channelS.findAll();
			channels.remove(channelS.findById(Channel.ADMIN_CHANNEL_ID));
			modelMap.addAttribute("Channels", channels);
		} else {
			Set<ChannelGroup> groups = user.getUserGroupsToGatekeep();
			groups.addAll(user.getUserGroupsToProvideContent());
			Set<Channel> channels = new HashSet<Channel>();
			for (ChannelGroup g : groups) {
				channels.addAll(g.getChannels());
			}
			modelMap.addAttribute("Channels", channels);
		}

		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");

		modelMap.addAttribute("storyGridForm", new ContentGridForm());

		return new ModelAndView("submissionGrid");
	}

	// is this unfinished? something seems fishy here, as in it probably isn't working. 
	// TODO get to the bottom of this.
	@SuppressWarnings("unchecked")
	@RequestMapping("/secure/updateGrid")
	public ModelAndView updateSubmissionGridView(HttpServletRequest request, ModelMap modelMap,
			@ModelAttribute ContentGridForm storyGridForm, @RequestParam String selection) {
		UserState userState = (UserState) request.getSession().getAttribute("userState");
		User user = userService.findUser(userState.getUser().getId());
		modelMap.addAttribute("currentUser", user);
		Set<ChannelGroup> groups = new HashSet<ChannelGroup>();
		if(user.getAssocGroupId() == null)
		{
			// don't add that to groups. 
			// because they don't have a group.
		}
		else
		{
			groups.add(channelGroupS.findById(user.getAssocGroupId()));
		}
		Set<Story> storiesUserCanReview = new HashSet<Story>();
		for (ChannelGroup g : groups) {
			for (Channel c : g.getChannels()) {
				storiesUserCanReview.addAll(c.getStories());
			}
		}
		// if they are a reviewer and an admin, then they can review the admin stories.
		if(User.isAdministrator(user)){
			storiesUserCanReview.addAll(channelS.findById(Channel.ADMIN_CHANNEL_ID).getStories());
		}
		
		// we have to turn this into a list afterwards, or else we'll get duplicates
		List<Story> temp = new ArrayList<Story>();
		temp.addAll(storiesUserCanReview);
		modelMap.addAttribute("rejectedStatus", Story.REJECTED_STATUS);
		switch (selection) {
		case ContentGridForm.NO_SUBMISSIONS:
			// Do Nothing
			break;
		case ContentGridForm.PENDING_SUBMISSIONS:
			List<Story> pendingSubmissions = new ArrayList<Story>();
			// because a user can only see items that they can review.
			for(Story s : storiesUserCanReview)
			{
				if(s.getApprovalStatus().equals(Story.PENDING_STATUS))
				{
					pendingSubmissions.add(s);
				}
			}
			Collections.sort(pendingSubmissions);
			modelMap.addAttribute("storySubmissions", pendingSubmissions);
			break;
		case ContentGridForm.APPROVED_SUBMISSIONS:
			List<Story> approvedSubmissions = new ArrayList<Story>();
			for(Story s : storiesUserCanReview)
			{
				if(s.getApprovalStatus().equals(Story.APPROVED_STATUS))
				{
					approvedSubmissions.add(s);
				}
			}
			Collections.sort(approvedSubmissions);
			modelMap.addAttribute("storySubmissions", approvedSubmissions);
			break;
		case ContentGridForm.REJECTED_SUBMISSIONS:
			List<Story> rejectedSubmissions = new ArrayList<Story>();
			for(Story s : storiesUserCanReview)
			{
				if(s.getApprovalStatus().equals(Story.REJECTED_STATUS))
				{
					rejectedSubmissions.add(s);
				}
			}
			Collections.sort(rejectedSubmissions);
			modelMap.addAttribute("storySubmissions", rejectedSubmissions);
			break;
		case ContentGridForm.ALL_SUBMISSIONS:
			modelMap.addAttribute("storySubmissions", storiesUserCanReview);
			break;
		default:
			// Do Nothing
			break;
		}
		return new ModelAndView("submissionGrid");
	}

	@RequestMapping("/secure/denySubmission")
	public ModelAndView submissionRejectView(HttpServletRequest request, ModelMap modelMap,
			@ModelAttribute("storyIdForm") StoryReviewForm srf) {
		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		Story s = storyS.denySubmission(srf.getId(), srf.getDenialReasons());
		modelMap.addAttribute("author", s.getAuthor());
		sendApprovalStatusEmail(s);
		return new ModelAndView("submissionActionSummary");
	}

	@RequestMapping("/secure/approveSubmission")
	public ModelAndView sumbissionApprovalView(HttpServletRequest request, ModelMap modelMap,
			@ModelAttribute("storyIdForm") StoryReviewForm srf) {
		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		UserState userState = (UserState) request.getSession().getAttribute("userState");
		User currentUser = userState.getUser();
		modelMap.addAttribute("currentUser", currentUser);
		Story s = storyS.approveSubmission(srf.getId(), currentUser);
		modelMap.addAttribute("author", s.getAuthor());
		sendApprovalStatusEmail(s);
		channelCrons.writeApprovedStory(s);
		
		return new ModelAndView("submissionActionSummary");
	}
	
	@RequestMapping("/secure/submissionReview")
	public ModelAndView submissionReviewView(HttpServletRequest request, ModelMap modelMap) {
//		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		logger.debug("submissionReview");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");

		String storyId = request.getParameter("storyId");
		Long id = Long.parseLong(storyId);
		Story storyReview = storyS.findById(id);
		modelMap.addAttribute("story", storyReview);
		modelMap.addAttribute("storyIdForm", new StoryReviewForm(id));
		
		String channels = "";
		int i = 1;
		for (Channel c : storyReview.getChannels()) {
			channels += c.getTitle();
			if (i != storyReview.getChannels().size())
				channels += ", ";
			i++;
		}
		modelMap.addAttribute("channels", channels);

		UserState userState = (UserState) request.getSession().getAttribute("userState");
		User user = userService.findUser(userState.getUser().getId());
		
		boolean canReview = false;
		if (user.getPermissions().contains(dataService.findPermissionByName(Permission.CONTENT_REVIEW))) {
			Set<Channel> channelStories = storyReview.getChannels();
			for (ChannelGroup g : user.getUserGroupsToGatekeep()) {
				for (Channel c : channelStories) {
					if(c.getChannelGroup() != null){
						canReview = c.getChannelGroup().equals(g);
						if (canReview) {
							break;
						}
					}
					else
					{
						if(User.isAdministrator(user)){
							canReview = true;
							break;
						}
					}
				}
				if (canReview) {
					break;
				}
			}
		}
		
		modelMap.addAttribute("today", new Date()); // we need this, because things which are both published & approved cannot be denied.
		modelMap.addAttribute("canReview", canReview);
		modelMap.addAttribute("canEdit", user.getEmail().equals(storyReview.getAuthor()));

		return new ModelAndView("submissionReview");
	}
	
	@RequestMapping("/secure/submissionReviewExpired")
	public ModelAndView submissionReviewExpiredView(HttpServletRequest request, ModelMap modelMap) {
//		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		logger.debug("submissionReview");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");

		String storyId = request.getParameter("expiredStoryId");
		Long id = Long.parseLong(storyId);
		Story storyReview = storyS.createStoryFromExpiredStory(id);
		modelMap.addAttribute("story", storyReview);
		modelMap.addAttribute("storyIdForm", new StoryReviewForm(id));
		
		String channels = "";
		int i = 1;
		for (Channel c : storyReview.getChannels()) {
			channels += c.getTitle();
			if (i != storyReview.getChannels().size())
				channels += ", ";
			i++;
		}
		modelMap.addAttribute("channels", channels);

		UserState userState = (UserState) request.getSession().getAttribute("userState");
		User user = userService.findUser(userState.getUser().getId());
		
		boolean canReview = false;
		if (user.getPermissions().contains(dataService.findPermissionByName(Permission.CONTENT_REVIEW))) {
			Set<Channel> channelStories = storyReview.getChannels();
			for (ChannelGroup g : user.getUserGroupsToGatekeep()) {
				for (Channel c : channelStories) {
					if(c.getChannelGroup() != null){
						canReview = c.getChannelGroup().equals(g);
						if (canReview) {
							break;
						}
					}
					else
					{
						if(User.isAdministrator(user)){
							canReview = true;
							break;
						}
					}
				}
				if (canReview) {
					break;
				}
			}
		}
		
		modelMap.addAttribute("today", new Date()); // we need this, because things which are both published & approved cannot be denied.
		modelMap.addAttribute("canReview", canReview);
		modelMap.addAttribute("canEdit", user.getEmail().equals(storyReview.getAuthor()));

		return new ModelAndView("submissionReviewExpired");
	}
	
	@RequestMapping("/secure/unexpireStory")
	public ModelAndView unexpireStory(HttpServletRequest request, ModelMap modelMap,
			@ModelAttribute("storyIdForm") StoryReviewForm srf) {
		logger.info("Unexpiring Submission");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		Long id = srf.getId();
		Story story = storyS.createStoryFromExpiredStory(id);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());            
		calendar.add(Calendar.DAY_OF_YEAR, 14);
		Date newExpireDate = calendar.getTime();
		story.setExpirationDate(newExpireDate);
		storyS.save(story);
		
		storyS.deleteExpiredStory(storyS.findExpiredStoryById(id));
		
		for(Channel c: story.getChannels()){
			logger.debug("Channel: " + c);
			try{
				rws.writeChannel(c);
			}catch(Exception e){
				logger.error("Error writing channel after expiring stories: " + c.getId());
				logger.error(e);
				e.printStackTrace();
			}
		}

		StoryForm sf = new StoryForm(story);
		// might need to mess with this.
		sf.setStoryId(story.getId());
		sf.setApprovedBy(story.getApprovedBy());
		// need to do this, so that on re-submit we can tell that we're
		// using a new story.
		modelMap.addAttribute("StoryForm", sf);

		UserState userState = (UserState) request.getSession().getAttribute("userState");
		User currentUser = userService.findUser(userState.getUser().getId());
		modelMap.addAttribute("currentUser", currentUser);

		Long currGroupId = currentUser.getAssocGroupId();
		List<ChannelGroup> groups;
		groups = new ArrayList<ChannelGroup>();
		groups.add((channelGroupS.findById(currGroupId)));
		modelMap.put("Groups", groups);
		
		boolean canApprove = false;
		ChannelGroup group = channelGroupS.findById(currGroupId);
		if (currentUser.getPermissions().contains(dataService.findPermissionByName(Permission.CONTENT_REVIEW)) && group.getContentGatekeepers().contains(currentUser)) {
			canApprove = true;
		}
		modelMap.addAttribute("canApprove", canApprove);
		
		List<Channel> channels = new ArrayList<Channel>();
		if (currentUser.getPermissions().contains(dataService.findPermissionByName(Permission.CHANNEL_GROUP_ADMINISTRATION))) {
			channels.addAll(channelS.findAll());
		} else {
			Set<ChannelGroup> groupsSet = currentUser.getUserGroupsToGatekeep();
			groups.addAll(currentUser.getUserGroupsToProvideContent());
			for (ChannelGroup g : groupsSet) {
				channels.addAll(g.getChannels());
			}
			
			channels.addAll(currentUser.getChannels());
			
		}
		Channel adminChannel = channelS.findById(Channel.ADMIN_CHANNEL_ID);
		if(User.isAdministrator(currentUser)){
			if(!channels.contains(adminChannel))
			{
				channels.add(adminChannel);
			}
		}
		else{
			channels.remove(adminChannel);
		}
		
		Collections.sort(channels);
		modelMap.addAttribute("Channels", channels);
		
		
		
		return new ModelAndView("submissionForm");
	}

	@RequestMapping("/secure/editSubmission")
	public ModelAndView editSubmissionView(HttpServletRequest request, ModelMap modelMap,
			@ModelAttribute("storyIdForm") StoryReviewForm srf) {
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		Long id = srf.getId();
		Story storyToEdit = storyS.findById(id);
		String approvedBy = storyToEdit.getApprovedBy();
		StoryForm sf = new StoryForm(storyToEdit);
		// might need to mess with this.
		sf.setStoryId(storyToEdit.getId());
		sf.setApprovedBy(storyToEdit.getApprovedBy());
		// need to do this, so that on re-submit we can tell that we're
		// using a new story.
		modelMap.addAttribute("StoryForm", sf);

		UserState userState = (UserState) request.getSession().getAttribute("userState");
		User currentUser = userService.findUser(userState.getUser().getId());
		modelMap.addAttribute("currentUser", currentUser);

		Long currGroupId = currentUser.getAssocGroupId();
		List<ChannelGroup> groups;
		groups = new ArrayList<ChannelGroup>();
		groups.add((channelGroupS.findById(currGroupId)));
		modelMap.put("Groups", groups);
		
		boolean canApprove = false;
		ChannelGroup group = channelGroupS.findById(currGroupId);
		if (currentUser.getPermissions().contains(dataService.findPermissionByName(Permission.CONTENT_REVIEW)) && group.getContentGatekeepers().contains(currentUser)) {
			canApprove = true;
		}
		modelMap.addAttribute("canApprove", canApprove);
		
		List<Channel> channels = new ArrayList<Channel>();
		if (currentUser.getPermissions().contains(dataService.findPermissionByName(Permission.CHANNEL_GROUP_ADMINISTRATION))) {
			channels.addAll(channelS.findAll());
		} else {
			Set<ChannelGroup> groupsSet = currentUser.getUserGroupsToGatekeep();
			groups.addAll(currentUser.getUserGroupsToProvideContent());
			for (ChannelGroup g : groupsSet) {
				channels.addAll(g.getChannels());
			}
			
			channels.addAll(currentUser.getChannels());
			
		}
		Channel adminChannel = channelS.findById(Channel.ADMIN_CHANNEL_ID);
		if(User.isAdministrator(currentUser)){
			if(!channels.contains(adminChannel))
			{
				channels.add(adminChannel);
			}
		}
		else{
			channels.remove(adminChannel);
		}
		
		Collections.sort(channels);
		modelMap.addAttribute("Channels", channels);

		return new ModelAndView("submissionForm");
	}
	
	@RequestMapping("/secure/deleteSubmission")
	public ModelAndView deleteSubmissionView(HttpServletRequest request, ModelMap modelMap,
			@ModelAttribute("storyIdForm") StoryReviewForm srf) {
		logger.info("Expiring Submission");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		Long id = srf.getId();
		Story dStory = storyS.findById(id);
		dStory.setExpirationDate(new Date());
		dStory.setRejectedReasoning(srf.getDenialReasons());
		storyS.save(dStory);
		channelCrons.moveExpiredStories();
		return new ModelAndView("deleteActionSummary");
	}
	
	private void sendApprovalStatusEmail(Story s){
		try {
			emailsvc.sendApprovalStatusEmail(s);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to send submission notification e-mail for story " + s.getTitle() + " (" + s.getId()
					+ "), continuing. \n" + e.getLocalizedMessage());
		}
	}
	
	private void sendReapprovalStatusEmail(Story s){
		try {
			emailsvc.seekingReapprovalStatusEmail(s);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to send submission notification e-mail for story " + s.getTitle() + " (" + s.getId()
					+ "), continuing. \n" + e.getLocalizedMessage());
		}
	}
	
	private void seekingApprovalStatusEmail(Story s)
	{
		try {
			emailsvc.seekingApprovalStatusEmail(s);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to send submission notification e-mail for story " + s.getTitle() + " (" + s.getId()
					+ "), continuing. \n" + e.getLocalizedMessage());
		}
	}
}

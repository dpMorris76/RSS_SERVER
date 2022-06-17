package com.centurylink.rss.web.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.centurylink.rss.business.service.ChannelDS;
import com.centurylink.rss.business.service.ChannelGroupDS;
import com.centurylink.rss.business.service.EmailService;
import com.centurylink.rss.business.service.JDBCUtil;
import com.centurylink.rss.business.service.RSSWritingService;
import com.centurylink.rss.business.service.UserDS;
import com.centurylink.rss.domain.entity.Channel;
import com.centurylink.rss.domain.entity.ChannelGroup;
import com.centurylink.rss.domain.entity.User;
import com.centurylink.rss.web.controller.util.BaseController;
import com.centurylink.rss.web.form.ChannelForm;
import com.centurylink.rss.web.form.ChannelReviewForm;
import com.centurylink.rss.web.service.util.UserState;

@Controller
public class ChannelPageController extends BaseController {

	@Autowired
	EmailService emailsvc;

	@Autowired
	JDBCUtil jdbcUtil;
	
	@Autowired 
	@Qualifier("xmlPathProperties")
	Properties filePaths;
	
	@Autowired
	RSSWritingService rws;
	
	@Autowired
	UserDS userService;
	
	@Autowired
	ChannelDS channelS;
	
	@Autowired
	ChannelGroupDS channelGroupS;

	private static Log log = LogFactory.getLog(ChannelPageController.class);

	// RSS pages

	@RequestMapping("/secure/channelHome")
	public ModelAndView channelHomeView(HttpServletRequest request, ModelMap modelMap) {

		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");

		return new ModelAndView("channelHome");
	}

	@RequestMapping("/secure/channelGrid")
	public ModelAndView channelGridView(HttpServletRequest request, ModelMap modelMap) {

		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		UserState userState = (UserState) request.getSession().getAttribute("userState");
		User user = userService.findUser(userState.getUser().getId());
		Long assocGroupId = user.getAssocGroupId();
		if (assocGroupId != null) {
			log.debug("Associated Group Id for user "+user.getId()+" was not null, returning channels from associated group");
			ChannelGroup grp = channelGroupS.findById(user.getAssocGroupId());
			modelMap.addAttribute("channels", grp.getChannels());
		} else {
			log.warn("Associated Group Id for user "+user.getId()+" returned null, no channels returned");
			modelMap.addAttribute("channels", null);
		}
		
		return new ModelAndView("channelGrid");
	}
	
	@RequestMapping("/secure/editChannel")
	public ModelAndView editChannelView(HttpServletRequest request, ModelMap modelMap,
			@ModelAttribute("chanIdForm") ChannelReviewForm crf) {
		String chanId = request.getParameter("chanId");
		Long id = Long.parseLong(chanId);
		Channel channelToEdit = channelS.findById(id);
		ChannelForm cf = new ChannelForm(channelToEdit);
		modelMap.addAttribute("ChannelForm", cf);

		List<Channel> channels = null;
		channels = channelS.findAll();
		
		modelMap.put("Channels", channels);

		UserState userState = (UserState) request.getSession().getAttribute("userState");
		User user = userService.findUser(userState.getUser().getId());
		modelMap.addAttribute("currentUser", user);

		return new ModelAndView("channelForm");
	}

	@RequestMapping("/secure/channelForm")
	public ModelAndView channelFormView(HttpServletRequest request, ModelMap modelMap) {

		ChannelForm channelForm = new ChannelForm();
		modelMap.put("ChannelForm", channelForm);

		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");

		return new ModelAndView("channelForm");
	}
	
	@RequestMapping(value = "/secure/updateChannel", method = RequestMethod.POST)
	public ModelAndView submitChannel(@ModelAttribute("channel") Channel channel, BindingResult result,
			ModelMap modelMap, HttpServletRequest request, @ModelAttribute ChannelForm channelForm) {
		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		UserState userState = (UserState) request.getSession().getAttribute("userState");
		User user = userService.findUser(userState.getUser().getId());
		if (channelForm.getChanId() == null) {
			channelForm.setChannelEditorEmail(user.getEmail());
			channelForm.setGrpId(user.getAssocGroupId());
			Long newChanId = channelS.saveNewFromForm(channelForm);
			String path = filePaths.getProperty("deployUrl");
			String deployPath = path+"/RSS";
			//Story.createNewChannelMetaStory(newChanId, deployPath);
			//Write out meta feed
			try {
				log.debug("Writing new channel xml...");
				Channel newChan = channelS.findById(newChanId);
				rws.writeChannel(newChan);
			} catch (Exception e) {
				log.error("Exception occured while writing xml for new channel with id: "+newChanId+".\n" + e.getLocalizedMessage());
				e.printStackTrace();
			}
		} else {
			channelForm.setGrpId(user.getAssocGroupId());
			channelS.updateFromForm(channelForm);
		}

		return new ModelAndView("redirect:/secure/channelGrid");
	}
}

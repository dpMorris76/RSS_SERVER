package com.centurylink.rss.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.centurylink.rss.business.service.ChannelDS;
import com.centurylink.rss.business.service.ChannelGroupDS;
import com.centurylink.rss.business.service.JDBCUtil;
import com.centurylink.rss.domain.entity.Channel;
import com.centurylink.rss.domain.entity.ChannelGroup;
import com.centurylink.rss.web.controller.util.BaseController;
import com.centurylink.rss.web.form.ContentGridForm;
import com.centurylink.rss.web.form.FeedForm;
import com.centurylink.rss.web.service.util.UserState;

@Controller
public class FeedController extends BaseController {

	@Autowired
	JDBCUtil jdbcUtil;
	
	@Autowired 
	@Qualifier("xmlPathProperties")
	Properties filePaths;
	
	@Autowired
	ChannelDS channelS;
	
	@Autowired
	ChannelGroupDS channelGroupS;

	private static Log log = LogFactory.getLog(FeedController.class);

	// RSS pages
	@RequestMapping("/secure/rssWelcome")
	public ModelAndView rssWelcomeView(HttpServletRequest request, ModelMap modelMap) {

		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");

		return new ModelAndView("rssWelcome");
	}

	// RSS pages
	@SuppressWarnings("unchecked")
	@RequestMapping("/secure/feeds")
	public ModelAndView feedsView(HttpServletRequest request, ModelMap modelMap) {

		List<ChannelGroup> groups = new ArrayList<ChannelGroup>();
		groups = channelGroupS.findAll();
		Collections.sort(groups);
		modelMap.addAttribute("AllGroups", groups);
		Channel adminChannel = channelS.findById(Channel.ADMIN_CHANNEL_ID);
		modelMap.addAttribute("adminChannel", adminChannel);
		String xmlPath = filePaths.getProperty("xmlLinkPath");
		String fullUrl = "/" + xmlPath;
		modelMap.addAttribute("fullUrl", fullUrl);
		
		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");

		return new ModelAndView("feeds");
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/secure/updateFeeds")
	public ModelAndView updateFeedView(HttpServletRequest request, ModelMap modelMap,
			@ModelAttribute ContentGridForm feedForm, @RequestParam String selection) {
		// for the layout.jsp to work. 
		modelMap.addAttribute("currentUser", ((UserState)request.getSession().getAttribute("userState")).getUser());
		
		List<ChannelGroup> all = new ArrayList<ChannelGroup>();
		all = channelGroupS.findAll();
		modelMap.addAttribute("AllGroups", all);
		
		Channel adminChannel = channelS.findById(Channel.ADMIN_CHANNEL_ID);
		modelMap.addAttribute("adminChannel", adminChannel);
		String xmlPath = filePaths.getProperty("xmlLinkPath");
		String fullUrl = "/" + xmlPath;
		modelMap.addAttribute("fullUrl", fullUrl);
		
		List<ChannelGroup> groups = new ArrayList<ChannelGroup>();
		
		switch (selection) {
		case FeedForm.ALL_GROUPS:
			groups.addAll(channelGroupS.findAll());
			
			break;
		default:
			ChannelGroup selectedGrp = channelGroupS.findById(Long.parseLong(feedForm.getSelection(), 10));
			if (selectedGrp != null) {
				groups.add(selectedGrp);
			}
			break;
		}
		if (groups != null) {
			Collections.sort(groups);
			modelMap.addAttribute("selectedGroups", groups);
		}
		return new ModelAndView("feeds");
	}
}

package com.centurylink.rss.web.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.centurylink.rss.business.service.ChannelDS;
import com.centurylink.rss.business.service.ChannelGroupDS;
import com.centurylink.rss.business.service.DataService;
import com.centurylink.rss.business.service.EmailService;
import com.centurylink.rss.business.service.JDBCUtil;
import com.centurylink.rss.business.service.RSSWritingService;
import com.centurylink.rss.business.service.StoryDS;
import com.centurylink.rss.business.service.UserDS;
import com.centurylink.rss.domain.entity.ExpiredStory;
import com.centurylink.rss.domain.entity.Link;
import com.centurylink.rss.schedule.controller.ChannelCrons;
import com.centurylink.rss.web.controller.util.BaseController;
import com.centurylink.rss.web.form.ExpiredGridForm;

@Controller
public class ExpiredStoriesController extends BaseController {
	
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

	private static final Logger logger = Logger.getLogger(ExpiredStoriesController.class);
	// RSS pages
	@RequestMapping("/secure/expiredStoriesGrid")
	public ModelAndView contentHomeView(HttpServletRequest request, ModelMap modelMap) {
		
		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		
		logger.debug("In the expired Grid with no form");

		modelMap.addAttribute("expiredGridForm", new ExpiredGridForm());
		
		return new ModelAndView("expiredStoriesGrid");
	}
	
	@RequestMapping("/secure/updateExpiredGrid")
	public ModelAndView updateExpiredGridView(HttpServletRequest request, ModelMap modelMap,
			@ModelAttribute ExpiredGridForm expiredGridForm) {
		logger.debug("Updating expired grid");

		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");

		List<ExpiredStory> expiredList = storyS.searchExpiredStories(expiredGridForm);
		
		modelMap.addAttribute("expiredSubmissions", expiredList);
		
		return new ModelAndView("expiredStoriesGrid");
	}
	
	@RequestMapping("/secure/expiredStory/{id}")
	public ModelAndView showExpiredStory(HttpServletRequest request, ModelMap modelMap, @PathVariable Long id){
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		String errorMsg = "RSS could not retrieve the story that was selected.";
		if(id != null){
			ExpiredStory sp = storyS.findExpiredStoryById(id);
			if(sp != null) {
				modelMap.addAttribute("sp", sp);
				modelMap.addAttribute("links", sp.getLinks());
			}
			else {
				modelMap.addAttribute("errorMsg", errorMsg);
			}
		}
		else {
			modelMap.addAttribute("errorMsg", errorMsg);
		}
		return new ModelAndView("expiredStory");
	} 
}

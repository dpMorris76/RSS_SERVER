package com.centurylink.rss.web.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.centurylink.rss.business.service.ChannelGroupDS;
import com.centurylink.rss.business.service.DataService;
import com.centurylink.rss.business.service.EmailService;
import com.centurylink.rss.business.service.JDBCUtil;
import com.centurylink.rss.business.service.UserDS;
import com.centurylink.rss.domain.entity.ChannelGroup;
import com.centurylink.rss.domain.entity.Permission;
import com.centurylink.rss.domain.entity.User;
import com.centurylink.rss.web.controller.util.BaseController;
import com.centurylink.rss.web.form.GroupForm;
import com.centurylink.rss.web.service.util.UserState;

@Controller
public class GroupPageController extends BaseController {

	@Autowired
	EmailService emailsvc;

	@Autowired
	JDBCUtil jdbcUtil;

	@Autowired
	UserDS userService;
	
	@Autowired
	DataService dataService;
	
	@Autowired
	@Qualifier("xmlPathProperties")
	Properties filePaths;
	
	@Autowired
	ChannelGroupDS channelGroupS;

	private static Log log = LogFactory.getLog(GroupPageController.class);

	// RSS pages
	@RequestMapping("/secure/groupHome")
	public ModelAndView groupHomeView(HttpServletRequest request, ModelMap modelMap) {

		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");

		return new ModelAndView("groupHome");
	}

	@RequestMapping("/secure/editGroup")
	public ModelAndView editGroupView(HttpServletRequest request, ModelMap modelMap) {
		String groupId = request.getParameter("groupId");
		Long id = Long.parseLong(groupId);
		ChannelGroup groupToEdit = channelGroupS.findById(id);
		GroupForm cgf = new GroupForm(groupToEdit);
		modelMap.addAttribute("channelGroupForm", cgf);

		Permission contentProvider = dataService.findPermissionByName(Permission.CONTENT_SUBMISSION);
		List<User> contentProviders = userService.findUsersByPermission(contentProvider);
		modelMap.addAttribute("contentProviders", contentProviders);
		Permission contentReviewer = dataService.findPermissionByName(Permission.CONTENT_REVIEW);
		List<User> contentReviewers = userService.findUsersByPermission(contentReviewer);
		modelMap.addAttribute("contentReviewers", contentReviewers);

		return new ModelAndView("groupForm");
	}

	@RequestMapping("/secure/groupForm")
	public ModelAndView groupFormView(HttpServletRequest request, ModelMap modelMap) {
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");

		modelMap.addAttribute("channelGroupForm", new GroupForm());
		Permission contentProvider = dataService.findPermissionByName(Permission.CONTENT_SUBMISSION);
		List<User> contentProviders = userService.findUsersByPermission(contentProvider);
		modelMap.addAttribute("contentProviders", contentProviders);
		Permission contentReviewer = dataService.findPermissionByName(Permission.CONTENT_REVIEW);
		List<User> contentReviewers = userService.findUsersByPermission(contentReviewer);
		modelMap.addAttribute("contentReviewers", contentReviewers);

		return new ModelAndView("groupForm");
	}

	@RequestMapping(value = "/secure/updateGroup", method = RequestMethod.POST)
	public ModelAndView submitGroup(ModelMap modelMap, HttpServletRequest request, @ModelAttribute GroupForm groupForm) {
		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		UserState userState = (UserState) request.getSession().getAttribute("userState");
		User user = userService.findUser(userState.getUser().getId());

		if (groupForm.getGroupId() == null) {
			channelGroupS.saveNewFromForm(groupForm);
		} else {
			channelGroupS.updateFromForm(groupForm);
		}
		
		return new ModelAndView("redirect:/secure/groupGrid");
	}

	@RequestMapping("/secure/groupGrid")
	public ModelAndView channelGroupGrid(HttpServletRequest request, ModelMap modelMap) {
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		UserState userState = (UserState) request.getSession().getAttribute("userState");
		User currentUser = userService.findUser(userState.getUser().getId());
		Long currGroupId = currentUser.getAssocGroupId();
		if (currGroupId != null) {
			log.debug("Associated Group Id for user "+currentUser.getId()+" was not null, returning associated group");
			Set<ChannelGroup> groups = new HashSet<ChannelGroup>();
			groups.add(channelGroupS.findById(currGroupId));
			modelMap.addAttribute("groups", groups);
		} else {
			log.warn("Associated Group Id for user "+currentUser.getId()+" returned null, no group returned");
			modelMap.addAttribute("groups", null);
		}
		
		return new ModelAndView("groupGrid");
	}
	
}

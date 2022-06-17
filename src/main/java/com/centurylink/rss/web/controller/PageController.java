package com.centurylink.rss.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.centurylink.rss.web.controller.util.BaseController;

@Controller
public class PageController extends BaseController {

	private static Log log = LogFactory.getLog(PageController.class);
	
	//Common page(s)
	@RequestMapping("/secure/admin")
	public ModelAndView adminView(HttpServletRequest request, ModelMap modelMap) {
		
		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap)) 
			return new ModelAndView("redirect:/signin");

		return new ModelAndView("admin");
	}
	@RequestMapping("/secure/tools")
	public ModelAndView toolsView(HttpServletRequest request, ModelMap modelMap) {
		
		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap)) 
			return new ModelAndView("redirect:/signin");

		return new ModelAndView("tools");
	}
}

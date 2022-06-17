package com.centurylink.rss.web.controller;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import com.centurylink.rss.business.service.DataService;
import com.centurylink.rss.business.service.ExcelService;
import com.centurylink.rss.business.service.StoryDS;
import com.centurylink.rss.business.service.UserDS;
import com.centurylink.rss.domain.entity.Story;
import com.centurylink.rss.domain.entity.User;
import com.centurylink.rss.web.controller.util.BaseController;

@Controller
public class TrackingController extends BaseController {

	@Autowired
	UserDS userService;
	
	@Autowired
	ExcelService excelService;
	
	@Autowired
	DataService dataService;
	
	@Autowired 
	StoryDS Storys;

	private static final Logger logger = Logger.getLogger(TrackingController.class);

	private static Log log = LogFactory.getLog(TrackingController.class);
	
	@RequestMapping("/secure/tracking")
	public ModelAndView trackingView(HttpServletRequest request, ModelMap modelMap) {
		
		return new ModelAndView("tracking");
	}
	
	@RequestMapping("/secure/export")
	public void exportView(HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=trackingReport.xlsx");

		List<Story> storyList = new ArrayList<Story>();
		
		//Loop through all stories to see if 
		for (Story s : Storys.findAll()){
			Calendar expirationDate = Calendar.getInstance();
			Calendar currentDate = Calendar.getInstance();
			expirationDate.setTime(s.getExpirationDate()); 
			if (expirationDate.after(currentDate)){
				storyList.add(s);
			}
		}
		
		int userCount = 0;
		
		for (User u : userService.findAllUsers()){
			userCount++;
		}
		
		OutputStream out = response.getOutputStream();
		try {
			excelService.exportSpreadsheet2007(storyList, out, userCount);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}


}

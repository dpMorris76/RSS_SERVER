package com.centurylink.rss.web.controller.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.centurylink.rss.domain.entity.User;
import com.centurylink.rss.web.service.util.UserState;

@Controller
public abstract class BaseController {
	
	private static Log log = LogFactory.getLog(BaseController.class);

	protected boolean setSessionInfo(HttpServletRequest request, ModelMap modelMap) {

		// Get current userState
		UserState userstate = (UserState) request.getSession().getAttribute("userState");
		
		// If no user state then we need to fail here and send to login
		if (userstate == null) {
			request.getSession().invalidate();
			return false;
		}

		// Add current User to the model
		User currentUser = (User) userstate.getUser();
		modelMap.addAttribute("currentUser", currentUser);

		// return success
		return true;

	}

}

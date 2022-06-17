package com.centurylink.rss.web.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.centurylink.rss.business.service.UserDS;
import com.centurylink.rss.domain.entity.User;
import com.centurylink.rss.web.auth.CustomAuthenticationProvider;
import com.centurylink.rss.web.controller.util.BaseController;
import com.centurylink.rss.web.service.util.UserState;

import oracle.net.aso.e;

@Controller
public class LoginController extends BaseController {
	
	@Autowired
	private String agileSignon;

	@Autowired
	UserDS userService;
	
	@Autowired
	private CustomAuthenticationProvider authenticationProvider;
	
	private static Log log = LogFactory.getLog(LoginController.class);
	
	@RequestMapping("/error")
	public ModelAndView error(HttpServletRequest request, ModelMap modelMap) {
//		Enumeration<String> names = request.getAttributeNames();
//		log.debug(names);
//		while (names.hasMoreElements()) {
//			log.debug(names.nextElement());
//		}
		
		Exception e = (Exception) request.getAttribute("javax.servlet.error.exception"); 
		log.error(e.getLocalizedMessage(), e);
		
		modelMap.addAttribute("errorMessage", e.toString());
		
		modelMap.addAttribute("welcomeURL", request.getContextPath() + "/secure/welcome");
		return new ModelAndView("error");
	}
	
	@RequestMapping("/signin")
	public ModelAndView signin(HttpServletRequest request, ModelMap modelMap) {

		// Check if we have a current session
		UserState userState = (UserState) request.getSession().getAttribute("userState");
		System.out.println("****************************************************** Hashmap contains: " + UserState.getCurrentUsersInfo().size());
		
		
		if (userState != null) {
			
			if (UserState.getCurrentUsersInfo().containsValue(userState.getUser())) {
				
				log.debug("User has multiple sessions!");
			}
			return new ModelAndView("redirect:/secure/welcome");
		}

		// Check if we have a security key
		String key = request.getParameter("key");
		if (key != null) {
			// Check if key yields valid user
			String username = userService.findUserIdByKey(key);
			if (username != null) {
				// Check if user is a registered user
				User user = userService.findUserByUsername(username);
				// create new basic feeder user
				if (user == null) {
					username = userService.createNewBasicFeederUserFromLdap(username, 0L);
					user = userService.findUserByUsername(username);
				}
				
				// Set Spring Authentication
				try {
					// Must be called from request filtered by Spring Security,
					// otherwise SecurityContextHolder is not updated
					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, "password");
					token.setDetails(new WebAuthenticationDetails(request));
					Authentication authentication = this.authenticationProvider.authenticate(token);
					log.debug("Logging in with [{}]" + authentication.getPrincipal());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} catch (Exception e) {

					SecurityContextHolder.getContext().setAuthentication(null);
					log.error("Failure in autoLogin", e);
					return new ModelAndView("notWelcome");
				}
				
				
				HttpSession session = request.getSession();
				userState = new UserState();
				if (UserState.getCurrentUsersInfo().containsKey(user)) {
					log.debug("User has multiple sessions! Deleting original session!");
					
					try {
						UserState.getCurrentUsersInfo().get(user).invalidate();
					} catch (Exception e) {
						// Do nothing, session already invalidated by someone. whoooOOooo spooky!
					}
					
					UserState.getCurrentUsersInfo().remove(user);
					userState.setUser(user, session);
				} else {
					userState.setUser(user, session);
				}
				
				
			
				session.setAttribute("userState", userState);

				return new ModelAndView("redirect:/secure/welcome");
			} else {
				return new ModelAndView("redirect:" + agileSignon + request.getRequestURL() + "?");
			}
		} else {
			return new ModelAndView("redirect:" + agileSignon + request.getRequestURL() + "?");
		}

//		return new ModelAndView("notWelcome");
	}
	
	@RequestMapping("/signout")
	public ModelAndView signoutView(HttpServletRequest request, ModelMap modelMap) {
		request.getSession().invalidate();
		return new ModelAndView("forward:signin");
	}

	@RequestMapping("/secure/welcome")
	public ModelAndView welcomeView(HttpServletRequest request, ModelMap modelMap) {
		
		if (!setSessionInfo(request, modelMap)) 
			return new ModelAndView("redirect:/signin");
		
		UserState userstate = (UserState) request.getSession().getAttribute("userState");
		userstate.MiscObjects.clear(); // really only used for bulkEdit need to tie things to userstate.
		
		return new ModelAndView("welcome");
	}
	
}

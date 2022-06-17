package com.centurylink.rss.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.centurylink.rss.business.service.ChannelDS;
import com.centurylink.rss.business.service.ChannelGroupDS;
import com.centurylink.rss.business.service.DataService;
import com.centurylink.rss.business.service.UserDS;
import com.centurylink.rss.domain.entity.Channel;
import com.centurylink.rss.domain.entity.ChannelGroup;
import com.centurylink.rss.domain.entity.Permission;
import com.centurylink.rss.domain.entity.User;
import com.centurylink.rss.web.auth.CustomAuthenticationProvider;
import com.centurylink.rss.web.controller.util.BaseController;
import com.centurylink.rss.web.form.AdminDropdownForm;
import com.centurylink.rss.web.form.AdminGridForm;
import com.centurylink.rss.web.form.AutoSelectChannelsForm;
import com.centurylink.rss.web.form.BulkEditForm;
import com.centurylink.rss.web.form.ChangeBulkEditUsersForm;
import com.centurylink.rss.web.form.UserPermissionsForm;
import com.centurylink.rss.web.service.util.UserState;

@Controller
public class AdminController extends BaseController {

	@Autowired
	private String agileSignon;

	@Autowired
	UserDS userService;
	
	@Autowired
	DataService dataService;
	
	@Autowired
	ChannelDS channelS;
	
	@Autowired
	ChannelGroupDS channelGroupS;

	private static final Logger logger = Logger.getLogger(AdminController.class);
	
	@Autowired
	private CustomAuthenticationProvider authenticationProvider;

	@RequestMapping("/secure/userHome")
	public ModelAndView userHomeView(HttpServletRequest request, ModelMap modelMap) {

		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");

		return new ModelAndView("userHome");
	}
	
	@RequestMapping("/secure/changeBulkEditUsers")
	public ModelAndView changeBulkEditUsers(HttpServletRequest request, ModelMap modelMap, @ModelAttribute ChangeBulkEditUsersForm changeBulkEditUsersForm)
	{
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		UserState userstate = (UserState) request.getSession().getAttribute("userState");
		ArrayList<User> users = null;
		ChannelGroup g = null;
		modelMap.addAttribute("bulkEditForm", new BulkEditForm());
		modelMap.addAttribute("changeBulkEditUsersForm", new ChangeBulkEditUsersForm());
		if(changeBulkEditUsersForm != null && changeBulkEditUsersForm.getNewADID() != null && !changeBulkEditUsersForm.getNewADID().equals(""))
		{
			String uname = changeBulkEditUsersForm.getNewADID().toLowerCase();
			User userToLoadReportsOf = userService.findUserByUsername(uname);
			if(userToLoadReportsOf != null)
			{
				// then we're going great
			}
			else 
			{
				userService.createNewBasicUserFromLdap(changeBulkEditUsersForm.getNewADID()); // try to generate this manager.
				
				userToLoadReportsOf = userService.findUserByUsername(uname);
				HashSet<Long> permissionIds = new HashSet<Long>();
				permissionIds.add(dataService.findPermissionByName(Permission.SUBSCRIPTION).getId());
				permissionIds.add(dataService.findPermissionByName(Permission.USER_ADMINISTRATION).getId());
				// set the new user's assoc group id to that of the current user, if none already set, meaning this guy is new.
				userToLoadReportsOf.setAssocGroupId(((User)userstate.getUser()).getAssocGroupId());
				userService.updatePermissions(userToLoadReportsOf.getId(), permissionIds);
			}
			// looks funky but makes a lot of sense... probably a better way to do it.
			if(userToLoadReportsOf != null)
			{
				logger.info("changing bulk edit user to: " + userToLoadReportsOf.getUsername());
				modelMap.addAttribute("selectedUser", userToLoadReportsOf);
				UserState userState = (UserState)request.getSession().getAttribute("userState");
				// need to tie this to user state so that bulk edit can  continue to write the correct data to screen.
				userState.MiscObjects.put("selectedUser", userToLoadReportsOf);
				users = userService.loadAndGetSubordinatesFromAdid(userToLoadReportsOf);
				// then we can load new people
				Long groupId = userToLoadReportsOf.getAssocGroupId();
				if(channelGroupS.findById(groupId) == null)
				{
					groupId = ChannelGroup.FIELD_OPERATIONS_ID;
				}
				g = channelGroupS.findById(groupId);
				g.getAutoChannels();
				modelMap.addAttribute("users", users);
				modelMap.addAttribute("channels", g.getChannels());
				
				HashSet<Long> autoChannelsIds = new HashSet<Long>();
				// need to add these for auto-selection to work.
				for(Channel c : g.getAutoChannels())
				{
					autoChannelsIds.add(c.getId());
				}
				
				modelMap.addAttribute("autoChannelsIds", autoChannelsIds);
				
				userState.MiscObjects.put("previousUsers", users);
				return new ModelAndView("bulkEdit");
			}
		}
		
		// it failed, adid probably bad.
		modelMap.addAttribute("changeBulkEditFormError", "Error loading based on ADID, is the ADID bad?");
		
		// add crucial stuff to the page.
		Long groupId = ((UserState) request.getSession().getAttribute("userState")).getUser().getAssocGroupId();
		if(channelGroupS.findById(groupId) == null)
		{
			groupId = ChannelGroup.FIELD_OPERATIONS_ID;
		}
		g = channelGroupS.findById(groupId);
		HashSet<Long> autoChannelsIds = new HashSet<Long>();
		modelMap.addAttribute("channels", g.getChannels());
		// need to add these for auto-selection to look correct.
		modelMap.addAttribute("users", users);
		for(Channel c : g.getAutoChannels())
		{
			autoChannelsIds.add(c.getId());
		}
		modelMap.addAttribute("autoChannelsIds", autoChannelsIds);
		
		return new ModelAndView("bulkEdit");
	}
	
	
	@RequestMapping("/secure/bulkEdit")
	public ModelAndView bulkEditView(HttpServletRequest request, ModelMap modelMap, @ModelAttribute BulkEditForm bulkEditForm) {
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		
		
		User currentUser = ((UserState)request.getSession().getAttribute("userState")).getUser();
		
		// this is a much much much better way to do it.
		// listen to Mark better
		UserState userState = (UserState)request.getSession().getAttribute("userState");
		@SuppressWarnings("unchecked")
		ArrayList<User> previousUsers = (ArrayList<User>)userState.MiscObjects.get("previousUsers");
		
		ArrayList<User> users = null;
		Long groupId = currentUser.getAssocGroupId();
		User selectedUser = null;
		if(userState.MiscObjects.get("selectedUser") != null)
		{
			selectedUser = (User)userState.MiscObjects.get("selectedUser");
			modelMap.put("selectedUser", selectedUser);
			groupId = selectedUser.getAssocGroupId();
		}
		else
		{
			selectedUser = userState.getUser();
			modelMap.put("selectedUser", userState.getUser());
		}
		
		if(channelGroupS.findById(groupId) == null)
		{	
			groupId = ChannelGroup.FIELD_OPERATIONS_ID;
		}
		logger.info("Entering bulk edit groupId = " + groupId + "   current user: " + currentUser.getUsername());
		if( previousUsers != null && bulkEditForm != null && bulkEditForm.getChannelIds() != null)
		{
			Set<Channel> addedChannels = channelS.findMultipleById(bulkEditForm.getChannelIds());
			Permission subscription = dataService.findPermissionByName(Permission.SUBSCRIPTION);
			HashSet<Long> subscriptionOnly = new HashSet<Long>();
			subscriptionOnly.add(subscription.getId());
			ArrayList<User> usersToSave = new ArrayList<User>();
			for(Long l: bulkEditForm.getUserIds())
			{
				User u = userService.findUser(l);
				
				u.setChannels(addedChannels);
				u.setAssocGroupId(groupId); // set them up with the channels, the groupId, and the subscriptions.
				HashSet<Long> permissions = new HashSet<Long>(); // preserve their current permissions though.
				for(Permission a : u.getPermissions())
				{
					permissions.add(a.getId());
				}
				// should probably use u.setPermissions(permissions) here to avoid un-needed hibernate calls.
				permissions.addAll(subscriptionOnly);
				userService.updatePermissions(l ,permissions);
				usersToSave.add(u);
				logger.info( currentUser.getFullName() + ", ADID: " + currentUser.getUsername() + " EDITED " + u.getFullName());
				previousUsers.remove(u);
			}
			userService.saveUsers(usersToSave);
			users = new ArrayList<User>();
			// if we're done, take them out of it.
			if(previousUsers.size() == 0)
			{
				userState.MiscObjects.put("previousUsers", null);
				return new ModelAndView("redirect:/secure/welcome");
			}
			for(User u: previousUsers)
			{
				users.add(u);
			}
			userState.MiscObjects.put("previousUsers", users);
		}
		else
		{
			 users = userService.loadAndGetSubordinatesFromAdid(currentUser);
			 userState.MiscObjects.put("previousUsers", users);
			// if this is our first time on the page, then we need to generate all the users.
			
		}

		
		ChannelGroup g = channelGroupS.findById(groupId);
		modelMap.addAttribute("bulkEditForm", new BulkEditForm());
		// not working as a arrayList for some reason.
		HashSet<Long> autoChannelsIds = new HashSet<Long>();
		// need to add these for auto-selection to work.
		for(Channel c : g.getAutoChannels())
		{
			autoChannelsIds.add(c.getId());
		}
		modelMap.addAttribute("users", users);
		modelMap.addAttribute("channels", g.getChannels());
		modelMap.addAttribute("autoChannelsIds", autoChannelsIds);
		modelMap.addAttribute("currentUser", currentUser);
		modelMap.addAttribute("changeBulkEditUsersForm", new ChangeBulkEditUsersForm());
		return new ModelAndView("bulkEdit");
	}
	
	@RequestMapping("/secure/userEdit")
	public ModelAndView userEditView(HttpServletRequest request, ModelMap modelMap) {
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		List<ChannelGroup> all = new ArrayList<ChannelGroup>();
		all = channelGroupS.findAll();
		modelMap.addAttribute("AllGroups", all);
		
		String userId = request.getParameter("userId");
		String username = request.getParameter("username");
		User user;
		Long id;
		try {
			id = Long.parseLong(userId);
			user = userService.findUser(id); 
		} catch (Exception e){
			user = userService.findUserByUsername(username);
			if (user == null) {
				user = userService.getUserInfoFromLdap(username);
				user.setUserActive(true);
				user.setPermissions(new HashSet<Permission>());
				userService.saveUser(user);
			}
			id = user.getId();
			if(id == null)
			{
				id = ChannelGroup.FIELD_OPERATIONS_ID; // because the default value should be field operations.
			}
		}
		modelMap.addAttribute("user", user);
		// default to the field operations. 
		if(user.getAssocGroupId() == null)
		{
			modelMap.addAttribute("UsersGroup", ChannelGroup.FIELD_OPERATIONS_ID);
		}
		else
		{
			modelMap.addAttribute("UsersGroup", user.getAssocGroupId());
		}
		List<Permission> allPermissions = dataService.findAllPermissions();
		modelMap.addAttribute("permissions", allPermissions);
		Set<Long> permissionsIds = new HashSet<Long>();
		for (Permission p:user.getPermissions()) {
			permissionsIds.add(p.getId());
		}
		UserPermissionsForm upf = new UserPermissionsForm(id, permissionsIds);
		ChannelGroup group = null;
		// if they don't have a assocGroupId then that is supposed to default to field operations
		if(user.getAssocGroupId() == null)
		{
			group = channelGroupS.findById(ChannelGroup.FIELD_OPERATIONS_ID);	
		}
		else
		{
			group = channelGroupS.findById(user.getAssocGroupId());
		}
		List<Channel> groupChannels = group.getChannels(); 
		HashSet<Long> autoChannelsIds = new HashSet<Long>();
		// need to add these for auto-selection to work.
		for(Channel c : group.getAutoChannels())
		{
			autoChannelsIds.add(c.getId());
		}
		
		modelMap.addAttribute("autoChannelsIds", autoChannelsIds);
		modelMap.addAttribute("channels", groupChannels);
		
		Set<Long> channelIdList = new HashSet<Long>();
		if (user.getChannels() != null) {
			for (Channel chan : user.getChannels()) {
				channelIdList.add(chan.getId());
			}
		}
		upf.setChannelIds(channelIdList);
//		Set<Long> importantChannelIdList = new HashSet<Long>();
//		if (user.getChannels() != null) {
//			for (Channel chan : user.getImportantChannels()) {
//				importantChannelIdList.add(chan.getId());
//			}
//		}
//		upf.setImportantChannelIds(importantChannelIdList);
		Set<Permission> basicUserPermissions = new HashSet<Permission>();
		basicUserPermissions.add(dataService.findPermissionByName(Permission.SUBSCRIPTION));

		Set<Permission> contentProviderPermissions = new HashSet<Permission>();
		contentProviderPermissions.add(dataService.findPermissionByName(Permission.SUBSCRIPTION));
		contentProviderPermissions.add(dataService.findPermissionByName(Permission.CONTENT_SUBMISSION));
		
		Set<Permission> contentGatekeeperPermissions = new HashSet<Permission>();
		contentGatekeeperPermissions.add(dataService.findPermissionByName(Permission.SUBSCRIPTION));
		contentGatekeeperPermissions.add(dataService.findPermissionByName(Permission.CONTENT_SUBMISSION));
		contentGatekeeperPermissions.add(dataService.findPermissionByName(Permission.CONTENT_REVIEW));
		
		Set<Permission> channelAdministratorPermissions = new HashSet<Permission>();
		channelAdministratorPermissions.add(dataService.findPermissionByName(Permission.SUBSCRIPTION));
		channelAdministratorPermissions.add(dataService.findPermissionByName(Permission.CONTENT_SUBMISSION));
		channelAdministratorPermissions.add(dataService.findPermissionByName(Permission.CONTENT_REVIEW));
		channelAdministratorPermissions.add(dataService.findPermissionByName(Permission.CHANNEL_ADMINISTRATION));
		channelAdministratorPermissions.add(dataService.findPermissionByName(Permission.CHANNEL_GROUP_ADMINISTRATION));
		
		Set<Permission> superAdministratorPermissions = new HashSet<Permission>();
		superAdministratorPermissions.add(dataService.findPermissionByName(Permission.SUBSCRIPTION));
		superAdministratorPermissions.add(dataService.findPermissionByName(Permission.CONTENT_SUBMISSION));
		superAdministratorPermissions.add(dataService.findPermissionByName(Permission.CONTENT_REVIEW));
		superAdministratorPermissions.add(dataService.findPermissionByName(Permission.CHANNEL_ADMINISTRATION));
		superAdministratorPermissions.add(dataService.findPermissionByName(Permission.CHANNEL_GROUP_ADMINISTRATION));
		superAdministratorPermissions.add(dataService.findPermissionByName(Permission.USER_ADMINISTRATION));
		
		Set<Permission> apsUserPermissions = new HashSet<Permission>();
		apsUserPermissions.add(dataService.findPermissionByName(Permission.SUBSCRIPTION));
		apsUserPermissions.add(dataService.findPermissionByName(Permission.USER_ADMINISTRATION));
		
		modelMap.addAttribute("basicUserPermissions", basicUserPermissions);
		modelMap.addAttribute("contentProviderPermissions", contentProviderPermissions);
		modelMap.addAttribute("contentGatekeeperPermissions", contentGatekeeperPermissions);
		modelMap.addAttribute("channelAdministratorPermissions", channelAdministratorPermissions);
		modelMap.addAttribute("superAdministratorPermissions", superAdministratorPermissions);
		modelMap.addAttribute("apsUserPermissions", apsUserPermissions);
		modelMap.addAttribute("userPermissionsForm", upf);

		return new ModelAndView("userEdit");
	}

	
	@RequestMapping("/secure/autoSelectChannels")
	public ModelAndView autoSelectChannelsView(HttpServletRequest request, ModelMap modelMap, @ModelAttribute AutoSelectChannelsForm autoSelectChannelsForm) {
		
		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		
		List<ChannelGroup> all = new ArrayList<ChannelGroup>();
		all = channelGroupS.findAll();
		
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		User user = ((UserState) request.getSession().getAttribute("userState")).getUser();
		ChannelGroup g = null;
		HashSet<Channel> channels = new HashSet<Channel>();
		if(user.getAssocGroupId() == null)
		{
			// incredibly fringe case.
			logger.info("User had no associated group id: redirecting to welcome page");
			return failure;
		}
		else if(autoSelectChannelsForm != null && autoSelectChannelsForm.getSwitcher() !=null && !autoSelectChannelsForm.getSwitcher().equals("")) {
			if(autoSelectChannelsForm.getSwitcher().equalsIgnoreCase("add") && autoSelectChannelsForm.getChannelIds() != null && autoSelectChannelsForm.getChannelIds().size() != 0)
			{
				g = channelGroupS.findById(Long.parseLong(autoSelectChannelsForm.getGroupId()));
				channels = (HashSet<Channel>)channelS.findMultipleById(autoSelectChannelsForm.getChannelIds());
				g.getAutoChannels().addAll(channels);
				channelGroupS.save(g);
				// add all the users to the group that is now autoselected.
				for(User u: userService.findUsersByGroup(g))
				{
					u.getChannels().addAll(channels);
					userService.saveUser(u);
				}
			}
			else if(autoSelectChannelsForm.getSwitcher().equalsIgnoreCase("remove") && autoSelectChannelsForm.getRemoveChannelIds() != null && autoSelectChannelsForm.getRemoveChannelIds().size() != 0)
			{
				g = channelGroupS.findById(Long.parseLong(autoSelectChannelsForm.getGroupId()));
				g.getAutoChannels().removeAll(channelS.findMultipleById(autoSelectChannelsForm.getRemoveChannelIds()));
				channelGroupS.save(g);
			}
			else if(autoSelectChannelsForm.getSwitcher().equalsIgnoreCase("group") && !autoSelectChannelsForm.getGroupId().equals("") && autoSelectChannelsForm.getChannelIds() != null && autoSelectChannelsForm.getChannelIds().size() != 0)
			{
				g = channelGroupS.findById(Long.parseLong(autoSelectChannelsForm.getGroupId()));
			}
			else // default to just grab by the group.
			{
				g = channelGroupS.findById(Long.parseLong(autoSelectChannelsForm.getGroupId()));
			}
		}
		else
		{
			g = channelGroupS.findById(user.getAssocGroupId());
		}
		channels.addAll(g.getChannels());
		channels.removeAll(g.getAutoChannels());
		all.remove(g);

		modelMap.addAttribute("group", g);
		modelMap.addAttribute("groups", all);
//		modelMap.addAttribute("autoChannels", g.getAutoChannels());
		modelMap.addAttribute("channels", channels);
		modelMap.addAttribute("autoSelectChannelsForm", new AutoSelectChannelsForm());
		

		return new ModelAndView("autoSelectChannels");
	}
	
	
	@RequestMapping("/secure/userGrid")
	public ModelAndView userGridView(HttpServletRequest request, ModelMap modelMap) {
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		
		List<ChannelGroup> all = new ArrayList<ChannelGroup>();
		all = channelGroupS.findAll();
		modelMap.addAttribute("AllGroups", all);
		modelMap.addAttribute("adminGridForm", new AdminGridForm());
		List<User> gridUsers = new ArrayList<User>();
// bad population.
//		try {
//			List<User> userList = userService.findAllUsers();
//			for (User usr : userList) {
//				gridUsers.add(usr);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		modelMap.addAttribute("gridUserList", gridUsers);

		ModelAndView failure = new ModelAndView("redirect:/secure/welcome");
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");

		return new ModelAndView("userGrid");
	}
	private static String junkStr = "";
	@RequestMapping(value = "/secure/updateAdminGrid", method = RequestMethod.POST)
	public ModelAndView updateAdminGrid(@ModelAttribute AdminGridForm adminGridForm, BindingResult result,
			ModelMap modelMap, HttpServletRequest request) {
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		
		if (result.hasErrors()) {
			// return "error";
		}
		List<User> gridUsers = new ArrayList<User>();
		Set<User> userList = new HashSet<User>();
		
		// initialize these to \\\\\\ in hopes that no ones name could possibly include \\\\\\
		String fname = junkStr;
		if(adminGridForm.getFname() != null && !adminGridForm.getFname().trim().equals(""))
			fname = adminGridForm.getFname().trim();
		
		String lname = junkStr;
		if(adminGridForm.getLname() != null && !adminGridForm.getLname().trim().equals(""))
			lname = adminGridForm.getLname().trim();
		
		String ADID = junkStr;
		if(adminGridForm.getAdid() != null && !adminGridForm.getAdid().trim().equals(""))
			ADID = adminGridForm.getAdid().trim();
		
		boolean LDAP = false;
		if(adminGridForm.getLdap() == null){
			logger.error("Someone tried a search with null ldap boolean select.");
		}else{
			LDAP = adminGridForm.getLdap().equalsIgnoreCase("true");
		}
		
		if(LDAP){
			logger.debug("Searching LDAP for fname:'" + fname + "' lname '" + lname + "' ADID '" + ADID + "'");
			if(!ADID.equals("")){
				User dummy = userService.getUserInfoFromLdap(ADID);
				if(dummy != null && dummy.getUsername() != null){
					userList.add(dummy);
				}
			}
			if(!fname.equals(junkStr) || !lname.equals(junkStr))
			{
				userList.addAll(userService.findLdapUsersByName(fname, lname));	
			}
			if(!ADID.equals(junkStr)){
				User u = userService.getUserInfoFromLdap(ADID);
				if(u != null && u.getUsername() != null){
					userList.add(u);
				}
				
			}
			// if they already existed add them this way. 
			for (User usr : userList) {
				String un = usr.getUsername();
				if (un != null) {
					User found = userService.findUserByUsername(un);
					if (found != null) {
						gridUsers.add(found);
					} else {
						gridUsers.add(usr);
					}
				}
			}
			
		}
		else{
			logger.debug("Searching RSS for fname:'" + fname + "' lname '" + lname + "' ADID '" + ADID + "'");
			if(!ADID.equals(junkStr)){
				User dummy = userService.findUserByUsername(ADID);
				if(dummy != null && dummy.getUsername() != null){
					userList.add(dummy);
				}
			}
			if(!fname.equals(junkStr) || !lname.equals(junkStr)){
				userList.addAll(userService.findUsersByName(fname, lname));
			}
			gridUsers.addAll(userList);
		}
		List<ChannelGroup> all = channelGroupS.findAll();
		modelMap.addAttribute("AllGroups", all);
		
		modelMap.addAttribute("adminGridForm", adminGridForm);
		modelMap.addAttribute("gridUserList", gridUsers);
		return new ModelAndView("userGrid");
	}

	// TODO make this not redirect.
	// Saves app changes through hibernate
	@RequestMapping(value = "/secure/updateUser", method = RequestMethod.POST)
	public ModelAndView updateUserPermissions(@ModelAttribute("userPermissionsForm") UserPermissionsForm upf, ModelMap modelMap,
			HttpServletRequest request) {
		if (!setSessionInfo(request, modelMap))
			return new ModelAndView("redirect:/signin");
		userService.updatePermissions(upf.getId(), upf.getPermissionIds());
		User user = userService.findUser(upf.getId());
		Long selectedId = Long.parseLong(upf.getSelection());
		User currentUser2 = ((UserState)request.getSession().getAttribute("userState")).getUser();
		logger.info( currentUser2.getFullName() + ", ADID: " + currentUser2.getUsername()  + " EDITED " + user.getFullName());
		// if we just change the associated group, then we probably want to 
		if(user.getAssocGroupId() != null && user.getAssocGroupId().equals(selectedId))
		{
			try{
				userService.updateChannels(upf.getId(), upf.getChannelIds());
				userService.updateImportantChannels(upf.getId(), upf.getImportantChannelIds());
				logger.info(upf.getImportantChannelIds().toString());
			}catch (Exception e){
				logger.debug("We should never hit this point: /secure/updateUser");
			}
			return new ModelAndView("redirect:userGrid");
		}
		else if(user.getAssocGroupId() == null)
		{
			// this means that they are fine with this person being in the fieldOperations group.
			userService.updateAssociatedGroupId(upf.getId(), selectedId);
			try{
				userService.updateChannels(upf.getId(), upf.getChannelIds());
				userService.updateImportantChannels(upf.getId(), upf.getImportantChannelIds());
				logger.info(upf.getImportantChannelIds().toString());
			}catch (Exception e){
				logger.debug("We should never hit this point: /secure/updateUser");
			}
			return new ModelAndView("redirect:userGrid");
		}
		else
		{
			// somebodies group just changed, clear it out.
			try{
				// can't be associated to anything, if you're in a new group!
				userService.updateChannels(upf.getId(), new HashSet<Long>());
				userService.updateImportantChannels(upf.getId(), new HashSet<Long>());
			}catch (Exception e){
				logger.debug("We should never hit this point: /secure/updateUser");
			}
			userService.updateAssociatedGroupId(upf.getId(), selectedId);
			return new ModelAndView("redirect:userEdit?username=" + user.getUsername() + "&userId=" + user.getId());
		}
	}
	
	@RequestMapping(value = "/secure/saveUser/{username}", method = RequestMethod.POST)
	public ModelAndView saveUser(@PathVariable("username") String stringuid, HttpServletRequest request) {

		String content = null;
		byte[] input = new byte[request.getContentLength()];
		try {
			InputStream is = request.getInputStream();
			StringBuffer sb = new StringBuffer();

			int bytesRead = is.read(input);
			while (bytesRead != -1) {
				sb.append(new String(input, 0, bytesRead));
				bytesRead = is.read(input);
			}
			content = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject json = null;
		try {
			json = new JSONObject(content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String isAdmin = null;
		User user = userService.findUserByUsername(stringuid);

		userService.saveUser(user);

		return null;
	}
	
	@RequestMapping("/secure/updateAdminGridDropdown")
	public ModelAndView updateUserGridView(HttpServletRequest request, ModelMap modelMap,
			@ModelAttribute AdminDropdownForm adminDropdownForm, @RequestParam String selection) {
		List<ChannelGroup> all = new ArrayList<ChannelGroup>();
		all = channelGroupS.findAll();
		modelMap.addAttribute("AllGroups", all);
		modelMap.addAttribute("adminGridForm", new AdminGridForm());
		List<User> gridUsers = new ArrayList<User>();
		
		switch (selection) {
		case AdminDropdownForm.ALL_USERS:
			Set<User> allUsers = new HashSet<User>(userService.findAllUsers());
			gridUsers.addAll(allUsers);
			break;
		default:
			Long selectedId = Long.parseLong(adminDropdownForm.getSelection(), 10);
			if (selectedId != null) {
				ChannelGroup selectedGroup = channelGroupS.findById(selectedId);
				if (selectedGroup != null) {
					gridUsers.addAll(userService.findUsersByGroup(selectedGroup));	
				}
			}
			break;
		}
		modelMap.addAttribute("gridUserList", gridUsers);
		return new ModelAndView("userGrid");
	}
}

package com.centurylink.rss.business.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.centurylink.agile.client.AgileAuthenticationClient;
import com.centurylink.rss.domain.entity.Channel;
import com.centurylink.rss.domain.entity.ChannelGroup;
import com.centurylink.rss.domain.entity.Permission;
import com.centurylink.rss.domain.entity.User;

@Service
public class UserDS {

	private static final Logger logger = Logger.getLogger(UserDS.class);

	private Boolean requireLdapAuthentication;

	@Autowired
	AgileAuthenticationClient agileAuthClient;
	
	@Autowired
	ChannelGroupDS channelGroupService;

	public List<com.centurylink.agile.domain.User> getSubordinatesFromAdid(String adId) {
		return agileAuthClient.getSubordinatesByAdid(adId);
	}

	/**
	 * loads subordinates by ADID or gets existing users from the database.
	 * 
	 * @param manager
	 * @return
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ArrayList<User> loadAndGetSubordinatesFromAdid(User manager) {
		List<com.centurylink.agile.domain.User> abc = agileAuthClient.getSubordinatesByAdid(manager.getUsername());
		ArrayList<User> ret = new ArrayList<User>();
		for (com.centurylink.agile.domain.User a : abc) {
			User existingUser = findUserByUsername(a.getUsername());
			if (existingUser != null) {
				ret.add(existingUser);
				// put them in the correct group if they are not.
				// this is un-needed here. from finding this stuff.
			} else {
				// save all the new users to the database.
				User tempUser = convertAuthUser(a);
				saveUser(tempUser);
				ret.add(tempUser);
			}
		}
		return ret;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Deprecated
	public void saveUsers(List<User> users) {
		User.saveUsers(users);
	}

	public Boolean getRequireLdapAuthentication() {
		return requireLdapAuthentication;
	}

	public void setRequireLdapAuthentication(Boolean requireLdapAuthentication) {
		this.requireLdapAuthentication = requireLdapAuthentication;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveUser(User user) {
		logger.debug("trying to save user from source with id " + user.getId() + ", username: " + user.getUsername()
				+ ", and active: " + user.getUserActive());
		User.saveUser(user);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<User> findUsersByName(String fName, String lName){
		logger.debug("called, fName==>" + fName + " lName==>" + lName);
		return User.findUsersByName(fName, lName);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<User> findLdapUsersByName(String fName, String lName) {
		logger.debug("called, fName==>" + fName + " lName==>" + lName);

		// Get list from LDAP
		List<User> users = convertAuthUsers(agileAuthClient.getUsersByName(fName, lName));
		List<User> results = new ArrayList<User>();

		// Get additional info and user objects from hibernate
		for (User user : users) {
			results.add(user);
		}

		return results;
	}
	public User findLdapUsersByAdid(String Adid){
		return convertAuthUser(agileAuthClient.getUserByAdId(Adid));
	}
	
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void createNewBasicUserFromLdap(String userid) {
		logger.debug("Creating new user from LDAP: " + userid);
		User newUser = getUserInfoFromLdap(userid);
		Permission basicPermission = Permission.findPermissionByName(Permission.SUBSCRIPTION);
		newUser.setPermissions(new HashSet<Permission>());
		newUser.getPermissions().add(basicPermission);
		User.saveUser(newUser);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public String createNewBasicFeederUserFromLdap(String userid, long groupId) {
		logger.debug("Creating new Rss feeder user from LDAP: " + userid);
		User newUser = getUserInfoFromLdap(userid);
		logger.debug("newUser: " + newUser.getUsername());
		if(newUser.getUsername() == null){
			return "";
		}
		else
		{
			Permission basicPermission = Permission.findPermissionByName(Permission.SUBSCRIPTION);
			newUser.setPermissions(new HashSet<Permission>());
			newUser.getPermissions().add(basicPermission);
			newUser.setAssocGroupId(groupId);
			newUser.setChannels(new HashSet<Channel>(channelGroupService.findById(groupId).getChannels()));
			User.saveUser(newUser);
			return newUser.getUsername();
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateChannels(Long id, Set<Long> channelIds) {
		User.updateChannels(id, channelIds);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateImportantChannels(Long id, Set<Long> channelIds) {
		User.updateImportantChannels(id, channelIds);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateAssociatedGroupId(Long userId, Long assocGroupId) {
		User.updateAssociatedGroup(userId, assocGroupId);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updatePermissions(long id, Set<Long> permissionIds) {
		User.updatePermissions(id, permissionIds);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<User> findUsersByGroup(ChannelGroup group) {
		return User.findUsersByGroup(group);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public User getUserInfoFromLdap(String adId) {
		logger.trace("Getting user info from LDAP");
		return convertAuthUser(agileAuthClient.getUserByAdId(adId));
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateList(List<String> usernames) {
		logger.debug("Updating user information from LDAP");
		for (String username : usernames) {
			// get both versions of the user
			User user = User.findUserByUsername(username);
			User user2 = convertAuthUser(agileAuthClient.getUserByAdId(username));

			// overwrite the Dao info with LDAP
			if (user2.getFname() != null) {
				user.setFname(user2.getFname());
			}
			if (user2.getMi() != null) {
				user.setMi(user2.getMi());
			}
			if (user2.getLname() != null) {
				user.setLname(user2.getLname());
			}
			if (user2.getPhnNbr() != null) {
				user.setPhnNbr(user2.getPhnNbr());
			}
			if (user2.getEmail() != null) {
				user.setEmail(user2.getEmail());
			}
			if (user2.getCity() != null) {
				user.setCity(user2.getCity());
			}
			if (user2.getStCd() != null) {
				user.setStCd(user2.getStCd());
			}
			if (user2.getSupervisorName() != null) {
				user.setSupervisorName(user2.getSupervisorName());
			}
			if (user2.getSupervisorNumber() != null) {
				user.setSupervisorNumber(user2.getSupervisorNumber());
			}
			if (user2.getEmployeeType() != null) {
				user.setEmployeeType(user2.getEmployeeType());
			}
			// save info to Dao
			User.saveUser(user);
		}
	}

	/*
	 * @Override
	 */
	public List<Permission> getAllPermissions() {
		List<Permission> permissions = new ArrayList<Permission>();
		for (Permission p : Permission.findAllPermissions()) {
			permissions.add(p);
			logger.debug("Found Permission <" + p.getPermissionName());
		}
		return permissions;
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<User> findUsersByPermission(Permission permission) {

		List<User> users = User.findUsersByPermission(permission);
		for (User user : users) {
			User found = convertAuthUser(agileAuthClient.getUserByAdId(user.getUsername()));
			if (found != null) {
				// overlay ldap data on existing user
				user.setFname(found.getFname());
				user.setMi(found.getMi());
				user.setLname(found.getLname());
				user.setPhnNbr(found.getPhnNbr());
				user.setEmail(found.getEmail());
				user.setCity(found.getCity());
				user.setStCd(found.getStCd());
				user.setSupervisorName(found.getSupervisorName());
				user.setSupervisorNumber(found.getSupervisorNumber());
				user.setExempt(found.getExempt());
			}
		}
		return users;
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public String findUserIdByKey(String key) {
		logger.trace("Finding user by ID: " + key);
		
		return agileAuthClient.findUserIdByKey(key);

	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public User findUser(Long id) {
		return User.findUser(id);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public User findUserByUsername(String username) {
		return User.findUserByUsername(username);
	}

	public User convertAuthUser(com.centurylink.agile.domain.User user) {
		User authUser = new User();
		if (user != null) {
			if (user.getUsername() != null) {
				authUser.setUsername(user.getUsername());
			}
			if (user.getFname() != null) {
				authUser.setFname(user.getFname());
			}
			if (user.getLname() != null) {
				authUser.setLname(user.getLname());
			}
			if (user.getMi() != null) {
				authUser.setMi(user.getMi());
			}
			if (user.getEmail() != null) {
				authUser.setEmail(user.getEmail());
			}
			if (user.getEmployeeType() != null) {
				authUser.setEmployeeType(user.getEmployeeType());
			}
			if (user.getUserActive() != null) {
				authUser.setUserActive(user.getUserActive());
			}
			if (user.getPhnNbr() != null) {
				authUser.setPhnNbr(user.getPhnNbr());
			}
			if (user.getCity() != null) {
				authUser.setCity(user.getCity());
			}
			if (user.getStCd() != null) {
				authUser.setStCd(user.getStCd());
			}
			if (user.getSupervisorName() != null) {
				authUser.setSupervisorName(user.getSupervisorName());
			}
			if (user.getSupervisorNumber() != null) {
				authUser.setSupervisorNumber(user.getSupervisorNumber());
			}
		} else {
			return null;
		}
		return authUser;
	}

	public List<User> convertAuthUsers(List<com.centurylink.agile.domain.User> users) {
		List<User> authUsers = new ArrayList<User>();
		for (com.centurylink.agile.domain.User user : users) {
			authUsers.add(convertAuthUser(user));
		}
		return authUsers;
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<User> findAllUsers() {
		return User.findAllUsers();
	}
}

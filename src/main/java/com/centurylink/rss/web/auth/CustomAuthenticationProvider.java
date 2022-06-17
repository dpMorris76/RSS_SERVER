package com.centurylink.rss.web.auth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.centurylink.rss.business.service.DataService;
import com.centurylink.rss.business.service.UserDS;
import com.centurylink.rss.domain.entity.Permission;
import com.centurylink.rss.domain.entity.User;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	UserDS userService;

	@Autowired
	DataService dataService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();

		// if (!loginService.validMNET(username, password, "AD")) {
		// throw new BadCredentialsException("Invalid Login");
		// }

		// Give user default role
		List<GrantedAuthority> authRoles = new ArrayList<GrantedAuthority>();
		User user = User.findUserByUsername(username);

		authRoles.add(new AuthenticationRole("ROLE_USER")); // all users are
															// users

		// All users may subscribe to channels
		Permission basicPermission = dataService.findPermissionByName(Permission.SUBSCRIPTION);
		Set<Permission> uPermissions = user.getPermissions();
		if (uPermissions == null) {
			user.setPermissions(new HashSet<Permission>());
			uPermissions = user.getPermissions();
		}
		if (!uPermissions.contains(basicPermission)) {
			user.getPermissions().add(basicPermission);
			userService.saveUser(user);
		}

		// Give user authentication roles based on their permissions.
		uPermissions = user.getPermissions();
		for (Permission p : uPermissions) {
			switch (p.getPermissionName()) {
			case Permission.SUBSCRIPTION:
				authRoles.add(new AuthenticationRole(Permission.SUBSCRIPTION));
				break;
			case Permission.CONTENT_SUBMISSION:
				authRoles.add(new AuthenticationRole(Permission.CONTENT_SUBMISSION));
				break;
			case Permission.CONTENT_REVIEW:
				authRoles.add(new AuthenticationRole(Permission.CONTENT_REVIEW));
				break;
			case Permission.CHANNEL_ADMINISTRATION:
				authRoles.add(new AuthenticationRole(Permission.CHANNEL_ADMINISTRATION));
				break;
			case Permission.CHANNEL_GROUP_ADMINISTRATION:
				authRoles.add(new AuthenticationRole(Permission.CHANNEL_GROUP_ADMINISTRATION));
				break;
			case Permission.USER_ADMINISTRATION:
				authRoles.add(new AuthenticationRole(Permission.USER_ADMINISTRATION));
				break;
			default:
				break;
			}
		}
		
		return new UsernamePasswordAuthenticationToken(username, password, authRoles);
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}
}

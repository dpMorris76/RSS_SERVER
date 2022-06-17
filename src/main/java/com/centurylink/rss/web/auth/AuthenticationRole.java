package com.centurylink.rss.web.auth;

import java.util.List;
 

import org.springframework.security.core.GrantedAuthority;
 
public class AuthenticationRole implements GrantedAuthority {
 
    private static final long serialVersionUID = 1L;
    private String name;
 
    private List<AuthenticationPrivilege> privileges;

    public AuthenticationRole(String name) {
    	this.name = name;
	}

	public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    @Override
    public String getAuthority() {
        return this.name;
    }
 
    public List<AuthenticationPrivilege> getPrivileges() {
        return privileges;
    }
 
    public void setPrivileges(List<AuthenticationPrivilege> privileges) {
        this.privileges = privileges;
    }
 
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Role [name=");
        builder.append(name);
        builder.append(", privileges=");
        builder.append(privileges);
        builder.append("]");
        return builder.toString();
    }
}

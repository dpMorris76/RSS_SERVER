package com.centurylink.rss.web.form;

import java.util.HashSet;
import java.util.Set;

public class UserPermissionsForm {
	private Long id;
	private Set<Long> permissionIds;
	private String selection;
	private Set<Long> channelIds;
	private Set<Long> importantChannelIds;

	public String getSelection() {
		return selection;
	}

	public void setSelection(String groupsToDisplay) {
		this.selection = groupsToDisplay;
	}
	
	public UserPermissionsForm(){}
	
	public UserPermissionsForm(Long id, Set<Long> permissionIds) {
		this.id = id;
		this.permissionIds = permissionIds;
	}

	public Set<Long> getPermissionIds() {
		if(permissionIds == null) 
		{
			//Return an nothing instead of null so that we can avoid a null pointer exception
			return new HashSet<Long>();
		}
		else
		{
			return permissionIds;
		}
		
	}

	public void setPermissionIds(Set<Long> permissionIds) {
		this.permissionIds = permissionIds;
	}

	public Set<Long> getChannelIds() {
		
		if(channelIds == null)
		{
			// we return a set of nothing, because of some stupid exceptions being thrown? 
			return new HashSet<Long>();
		}
		else
		{	
			return channelIds;
		}
	}

	public void setChannelIds(Set<Long> channelIds) {
		this.channelIds = channelIds;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Long> getImportantChannelIds() {
		if(importantChannelIds == null)
		{
			return new HashSet<Long>();
		}
		else
		{
			return importantChannelIds;
		}
	}

	public void setImportantChannelIds(Set<Long> importantChannelIds) {
		this.importantChannelIds = importantChannelIds;
	}
}

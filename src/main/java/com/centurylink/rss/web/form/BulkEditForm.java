package com.centurylink.rss.web.form;

import java.util.HashSet;
import java.util.Set;

import com.centurylink.agile.domain.User;

public class BulkEditForm {
	private Set<Long> userIds;
	private Set<Long> channelIds;
	// this is used for spring processing, so that we can tell if they have or have not been already using this form.
	
	public void setchannelIds(Set<Long> channelIds) {
		this.channelIds = channelIds;
	}
	
	public void setUserIds(Set<Long> userIds) {
		this.userIds = userIds;
	}
	
	// this is the correct way to make these, so that it preserves data and doesn't not write because set == null. 
	// only for forms though.
	public Set<Long> getChannelIds() {
		if(channelIds == null)
		{
			return new HashSet<Long>();
		}
		else
		{
			return channelIds;
		}
	}
	
	public Set<Long> getUserIds() {
		if(userIds == null)
		{
			return new HashSet<Long>();
		}
		else
		{
			return userIds;
		}
	}
	
}

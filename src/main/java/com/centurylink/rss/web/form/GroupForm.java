package com.centurylink.rss.web.form;

import java.util.ArrayList;
import java.util.List;

import com.centurylink.rss.domain.entity.ChannelGroup;
import com.centurylink.rss.domain.entity.User;

public class GroupForm {
	
	public Long groupId;
	public String groupTitle;
	public List<Long> contentProviderIds;
	public List<Long> contentGatekeeperIds;
	
	public GroupForm() {
		
	}
	
	public GroupForm(ChannelGroup group) {
		this.groupId = group.getId();
		this.groupTitle = group.getTitle();
		List<Long> ids = new ArrayList<Long>();
		if (group.getContentProviders() != null) {
			for (User u : group.getContentProviders()) {
				ids.add(u.getId());
			}
		}
		this.setContentProviderIds(ids);
		ids = new ArrayList<Long>();
		if (group.getContentGatekeepers() != null) {
			for (User u : group.getContentGatekeepers()) {
				ids.add(u.getId());
			}
		}
		this.setContentGatekeeperIds(ids);
	}
	
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupTitle() {
		return groupTitle;
	}
	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}
	public List<Long> getContentProviderIds() {
		return contentProviderIds;
	}
	public void setContentProviderIds(List<Long> contentProviderIds) {
		this.contentProviderIds = contentProviderIds;
	}
	public List<Long> getContentGatekeeperIds() {
		return contentGatekeeperIds;
	}
	public void setContentGatekeeperIds(List<Long> contentGatekeeperIds) {
		this.contentGatekeeperIds = contentGatekeeperIds;
	}

}

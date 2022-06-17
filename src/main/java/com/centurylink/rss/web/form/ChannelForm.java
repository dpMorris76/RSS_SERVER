package com.centurylink.rss.web.form;

import com.centurylink.rss.domain.entity.Channel;

public class ChannelForm {
	private String channelTitle;
	private String channelLink;
	private String channelCategories;
	private String channelCommentsLink;
	private String channelEditorEmail;
	private String channelDescription;
	private String channelNewGroup;
	private Long chanId;
	private Long grpId;
	
	public ChannelForm () {
		
	}
	
	public ChannelForm(Channel c) {
		this.chanId = c.getId();
		this.channelTitle = c.getTitle();
		this.channelLink = c.getLink();
		this.channelEditorEmail = c.getManagingEditor();
		this.channelDescription = c.getDescription();
		this.grpId = c.getChannelGroup().getId();
	}
	public String getChannelTitle() {
		return channelTitle;
	}
	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}
	public String getChannelLink() {
		return channelLink;
	}
	public void setChannelLink(String channelLink) {
		this.channelLink = channelLink;
	}
	public String getChannelCategories() {
		return channelCategories;
	}
	public void setChannelCategories(String channelCategories) {
		this.channelCategories = channelCategories;
	}
	public String getChannelCommentsLink() {
		return channelCommentsLink;
	}
	public void setChannelCommentsLink(String channelCommentsLink) {
		this.channelCommentsLink = channelCommentsLink;
	}
	public String getChannelEditorEmail() {
		return channelEditorEmail;
	}
	public void setChannelEditorEmail(String channelAuthorEmail) {
		this.channelEditorEmail = channelAuthorEmail;
	}
	public Long getGrpId() {
		return grpId;
	}
	public void setGrpId(Long grpId) {
		this.grpId = grpId;
	}
	public String getChannelDescription() {
		return channelDescription;
	}
	public void setChannelDescription(String channelDescription) {
		this.channelDescription = channelDescription;
	}
	public String getChannelNewGroup() {
		return channelNewGroup;
	}
	public void setChannelNewGroup(String channelNewGroup) {
		this.channelNewGroup = channelNewGroup;
	}
	public Long getChanId() {
		return chanId;
	}
	public void setChanId(Long chanId) {
		this.chanId = chanId;
	}
}

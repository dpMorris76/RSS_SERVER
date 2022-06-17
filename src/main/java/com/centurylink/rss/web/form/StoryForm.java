package com.centurylink.rss.web.form;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.centurylink.rss.domain.entity.Channel;
import com.centurylink.rss.domain.entity.Link;
import com.centurylink.rss.domain.entity.Story;

public class StoryForm {
	private String storyTitle;
	private String storyLink;
	private String storyLinkName;
	private String storyCategories;
	private String storyCommentsLink;
	private String storyAuthorEmail;
	private String storyPublishDate;
	private String storyDescription;
	private String storyPocName;
	private String storyPocPhNbr;
//	private String storyChannels;
//	private List<Long> channelIds;
	private Long channelId;
	private Long storyId;
	private String expirationDate;
	private Boolean isHighPriority;
	private String approvedBy = "";
	private List<Link> links = new ArrayList<Link>();
	
	public StoryForm() {
		this.isHighPriority = false;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		   //get current date time with Date()
		   Date date = new Date();
		   this.storyPublishDate = dateFormat.format(date);
		   
		   Date expdate = new Date();
		   Calendar cal = Calendar.getInstance();
		   cal.setTime(expdate);
		   cal.add(Calendar.DATE, 14);
		   expdate = cal.getTime();
		   this.expirationDate = dateFormat.format(expdate);
	}
	
	public StoryForm(Story s) {
		this.storyAuthorEmail = s.getAuthor();
		this.storyCategories = s.getCategory();
//		this.storyChannels = s.getChannels();
		Set<Channel> channels = s.getChannels();
		List<Long> ids = new ArrayList<Long>();
		for (Channel c : channels) {
			ids.add(c.getId());
		}
//		this.channelIds = ids;
		this.channelId = ids.get(0);
		this.storyCommentsLink = s.getComments();
		this.storyDescription = s.getDescription();
//		this.storyLink = s.getLink();
//		this.storyLinkName = s.getLinkName();
		if(s.getLinks() != null){
			this.links.addAll(s.getLinks());
		}
//		this.links.addAll(s.getLinks());
//		SimpleDateFormat inFormatter = new SimpleDateFormat("MM/dd/yyyy' 'hh:mm");
		SimpleDateFormat inFormatter = new SimpleDateFormat("yyyy/MM/dd' 'HH:mm");
		this.storyPublishDate = inFormatter.format(s.getPublishDate()).toString();
		this.expirationDate = inFormatter.format(s.getExpirationDate()).toString();
		this.storyTitle = s.getTitle();
		this.storyId = s.getId();
		this.isHighPriority = s.getIsHighPriority();
		this.storyPocName = s.getPocName();
		this.storyPocPhNbr = s.getPocPhNbr();
	}
	
	public Long getStoryId() {
		return storyId;
	}

	public void setStoryId(Long storyId) {
		this.storyId = storyId;
	}
	
	public String getStoryTitle() {
		return storyTitle;
	}
	
	public void setStoryTitle(String storyTitle) {
		this.storyTitle = storyTitle;
	}
	
	public String getStoryLink() {
		return storyLink;
	}
	
	public void setStoryLink(String storyLink) {
		this.storyLink = storyLink;
	}
	
	public String getStoryLinkName() {
		return storyLinkName;
	}

	public void setStoryLinkName(String storyLinkName) {
		this.storyLinkName = storyLinkName;
	}

	public String getStoryCategories() {
		return storyCategories;
	}
	
	public void setStoryCategories(String storyCategories) {
		this.storyCategories = storyCategories;
	}
	
	public String getStoryCommentsLink() {
		return storyCommentsLink;
	}
	
	public void setStoryCommentsLink(String storyCommentsLink) {
		this.storyCommentsLink = storyCommentsLink;
	}
	
	public String getStoryAuthorEmail() {
		return storyAuthorEmail;
	}
	
	public void setStoryAuthorEmail(String storyAuthorEmail) {
		this.storyAuthorEmail = storyAuthorEmail;
	}
	
	public String getStoryPublishDate() {
		return storyPublishDate;
	}
	
	public void setStoryPublishDate(String storyPublishDate) {
		this.storyPublishDate = storyPublishDate;
	}
	
	public String getStoryDescription() {
		return storyDescription;
	}
	
	public void setStoryDescription(String storyDescription) {
		this.storyDescription = storyDescription;
	}
	
	public void setApprovedBy(String approvedBy)
	{
		this.approvedBy = approvedBy;
	}
	
	public String getApprovedBy()
	{
		return approvedBy;
	}
	
//	public String getStoryChannels() {
//		return storyChannels;
//	}
	
//	public void setStoryChannels(String storyChannels) {
//		this.storyChannels = storyChannels;
//	}
	
	public String getExpirationDate() {
		return expirationDate;
	}
	
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Boolean getIsHighPriority() {
		return isHighPriority;
	}

	public void setIsHighPriority(Boolean isHighPriority) {
		this.isHighPriority = isHighPriority;
	}

	public String getStoryPocName() {
		return storyPocName;
	}

	public void setStoryPocName(String storyPocName) {
		this.storyPocName = storyPocName;
	}

	public String getStoryPocPhNbr() {
		return storyPocPhNbr;
	}

	public void setStoryPocPhNbr(String storyPocPhNbr) {
		this.storyPocPhNbr = storyPocPhNbr;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	

//	public List<Long> getChannelIds() {
//		return channelIds;
//	}
//
//	public void setChannelIds(List<Long> channelIds) {
//		this.channelIds = channelIds;
//	}
}

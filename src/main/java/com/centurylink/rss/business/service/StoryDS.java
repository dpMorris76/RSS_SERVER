package com.centurylink.rss.business.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.centurylink.rss.domain.entity.Channel;
import com.centurylink.rss.domain.entity.ExpiredStory;
import com.centurylink.rss.domain.entity.Link;
import com.centurylink.rss.domain.entity.Story;
import com.centurylink.rss.domain.entity.User;
import com.centurylink.rss.web.form.ExpiredGridForm;
import com.centurylink.rss.web.form.StoryForm;

@Service
public class StoryDS {
	@Autowired
	DataService ds;
	
	@Autowired 
	ChannelDS channelS;
	
	private static final Logger logger = Logger.getLogger(StoryDS.class);

	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Story> findExpiredStories()
	{
		return Story.findExpiredStories();
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(Story s) {
		Story.saveStory(s);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Story findById(long id) {
		return Story.findById(id);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Story> findByApprovalStatus(String approvalStatus) {
		return Story.findStoriesByApprovalStatus(approvalStatus);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Story> findByAuthor(User user) {
		return Story.findStoriesByAuthor(user);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Story> findAll() {
		return Story.findAllStories();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateFromForm(StoryForm storyForm) {
		Story.updateStoryFromForm(storyForm);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateAndApproveFromForm(StoryForm storyForm) {
		Story.updateAndApproveStoryFromForm(storyForm);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Story saveNewFromForm(StoryForm storyForm, String deployPath) {
		return Story.saveNewStoryFromForm(storyForm, deployPath);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Story saveAndApproveNewFromForm(StoryForm storyForm, String userFullName) {
		return Story.saveAndApproveNewStoryFromForm(storyForm, userFullName);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void createNewChannelMetaStory(Long newChannelId, String deployPath) {
		Story.createNewChannelMetaStory(newChannelId, deployPath);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Story denySubmission(Long id, String denialReasons) {
		return Story.denySubmission(id, denialReasons);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Story approveSubmission(Long id, User user) {
		return Story.approveSubmission(id, user);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteStory(Story s) {
		Story.deleteStory(s);
	}
	
	//Expired Story
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<ExpiredStory> searchExpiredStories(ExpiredGridForm exStory) {
		return ExpiredStory.searchExpiredStories(exStory);
	}
	
	/**
	 * Deletes all the stories given to it, and adds them to the expired stories table.
	 * @param expiredStory
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void expireStory(Story expireStory){
		logger.debug("Expiring story: " + expireStory.getTitle());
		if (ExpiredStory.findById(expireStory.getId()) == null) {
			ExpiredStory es = new ExpiredStory(expireStory);
			if (!es.getChannelName().equals("")) {
				ExpiredStory.saveExpiredStory(es);
				Link.deleteAllLinksForStory(expireStory);
				deleteStory(expireStory);
			} else {
				logger.error("While trying to expire a story with ID:" + expireStory.getId() + " the story had no channels.");
				logger.error("Either delete this story in the database, or make an entry in CHANNEL_STORIES for this story.");
			}
		} else {
			logger.error("While trying to expire a story with ID:" + expireStory.getId() + " another story was found with the same ID.");
			logger.error("The story should be deleted correctly, but you might need to check and make sure.");
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ExpiredStory findExpiredStoryById(Long id) {
		return ExpiredStory.findById(id);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveLink(Link link){
		Link.saveLink(link);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Link findLinkById(Long id){
		return Link.findLinkByid(id);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Link> findAllLinks(){
		return Link.findAllLinks();
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Link> findAllLinksByStory(Story story){
		return Link.findAllLinksByStory(story);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Story createStoryFromExpiredStory(Long id){
		Story story = new Story();
		ExpiredStory e = findExpiredStoryById(id);
		
		story.setApprovedBy(e.getApprovedBy());
		story.setApprovalStatus(e.getApprovalStatus());
		story.setAuthor(e.getAuthor());
		story.setCategory(e.getCategory());
		
		Set<Channel> channelSet = new HashSet<Channel>();
		for(String s: e.getChannelName().split(",")){
			channelSet.add(channelS.findChannelByTitle(s));
		}
		story.setChannels(channelSet);
		story.setComments(e.getComments());
		story.setCount(e.getCount());
		story.setDescription(e.getDescription());
		story.setEnclosure(e.getEnclosure());
		story.setExpirationDate(e.getExpirationDate());
		story.setGuid(e.getGuid());
		story.setIsHighPriority(e.getIsHighPriority());
		story.setPublishDate(e.getPublishDate());
		story.setRejectedReasoning(e.getRejectedReasoning());
		story.setSource(e.getSource());
		story.setTitle(e.getTitle());
		
		if(!e.getLink().equalsIgnoreCase("")){
			String[] linkStrings = e.getLink().split(";");
			String[] linkNameString = e.getLinkName().split(";");
			
			int currentLinkIndex = 0;
			List<Link> linkList = new ArrayList<Link>();
			while (currentLinkIndex < linkStrings.length && currentLinkIndex < linkNameString.length){
				Link currentLink = new Link();
				currentLink.setLink(linkStrings[currentLinkIndex]);
				currentLink.setLinkName(linkNameString[currentLinkIndex]);
				currentLink.setStory(story);
				linkList.add(currentLink);
				currentLinkIndex++;
			}
			story.setLinks(linkList);
		}

		return story;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteExpiredStory(ExpiredStory eStory) {
		logger.info("Deleting expired story with id: " + eStory);
		ExpiredStory.deleteExpiredStory(eStory);
	}
	
}

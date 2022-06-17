package com.centurylink.rss.domain.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.centurylink.rss.domain.entity.util.HibernateUtil;
import com.centurylink.rss.web.form.ExpiredGridForm;

@Entity
@Table(name = "expired_stories")
public class ExpiredStory implements Serializable, Comparable {
	private static final Logger logger = Logger.getLogger(ExpiredStory.class);
	
	@Transient
	private Long version = 1L;

	@Transient
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", length = 11, unique=true)
	private Long id;
	
	@Column(name = "title", length = 50, nullable = false)
	private String title;
	
	@Column(name="channel_name", length= 100, nullable = false)
	private String channelName;
	
	@Column(name = "link", length = 2000, nullable = true)
	private String link;
	
	@Column(name = "link_name", length = 300, nullable = true)
	private String linkName;
	
	@Lob
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "author", length = 50, nullable = false)
	private String author;
	
	@Column(name = "category", length = 200, nullable = false)
	private String category;
	
	@Column(name = "comments", length = 50, nullable = true)
	private String comments;
	
	@Column(name = "enclosure", length = 50, nullable = true)
	private String enclosure;
	
	@Column(name = "guid", length = 100, nullable = true)
	private String guid;
	
	@Column(name = "publish_date", nullable = false)
	private Date publishDate;
	
	@Column(name = "source", length = 100, nullable = true)
	private String source;
	
	@Column(name = "approval_status", length = 10, nullable = false)
	private String approvalStatus;
	
	@Column(name = "rejected_reasoning", length = 100, nullable = true)
	private String rejectedReasoning;
	
	@Column(name = "expiration_date", nullable = false)
	private Date expirationDate;
	
	@Column(name = "count", nullable = true)
	private Integer count;
	
	@Column(name = "is_high_priority", nullable = false)
	private Boolean isHighPriority;
	
	@Column(name = "approved_by", length = 30, nullable = true)
	private String approvedBy;
	
	public ExpiredStory(){
		
	}
	
	public ExpiredStory(Story s){
		this.approvedBy = s.getApprovedBy();
		this.approvalStatus = s.getApprovalStatus();
		this.author = s.getAuthor();
		this.category = s.getCategory();
		this.channelName = "";
		for(Channel c: s.getChannels()){
			this.channelName += c.getTitle() + ", ";
		}
		if(this.channelName.length() > 3){
			this.channelName = this.channelName.substring(0, this.channelName.length() - 2);
		}
		if(this.channelName.length() > 100){
			this.channelName = this.channelName.substring(0, 99);
		}
		this.comments = s.getComments();
		this.count = s.getCount();
		this.description = s.getDescription();
		this.enclosure = s.getEnclosure();
		this.expirationDate = s.getExpirationDate();
		this.guid = s.getGuid();
		this.id = s.getId();
		this.isHighPriority = s.getIsHighPriority();
		
		String linkString = "";
		String linkNameString = "";
		
		// TODO MAKE THIS WORK ... -Brandon did it
		for(Link l: s.getLinks()){
			linkString += l.getLink() + ";";
			linkNameString += l.getLinkName() + ";";
		}
		if (!linkString.equals("")) {
			linkString = linkString.substring(0, linkString.length()-1);
			linkNameString = linkNameString.substring(0, linkNameString.length()-1);
		}
		
		this.link = linkString;
		this.linkName = linkNameString;
		this.publishDate = s.getPublishDate();
		this.rejectedReasoning = s.getRejectedReasoning();
		this.source = s.getSource();
		this.title = s.getTitle();		
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		if (link == null){
			return "";
		}else if (link.equals("null") ){
			return "";
		}
		else return link;
	}

	public void setLink(String link) {
		if (link.trim().equals("")) this.link = "null";
		else this.link = link;
	}
	
	public String getLinkName() {
		if (linkName == null){
			return "";
		}else if (linkName.equals("null") ){
			return "";
		}
		else return linkName;
	}
	
	public void setLinkName(String linkName) {
		if (linkName.trim().equals("")) this.linkName = "null";
		else this.linkName = linkName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getEnclosure() {
		return enclosure;
	}

	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getRejectedReasoning() {
		return rejectedReasoning;
	}

	public void setRejectedReasoning(String rejectedReasoning) {
		this.rejectedReasoning = rejectedReasoning;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getCount() {
		return count;
	}
	
	public Boolean getIsHighPriority() {
		return isHighPriority;
	}

	public void setIsHighPriority(Boolean isHighPriority) {
		this.isHighPriority = isHighPriority;
	}
	
	public String getApprovedBy() {
		return approvedBy;
	}
	
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	
	public List<Link> getLinks() {
		List<Link> links = new ArrayList<Link>();
		if (this.link != null && !this.link.trim().equals("")) {
			String[] linkURLs = this.link.split(";");
			String[] linkNames = this.linkName.split(";");
			
			for (int i = 0; i < linkURLs.length; i++) {
				Link l = new Link();
				if (linkURLs[i].equalsIgnoreCase("")) {
					break;
				} else {
					l.setLink(linkURLs[i]);
				}
				if (linkNames[i].equalsIgnoreCase("")) {
					l.setLinkName("Link " + String.valueOf(i));
				} else {
					l.setLinkName(linkNames[i]);
				}
				links.add(l);
			}
		}
		
		return links;
	}

	//DAO

	public static void saveExpiredStory(ExpiredStory expiredStory) {
		logger.info("Saving Expired Story "+expiredStory.getTitle()+"("+expiredStory.getId()+") to database");
		logger.info("Saving Expiring Story with the following info: \n"+
					"Expire Story id :" + expiredStory.getId() + "\n" +
					"Publish Date: " + expiredStory.getPublishDate() + "\n" + 
					"Expire Date: " + expiredStory.getExpirationDate() + "\n" +
					"Reject/Deny Reasoning: " + expiredStory.getRejectedReasoning() + "\n" + 
					"Approved By: " +  expiredStory.getApprovedBy() + "\n" + 
					"Approved Status: " +  expiredStory.getApprovalStatus() + "\n" + 
					"high Priority: " +  expiredStory.getIsHighPriority() + "\n" + 
					"Author: " + expiredStory.getAuthor() + "\n" + 
					"Channel: " + expiredStory.getChannelName() + "\n" + 
					"Comment: " + expiredStory.getComments() + "\n" +
					"Description: " + expiredStory.getDescription() + "\n" +
					"Category: " + expiredStory.getCategory() + "\n"
				);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.saveOrUpdate(expiredStory);
	}
	
	public static ExpiredStory findById(Long id) {
		logger.debug("Looking for story with id "+id);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(ExpiredStory.class);
		crit.add(Restrictions.eq("id", id));
		@SuppressWarnings("unchecked")
		List<ExpiredStory> results = crit.list();
		if(results.size() > 0){
			return results.get(0);
		} else {
			return null;
		}
	}

	@Deprecated
	public static List<ExpiredStory> findAllExpiredStories() {
		logger.trace("Searcing for all stories");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(ExpiredStory.class);
		@SuppressWarnings("unchecked")
		List<ExpiredStory> results = crit.list();
		
		return results;
	}
	
	public static List<ExpiredStory> searchExpiredStories(ExpiredGridForm exStory) {
		logger.info("Searcing for expired stories");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(ExpiredStory.class);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date from = null;
		Date to = null;
		boolean search = false;
		
		if (exStory.getFrom() != null && !exStory.getFrom().trim().equals("")) {
			try{
				from = dateFormat.parse(exStory.getFrom());
			} catch(ParseException e) {
				logger.error(e);
				from = null;
			}
		}
		if (exStory.getTo() != null && !exStory.getTo().trim().equals("")) {
			try {
				to = dateFormat.parse(exStory.getTo());
			} catch(ParseException e) {
				logger.error(e);
				to = null;
			}
		}
		
        if (from != null && to != null) {
			search = true;
			crit.add(Restrictions.between("publishDate", from, to));
			crit.add(Restrictions.between("expirationDate", from, to));
		} else {
			if(from != null ) {
				search = true;
				crit.add(Restrictions.between("publishDate", from, new Date()));
				crit.add(Restrictions.between("expirationDate", from, new Date()));
			}
			else if (to != null) {
				search = true;
				crit.add(Restrictions.between("publishDate", new Date(0), to));
				crit.add(Restrictions.between("expirationDate", new Date(0), to));
			}
		}
        
		if (exStory.getTitle() != null && !exStory.getTitle().trim().equals("")) {
			search = true;
			crit.add(Restrictions.like("title", exStory.getTitle(), MatchMode.ANYWHERE).ignoreCase());
		}
		
		if (exStory.getChannel() != null && !exStory.getChannel().trim().equals("")) {
			search = true;
			crit.add(Restrictions.like("channelName", exStory.getChannel(), MatchMode.ANYWHERE).ignoreCase());
		}
		
		if (exStory.getApprovedBy() != null && !exStory.getApprovedBy().trim().equals("")) {
			search = true;
			
			crit.add(Restrictions.like("approvedBy", "%" + exStory.getApprovedBy() + "%", MatchMode.START).ignoreCase());
		}
		
		if (exStory.getAuthor() != null && !exStory.getAuthor().trim().equals("")) {
			
			List<User> userList = new ArrayList<User>();
			if(exStory.getAuthor().contains(",")){
				userList.addAll(User.findUsersByName(exStory.getAuthor().substring(exStory.getAuthor().indexOf(",")+1, exStory.getAuthor().length()).trim(),exStory.getAuthor().substring(0, exStory.getAuthor().indexOf(",")-1).trim()));
			}else{
				userList.addAll(User.findUsersByLastName(exStory.getAuthor()));
			}
			for(User u: userList){
				search = true;
				crit.add(Restrictions.like("author", "%" + u.getEmail() + "%", MatchMode.START).ignoreCase());
			}
		}
		if (search) {
			@SuppressWarnings("unchecked")
			List<ExpiredStory> results = crit.list();
			return results;
		}
		else
		{
			return new ArrayList<ExpiredStory>();
		}
	}
	
	public static List<ExpiredStory> findExpiredStoriesByAuthor(User user) {
		logger.debug("Searching for all stories authored by "+user.getFullName());
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(ExpiredStory.class);
		crit.add(Restrictions.eq("author", user.getEmail()));
		@SuppressWarnings("unchecked")
		List<ExpiredStory> results = crit.list();
		
		return results;
	}
	
	public static void deleteExpiredStory(ExpiredStory eStory){
		logger.debug("Deleting expired story with id: " + eStory.getId());
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.delete(eStory);
	}
	
	@Override
	public int compareTo(Object arg0) {
		ExpiredStory arg = (ExpiredStory)arg0;
		return this.title.compareToIgnoreCase(arg.title);
	}
	
	// works for seemingly no reason
	static class ExpiredStoryComparator implements Comparator<ExpiredStory> {
		@Override
		public int compare(ExpiredStory c1, ExpiredStory c2)
		{
			return c1.compareTo(c2);
		}
	}
	
	private static ExpiredStoryComparator expiredStoryComparator = new ExpiredStoryComparator();
	
	public static ExpiredStoryComparator getComparator() {
		return expiredStoryComparator;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
}

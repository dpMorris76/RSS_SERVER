package com.centurylink.rss.domain.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.centurylink.rss.domain.entity.util.HibernateUtil;
import com.centurylink.rss.web.form.StoryForm;

@Entity
@Table(name = "stories")
public class Story implements Serializable, Comparable {
	
	private static final Logger logger = Logger.getLogger(Story.class);
	
	public static final String APPROVED_STATUS = "Approved";
	public static final String PENDING_STATUS = "Pending";
	public static final String REJECTED_STATUS = "Rejected";
	public static final String EXPIRED_STATUS = "Expired";
	
	// testing stuff w/ jsp pages.
	// this is useful, because I don't really like getters and setters
	// and because it can be called in jsp page
	// otherwise it defaults to calling get / set APPROVED_STATUS
	public static String APPROVED_STATUS()
	{
		return APPROVED_STATUS;
	}
	public static String REJECTED_STATUS()
	{
		return REJECTED_STATUS;
	}
	public static String PENDING_STATUS()
	{
		return PENDING_STATUS;
	}
	
	@Transient
	private Long version = 1L;

	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "hibernate_seq_gen", sequenceName = "HIBERNATE_SEQ",  allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_seq_gen")
	@Column(name = "id", length = 11, unique=true)
	private Long id;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "channel_stories", joinColumns = { @JoinColumn(name = "story_id") }, inverseJoinColumns = { @JoinColumn(name = "channel_id") })
	private Set<Channel> channels;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "story", fetch = FetchType.LAZY, orphanRemoval=true)
	private List<Link> links;
	
	@Column(name = "title", length = 50, nullable = false)
	private String title;
	
	@Lob
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "author", length = 50, nullable = false)
	private String author;
	
	@Column(name = "category", length = 50, nullable = false)
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
	
	@Column(name = "approved_by", nullable = true)
	private String approvedBy;
	
	@Column(name = "POC_NAME", nullable = true, length=100)
	private String pocName;
	
	@Column(name = "POC_PH_NBR", nullable = true, length=25)
	private String pocPhNbr;
	
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

	public Set<Channel> getChannels() {
		return channels;
	}

	public void setChannels(Set<Channel> channels) {
		this.channels = channels;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

//	public String getLink() {
//		if (link.equals("null")) return "";
//		else return link;
//	}
//
//	public void setLink(String link) {
//		if (link.trim().equals("")) this.link = "null";
//		else this.link = link;
//	}
//	
//	public String getLinkName() {
//		if (linkName.equals("null")) return "";
//		else return linkName;
//	}
//	
//	public void setLinkName(String linkName) {
//		if (linkName.trim().equals("")) this.linkName = "null";
//		else this.linkName = linkName;
//	}
	
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
	
	public String getPocName() {
		return pocName;
	}
	public void setPocName(String pocName) {
		this.pocName = pocName;
	}
	public String getPocPhNbr() {
		return pocPhNbr;
	}
	public void setPocPhNbr(String pocPhNbr) {
		this.pocPhNbr = pocPhNbr;
	}
	
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public static Story saveAndApproveNewStoryFromForm(StoryForm storyForm, String userFullName) {
		logger.debug("Creating a new story from form");
		Story story = new Story();
		story.setApprovedBy(userFullName);
		story.setApprovalStatus(APPROVED_STATUS);
		saveStoryFromForm(story, storyForm);
		return story;
	}
	
	public static Story saveNewStoryFromForm(StoryForm storyForm, String deployPath) {
		logger.debug("Creating a new story from form");
		Story story = new Story();
		story.setApprovalStatus(PENDING_STATUS);
		saveStoryFromForm(story, storyForm);
//		createNewMetaStory(story, deployPath);
		return story;
	}
	
	public static void updateAndApproveStoryFromForm(StoryForm storyForm) {
		logger.debug("Updating existing story from form");
		Story story = findById(storyForm.getStoryId());
		story.setApprovalStatus(APPROVED_STATUS);
		saveStoryFromForm(story, storyForm);
	}
	
	public static void updateStoryFromForm(StoryForm storyForm) {
		logger.debug("Updating existing story from form");
		Story story = findById(storyForm.getStoryId());
		story.setApprovalStatus(PENDING_STATUS);
		saveStoryFromForm(story, storyForm);
	}
	
	private static void saveStoryFromForm(Story story, StoryForm storyForm) {
		logger.debug("Updating existing story from form");
		story.setAuthor(storyForm.getStoryAuthorEmail());
//		List<Long> channelIds = storyForm.getChannelIds();
		List<Long> channelIds = new ArrayList<Long>();
		channelIds.add(storyForm.getChannelId());
		Set<Channel> channels = new HashSet<Channel>();
		String category = "";
		String comma = "";
		for (Long id : channelIds) {
			Channel channel = Channel.findById(id);
			channels.add(channel);
			category += comma + channel.getTitle();
			comma = ", ";
		}
		story.setCategory(category.trim());
		story.setChannels(channels);
		story.setComments(storyForm.getStoryCommentsLink());
		story.setDescription(storyForm.getStoryDescription());
		
		//TODO remove this when the client gets updates
		story.setDescription(story.getDescription());
		
		story.setIsHighPriority(storyForm.getIsHighPriority());
		//Date form needs to be converted to an actual 'Date' type
		String formPubDate = storyForm.getStoryPublishDate();
		String formExpDate = storyForm.getExpirationDate();
		SimpleDateFormat inFormatter = new SimpleDateFormat("yyyy/MM/dd' 'HH:mm");
		try {
			Date date = inFormatter.parse(formPubDate);
//			System.out.println("PUBLISHED DATE PARSED '"+date+"'");
			story.setPublishDate(date);
		} catch (ParseException e) {
			logger.error("Selected date failed to parse");
			Date today = new Date();
			story.setPublishDate(today);
			e.printStackTrace();
		}
		if (formExpDate != null && !formExpDate.equals("")) {
			try {
				Date date = inFormatter.parse(formExpDate);
//				System.out.println("PUBLISHED DATE PARSED '"+date+"'");
				story.setExpirationDate(date);
			} catch (ParseException e) {
				logger.error("Selected date failed to parse");
				Date today = new Date();
				story.setExpirationDate(today);
				e.printStackTrace();
			}
		} else {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(story.getPublishDate());
			calendar.add(Calendar.DATE, 14);
			story.setExpirationDate(calendar.getTime());
		}
		story.setTitle(storyForm.getStoryTitle());
		story.setPocPhNbr(storyForm.getStoryPocPhNbr());
		story.setPocName(storyForm.getStoryPocName());
		
		if(story.getId() != null){
			Link.deleteAllLinksForStory(story);
		}
//		story.setApprovalStatus("Pending");
		saveStory(story);
		
		String[] linkURLs = storyForm.getStoryLink().split(",");
		String[] linkNames = storyForm.getStoryLinkName().split(",");
		
		for(int i=0;  i< linkURLs.length; i++){
			Link l = new Link();
			if(linkURLs[i].equalsIgnoreCase("")){
				break;
			}else{
				l.setLink(linkURLs[i].replaceAll("\\|@\\|", ","));
			}
			if(linkNames[i].equalsIgnoreCase("")){
				l.setLinkName("Link " + String.valueOf(i));
			}else{
				l.setLinkName(linkNames[i].replaceAll("\\|@\\|", ","));
			}
			l.setStory(story);
			l.saveLink(l);
		}
	}
	
	public static void saveStory(Story story) {
		logger.info("Saving story"+story.getTitle()+"("+story.getId()+") to database");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.saveOrUpdate(story);
	}
	
	public static List<Story> findAllStories() {
		logger.trace("Searcing for all stories");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(Story.class);
		@SuppressWarnings("unchecked")
		List<Story> results = crit.list();
		
		return results;
	}
	
	/**
	 * We don't grab anything that is not approved && expired.
	 * @return
	 */
	public static List<Story> findExpiredStories() {	
		Date rn = new Date();
		logger.debug("Searching for all stories expired at: " + rn.toString());
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(Story.class);
		crit.add(Restrictions.lt("expirationDate", rn));
		crit.add(Restrictions.eq("approvalStatus", Story.APPROVED_STATUS));
		@SuppressWarnings("unchecked")
		List<Story> results = crit.list();
		return results;
	}
	
	public static List<Story> findStoriesByAuthor(User user) {
		logger.debug("Searching for all stories authored by "+user.getFullName());
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(Story.class);
		crit.add(Restrictions.eq("author", user.getEmail()));
		@SuppressWarnings("unchecked")
		List<Story> results = crit.list();
		
		return results;
	}
	
	public static List<Story> findStoriesByApprovalStatus(String approvalStatus) {
		logger.debug("Searching for all stories authored in "+approvalStatus+" status");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(Story.class);
		crit.add(Restrictions.eq("approvalStatus", approvalStatus));
		@SuppressWarnings("unchecked")
		List<Story> results = crit.list();
		
		return results;
	}
	
	public static Story findById(Long id) {
		logger.debug("Looking for story with id "+id);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(Story.class);
		crit.add(Restrictions.eq("id", id));
		@SuppressWarnings("unchecked")
		List<Story> results = crit.list();
		
		if (results != null && results.size() >= 1) {
			return results.get(0);
		} else {
			return null;
		}
	}
	
	public static void deleteStory(Story s){
		logger.debug("Deleting story with id: " + s.getId());
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		for(Channel c: s.getChannels()){
			c.getStories().remove(s); // because if s is in there then we have a problem Houston. 
			Channel.saveChannel(c);
		}
		s.getChannels().clear();
		saveStory(s); // to save the fact that we cleared all of the channels from it. 
		session.delete(s);
	}
	
	@Deprecated
	private static void createNewMetaStory(Story actual, String deployPath) {
		logger.info("Creating admin approval story for story "+actual.getTitle()+"("+actual.getId()+")");
		Story meta = new Story();
		
		String titleString = actual.getAuthor()+" is requesting content approval.";
		meta.setTitle(titleString);
		
		String descString = "User <b>"+actual.getAuthor()+"</b> is requesting approval for a story titled '<b>"+actual.getTitle()+"</b>' in channel: <b>";
		String comma = "";
		for (Channel chan : actual.getChannels()) {
			descString += comma+chan.getTitle();
			comma = ", ";
		}
		descString += "</b>. This story may be viewed in full from within the app.";
		meta.setDescription(descString);
		
		Date today = new Date();
		meta.setPublishDate(today);
		
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(meta.getPublishDate());
		calendar.add(Calendar.DATE, 14);
		meta.setExpirationDate(calendar.getTime());
		
//		System.err.println(deployPath);
		logger.debug("Setting link for meta story to: " + deployPath);
//		meta.setLink(deployPath + "/secure/submissionReview?storyId=" + actual.getId());
		meta.setAuthor("AUTOMATED");
		
		
		Set<Channel> channels = new HashSet<Channel>();
		channels.add(Channel.findById(Long.parseLong("1", 10)));
		meta.setChannels(channels);
		meta.setCategory("ADMIN FEED");
		meta.setIsHighPriority(false);
		meta.setApprovalStatus(APPROVED_STATUS);
		
		saveStory(meta);
	}
	
	@Deprecated
	public static void createNewChannelMetaStory(Long newChannelId, String deployPath) {
		logger.info("Creating new channel story for channel id: "+newChannelId);
		Channel newChannel = Channel.findById(newChannelId);
		Story meta = new Story();
		
		String titleString = "Channel Created";
		meta.setTitle(titleString);
		
		String descString = "Channel '<b>"+newChannel.getTitle()+"</b>' created on: <b>"+new Date()+"</b>";
		meta.setDescription(descString);
		
		Date today = new Date();
		meta.setPublishDate(today);
		
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(meta.getPublishDate());
		calendar.add(Calendar.DATE, 10000);
		meta.setExpirationDate(calendar.getTime());
		
		System.err.println(deployPath);
//		meta.setLink(deployPath);
		meta.setAuthor("AUTOMATED");
		meta.setIsHighPriority(false);
		
		
		Set<Channel> channels = new HashSet<Channel>();
		channels.add(newChannel);
		meta.setChannels(channels);
		meta.setCategory("NEW");
		meta.setApprovalStatus(APPROVED_STATUS);
		
		saveStory(meta);
	}
	
	public static Story denySubmission(Long id, String denialReasons) {
		logger.debug("Denying story with id "+id+" with reason(s) "+denialReasons);
		Story s = findById(id);
		s.setApprovedBy(null);
		updateStoryApprovalStatus(s, REJECTED_STATUS, denialReasons);
		return s;
	}
	
	public static Story approveSubmission(Long id, User user) {
		logger.debug("Approving story with id "+id);
		//currentUser.getFullName()
		Story s = findById(id);
		s.setApprovedBy(user.getFullName()); // set this before update because update also saves.
		updateStoryApprovalStatus(s, APPROVED_STATUS, null);
		return s;
	}
	
	private static void updateStoryApprovalStatus(Story s, String newStatus, String denialReasons) {
		logger.debug("Modifying status of story id "+s.getId());
		s.setRejectedReasoning(denialReasons);
		s.setApprovalStatus(newStatus);
		saveStory(s);
	}
	
	@Override
	public int compareTo(Object arg0) {
		Story arg = (Story)arg0;
		return this.title.compareToIgnoreCase(arg.title);
	}
	
	// works for seemingly no reason
	static class StoryComparator implements Comparator<Story>{
		@Override
		public int compare(Story c1, Story c2)
		{
			return c1.compareTo(c2);
		}
	}
	
	private static StoryComparator storyComparator = new StoryComparator();
	
	public static StoryComparator getComparator()
	{
		return storyComparator;
	}
	
	
}

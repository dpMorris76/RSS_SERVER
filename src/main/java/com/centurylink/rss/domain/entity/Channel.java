package com.centurylink.rss.domain.entity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.centurylink.rss.domain.entity.util.HibernateUtil;
import com.centurylink.rss.web.form.ChannelForm;

@Entity
@Table(name = "channels")
public class Channel implements Serializable, Comparable<Channel> {
	
	private static final Logger logger = Logger.getLogger(Channel.class);
	
	public static final Long ADMIN_CHANNEL_ID = new Long(1);
	
	@Transient
	private Long version = 1L;

	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_seq_gen")
	@SequenceGenerator(name = "hibernate_seq_gen", sequenceName = "HIBERNATE_SEQ",  allocationSize = 1)
	@Column(name = "id", length = 11)
	private Long id;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "channel_stories", joinColumns = { @JoinColumn(name = "channel_id") }, inverseJoinColumns = { @JoinColumn(name = "story_id") })
	private Set<Story> stories;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "group_channels", joinColumns = { @JoinColumn(name = "channel_id") }, inverseJoinColumns = { @JoinColumn(name = "group_id") })
	private ChannelGroup channelGroup;
	
	@Column(name = "title", length = 50, nullable = false)
	private String title;
	
	@Column(name = "link", length = 100, nullable = false)
	private String link;
	
	@Column(name = "description", length = 200, nullable = false)
	private String description;
	
	@Column(name = "language", length = 20, nullable = false)
	private String language;
	
	@Column(name = "rating", length = 10, nullable = true)
	private String rating;
	
	@Column(name = "copyright", length = 10, nullable = true)
	private String copyright;
	
	@Column(name = "publish_date", nullable = true)
	private Date publishDate;
	
	@Column(name = "last_build_date", nullable = false)
	private Date lastBuildDate;
	
	@Column(name = "generator", length = 15, nullable = false)
	private String generator;
	
	@Column(name = "docs", length = 20, nullable = true)
	private String docs;
	
	@Column(name = "cloud", length = 40, nullable = true)
	private String cloud;
	
	@Column(name = "ttl", length = 40, nullable = true)
	private String ttl;
	
	@Column(name = "managing_editor", length = 50, nullable = true)
	private String managingEditor;

	@Column(name = "web_master", length = 50, nullable = true)
	private String webMaster;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Story> getStories() {
		return stories;
	}

	public void setStories(Set<Story> stories) {
		this.stories = stories;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public Date getLastBuildDate() {
		return lastBuildDate;
	}

	public void setLastBuildDate(Date lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public String getDocs() {
		return docs;
	}

	public void setDocs(String docs) {
		this.docs = docs;
	}

	public String getCloud() {
		return cloud;
	}

	public void setCloud(String cloud) {
		this.cloud = cloud;
	}

	public String getTtl() {
		return ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = ttl;
	}

	public String getManagingEditor() {
		return managingEditor;
	}

	public void setManagingEditor(String managingEditor) {
		this.managingEditor = managingEditor;
	}

	public String getWebMaster() {
		return webMaster;
	}

	public void setWebMaster(String webMaster) {
		this.webMaster = webMaster;
	}
	
	// DAO methods
	public static List<Channel> findAllChannels() {
		logger.debug("Grabbing list of all channels with non-null titles");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(Channel.class);
		crit.add(Restrictions.isNotNull("title"));
		@SuppressWarnings("unchecked")
		List<Channel> result = crit.list();
		return result;
	}
	
	public static void saveChannel(Channel c) {
		logger.info("Saving channel "+c.getTitle()+"("+c.getId()+") to database");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.saveOrUpdate(c);
	}
	
	public static Channel findById(Long id) {
		logger.debug("Searching for channel by id: "+id);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(Channel.class);
		crit.add(Restrictions.eq("id", id));
		@SuppressWarnings("unchecked")
		List<Channel> results = crit.list();
		
		return results.get(0);
	}
	public static Channel findChannelByTitle(String title) {
		logger.debug("Searching for channel by title: "+title);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(Channel.class);
		crit.add(Restrictions.eq("title", title));
		@SuppressWarnings("unchecked")
		List<Channel> results = crit.list();
		logger.debug("Found "+ results.size() + "channels with the title "+ title);

		return results.get(0);
	}
	
	

	
	public static Set<Channel> findMultipleById(Set<Long> ids){
		logger.debug("Finding a set of channels");
		Set<Channel> temp = new HashSet<Channel>();
		for(Long id : ids){
			temp.add(findById(id));
		}
		return temp;
	}
	
	public static Long saveNewChannelFromForm(ChannelForm channelForm) {
		logger.debug("Creating and saving a new channel from channel creation form.");
		Channel channel = new Channel();
		Long newChanId = saveChannelFromForm(channel, channelForm);
		return newChanId;
	}
	
	public static void updateChannelFromForm(ChannelForm channelForm) {
		logger.trace("Bringing up existing form to update channel");
		Channel channel = findById(channelForm.getChanId());
		saveChannelFromForm(channel, channelForm);
	}
	
	
	public static long saveChannelFromForm(Channel channel, ChannelForm channelForm) {
		logger.debug("Saving channel "+channel.title+" ("+channel.id+") from form");
		channel.setManagingEditor(channelForm.getChannelEditorEmail());
		ChannelGroup group;
		
		group = ChannelGroup.findById(channelForm.getGrpId());
		
		channel.setChannelGroup(group);
		channel.setDescription(channelForm.getChannelDescription());
		channel.setLink("http://"+channelForm.getChannelLink());
		channel.setGenerator("CTL RSS FEEDER");
		channel.setLanguage("English");
		Date today = new Date();
		channel.setLastBuildDate(today);
		channel.setPublishDate(new Date());		
		channel.setTitle(channelForm.getChannelTitle());
		saveChannel(channel);
		return channel.getId();
	}
	
	
	public static List<Channel> findChannelsByManagingEditor(User user) {
		logger.trace("Returning list of Channels by their Managing Editor");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(Channel.class);
		crit.add(Restrictions.eq("managingEditor", user.getEmail()));
		@SuppressWarnings("unchecked")
		List<Channel> results = crit.list();
		
		return results;
	}

	public ChannelGroup getChannelGroup() {
		return channelGroup;
	}

	public void setChannelGroup(ChannelGroup channelGroup) {
		this.channelGroup = channelGroup;
	}
	
	// works for seemingly no reason
	static class ChannelComparator implements Comparator<Channel>{
		@Override
		public int compare(Channel c1, Channel c2)
		{
			return c1.compareTo(c2);
		}
	}
	
	private static ChannelComparator channelComparator = new ChannelComparator();
	
	public static ChannelComparator getComparator()
	{
		return channelComparator;
	}

	@Override
	public int compareTo(Channel arg) {
		return this.title.compareToIgnoreCase(arg.title);
	}
	
}


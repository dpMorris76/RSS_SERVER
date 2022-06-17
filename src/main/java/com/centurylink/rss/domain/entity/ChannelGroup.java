package com.centurylink.rss.domain.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.centurylink.rss.domain.entity.util.HibernateUtil;
import com.centurylink.rss.web.form.ChannelForm;
import com.centurylink.rss.web.form.GroupForm;

@Entity
@Table(name = "groups")
public class ChannelGroup implements Comparable<ChannelGroup> {
	private static final Logger logger = Logger.getLogger(ChannelGroup.class);

	@Transient
	private Long version = 1L;

	@Transient
	private static final long serialVersionUID = 1L;
	
	public static final long FIELD_OPERATIONS_ID = 0;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_seq_gen")
	@SequenceGenerator(name = "hibernate_seq_gen", sequenceName = "HIBERNATE_SEQ",  allocationSize = 1)
	@Column(name = "id", length = 11)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "group_channels", joinColumns = { @JoinColumn(name = "group_id") }, inverseJoinColumns = { @JoinColumn(name = "channel_id") })
	private List<Channel> channels;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "auto_group_channels", joinColumns = { @JoinColumn(name = "group_id") }, inverseJoinColumns = { @JoinColumn(name = "channel_id") })
	private List<Channel> autoChannels;
	
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "group_content_providers", joinColumns = { @JoinColumn(name = "group_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
	private List<User> contentProviders;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "group_content_gatekeepers", joinColumns = { @JoinColumn(name = "group_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
	private List<User> contentGatekeepers;
	
//	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinTable(name = "group_content_error_receivers", joinColumns ={@JoinColumn(name = "group_id") }, inverseJoinColumns = { @JoinColumn(name= "user_id") })
//	private Set<User> contentErrorReceivers;
	
	@Column(name = "title", length = 50, nullable = false)
	private String title;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Channel> getChannels() {
		Collections.sort(channels);
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<User> getContentProviders() {
		Collections.sort(contentProviders);
		return contentProviders;
	}

	public void setContentProviders(List<User> contentProviders) {
		this.contentProviders = contentProviders;
	}

	public List<User> getContentGatekeepers() {
		Collections.sort(contentGatekeepers);
		return contentGatekeepers;
	}

//	public Set<User> getContentErrorReceivers(){
//		return contentErrorReceivers;
//	}
	public void setContentGatekeepers(List<User> contentGatekeepers) {
		this.contentGatekeepers = contentGatekeepers;
	}
	
//	/** This method is designed to return all the channels in a group in alphabetical order
//	*/
//	public List<Channel> getAlphaBetaChannels()
//	{
//		List<Channel> temp = new ArrayList<Channel>();
//		temp.addAll(this.channels);
//		temp.sort(Channel.getComparator());
//		
//		return temp;
//	}

	// DAO methods
	public static List<ChannelGroup> findAllGroups() {
		logger.debug("Searching for all groups");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(ChannelGroup.class);
		crit.add(Restrictions.isNotNull("title"));
		@SuppressWarnings("unchecked")
		List<ChannelGroup> result = crit.list();
		Collections.sort(result);
		return result;
	}

	public static void saveGroup(ChannelGroup g) {
		logger.info("Saving group "+g.getTitle()+"("+g.getId()+")");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.saveOrUpdate(g);
	}

	public static ChannelGroup findById(Long id) {
		logger.debug("Searching for group matching id "+id);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(ChannelGroup.class);
		crit.add(Restrictions.eq("id", id));
		@SuppressWarnings("unchecked")
		List<ChannelGroup> results = crit.list();
		if(results.size() == 0)
		{
			return null;
		}
		return results.get(0);
	}
	
	public static long saveGroupFromForm(ChannelForm channelForm) {
		logger.debug("Saving group from form");
		ChannelGroup group = new ChannelGroup();
		group.setTitle(channelForm.getChannelNewGroup());
		saveGroup(group);
		return group.getId();
	}
	
	public static long saveNewGroupFromForm(GroupForm groupForm) {
		logger.trace("Creating a new group to save form info to");
		ChannelGroup group = new ChannelGroup();
		return saveGroupFromForm(group, groupForm);
	}
	
	public static long updateGroupFromForm(GroupForm groupForm) {
		logger.debug("Updating group from form");
		ChannelGroup group = findById(groupForm.getGroupId());
		return saveGroupFromForm(group, groupForm);
	}
	
	public static long saveGroupFromForm(ChannelGroup group, GroupForm groupForm) {
		logger.debug("Saving group "+group.getTitle()+" ("+group.getId()+") to database");
		List<Long> contentReviewerIds = groupForm.getContentGatekeeperIds();
		List<User> contentReviewers = new ArrayList<User>();
		if (contentReviewerIds != null) {
			for (Long id : contentReviewerIds) {
				contentReviewers.add(User.findUser(id));
			}
		}
		group.setContentGatekeepers(contentReviewers);
		
		List<Long> contentProviderIds = groupForm.getContentProviderIds();
		List<User> contentProviders = new ArrayList<User>();
		if (contentProviderIds != null) {
			for (Long id : contentProviderIds) {
				contentProviders.add(User.findUser(id));
			}
		}
		group.setContentProviders(contentProviders);
		
		group.setTitle(groupForm.getGroupTitle());
		
		saveGroup(group);
		
		return group.getId();
	}
	
	// works for seemingly no reason
	static class ChannelGroupComparator implements Comparator<ChannelGroup>{
		@Override
		public int compare(ChannelGroup c1, ChannelGroup c2)
		{
			return c1.compareTo(c2);
		}
	}
	
	private static ChannelGroupComparator channelGroupComparator = new ChannelGroupComparator();
	
	public static ChannelGroupComparator getComparator()
	{
		return channelGroupComparator;
	}

	@Override
	public int compareTo(ChannelGroup arg) {
		return this.title.compareToIgnoreCase(arg.title);
	}

	public List<Channel> getAutoChannels() {
		Collections.sort(autoChannels);
		return autoChannels;
	}

	public void setAutoChannels(List<Channel> autoChannels) {
		this.autoChannels = autoChannels;
	}
	
}

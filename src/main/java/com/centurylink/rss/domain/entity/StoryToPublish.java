package com.centurylink.rss.domain.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.centurylink.rss.domain.entity.util.HibernateUtil;

/**
 * Class for use with the publish scheduler
 * it is an overglorified struct to get things back from the DB.
 * contains a date, a channel id, and it's own ID.
 * @author jehlmann
 *
 */
@Entity
@Table(name = "STORIES_TO_PUBLISH")
public class StoryToPublish {
	
	private static final Logger logger = Logger.getLogger(StoryToPublish.class);
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_seq_gen")
	@SequenceGenerator(name = "hibernate_seq_gen", sequenceName = "HIBERNATE_SEQ",  allocationSize = 1)
	@Column(name="id", length=11)
	private Long id;
	
	@Column(name = "story_id", length = 11)
	private Long storyId;
	
	@Column(name = "write_date")
	private Date date;

	public Long getStoryId() {
		return storyId;
	}

	public Date getDate() {
		return date;
	}

	public void setStoryId(Long channelId) {
		this.storyId = channelId;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public static StoryToPublish getEarliestStoryToPublish()
	{
		// I think that it's working now!!
		List set = HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from StoryToPublish where date =  (select min(date) from StoryToPublish)").list();
		logger.debug("finding minimum publish date.");
		if(set.size() != 0)
		{
			return (StoryToPublish)set.get(0);
		}
		else
		{
			return null; // we didn't have a min value right now.
		}
	}
	
	public static void saveCR(StoryToPublish cr) {
		logger.info("Saving CR with story ID: "+ cr.getStoryId() + " with publish date of: " + cr.getDate());
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.saveOrUpdate(cr);
	}
	
	public static StoryToPublish findSPByStoryId(long id)
	{
		List stuff = HibernateUtil.findByWhereClause(StoryToPublish.class, "where storyId = " + id);
		if(stuff.isEmpty())
		{
			return null;
		}
		else
		{
			return (StoryToPublish)stuff.get(0);
		}
	}
	
	public static boolean deleteByStoryId(long id)
	{
		StoryToPublish cr = StoryToPublish.findSPByStoryId(id);
		if(cr == null)
		{
			return false;
		}
		else
		{
			HibernateUtil.delete(cr);
			return true;
		}
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}

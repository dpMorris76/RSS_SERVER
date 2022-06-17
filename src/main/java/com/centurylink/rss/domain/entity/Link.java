package com.centurylink.rss.domain.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.centurylink.rss.domain.entity.util.HibernateUtil;


@Entity
@Table(name = "links")
public class Link implements Serializable{

	private static final Logger logger = Logger.getLogger(Link.class);

	@Transient
	private Long version = 1L;

	@Transient
	private static final long serialVersionUID = 1L;

	
	@Id
	@SequenceGenerator(name = "hibernate_seq_gen", sequenceName = "HIBERNATE_SEQ",  allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_seq_gen")
	@Column(name = "link_id", length = 11, unique=true)
	private Long id;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "story_id", nullable = false, updatable = true)
	private Story story;
	
	@Column(name = "link_url", length = 500, nullable = false)
	private String link;
		
	@Column(name = "link_name", length = 70, nullable = true)
	private String linkName;

	public Story getStory() {
		return story;
	}

	public void setStory(Story story) {
		this.story = story;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public Long getId() {
		return id;
	}
	
	public static void saveLink(Link link) {
		logger.info("Saving link "+link.getLinkName()+ "to story: "+ link.getStory().getId() +" to thedatabase");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.saveOrUpdate(link);
	}
	
	public static List<Link> findAllLinks() {
		logger.trace("Searcing for all links");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(Link.class);
		@SuppressWarnings("unchecked")
		List<Link> results = crit.list();
		
		return results;
	}
	
	public static Link findLinkByid(Long id) {
		logger.debug("Looking for link with id "+id);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(Link.class);
		crit.add(Restrictions.eq("id", id));
		@SuppressWarnings("unchecked")
		List<Link> results = crit.list();
		
		return results.get(0);
	}
	
	public static List<Link> findAllLinksByStory(Story story) {
		logger.debug("Looking for links with story id "+story.getId());
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(Link.class);
		crit.add(Restrictions.eq("story", story));
		@SuppressWarnings("unchecked")
		List<Link> results = crit.list();
		
		return results;
	}
	public static void deleteAllLinksForStory(Story story) {
		List<Link>  Links = story.getLinks();
		story.getLinks().clear();
		Story.saveStory(story);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		for(Link l: Links){
			session.delete(l);
		}
	}
}

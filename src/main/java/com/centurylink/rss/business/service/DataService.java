package com.centurylink.rss.business.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.centurylink.rss.domain.entity.ExpiredStory;
import com.centurylink.rss.domain.entity.Permission;
import com.centurylink.rss.domain.entity.Story;
import com.centurylink.rss.domain.entity.StoryToPublish;
import com.centurylink.rss.domain.entity.SupportContact;
import com.centurylink.rss.domain.entity.util.HibernateUtil;

@Service
public class DataService {
	
	private static final Logger logger = Logger.getLogger(DataService.class);

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(Object o) {

		logger.info("Deleting an object of: " + o.getClass().getName());
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.delete(o);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(Object o) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.saveOrUpdate(o);
		} catch (Exception e) {
			logger.debug(e);
			e.printStackTrace();
		}
	}
	
	/* SupportContact */
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public List<SupportContact> findAllSupportContacts()
	{
		return SupportContact.findAllSupportContacts();
	}
	
	/* StoryToPublish */
	@Transactional(propagation=Propagation.REQUIRED, readOnly = true)
	public StoryToPublish getEarliestStoryToPublish()
	{
		return StoryToPublish.getEarliestStoryToPublish();
	}

	
	@Transactional(propagation=Propagation.REQUIRED, readOnly = false)
	public void saveSP(StoryToPublish sp) {
		StoryToPublish.saveCR(sp);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly = true)
	public StoryToPublish findSPByStoryId(long id)
	{
		return StoryToPublish.findSPByStoryId(id);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly = false)
	public boolean deleteByStoryId(long id)
	{
		return StoryToPublish.deleteByStoryId(id);
	}
	
	/* Permission */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Permission findPermission(Long id) {
		return Permission.findPermission(id);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Set<Permission> findMultiplePermissions(Collection<Long> permissionIds) {		
		return Permission.findMultiplePermissions(permissionIds);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Permission> findAllPermissions() {
		return Permission.findAllPermissions();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Deprecated
	public void savePermission(Permission permission) {
		Permission.savePermission(permission);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Permission findPermissionByName(String name) {
		return Permission.findPermissionByName(name);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void saveExpiredStory(ExpiredStory eStory){
		ExpiredStory.saveExpiredStory(eStory);
	}
}
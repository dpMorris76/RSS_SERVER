package com.centurylink.rss.domain.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

@Entity
@Table(name = "permissions")
public class Permission implements Serializable {
	private static final Logger logger = Logger.getLogger(Permission.class);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_seq_gen")
	@SequenceGenerator(name = "hibernate_seq_gen", sequenceName = "HIBERNATE_SEQ",  allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Transient
	private Long version = 1L;

	@Transient
	private static final long serialVersionUID = 1L;

	@Transient
	public static final String SUBSCRIPTION = "Subscription";
	@Transient
	public static final String CONTENT_SUBMISSION = "Content_Submission";
	@Transient
	public static final String CONTENT_REVIEW = "Content_Review";
	@Transient
	public static final String CHANNEL_ADMINISTRATION = "Channel_Administration";
	@Transient
	public static final String CHANNEL_GROUP_ADMINISTRATION= "Channel_Group_Administration";
	@Transient
	public static final String USER_ADMINISTRATION= "User_Administration";
	
	@Transient
	public static final long SUBSCRITPION_ID = 1L;
	@Transient
	public static final long CONTENT_SUBMISSION_ID = 2L;
	@Transient
	public static final long CONTENT_REVIEW_ID = 3L;
	@Transient
	public static final long CHANNEL_ADMINISTRATION_ID = 4L;
	@Transient
	public static final long CHANNEL_GROUP_ADMINISTRATION_ID = 5L;
	@Transient
	public static final long USER_ADMINISTRATION_ID = 6L;
	
	
	
	@Column(name = "name", length = 45, nullable = false)
	private String permissionName;

	public Permission() {
	}

	public Permission(Long permissionId) {
		this.id = permissionId;
	}

	public Permission(Long permissionId, String permissionName) {
		this.id = permissionId;
		this.permissionName = permissionName;
	}
	/* Getters and Setters */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getPermissionName() {
		return this.permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	// DAO Methods
	public static Permission findPermission(Long id) {
		logger.debug("Finding permission by id "+id);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Permission v = (Permission) session.get(Permission.class, id);
		return v;
	}
	
	public static Set<Permission> findMultiplePermissions(Collection<Long> permissionIds) {		
		logger.debug("Finding a set of permissions");
		Set<Permission> permissions = new HashSet<Permission>();
		for (Long id : permissionIds) {
			permissions.add(findPermission(id));
		}
		return permissions;
	}
	
	public static List<Permission> findAllPermissions() {
		logger.debug("Finding all permissions");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(Permission.class);
		@SuppressWarnings("unchecked")
		List<Permission> result = crit.list();
		return result;
	}

	@Deprecated
	public static void savePermission(Permission permission) {
		logger.info("Permission "+permission.getPermissionName()+ "  saved.");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.saveOrUpdate(permission);
	}
	
	public static Permission findPermissionByName(String name) {
		logger.debug("Searching for permission with name "+name);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(Permission.class);
		crit.add(Restrictions.ilike("permissionName", name));
		@SuppressWarnings("unchecked")
		List<Permission> result = crit.list();
		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}
	
}

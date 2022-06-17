package com.centurylink.rss.domain.entity;

import java.io.Serializable;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.centurylink.rss.domain.entity.util.HibernateUtil;
import com.centurylink.rss.web.controller.AdminController;

/**
 * User is the only entity that uses a service
 * go there to access it's methods
 * IDK why this is the case
 * - Jay
 * @author FX
 *
 */
@Entity
@Table(name = "users")
public class User implements Serializable, Comparable<User> {

	private static final Logger logger = Logger.getLogger(AdminController.class);

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
	@JoinTable(name = "user_permissions", joinColumns = { @JoinColumn(name = "users_id") }, inverseJoinColumns = { @JoinColumn(name = "permission_id") })
	private Set<Permission> permissions = new HashSet<Permission>();
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "user_channels", joinColumns = { @JoinColumn(name = "users_id") }, inverseJoinColumns = { @JoinColumn(name = "channel_id") })
	private Set<Channel> channels;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "user_important_channels", joinColumns = { @JoinColumn(name = "users_id") }, inverseJoinColumns = { @JoinColumn(name = "channel_id") })
	private Set<Channel> importantChannels;
	
	@Column(name = "username", length = 45, nullable = false)
	private String username;

	@Transient
	private Boolean authenticated;

	@Column(name = "last_name", length = 30, nullable = true)
	private String lname;
	@Column(name = "first_name", length = 30, nullable = true)
	private String fname;
	@Column(name = "m_i", length = 30, nullable = true)
	private String mi;

	@Column(name = "is_active", nullable = false)
	private Boolean userActive;

	@Column(name = "phone_num", length = 25, nullable = true)
	private String phnNbr;
	@Column(name = "email", length = 50, nullable = true)
	private String email;
	@Column(name = "city", length = 80, nullable = true)
	private String city;
	@Column(name = "state", length = 2, nullable = true)
	private String stCd;
	@Column(name = "employee_type", length = 25, nullable = true)
	private String employeeType;
	@Transient
	private String exempt;
	@Column(name = "supervisor", length = 60, nullable = true)
	private String supervisorName;
	@Column(name = "supervisor_phone", length = 25, nullable = true)
	private String supervisorNumber;
	@Column(name = "assoc_group_id", length = 11)
	private Long assocGroupId;
	@Transient
	private String manager;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "group_content_providers", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "group_id") })
	private Set<ChannelGroup> userGroupsToProvideContent;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "group_content_gatekeepers", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "group_id") })
	private Set<ChannelGroup> userGroupsToGatekeep;

	public User() {
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

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public String getUserPermissionDesc() {
		if (permissions == null)
			return null;
		String permissionDesc = "";
		for (Permission permission : permissions) {
			permissionDesc += permission.getPermissionName() + ", ";
		}
		if (!permissionDesc.isEmpty())
			permissionDesc = permissionDesc.substring(0, permissionDesc.length() - 2);

		return permissionDesc;
	}
	
	public Set<Channel> getChannels() {
		return channels;
	}

	public void setChannels(Set<Channel> channels) {
		this.channels = channels;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
	}
	public String getLname() {
		return this.lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getMi() {
		return this.mi;
	}

	public void setMi(String mi) {
		this.mi = mi;
	}

	public String getPhnNbr() {
		return this.phnNbr;
	}

	public void setPhnNbr(String text) {
		this.phnNbr = text;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String text) {
		this.email = text;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStCd() {
		return this.stCd;
	}

	public void setStCd(String text) {
		this.stCd = text;
	}

	public String getEmployeeType() {
		return this.employeeType;
	}

	public void setEmployeeType(String text) {
		this.employeeType = text;
	}

	public String getExempt() {
		return this.exempt;
	}

	public void setExempt(String text) {
		this.exempt = text;
	}

	@Transient
	public String getFullName() {
		if (lname != null && fname != null)
			return lname + ", " + fname + (mi != null ? " " + mi : "");
		return username;
	}
	
	public Set<ChannelGroup> getUserGroupsToProvideContent() {
		return userGroupsToProvideContent;
	}

	public void setUserGroupsToProvideContent(Set<ChannelGroup> userGroupsToProvideContent) {
		this.userGroupsToProvideContent = userGroupsToProvideContent;
	}

	public Set<ChannelGroup> getUserGroupsToGatekeep() {
		return userGroupsToGatekeep;
	}

	public void setUserGroupsToGatekeep(Set<ChannelGroup> userGroupsToGatekeep) {
		this.userGroupsToGatekeep = userGroupsToGatekeep;
	}

	@Transient
	public void setAssignedToFormat(String assignedToFormat) {
	}

	@Transient
	public void setFullName(String fullName) {
	}

	public Boolean getUserActive() {
		return userActive;
	}

	public void setUserActive(Boolean userActive) {
		this.userActive = userActive;
	}

	@Transient
	public String getStatus() {
		if (userActive == null) {
			return "";
		} else if (userActive) {
			return "Yes";
		} else {
			return "No";
		}
	}

	@Transient
	public void setStatus(String status) {
	}

	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public String getSupervisorNumber() {
		return supervisorNumber;
	}

	public void setSupervisorNumber(String supervisorNumber) {
		this.supervisorNumber = supervisorNumber;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}
	
	public Long getAssocGroupId() {
		return assocGroupId;
	}

	public void setAssocGroupId(Long assocGroupId) {
		this.assocGroupId = assocGroupId;
	}


	// DAO Methods
	public static void saveUser(User user) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			String un = user.getUsername();
			if (un != null) {
				user.setUsername(un.toLowerCase());
				user.setUserActive(true);
				session.saveOrUpdate(user);
//				session.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isAdministrator(User u){
		for(Permission p : u.getPermissions()){
			if(p.getId().equals(Permission.CHANNEL_ADMINISTRATION_ID) 
					|| p.getId().equals(Permission.CHANNEL_GROUP_ADMINISTRATION_ID)
					|| p.getId().equals(Permission.USER_ADMINISTRATION_ID))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isAboveContentProvider(User u){
		for(Permission p : u.getPermissions()){
			if(p.getId().equals(Permission.CHANNEL_ADMINISTRATION_ID) 
					|| p.getId().equals(Permission.CHANNEL_GROUP_ADMINISTRATION_ID)
					|| p.getId().equals(Permission.USER_ADMINISTRATION_ID)
					|| p.getId().equals(Permission.CONTENT_REVIEW_ID)
					)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isSuperAdministrator(User u){
		return (u.getPermissions().size() == 6);
	}
	
	/** @Param List<User> users 
	 * broken mostly because of the fact that 
	 * userIds are generated by the DB.
	 */
	@Deprecated 
	public static void saveUsers(List<User> users)
	{
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		for(User user: users)
		{
			if(user.getUsername() == null)
			{
				user.setUsername(user.getUsername().toLowerCase());
				user.setUserActive(true);
				session.saveOrUpdate(user);
			}
		}
		session.flush();
	}

	public static User findUser(Long id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		User v = (User) session.get(User.class, id);
		return v;
	}

	public static List<User> findAllUsers() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(User.class);
		crit.addOrder( Order.asc("lname") );
		@SuppressWarnings("unchecked")
		List<User> result = crit.list();
		return result;
	}

	public static List<User> findAllActiveUsers() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(User.class);
		crit.add(Restrictions.eq("userActive", true));
		crit.addOrder( Order.asc("lname") );
		@SuppressWarnings("unchecked")
		List<User> result = crit.list();

		return result;
	}
	public static User findUserByUsername(String username) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(User.class);
		crit.add(Restrictions.eq("username", username).ignoreCase());
		// crit.addOrder( Order.asc("lname") );

		@SuppressWarnings("unchecked")
		List<User> result = crit.list();
		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}
	
	public static List<User> findUsersByName(String fName, String lName) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(User.class);
		crit.add(Restrictions.like("fname", fName + "%").ignoreCase());
		crit.add(Restrictions.like("lname", lName + "%").ignoreCase());
		
		return crit.list();
	}
	
	public static List<User> findUsersByLastName(String lName) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(User.class);
		
		crit.add(Restrictions.like("lname", lName + "%").ignoreCase());
		
		return crit.list();
	}

	public static List<User> findUsersByPermission(Permission permission) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(User.class);

		Criteria critPermission = crit.createCriteria("permissions");

		Junction junc = Restrictions.disjunction();
		junc.add(Restrictions.eq("id", permission.getId()));
		critPermission.add(junc);

		@SuppressWarnings("unchecked")
		List<User> result = crit.list();
//		Collections.sort(result);
		return result;
	}
	
	public static List<User> findUsersByGroup(ChannelGroup group) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(User.class);
//		System.err.println("Finding users by their associated group...");
		Long groupId = group.getId();
		if (groupId != null){
			crit.add(Restrictions.eq("assocGroupId", groupId));
		} else {
			System.err.println("Group id not found");
			return null;
		}

		@SuppressWarnings("unchecked")
		List<User> result = crit.list();
//		System.err.println("Users associated with this group: "+result.size());
		return result;
	}
	
	public static void updatePermissions(Long id, Set<Long> permissionIds) {
		User user = findUser(id);
		Set<Permission> thingsToSet = Permission.findMultiplePermissions(permissionIds);
		Permission contentReview = Permission.findPermissionByName(Permission.CONTENT_REVIEW);
		Permission contentSubmission = Permission.findPermissionByName(Permission.CONTENT_SUBMISSION);
		if(user.getPermissions().contains(contentReview) && !thingsToSet.contains(contentReview) )
		{
			user.setUserGroupsToGatekeep(new HashSet<ChannelGroup>());
		}
		if(user.getPermissions().contains(contentSubmission) && !thingsToSet.contains(contentSubmission))
		{
			user.setUserGroupsToProvideContent(new HashSet<ChannelGroup>());
		}
		user.setPermissions(thingsToSet);
		saveUser(user);
	}
	
	public static void updateAssociatedGroup(Long userId, Long assocGroupId) {
		User user = findUser(userId);
		user.setAssocGroupId(assocGroupId);
		saveUser(user);
	}
	
	public static void updateChannels(Long userId, Set<Long> channelIds){
		User user = findUser(userId);
		user.setChannels(Channel.findMultipleById(channelIds));
		saveUser(user);
	}
	
	public static void updateImportantChannels(Long userId, Set<Long> channelIds){
		User user = findUser(userId);
		user.setImportantChannels(Channel.findMultipleById(channelIds));
		saveUser(user);
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof User))
			return false;
		if (obj == this)
			return true;
		return this.username.equals(((User) obj).getUsername());
	}

	public int hashCode() {
		return username.hashCode();// for simplicity reason
	}


	@Override
	public int compareTo(User o) {
		return this.getFullName().compareToIgnoreCase(o.getFullName());
	}

	public Set<Channel> getImportantChannels() {
		return importantChannels;
	}

	public void setImportantChannels(Set<Channel> importantChannels) {
		this.importantChannels = importantChannels;
	}

	

}

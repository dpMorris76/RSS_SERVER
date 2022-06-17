//package com.centurylink.rss.business.service;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.hibernate.SessionFactory;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.centurylink.agile.client.AgileAuthenticationClient;
//import com.centurylink.rss.domain.entity.Channel;
//import com.centurylink.rss.domain.entity.ChannelGroup;
//import com.centurylink.rss.domain.entity.Permission;
//import com.centurylink.rss.domain.entity.User;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/applicationContext.xml" })
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@Transactional
//public class UserServiceTest {
//	@Autowired
//	@Qualifier("sessionFactory")
//	SessionFactory sf;
//	@Autowired
//	UserDS us;
//	@Autowired
//	AgileAuthenticationClient agileAuthClient;
//
//	private User u1, u2, u3;
//	private List<User> lu;
//	private Permission p1, p2;
//	private Set<Long> sl;
//	private Set<Permission> sp;
//	private ChannelGroup g1, g2;
//	private Channel c1, c2;
//	private Set<Channel> sc;
//	private List<String> ls;
//
//	@Before
//	public void setup() {
//		u1 = new User();
//		u2 = new User();
//		u3 = new User();
//		lu = new ArrayList<User>();
//		p1 = new Permission();
//		p2 = new Permission();
//		sl = new HashSet<Long>();
//		sp = new HashSet<Permission>();
//		g1 = new ChannelGroup();
//		g2 = new ChannelGroup();
//		c1 = new Channel();
//		c2 = new Channel();
//		sc = new HashSet<Channel>();
//		ls = new ArrayList<String>();
//		
//		c1.setTitle("TITLE1");
//		c1.setLink("LINK1");
//		c1.setDescription("DESCRIPTION1");
//		c1.setLanguage("LANGUAGE1");
//		c1.setLastBuildDate(new Date());
//		c1.setGenerator("GENERATOR1");
//		c1.setManagingEditor("MANAGEDITOR1");
//		
//		c2.setTitle("TITLE2");
//		c2.setLink("LINK2");
//		c2.setDescription("DESCRIPTION2");
//		c2.setLanguage("LANGUAGE2");
//		c2.setLastBuildDate(new Date());
//		c2.setGenerator("GENERATOR2");
//		c2.setManagingEditor("MANAGEDITOR2");
//		
//		sf.getCurrentSession().save(c1);
//		sf.getCurrentSession().save(c2);
//		
//		sc.add(c1);
//		sc.add(c2);
//		
//		g1.setTitle("TITLE1");
//		g2.setTitle("TITLE2");
//		
//		sf.getCurrentSession().save(g1);
//		sf.getCurrentSession().save(g2);
//
//		p1.setId(10L);
//		p1.setPermissionName("PNAME1");
//		
//		p2.setId(11L);
//		p2.setPermissionName("PNAME2");
//
//		sf.getCurrentSession().save(p1);
//		sf.getCurrentSession().save(p2);
//		
//		u1.setUserActive(true);
//		u1.setUsername("Bob");
//		u1.setAssocGroupId(g1.getId());
//		u1.setChannels(sc);
//
//		u2.setUserActive(true);
//		u2.setUsername("Sue");
//		u2.setAssocGroupId(g2.getId());
//		u2.setChannels(sc);
//		
//		u3.setUsername(agileAuthClient.getUserByAdId("aa06863").getUsername());
//		u3.setUserActive(true);
//		
//		sf.getCurrentSession().save(u1);
//		sf.getCurrentSession().save(u2);
//		sf.getCurrentSession().save(u3);
//	}
//
//	@Test
//	public void testLoadAndGetSubordinatesFromAdid() {
//		//AuthenticationService must be running for this case to pass. Otherwise, NullPointerException occurs.
//		us.loadAndGetSubordinatesFromAdid(u3);
//		
//		assertNotNull("No information found", us.findUser(u3.getId()));
//		assertEquals("User3 not found", "aa06863", User.findUserByUsername("aa06863").getUsername());
//	}
//
//	@Test
//	public void testSaveUsers() {
//		lu.add(u2);
//		us.saveUsers(lu);
//		User found = User.findUser(u2.getId());
//		
//		assertNotNull("User not found", found);
//		assertEquals("User not equal", u2.getId(), found.getId());
//	}
//
//	@Test
//	public void testSaveUser() {
//		us.saveUser(u2);
//		User found = us.findUser(u2.getId());
//		
//		assertNotNull("User not found", found);
//		
//		assertEquals("User not equal", u2.getId(), found.getId());
//	}
//
//	@Test
//	public void testFindUsersByName() {
//		User found = User.findUserByUsername("Bob");
//
//		assertNotNull("User1 not found", found);
//		assertEquals("User1 not equal", u1.getId(), found.getId());
//	}
//
//	@Test
//	public void testCreateNewBasicUserFromLdap() {
//		//AuthenticationService must be running for this case to pass. Otherwise, NullPointerException occurs.
//		us.createNewBasicUserFromLdap("AB03590");
//		
//		assertEquals("Username not found", "ab03590", User.findUserByUsername("ab03590").getUsername());
//	}
//
//	@Test
//	public void testUpdateChannels() {
//		sl.add(c1.getId());
//		User.updateChannels(u1.getId(), sl);
//		User found = User.findUser(u1.getId());
//		
//		assertNotNull("User not found", found);
//		assertEquals("User not equal", u1.getChannels(), found.getChannels());
//	}
//
//	@Test
//	public void testUpdateImportantChannels() {
//		sl.add(c1.getId());
//		User.updateImportantChannels(u1.getId(), sl);
//		User found = User.findUser(u1.getId());
//		
//		assertNotNull("User not found", found);
//		assertNotNull("User List not found", sl);
//		assertEquals("User not equal", u1.getId(), found.getId());
//		assertEquals("User Channels not equal", u1.getImportantChannels(), found.getImportantChannels());
//	}
//
//	@Test
//	public void testUpdateAssociatedGroupId() {
//		User.updateAssociatedGroup(u1.getId(), g1.getId());
//		lu = User.findUsersByGroup(g1);
//		
//		assertNotNull("User not found", lu);
//		assertTrue("User not contained", lu.contains(u1));
//		assertEquals("User not equal", 1, lu.size());
//	}
//
//	@Test
//	public void testUpdatePermissions() {
//		sl.add(p1.getId());
//		User.updatePermissions(u1.getId(), sl);
//		lu = User.findUsersByPermission(p1);
//		
//		assertNotNull("User not found", lu);
//		assertTrue("User not contained", lu.contains(u1));
//		assertEquals("User not equal", 1, lu.size());
//	}
//
//	@Test
//	public void testFindUsersByGroup() {
//		lu = User.findUsersByGroup(g1);
//		
//		assertNotNull("User not found", lu);
//		assertTrue("User not contained", lu.contains(u1));
//		assertEquals("User not equal", 1, lu.size());
//	}
//
//	@Test
//	public void testUpdateList() {
//		ls.add(u1.getUsername());
//		User found = User.findUserByUsername(u1.getUsername());
//		
//		assertNotNull("User not found", found);
//		assertTrue("User not contained", ls.contains(u1.getUsername()));
//		assertEquals("User not equal", u1.getUsername(), found.getUsername());
//	}
//
//	@Test
//	public void testFindUsersByPermission() {
//		sp.add(p1);
//		u1.setPermissions(sp);
//		lu = us.findUsersByPermission(p1);
//		
//		assertNotNull("Permission1 not found", lu);
//		assertTrue("User1 Permission not equal", lu.contains(u1));
//		assertEquals("Permission1 not equal", 1, lu.size());
//	}
//
//	@Test
//	public void testFindUser() {
//		User found = User.findUser(u1.getId());
//		
//		assertNotNull("User1 not found", found);
//		assertEquals("User1 id not equal", u1.getId(), found.getId());
//		assertEquals("User1 name not equal", u1.getUsername(), found.getUsername());
//	}
//
//	@Test
//	public void testFindUserByUsername() {
//		User found = User.findUser(u1.getId());
//		
//		assertNotNull("User not found", found);
//		assertEquals("Use1 name not equal", u1.getUsername(), found.getUsername());
//	}
//
//	@Test
//	public void testConvertAuthUser() {
//		u1 = us.findUserByUsername("Bob");
//		
//		assertNotNull("User not found", u1);
//		assertEquals("User not equal", u1.getUsername(), u1.getUsername());
//	}
//
//	@Test
//	public void testConvertAuthUsers() {
//		lu.add(u1);
//		
//		assertNotNull("Users not found", lu);
//		assertEquals("User not equal", 1, lu.size());
//	}
//
//	@Test
//	public void testFindAllUsers() {
//		lu = User.findAllUsers();
//		
//		assertNotNull("User not found", lu);
//		assertTrue("User not found", lu.contains(u1));
//		assertEquals("User not equal", 13, lu.size());
//	}
//
//}

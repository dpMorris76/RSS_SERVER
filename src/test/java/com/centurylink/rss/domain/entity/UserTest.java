//package com.centurylink.rss.domain.entity;
//
//import static org.junit.Assert.*;
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
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/applicationContext.xml" })
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@Transactional
//public class UserTest {
//	@Autowired
//	@Qualifier("sessionFactory")
//	SessionFactory sf;
//
//	private User u1, u2;
//	private List<User> lu;
//	private Channel c1, c2;
//	private Set<Channel> sc;
//	private Permission p1, p2;
//	private Set<Permission> sp;
//	private ChannelGroup g1, g2;
//	private Set<Long> sl;
//
//	@Before
//	public void setup() {
//		u1 = new User();
//		u2 = new User();
//		lu = new ArrayList<User>();
//		c1 = new Channel();
//		c2 = new Channel();
//		sc = new HashSet<Channel>();
//		p1 = new Permission();
//		p2 = new Permission();
//		sp = new HashSet<Permission>();
//		g1 = new ChannelGroup();
//		g2 = new ChannelGroup();
//		sl = new HashSet<Long>();
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
//		c1.setTitle("TITLE1");
//		c1.setLink("LINK1");
//		c1.setDescription("DESCRIPTION1");
//		c1.setLanguage("LANGUAGE1");
//		c1.setLastBuildDate(new Date());
//		c1.setGenerator("GENERATOR1");
//
//		c2.setTitle("TITLE2");
//		c2.setLink("LINK2");
//		c2.setDescription("DESCRIPTION2");
//		c2.setLanguage("LANGUAGE2");
//		c2.setLastBuildDate(new Date());
//		c2.setGenerator("GENERATOR2");
//
//		sc.add(c1);
//		sc.add(c2);
//
//		u1.setUserActive(true);
//		u1.setUsername("Bunny");
//		u1.setChannels(sc);
//		u1.setAssocGroupId(g1.getId());
//
//		u2.setUserActive(true);
//		u2.setUsername("Bear");
//		u2.setChannels(sc);
//		u2.setAssocGroupId(g2.getId());
//
//		sf.getCurrentSession().save(u1);
//	}
//
//	@Test
//	public void testSaveUser() {
//		User.saveUser(u2);
//		User found = User.findUser(u2.getId());
//
//		assertNotNull("User not found", found);
//		assertEquals("User not equal", u2.getId(), found.getId());
//	}
//
//	@Test
//	public void testSaveUsers() {
//		lu = User.findAllUsers();
//		User.saveUsers(lu);
//		User found = User.findUser(u1.getId());
//
//		assertNotNull("User found", lu);
//		assertEquals("User not equal", u1.getId(), found.getId());
//	}
//
//	@Test
//	public void testFindUser() {
//		User found = User.findUser(u1.getId());
//
//		assertNotNull("User not found", found);
//		assertEquals("User not equal", u1.getId(), found.getId());
//	}
//
//	@Test
//	public void testFindAllUsers() {
//		lu = User.findAllUsers();
//
//		assertNotNull("User not found", lu);
//		assertTrue("User not found", lu.contains(u1));
//		assertEquals("User not equal", 11, lu.size());
//	}
//
//	@Test
//	public void testFindAllActiveUsers() {
//		lu = User.findAllActiveUsers();
//
//		assertNotNull("User not found", lu);
//		assertTrue("User not contained", lu.contains(u1));
//		assertEquals("User not equal", 11, lu.size());
//	}
//
//	@Test
//	public void testFindUserByUsername() {
//		User found = User.findUserByUsername("Bunny");
//
//		assertNotNull("User not found", found);
//		assertEquals("User not equal", u1.getUsername(), found.getUsername());
//	}
//
//	@Test
//	public void testFindUsersByPermission() {
//		sp.add(p1);
//		u1.setPermissions(sp);
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
//		assertNotNull("User1 not found", lu);
//		assertTrue("User1 not contained", lu.contains(u1));
//		assertEquals("User1 not equal", 1, lu.size());
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
//	public void testUpdateAssociatedGroup() {
//		User.updateAssociatedGroup(u1.getId(), g1.getId());
//		lu = User.findUsersByGroup(g1);
//
//		assertNotNull("User not found", lu);
//		assertTrue("User not contained", lu.contains(u1));
//		assertEquals("User not equal", 1, lu.size());
//	}
//
//	@Test
//	public void testUpdateChannels() {
//		sl.add(c1.getId());
//		User.updateChannels(u1.getId(), sl);
//		User found = User.findUser(u1.getId());
//
//		assertNotNull("Use1 not found", found);
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
//}

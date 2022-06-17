//package com.centurylink.rss.domain.entity;
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
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/applicationContext.xml" })
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@Transactional
//public class ChannelTest {
//	@Autowired
//	@Qualifier("sessionFactory")
//	SessionFactory sf;
//
//	private Channel c1, c2;
//	private List<Channel> lc;
//	private Set<Long> sl;
//	private Set<Channel> sc;
//	private User u1, u2;
//
//	@Before
//	public void setup() {
//		c1 = new Channel();
//		c2 = new Channel();
//		lc = new ArrayList<Channel>();
//		sl = new HashSet<Long>();
//		sc = new HashSet<Channel>();
//		u1 = new User();
//		u2 = new User();
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
//
//		sc.add(c1);
//		sc.add(c2);
//
//		u1.setUserActive(true);
//		u1.setUsername("Bunny");
//		u1.setChannels(sc);
//
//		u2.setUserActive(true);
//		u2.setUsername("Bear");
//		u2.setChannels(sc);
//
//		sf.getCurrentSession().save(u1);
//		sf.getCurrentSession().save(u2);
//	}
//
//	@Test
//	public void testFindAllChannels() {
//		lc = Channel.findAllChannels();
//
//		assertNotNull("Channel not found", lc);
//		assertTrue("Channel not found", lc.contains(c1));
//		assertEquals("Channel not equal", 4, lc.size());
//	}
//
//	@Test
//	public void testSaveChannel() {
//		Channel.saveChannel(c2);
//		Channel found = Channel.findById(c2.getId());
//
//		assertNotNull("Channel not found", found);
//		assertEquals("Channel not equal", c2.getId(), found.getId());
//
//	}
//
//	@Test
//	public void testFindById() {
//		Channel found = Channel.findById(c1.getId());
//
//		assertNotNull("Channel not found", found);
//		assertEquals("Channel not equal", c1.getId(), found.getId());
//	}
//
//	@Test
//	public void testFindMultipleById() {
//		sl.add(c1.getId());
//		sc = Channel.findMultipleById(sl);
//
//		assertNotNull("Channel not found", sc);
//		assertTrue("Channel not contained", sc.contains(c1));
//		assertEquals("Channel not equal", 1, sc.size());
//	}
//
//	// These cases are not being used for now because the methods are exercised
//	// in our Selenium test cases.
//	// @Test
//	// public void testSaveNewChannelFromForm() {
//	// fail("Not yet implemented");
//	// }
//	//
//	// @Test
//	// public void testUpdateChannelFromForm() {
//	// fail("Not yet implemented");
//	// }
//	//
//	// @Test
//	// public void testSaveChannelFromForm() {
//	// fail("Not yet implemented");
//	// }
//
//	@Test
//	public void testFindChannelsByManagingEditor() {
//		lc = Channel.findChannelsByManagingEditor(u1);
//
//		assertNotNull("Channel not found", lc);
//	}
//
//}

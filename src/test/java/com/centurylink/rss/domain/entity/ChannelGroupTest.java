//package com.centurylink.rss.domain.entity;
//
//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.List;
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
//public class ChannelGroupTest {
//	@Autowired
//	@Qualifier("sessionFactory")
//	SessionFactory sf;
//
//	private ChannelGroup g1, g2;
//	private List<ChannelGroup> lg;
//
//	@Before
//	public void setup() {
//		g1 = new ChannelGroup();
//		g2 = new ChannelGroup();
//		lg = new ArrayList<ChannelGroup>();
//
//		g1.setTitle("TITLE1");
//		g2.setTitle("TITLE2");
//
//		sf.getCurrentSession().save(g1);
//	}
//
//	@Test
//	public void testFindAllGroups() {
//		lg = ChannelGroup.findAllGroups();
//
//		assertNotNull("Group not found", lg);
//		assertTrue("Group not found", lg.contains(g1));
//		assertEquals("Group not equal", 2, lg.size());
//	}
//
//	@Test
//	public void testSaveGroup() {
//		ChannelGroup.saveGroup(g2);
//		ChannelGroup found = ChannelGroup.findById(g2.getId());
//
//		assertNotNull("Group not found", found);
//		assertEquals("Group not equal", g2.getId(), found.getId());
//	}
//
//	@Test
//	public void testFindById() {
//		ChannelGroup found = ChannelGroup.findById(g1.getId());
//
//		assertNotNull("Group not found", found);
//		assertEquals("Group not equal", g1.getId(), found.getId());
//	}
//
//	// These cases are not being used for now because the methods are exercised
//	// in our Selenium test cases.
//	// @Test
//	// public void testSaveGroupFromFormChannelForm() {
//	// fail("Not yet implemented");
//	// }
//	//
//	// @Test
//	// public void testSaveNewGroupFromForm() {
//	// fail("Not yet implemented");
//	// }
//	//
//	// @Test
//	// public void testUpdateGroupFromForm() {
//	// fail("Not yet implemented");
//	// }
//	//
//	// @Test
//	// public void testSaveGroupFromFormChannelGroupGroupForm() {
//	// fail("Not yet implemented");
//	// }
//
//}

//package com.centurylink.rss.domain.entity;
//
//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.Date;
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
//public class StoryTest {
//	@Autowired
//	@Qualifier("sessionFactory")
//	SessionFactory sf;
//
//	private Story s1, s2;
//	private List<Story> ls;
//	private User u1, u2;
//
//	@Before
//	public void setup() {
//		s1 = new Story();
//		s2 = new Story();
//		ls = new ArrayList<Story>();
//		u1 = new User();
//		u2 = new User();
//
//		s1.setTitle("TITLE1");
//		s1.setLink("LINK1");
//		s1.setLinkName("LINKNAME1");
//		s1.setDescription("DESCRIPTION1");
//		s1.setAuthor("AUTHOR1");
//		s1.setCategory("CATEGORY1");
//		s1.setPublishDate(new Date());
//		s1.setApprovalStatus("APPSTATUS1");
//		s1.setExpirationDate(new Date());
//		s1.setIsHighPriority(true);
//
//		s2.setTitle("TITLE2");
//		s2.setLink("LINK2");
//		s2.setLinkName("LINKNAME2");
//		s2.setDescription("DESCRIPTION2");
//		s2.setAuthor("AUTHOR2");
//		s2.setCategory("CATEGORY2");
//		s2.setPublishDate(new Date());
//		s2.setApprovalStatus("APPSTATUS2");
//		s2.setExpirationDate(new Date());
//		s2.setIsHighPriority(true);
//
//		sf.getCurrentSession().save(s1);
//
//		u1.setUserActive(true);
//		u1.setUsername("Bunny");
//		u1.setEmail("AUTHOR1");
//
//		u2.setUserActive(true);
//		u2.setUsername("Bear");
//		u2.setEmail("AUTHOR2");
//
//		sf.getCurrentSession().save(u1);
//		sf.getCurrentSession().save(u2);
//	}
//
//	// These cases are not being used for now because the methods are exercised
//	// in our Selenium test cases.
//	// @Test
//	// public void testSaveAndApproveNewStoryFromForm() {
//	// fail("Not yet implemented");
//	// }
//	//
//	// @Test
//	// public void testSaveNewStoryFromForm() {
//	// fail("Not yet implemented");
//	// }
//	//
//	// @Test
//	// public void testUpdateAndApproveStoryFromForm() {
//	// fail("Not yet implemented");
//	// }
//	//
//	// @Test
//	// public void testUpdateStoryFromForm() {
//	// fail("Not yet implemented");
//	// }
//
//	@Test
//	public void testSaveStory() {
//		Story.saveStory(s2);
//		Story found = Story.findById(s2.getId());
//
//		assertNotNull("Story not found", found);
//		assertEquals("Story not equal", s2.getId(), found.getId());
//	}
//
//	@Test
//	public void testFindAllStories() {
//		ls = Story.findAllStories();
//
//		assertNotNull("Story not found", ls);
//		assertTrue("Story not contained", ls.contains(s1));
//		assertEquals("Story not equal", 5, ls.size());
//	}
//
//	@Test
//	public void testFindStoriesByAuthor() {
//		ls = Story.findStoriesByAuthor(u1);
//
//		assertNotNull("Story not found", ls);
//		assertTrue("Story not contained", ls.contains(s1));
//		assertEquals("Story not equal", 1, ls.size());
//	}
//
//	@Test
//	public void testFindStoriesByApprovalStatus() {
//		ls = Story.findStoriesByApprovalStatus("APPSTATUS1");
//
//		assertNotNull("Story not found", ls);
//		assertTrue("Story not contained", ls.contains(s1));
//		assertEquals("Story not equal", 1, ls.size());
//	}
//
//	@Test
//	public void testFindById() {
//		Story found = Story.findById(s1.getId());
//
//		assertNotNull("Story not found", found);
//		assertEquals("Story not equal", s1.getId(), found.getId());
//	}
//
//	@Test
//	public void testCreateNewChannelMetaStory() {
//		// method not used
//	}
//
//	@Test
//	public void testDenySubmission() {
//		Story deny = Story.denySubmission(s1.getId(), "N/A");
//
//		assertNotNull("Story not found", deny);
//		assertEquals("Story not denied", s1.getApprovalStatus(), deny.getApprovalStatus());
//	}
//
//	@Test
//	public void testApproveSubmission() {
//		Story.saveStory(s2);
//		Story approve = Story.approveSubmission(s2.getId(), u1);
//
//		assertNotNull("Story not found", approve);
//		assertEquals("Story not approved", s2.getApprovalStatus(), approve.getApprovalStatus());
//	}
//
//}

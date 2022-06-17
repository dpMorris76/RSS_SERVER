//package com.centurylink.rss.domain.entity;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//
//import java.util.Calendar;
//import java.util.Date;
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
//public class StoryToPublishTest {
//	@Autowired
//	@Qualifier("sessionFactory")
//	SessionFactory sf;
//
//	private StoryToPublish sp1, sp2;
//	private Story s1, s2;
//	private Calendar cal1 = Calendar.getInstance();
//	private Calendar cal2 = Calendar.getInstance();
//	
//	@Before
//	public void setup() {
//		cal2.set(Calendar.HOUR_OF_DAY, cal1.get(Calendar.HOUR_OF_DAY) + 1);
//		sp1 = new StoryToPublish();
//		sp2 = new StoryToPublish();
//		s1 = new Story();
//		s2 = new Story();
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
//		sf.getCurrentSession().save(s2);
//		
//		sp1.setId(11111111111L);
//		sp1.setStoryId(s1.getId());
//		sp1.setDate(cal1.getTime());
//		
//		sp2.setId(11111111112L);
//		sp2.setStoryId(s2.getId());
//		sp2.setDate(cal2.getTime());
//		
//		sf.getCurrentSession().save(sp1);
//		sf.getCurrentSession().save(sp2);
//	}
//
//	@Test
//	public void testgetEarliestStoryToPublish() {
//		StoryToPublish found = StoryToPublish.getEarliestStoryToPublish();
//
//		assertNotNull("STP not found", found);
//		assertEquals("STP not equal", sp1.getId(), found.getId());
//	}
//
//	@Test
//	public void testSaveCR() {
//		 StoryToPublish.saveCR(sp2);
//		 StoryToPublish found = StoryToPublish.findSPByStoryId(s2.getId());
//
//		assertNotNull("STP not found", found);
//		 assertEquals("STP not equal", sp2.getId(), found.getId());
//	}
//
//	@Test
//	public void testFindSPByStoryId() {
//		StoryToPublish found = StoryToPublish.findSPByStoryId(s1.getId());
//
//		assertNotNull("STP not found", found);
//		assertEquals("STP not equal", sp1.getId(), found.getId());
//	}
//
//	@Test
//	public void testDeleteByStoryId() {
//		StoryToPublish.saveCR(sp2);
//		StoryToPublish.deleteByStoryId(s2.getId());
//		StoryToPublish deleted = StoryToPublish.findSPByStoryId(s2.getId());
//
//		assertNull("STP not deleted", deleted);
//	}
//
//}

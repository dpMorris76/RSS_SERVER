//package com.centurylink.rss.business.service;
//
//import static org.junit.Assert.*;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
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
//import com.centurylink.rss.domain.entity.Story;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "file:src/test/resources/WEB-INF/applicationContext.xml" })
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@Transactional
//public class DataServiceTest {
//	@Autowired
//	@Qualifier("sessionFactory")
//	SessionFactory sf;
//
//	private Story s1, s2;
//	private List<Story> ls;
//
//	@Before
//	public void setup() {
//		s1 = new Story();
//		s2 = new Story();
//		ls = new ArrayList<Story>();
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
//		sf.getCurrentSession().flush();
//	}
//
//	@Test
//	public void testFindAllStories() {
//		ls = Story.findAllStories();
//
//		assertNotNull("Story not found", ls);
//		assertTrue("Story not true", ls.contains(s1));
//		assertEquals("Story not equal", 5, ls.size());
//	}
//
//	@Test
//	public void testDelete() {
//		// method not used
//	}
//
//	@Test
//	public void testSave() {
//		Story.saveStory(s2);
//		Story found = Story.findById(s2.getId());
//
//		assertNotNull("Story not found", found);
//		assertEquals("Story not equal", s2.getId(), found.getId());
//	}
//
//}
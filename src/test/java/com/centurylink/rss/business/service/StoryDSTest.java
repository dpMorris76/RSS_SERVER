//package com.centurylink.rss.business.service;
//
//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.hibernate.SessionFactory;
//import org.junit.After;
//import org.junit.Assert;
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
//import com.centurylink.rss.domain.entity.Story;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/applicationContext.xml" })
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@Transactional
//public class StoryDSTest {
//	@Autowired
//	@Qualifier("sessionFactory")
//	SessionFactory sf;
//
//	Story story;
//	StoryDS sds;
//	List<Story> expiredStories;
//	
//	@Before
//	public void setUp() {
//		story = new Story();
//		sds = new StoryDS();
//		expiredStories = new ArrayList<Story>();
//	}
//
//	@After
//	public void tearDown() {
//	}
//
//	@Test
//	public void testFindExpiredStories() {
//		expiredStories = story.findExpiredStories();
//		int size = expiredStories.size();
//		assertEquals("Incorrect number of stories reported", size, story.findExpiredStories().size());
//	}
//
//	@Test
//	public void testExpireStories() {
//		expiredStories = story.findExpiredStories();
//		sds.expireStories(expiredStories);
//		List<Story> expiredList = story.findExpiredStories();
//		assertEquals("List is not empty", 0, expiredList.size());
//	}
//
//}

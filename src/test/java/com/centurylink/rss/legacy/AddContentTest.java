package com.centurylink.rss.legacy;
// THIS IS NOW AN INACTIVE TEST CASE.

//package ContentSubmission;
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
//import static org.junit.Assert.*;
//
//import java.util.Calendar;
//import java.util.Date;
//
//import com.centurylink.rss.domain.entity.Story;
//import com.centurylink.rss.web.form.StoryForm;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "file:src/test/resources/WEB-INF/applicationContext.xml" })
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
//@Transactional
//public class AddContentTest {
//
//	@Autowired
//	@Qualifier("sessionFactory")
//	SessionFactory sf;
//
//	private Story u;
//	private StoryForm c;
//	Calendar date;
//	Calendar date2;
//	
//	@Before
//	public void setup() {
//		
//		date = Calendar.getInstance();
//		
//		
//		Date pubDate = new Date(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 0, 0);
//		Date expDate = new Date(date.get(Calendar.YEAR), date.get(Calendar.MONTH)+1, date.get(Calendar.DATE), 0, 0);
//		
//		u = new Story();
//		u.setTitle("Test Story");
//		u.setLink("www.testwebsite.com");
//		u.setDescription("Test Description brooooooo");
//		u.setAuthor("aa06863@localhost");
//		u.setCategory("Test Channel");
//		u.setPublishDate(pubDate);
//		u.setApprovalStatus("Approved");
//		u.setExpirationDate(expDate);
//		u.setIsHighPriority(false);
//		
//		sf.getCurrentSession().saveOrUpdate(u);
//		
//		System.out.println(u.getId().toString());
//		
////		c = new StoryForm();
////		
////		c.setStoryId(u.getId());
////		c.setStoryTitle(u.getTitle());
////		c.setStoryLink(u.getLink());
////		c.setStoryDescription(u.getDescription());
////		c.setStoryAuthorEmail(u.getAuthor());
////		c.setStoryCategories(u.getCategory());
////		c.setStoryPublishDate("24-May-16");
////		c.setExpirationDate("24-JUNE-16");
////		c.setIsHighPriority(false);
////		
////		
////		
////		sf.getCurrentSession().saveOrUpdate(c);
//		
//	}
//
//	@Test
//	public void testFindStory() {
//		Long id = u.getId();
//		Story result = u.findById(id);
//			System.out.println("This is the ID " + result.toString());
//		assertNotNull("Story not found", result);
//		assertEquals(u.getTitle(), result.getTitle());
//		
//	}
//	@Test
//	public void testSaveAndApproveNewStoryFromForm() {
//		
//
//		Date pubDate = new Date(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 0, 0);
//		Date expDate = new Date(date.get(Calendar.YEAR), date.get(Calendar.MONTH)+1, date.get(Calendar.DATE), 0, 0);
//		
//		
//		u = new Story();
//		c = new StoryForm();
//		u.setApprovalStatus("Approved");
//		u.setTitle("Test Story");
//		u.setLink("www.testwebsite.com");
//		u.setDescription("Test Description brooooooo");
//		u.setAuthor("aa06863@localhost");
//		u.setCategory("Test Channel");
//		u.setPublishDate(pubDate);
//		u.setApprovalStatus("Approved");
//		u.setExpirationDate(expDate);
//		u.setIsHighPriority(false);
//		sf.getCurrentSession().saveOrUpdate(u);
//		sf.getCurrentSession().saveOrUpdate(c);
//		
//		Story k = Story.saveAndApproveNewStoryFromForm(c, "aa06863@localhost");
//		assertNotNull("Story Saved and Approved", k.getTitle());
//		assertEquals("Approved", k.getApprovalStatus());
//		
//
//	}
//
//}

//package com.centurylink.rss.domain.entity;
//
//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.hibernate.SessionFactory;
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
//public class SupportContactTest {
//	@Autowired
//	@Qualifier("sessionFactory")
//	SessionFactory sf;
//
//	@Test
//	public void testFindAllSupportContacts() {
//		SupportContact c1 = new SupportContact();
//		List<SupportContact> ls = new ArrayList<SupportContact>();
//		c1.setName("NAME1");
//		c1.setEmail("EMAIL1");
//		sf.getCurrentSession().save(c1);
//		ls = SupportContact.findAllSupportContacts();
//
//		assertNotNull("Contact not found", ls);
//		assertTrue("Contact not found", ls.contains(c1));
//		assertEquals("Contact not equal", 5, ls.size());
//	}
//
//}

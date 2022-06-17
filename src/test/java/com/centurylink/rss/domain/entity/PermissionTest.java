//package com.centurylink.rss.domain.entity;
//
//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.Collection;
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
//public class PermissionTest {
//	@Autowired
//	@Qualifier("sessionFactory")
//	SessionFactory sf;
//
//	private Permission p1, p2;
//	private List<Permission> lp;
//	private Collection<Long> cl;
//	private Set<Permission> sp;
//
//	@Before
//	public void setup() {
//		p1 = new Permission();
//		p2 = new Permission();
//		lp = new ArrayList<Permission>();
//		sp = new HashSet<Permission>();
//		cl = new ArrayList<Long>();
//
//		p1.setPermissionName("NAME1");
//		p2.setPermissionName("NAME2");
//
//		sf.getCurrentSession().save(p1);
//	}
//
//	@Test
//	public void testFindPermission() {
//		Permission found = Permission.findPermission(p1.getId());
//
//		assertNotNull("Permission not found", found);
//		assertEquals("Permission id not equal", p1.getId(), found.getId());
//		assertEquals("Permission name not equal", p1.getPermissionName(), found.getPermissionName());
//	}
//
//	@Test
//	public void testFindMultiplePermissions() {
//		cl.add(p1.getId());
//		sp = Permission.findMultiplePermissions(cl);
//
//		assertNotNull("Permission not found", sp);
//		assertTrue("Permission not contained", sp.contains(p1));
//		assertEquals("Permission not equal", 1, sp.size());
//	}
//
//	@Test
//	public void testFindAllPermissions() {
//		lp = Permission.findAllPermissions();
//
//		assertNotNull("Permission not found", lp);
//		assertTrue("Permission not contained", lp.contains(p1));
//		assertEquals("Permission not equal", 7, lp.size());
//	}
//
//	@Test
//	public void testSavePermission() {
//		Permission.savePermission(p2);
//		Permission found = Permission.findPermission(p2.getId());
//
//		assertNotNull("Permission not found", found);
//		assertEquals("Permission not equal", p2.getId(), found.getId());
//	}
//
//	@Test
//	public void testFindPermissionByName() {
//		Permission found = Permission.findPermissionByName("NAME1");
//
//		assertNotNull("Permission1 not found", found);
//		assertEquals("Permission1 name not equal", p1.getPermissionName(), found.getPermissionName());
//	}
//
//}

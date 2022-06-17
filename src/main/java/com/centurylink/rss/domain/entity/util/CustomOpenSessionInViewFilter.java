package com.centurylink.rss.domain.entity.util;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;

public class CustomOpenSessionInViewFilter extends OpenSessionInViewFilter {

	/* @Transactional is supposed to manage this stuff for us, but is not currently working .: we are going to manage the flushes manually. */
	
//	@Override
//	protected org.hibernate.Session getSession(org.hibernate.SessionFactory sessionFactory)
//			throws org.springframework.dao.DataAccessResourceFailureException {
//		Session session = sessionFactory.openSession();
//		session.setFlushMode(FlushMode.AUTO);
//		
//		return session;
//	}
//
//	@Override
//	protected void closeSession(org.hibernate.Session session, org.hibernate.SessionFactory sessionFactory) {
//		session.flush();
//		session.close();
//	}
}

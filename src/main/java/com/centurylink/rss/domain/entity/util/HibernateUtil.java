package com.centurylink.rss.domain.entity.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
@Service
public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static Log log = LogFactory.getLog(HibernateUtil.class);

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

	public void setSessionFactory(SessionFactory sessionFactory) {
		HibernateUtil.sessionFactory = sessionFactory;
	}
	
	public static void create(Object object) {
		sessionFactory.getCurrentSession().save(object);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> findAll(Class<?> T) {
		    return sessionFactory.getCurrentSession().createQuery("from " + T.getCanonicalName()).list();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T findById(Class<?> T, Long id) {
		if (id == null) return null;
		return (T) sessionFactory.getCurrentSession().get(T, id);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> findByName(Class<?> T, String name) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				"from " + T.getCanonicalName() + " A where A.name = :name");	 
		query.setParameter("name", name);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> findByWhereClause(Class<?> T, String whereClause) {
		return sessionFactory.getCurrentSession()
				.createQuery("from " + T.getCanonicalName() + " A " + whereClause).list();
	}

	public static void delete(Object object) {
		sessionFactory.getCurrentSession().delete(object);
		sessionFactory.getCurrentSession().flush();
	}
	
	public static <T> List<T> findByKeyValue(Class<?> T, HashMap keyValues) {
		String query = "from " + T.getCanonicalName() + " where ";

		Iterator keys = keyValues.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String value = (String) keyValues.get(key);
			value = value.replaceAll("'", "''");
			query += key + " = '" + value + "'";
			if (keys.hasNext()) {
				query += " AND ";
			}
		}

		Session session = sessionFactory.getCurrentSession();
		// session.beginTransaction();
		try {
			List result = session.createQuery(query).list();
			// session.getTransaction().commit();
			return result;
		} catch (HibernateException ex) {
			System.out.println("Trying to rollback after HibernateException");
			try {
				session.getTransaction().rollback();
			} catch (Throwable rbEx) {
				System.out
						.println("Could not rollback transaction after exception! "
								+ rbEx.getMessage());
			}
			// start a new transaction for downstream processes
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			throw ex;
		}
	}
	
	public static long selectCountWhere(Class<?> T, String whereClause) {
		String query = "select count(*) from " + T.getName() + " as A "+ whereClause;
		log.debug(query);
		Session session = sessionFactory.getCurrentSession();
		try {
			Long count = (Long) session.createQuery(query).uniqueResult();
			log.debug("count: " + count);
			return count==null?0:count.longValue();
		} catch (HibernateException ex) {
			log.debug("HibernateUtil: trying to rollback after HibernateException");
			try {
				session.getTransaction().rollback();
			} catch (Throwable rbEx) {
				log.debug("Could not rollback transaction after exception! " +  rbEx.getMessage());
			}
			// start a new transaction for downstream processes
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			throw ex;
		}
	}

	public static void update(Object _obj) {
		try {
		sessionFactory.getCurrentSession().update(_obj);
		sessionFactory.getCurrentSession().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static <T> List<T> findLiteByProperties(Class<?> T, String[] attrNames, String[] restriction, Object[] value) {
		Session session = sessionFactory.getCurrentSession();
		
		try {
			Criteria crit = session.createCriteria(T);
			if (restriction != null && restriction.length>0) {
				for (int i = 0; i < restriction.length; i++) {
					if (restriction[i] != null && !restriction[i].equals("")) {
						String className = value[i].getClass().getSimpleName().toLowerCase();
						if (className.equals("string") || className.equals("long")) {
							crit.add(Restrictions.eq("this."+restriction[i], value[i]));
						} else if (className.equals("arraylist") || className.equals("persistentbag")){ 
							List<String> values = (List<String>) value[i];
							crit.add(Restrictions.in("this."+restriction[i], values));
						} else if (className.equals("date[]")){ 
							Date[] dates = (Date[]) value[i];
							crit.add(Restrictions.between("this." + restriction[i], dates[0], dates[1]));
						} else {
							className = className.toLowerCase();
							if (className.equals("user")) { //work items don't have a user, they have 'assignee', etc.
								className = restriction[i];
							}
							crit.add(Restrictions.eq("this."+className, value[i]));
						} 
					}
				}
			}
			crit.addOrder(Order.desc("createdDate"));
			if (attrNames != null) {
				ProjectionList pl = Projections.projectionList();
				for (int i = 0; i<attrNames.length; i++) {
					pl.add(Projections.property(attrNames[i]), attrNames[i]);
				}
				crit.setProjection(pl).setResultTransformer(Transformers.aliasToBean(T));
			}
			return crit.list();
		} catch (HibernateException ex) {
			System.out.println("ManagerBase: trying to rollback after HibernateException");
			try {
				session.getTransaction().rollback();
			} catch (Throwable rbEx) {
				System.out.println("Could not rollback transaction after exception! " +  rbEx.getMessage());
			}
			// start a new transaction for downstream processes
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			throw ex;
		}
		
	}
	
	public static <T> List<T> findLiteById(Class<?> T, String[] attrNames, String[] ids) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria crit = session.createCriteria(T);
			List<Long> longIds = new ArrayList<Long>();
			if (ids != null) {
				for (int i=0; i<ids.length; i++) {
					try {
						longIds.add(Long.valueOf(ids[i]));
					} catch(NumberFormatException e) {
						throw e;
					}
				}
				crit.add(Restrictions.in("this.id", longIds));
			}

			if (attrNames != null) {
				ProjectionList pl = Projections.projectionList();
				for (int i = 0; i<attrNames.length; i++) {
					pl.add(Projections.property(attrNames[i]), attrNames[i]);
				}
				crit.setProjection(pl).setResultTransformer(Transformers.aliasToBean(T));
			}
			return crit.list();
		} catch (HibernateException ex) {
			System.out.println("ManagerBase: trying to rollback after HibernateException");
			try {
				session.getTransaction().rollback();
			} catch (Throwable rbEx) {
				System.out.println("Could not rollback transaction after exception! " +  rbEx.getMessage());
			}
			// start a new transaction for downstream processes
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			throw ex;
		}
	}
}


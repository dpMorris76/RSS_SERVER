package com.centurylink.rss.domain.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.centurylink.rss.domain.entity.util.HibernateUtil;

@Entity
@Table(name = "support_contacts")
public class SupportContact {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_seq_gen")
	@SequenceGenerator(name = "hibernate_seq_gen", sequenceName = "HIBERNATE_SEQ",  allocationSize = 1)
	@Column(name = "CONTACT_ID", length = 11)
	private Long id;
	
	@Column(name = "CONTACT_NAME", length = 50, nullable = false)
	private String name;
	
	@Column(name = "EMAIL", length = 75, nullable = false)
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public static List<SupportContact> findAllSupportContacts() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(SupportContact.class);
		@SuppressWarnings("unchecked")
		List<SupportContact> result = crit.list();
		return result;
	}

	
	
}

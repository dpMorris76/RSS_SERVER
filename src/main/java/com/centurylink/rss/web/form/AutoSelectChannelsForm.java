package com.centurylink.rss.web.form;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.Session;

import com.centurylink.rss.domain.entity.util.HibernateUtil;

@Entity
@Table(name = "auto_group_channels")
public class AutoSelectChannelsForm {
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_seq_gen")
	@SequenceGenerator(name = "hibernate_seq_gen", sequenceName = "HIBERNATE_SEQ")
	@Column(name = "group_Id", length = 11)
		private String groupId;
	@Column(name = "channel_Id", length = 11)
		private String channelId;
		private Set<Long> removeChannelIds; //Channel that will be removed from Auto Selected Channels.
		private Set<Long> channelIds;
		
//		switcher == "remove": set group channel ids to remove channel ids
//		switcher == "add": add to group's channel ids things in channel ids
//		switcher == "group": change group used for channels based on id
		private String switcher;
		
		public void removeChannelIds(long ids) {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.createQuery("Delete CHANNEL_ID u where ");
		}
		
		public void channelIds(Long ids) {
			
			
		}
		
		
		
		public String getGroupId() {
			return groupId;
		}
		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}
		public String getChannelId() {
			return channelId;
		}
		public void setChannelId(String channelId) {
			this.channelId = channelId;
		}
		public Set<Long> getChannelIds() {
			return channelIds;
		}
		public void setChannelIds(Set<Long> channelIds) {
			this.channelIds = channelIds;
		}
		public String getSwitcher() {
			return switcher;
		}
		public void setSwitcher(String switcher) {
			this.switcher = switcher;
		}
		public Set<Long> getRemoveChannelIds() {
			return removeChannelIds;
		}
		public void setRemoveChannelIds(Set<Long> removeChannelIds) {
			this.removeChannelIds = removeChannelIds;
		}
		
}

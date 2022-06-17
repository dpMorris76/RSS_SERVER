package com.centurylink.rss.business.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.centurylink.rss.domain.entity.Channel;
import com.centurylink.rss.domain.entity.User;
import com.centurylink.rss.domain.entity.util.HibernateUtil;
import com.centurylink.rss.web.form.ChannelForm;

@Service
public class ChannelDS {
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Channel> findAll() {
		return Channel.findAllChannels();
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(Channel c) {
		Channel.saveChannel(c);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Channel findById(Long id) {
		return Channel.findById(id);
	}

	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public Set<Channel> findMultipleById(Set<Long> ids){
		return Channel.findMultipleById(ids);
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public Long saveNewFromForm(ChannelForm channelForm) {
		return Channel.saveNewChannelFromForm(channelForm);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Channel> findMultipleByManagingEditor(User user) {
		return Channel.findChannelsByManagingEditor(user);
	}
	
	@Transactional(readOnly = false, propagation=Propagation.REQUIRED)
	public long saveFromForm(Channel channel, ChannelForm channelForm) {
		return Channel.saveChannelFromForm(channel, channelForm);
	}
	
	@Transactional(readOnly = false, propagation=Propagation.REQUIRED)
	public void updateFromForm(ChannelForm channelForm) {
		Channel.updateChannelFromForm(channelForm);
	}
	
	@Transactional(readOnly = true, propagation=Propagation.REQUIRED)
	public Channel findChannelByTitle(String title) {
		return Channel.findChannelByTitle(title);
	}
}

package com.centurylink.rss.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.centurylink.rss.domain.entity.ChannelGroup;
import com.centurylink.rss.web.form.ChannelForm;
import com.centurylink.rss.web.form.GroupForm;

@Service
public class ChannelGroupDS {
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<ChannelGroup> findAll() {
		return ChannelGroup.findAllGroups();
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(ChannelGroup g) {
		ChannelGroup.saveGroup(g);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ChannelGroup findById(Long id) {
		return ChannelGroup.findById(id);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long saveNewFromForm(GroupForm groupForm) {
		return ChannelGroup.saveNewGroupFromForm(groupForm);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long updateFromForm(GroupForm groupForm) {
		return ChannelGroup.updateGroupFromForm(groupForm);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long saveFromForm(ChannelGroup group, GroupForm groupForm) {
		return ChannelGroup.saveGroupFromForm(group, groupForm);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long saveFromForm(ChannelForm channelForm) {
		return ChannelGroup.saveGroupFromForm(channelForm);
	}
}

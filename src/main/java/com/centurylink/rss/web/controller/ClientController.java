package com.centurylink.rss.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.centurylink.rss.business.service.ChannelDS;
import com.centurylink.rss.business.service.ChannelGroupDS;
import com.centurylink.rss.business.service.UserDS;
import com.centurylink.rss.domain.entity.Channel;
import com.centurylink.rss.domain.entity.ChannelGroup;
import com.centurylink.rss.domain.entity.User;
import com.centurylink.rss.web.entity.LightWeightChannel;
import com.centurylink.rss.web.entity.LightWeightGroup;
import com.google.gson.Gson;

@RestController
public class ClientController {	
	private static Gson gson = new Gson();
	
	@Autowired
	UserDS userService;
	
	@Autowired
	ChannelDS channelService;
	
	@Autowired
	ChannelGroupDS groupService;
	
	Logger logger = LoggerFactory.getLogger(ClientController.class);
	

	@RequestMapping(value = "/client", method = RequestMethod.GET)
	public String checkOn() {
		return "True";
	}
	
	@RequestMapping(value = "/client/checkUsername/{username}", method = RequestMethod.GET)
	public String checkUsername(@PathVariable("username") String username) {
		if (userService.findUserByUsername(username) ==  null) {
			return "False";
		}
		
		return "True";
	}
	
	@RequestMapping(value = "/client/canChangeInstance/{username}", method = RequestMethod.GET)
	public String canChangeInstance(@PathVariable("username") String username) {
		User currUser = userService.findUserByUsername(username);
		if(User.isAboveContentProvider(currUser)){
			return "True";
		}
		return "False";
		
	}
	
	@RequestMapping(value = "/client/createUser/{username}/{groupId}", method = RequestMethod.GET)
	public String createUser(@PathVariable("username") String username, @PathVariable("groupId") long groupId) {
		String name = userService.createNewBasicFeederUserFromLdap(username, groupId);
		if(name == ""){
			return "user not created";
		}else{
			return name;
		}
	}
	
	@RequestMapping(value = "/client/getChannels/{username}", method = RequestMethod.GET)
	public List<LightWeightChannel> getLightWeightChannels(@PathVariable("username") String username) {
		User user = userService.findUserByUsername(username);
		List<LightWeightChannel> lwcList = new ArrayList<LightWeightChannel>();
		
		if (user.getUserPermissionDesc().toLowerCase().contains("admin")) {
			LightWeightChannel lwc = new LightWeightChannel();
			Channel channel = channelService.findById(Channel.ADMIN_CHANNEL_ID);
			lwc.setChannelId(channel.getId().toString());
			lwc.setChannelName(channel.getTitle());
			lwc.setGroupName("Admin");
			lwcList.add(lwc);
		}
		
		for (Channel channel : user.getChannels()) {
			LightWeightChannel lwc = new LightWeightChannel();
			lwc.setChannelId(channel.getId().toString());
			lwc.setChannelName(channel.getTitle());
			lwc.setGroupName(channel.getChannelGroup().getTitle());
			lwcList.add(lwc);
		} 
		
		return lwcList;
	}
	
	@RequestMapping(value = "/client/getGroups", method = RequestMethod.GET)
	public List<LightWeightGroup> getLightWeightGroups() {
		List<ChannelGroup> channelGroupList = groupService.findAll();
		List<LightWeightGroup> lwgList= new ArrayList<LightWeightGroup>();
		
		for(ChannelGroup cg : channelGroupList){
			LightWeightGroup lwg = new LightWeightGroup();
			lwg.setGroupName(cg.getTitle());
			lwg.setGroupId(String.valueOf(cg.getId()));
			lwgList.add(lwg);
		}
		
		return lwgList;
	}
}

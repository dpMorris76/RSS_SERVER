package com.centurylink.rss.web.service.util;

import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centurylink.rss.domain.entity.User;

public class UserState implements HttpSessionBindingListener {

	private static Log log = LogFactory.getLog(UserState.class);
    
	private User user = null;
    
    HashMap<String,String> currentViewInfo = new HashMap<String,String>();
    
    public HashMap<String, Object> MiscObjects = new HashMap<String, Object>();
    
    private static HashMap<User, HttpSession> currentUsersInfo = new HashMap<User, HttpSession>();
    
    public UserState() {
        super();
    }
    
    public static HashMap<User, HttpSession> getCurrentUsersInfo() {
    	return currentUsersInfo;
    }

    public void setUser(User user, HttpSession session) {
        this.user = user;
        currentUsersInfo.put(user, session);
    }

    public User getUser() {
        return user;
    }
    
	public HashMap<String, String> getCurrentViewInfo() {
		return currentViewInfo;
	}
	
	public void setCurrentViewInfo(HashMap<String, String> currentViewInfo) {
		this.currentViewInfo = currentViewInfo;
	}
 
	public void valueBound(HttpSessionBindingEvent event) {
		log.debug("Binding user state");
    }

	public void valueUnbound(HttpSessionBindingEvent event) {
		log.debug("Unbinding user state");
		cleanUp(event.getSession());
	}

    private void cleanUp(HttpSession session) {
        currentUsersInfo.remove(session);
        this.user = null;
    }
	
}


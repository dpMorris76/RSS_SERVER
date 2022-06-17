package com.centurylink.rss.web.service.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class SessionTimeoutCookieFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResp = (HttpServletResponse) resp;
        HttpServletRequest httpReq = (HttpServletRequest) req;
        long currTime = System.currentTimeMillis();
        long expiryTime = currTime +  1800 * 1000;
        Cookie cookie = new Cookie("serverTime", "" + currTime);
        cookie.setPath("/");
        httpResp.addCookie(cookie);
        if (httpReq.getRemoteUser() != null) {
            cookie = new Cookie("sessionExpiry", "" + expiryTime);
        } else {
            cookie = new Cookie("sessionExpiry", "" + currTime);
        }
        cookie.setPath("/");
        httpResp.addCookie(cookie);
        filterChain.doFilter(req, resp);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}

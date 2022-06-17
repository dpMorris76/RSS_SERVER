<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div>
	<div id="viewtitle">
		<h3 id="titleheader">Welcome to the RSS Feeder</h3>
		<p>The <a href="feeds">Subscriptions</a> page is where you can see the channels and channel groups available for subscription.</p>
		<sec:authorize	access="hasAnyRole('Content_Submission','Content_Review')">
		<p>The <a href="contentHome">Content</a> pages are used to create or review content stories.</p>
		</sec:authorize>
		<sec:authorize	access="hasAnyRole('Channel_Administration', 'Channel_Group_Administration')">
		<p>The <a href="channelHome">Channels</a> pages are used to create or review channels.</p>
		</sec:authorize>
		<sec:authorize	access="hasRole('Channel_Group_Administration')">
		<p>The <a href="groupHome">Groups</a> pages are used to create or review channel groups and change the users able to submit or review content for a group.</p>
		</sec:authorize>
		<sec:authorize access="hasRole('User_Administration')">
		<p>The <a href="userHome">Users</a> grid can be used to find users and modify their permissions for the app.</p>
		</sec:authorize>
		
		<p>The <a href="tools">Tools</a> page contains links to commonly used pages.</p>
	</div>
</div>
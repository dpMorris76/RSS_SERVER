<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="hasRole('User_Administration')">
  <div>
    <div id="viewtitle">
      <span id="titleheader">Welcome to the RSS Feeder user
        modification and list section.</span>
      <p>
        The <a href="userGrid">Single Edit</a> page is used to
        individually modify user permissions
      </p>
      <p>
        The <a href="bulkEdit">Bulk Edit</a> page is used to modify
        multiple users simultaneously.
      </p>
      <sec:authorize access="hasRole('Subscription')">
        <sec:authorize access="hasRole('Content_Submission')">
          <sec:authorize access="hasRole('Content_Review')">
            <sec:authorize access="hasRole('Channel_Administration')">
              <sec:authorize
                access="hasRole('Channel_Group_Administration')">
                <p>
                  The <a href="autoSelectChannels">Auto-Selected
                    Channels</a> page is used to modify the default channels
                  for a selected group.
                </p>
                <!-- 		<p>Admins are able to alter user permissions, groups, and channels.</p> -->
              </sec:authorize>
            </sec:authorize>
          </sec:authorize>
        </sec:authorize>
      </sec:authorize>
    </div>
  </div>
  <script>
  function calcOffset() {
	    var serverTime = getCookie('serverTime');
	    serverTime = serverTime==null ? null : Math.abs(serverTime);
	    var clientTimeOffset = (new Date()).getTime() - serverTime;
	    setCookie('clientTimeOffset', clientTimeOffset);
	    checkSession();
	}
	
	function checkSession() {

	    var sessionExpiry = Math.abs(getCookie('sessionExpiry'));
	    var timeOffset = Math.abs(getCookie('clientTimeOffset'));
	    var localTime = (new Date()).getTime();
	    if (sessionExpiry - localTime <= 60000) {
	    	alert("You session has expired. Anything you submit at this point will not go through. Please Refresh.");
	    } else if(sessionExpiry - localTime <= 300000){
	    	alert("You must actively use this site to avoid having your session expired.");
	        setTimeout('checkSession()', 240000);
	    }
	    else {		
	        setTimeout('checkSession()', 60000);
	    }
	}
	
	function getCookie(cname) {
	    var name = cname + "=";
	    var decodedCookie = decodeURIComponent(document.cookie);
	    var ca = decodedCookie.split(';');
	    for(var i = 0; i <ca.length; i++) {
	        var c = ca[i];
	        while (c.charAt(0) == ' ') {
	            c = c.substring(1);
	        }
	        if (c.indexOf(name) == 0) {
	            return c.substring(name.length, c.length);
	        }
	    }
	    return "";
	}
	
	function setCookie(cname, exdays) {
	    var d = new Date();
	    d.setTime(d.getTime() + (exdays));
	    var expires = "expires="+ d.toUTCString();
	    document.cookie = cname + "=" + expires + ";path=/";
	}

	window.onLoad = calcOffset();
  </script>
</sec:authorize>

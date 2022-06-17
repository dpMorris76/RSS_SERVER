<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<sec:authorize
  access="hasAnyRole('Content_Submission', 'Content_Review')">
  <c:choose>
  <c:when test="${errorMsg != null}">
    <span>
      ${errorMsg }
    </span>
  </c:when>
  <c:otherwise>
    <div>
      <div>
        Id: ${sp.id}
      </div>
      <div>
        Title: ${sp.title}
      </div>
      <div>
        Channel Name(s): ${sp.channelName}
      </div>
      <div>
      	<c:forEach items="${links}" var="element">
			<div>
			  <div class="fieldVal">Link: <a href="${element.link}"
	           	target="_blank">${element.linkName}</a>
	          </div>
	        </div>
		</c:forEach>
      </div>
      <div>
        Description: ${sp.description}
      </div>
      <div>
        Published on: ${sp.publishDate}
      </div>
      <div>
        Expired on: ${sp.expirationDate}
      </div>
      <div>
        Approved by: ${sp.approvedBy}
      </div>
      <div>
        Was high priority: <c:choose><c:when test="${sp.isHighPriority}">Yes</c:when><c:otherwise>No</c:otherwise></c:choose>
      </div>
    </div>
  </c:otherwise>
  </c:choose>
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
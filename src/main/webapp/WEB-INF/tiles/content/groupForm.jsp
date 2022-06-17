<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<sec:authorize	access="hasAnyRole('Channel_Group_Administration')">
<script type="text/javascript" src="js/validate_Group.js"></script>
<script type="text/javascript" src="js/tinymce/tinymce.min.js"></script>
<script type="text/javascript">
tinymce.init({
    selector: "textarea"
 });
</script>
<div>
	<div id="viewtitle">
	
		<span id="titleheader">Channel Group Form <input type="button" class="btn help" value="?" id="helpButton" /></span>
		<!-- Instructions popup -->
			<div id="instructionPopup" style="display: none;"
				title="Instructions" align="center" tabindex="-1">
				<div id="instructions">
					Please fill out all <span class="required">required</span> fields. <br />
					<br /> <span class="emphasized">Group Title</span> is the name you would like for this group. Changing this name will not affect its id or the content published to its groups.
					<br /> <br />
					<span class="emphasized">Select Content Providers</span> for this group will specify that those users that have this AND the 'Content_Submission' permission in the Users menu will be able to post content to channels within this group.
					<br /> <br />
					<span class="emphasized">Select Content Reviewers</span> for this group will specify that those users that have this AND the 'Content_Review' permission in the Users menu will be able to publish, edit, or deny content to channels within this group.
					<br /> <br />
					Upon completion, select '<span class="emphasized">Submit</span>' 
					to create or update this group or its associated users.
				</div>
			</div>
			<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
	<link rel="stylesheet" href="css/jquery-ui-1.11.4.custom/jquery-ui.css" type="text/css">
	<script type="text/javascript">
		$(function() {
			$("#helpButton").click(function(event) {
				//Initial settings for dialogue
				$("#instructionPopup").dialog({
					autoOpen : false,
					modal : true,
					buttons : {
						'OK' : function() {
							$(this).dialog("close");
						}
					}
				});

				//Open the dialogue
				$("#instructionPopup").dialog("open");
				event.preventDefault();
			});
		});
	</script>
	<!-- -->
		<div id="edits">
			<form:form method="POST" id="channelGroupForm" action="updateGroup" modelAttribute="channelGroupForm">
				<form:hidden path="groupId"/>
				<p class="required">* indicates a REQUIRED field</p>
       			Group Title (Max length:50 characters)<span class="required">*</span>
       			<br/>
       			<form:input path="groupTitle"></form:input>
       			<br /><br/>
       			Select Content Providers for this Group
       			<div id="storyFormChannels" >
					<c:forEach var="user" items="${contentProviders}">
						<form:checkbox path="contentProviderIds" value="${user.id}" class="formCheckbox"/>${user.lname}, ${user.fname} (${user.username}) <br />
					</c:forEach>
       			</div>
				<br />
				Select Content Reviewers for this Group
				<div id="storyFormChannels" >
					<c:forEach var="user" items="${contentReviewers}">
						<form:checkbox path="contentGatekeeperIds" value="${user.id}" class="formCheckbox"/>${user.lname}, ${user.fname} (${user.username})<br />
					</c:forEach>
       			</div>
				<br />
				<input type="submit" value="Submit"></input><br/>
			</form:form>
		</div>
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<sec:authorize	access="hasAnyRole('Channel_Administration', 'Channel_Group_Administration')">
	
<script type="text/javascript" src="js/tinymce/tinymce.min.js"></script>
<script type="text/javascript">
tinymce.init({
    selector: "textarea"
 });
</script>
<div>
	<div id="viewtitle">
		<span id="titleheader">Channel Submission Form <input type="button" class="btn help" value="?" id="helpButton" /></span>
		<!-- Instructions popup -->
			<div id="instructionPopup" style="display: none;" title="Instructions" align="center" tabindex="-1">
				<div id="instructions">Look over the channel and if you would
					like to edit it, select the '<span class="emphasized">Edit Channel</span>' button.</div>
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
				$("#denialPopup").dialog("open");
				event.preventDefault();
			});
		});
	</script>
	<!-- -->
		<div id="edits">
			<form:form method="POST" id="channelForm" action="updateChannel" modelAttribute="ChannelForm">
				<form:hidden path="channelEditorEmail" value="${currentUser.email}"></form:hidden>
				<form:hidden path="channelCategories"></form:hidden>
				<form:hidden path="channelCommentsLink"></form:hidden>
				<form:hidden path="channelLink"></form:hidden>
				Group(s)<br />
				<form:select path="channelGroups">
				<form:option value="">Select an existing group for this channel to be in</form:option>
				<c:forEach var="grp" items="${Groups}">
						<c:if test="${grp.title != null}">
							<form:option value="${grp.id}">${grp.title} (id:${grp.id})</form:option>
						</c:if>
       				</c:forEach>
       			</form:select>
       			<br/><br/>
       			Add Group Title (if the one you would like does not exist in the above list):
       			<br/>
       			<form:input path="channelNewGroup"></form:input>
				<br />
				<br /> Publish Date (For channels, any stories on this channel will only publish at this time)<br />
				<form:input type="datetime-local" path="channelPublishDate"></form:input>
				<br /><br/>
				Channel Title<br />
				<form:input path="channelTitle"></form:input>
				<br />
				<br />
				<br />Description<br />
				<form:textarea path="channelDescription"></form:textarea>
				<br /><br/>
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
    if (sessionExpiry - localTime < 30000) {
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
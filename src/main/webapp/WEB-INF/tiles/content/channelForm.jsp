<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<sec:authorize	access="hasAnyRole('Channel_Administration', 'Channel_Group_Administration')">
<script type="text/javascript" src="js/validate_Channel.js"></script>
<script type="text/javascript" src="js/tinymce/tinymce.min.js"></script>
<script type="text/javascript">
tinymce.init({
    selector: "textarea",
    height: 250
 });
</script>
<div>
	<div id="viewtitle">
		<span id="titleheader">Channel Submission Form <input type="button" class="btn help" value="?" id="helpButton" /></span>
		<!-- Instructions popup -->
			<div id="instructionPopup" style="display: none;"
				title="Instructions" align="center" tabindex="-1">
				<div id="instructions">
					Please fill out all <span class="required">required</span> fields.
					<br /> <br /> <span class="emphasized">Channel Title</span> is the name you would like
					for this channel.<br /> <br /> <span class="emphasized">Description</span>
					is a short summary of what this channel is about.<br /> <br />Upon
					completion, select '<span class="emphasized">Submit</span>' to
					create the channel in the specified group.
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
			<form:form method="POST" id="channelForm" action="updateChannel" modelAttribute="ChannelForm">
				<form:hidden path="chanId" />
				<form:hidden path="channelEditorEmail" value="${currentUser.email}"></form:hidden>
				<form:hidden path="channelCategories"></form:hidden>
				<form:hidden path="channelCommentsLink"></form:hidden>
				<form:hidden path="channelLink"></form:hidden>
				<p class="required">* indicates a REQUIRED field</p>
				<br/>
				Channel Title (Max length:50 characters)<span class="required">*</span><br />
				<form:input path="channelTitle"></form:input>
				<br />
				<br />
				<br />Description<span class="required">*</span><br />
				<form:textarea path="channelDescription" id="tinyeditor"></form:textarea>
				<br /><br/>
				<input type="submit" value="Submit" id="submit" onClick="return trySubmit()"></input><br/>
			</form:form>
		</div>
	</div>
</div>
<script>
$.datepicker.regional[""].dateFormat = 'yy-mm-dd';
$.datepicker.setDefaults($.datepicker.regional['']);
$('#pupDatePicker').datepicker();
$("#ui-datepicker-div").css("display", "none");    

function trySubmit() {
    var editorContent = tinyMCE.get('tinyeditor').getContent();
    if (editorContent == '' || editorContent == null)
    {
        // Add error message if not already present
        if (!$('#editor-error-message').length)
        {
            $('<span id="editor-error-message">Description required</span>').insertAfter($(tinyMCE.get('tinyeditor').getContainer()));
        }
        
        return false;
    }
    else if(editorContent.length > 3999)
    {
    	if (!$('#editor-error-message').length)
        {
            $('<span id="editor-error-message">Description too large</span>').insertAfter($(tinyMCE.get('tinyeditor').getContainer()));
        }
    	
        return false;
    }
    else
    {
        // Remove error message
        if ($('#editor-error-message'))
            $('#editor-error-message').remove();
       		return true;
    }
}

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
//)
</script>
</sec:authorize>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authorize	access="hasRole('Content_Submission')">
<script type="text/javascript" src="js/tinymce/tinymce.min.js"></script>
<script type="text/javascript">
tinymce.init({
    selector: "textarea",
    height: 500,
    plugins: "textcolor colorpicker autoresize textcolor",
 
    toolbar: "undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | fontsizeselect | forecolor  backcolor",

 });
</script>
<script type="text/javascript" src="js/validate_Content.js"></script>
<link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css">
<script src="js/jquery.datetimepicker.full.min.js"></script>
<div>
	<div id="viewtitle">
		<span id="titleheader">Content Submission Form <input type="button" class="btn help" value="?" id="helpButton" /></span>
			<div id="instructionPopup" style="display: none;"
				title="Instructions" align="center" tabindex="-1">
				<div id="instructions">
					Please fill out all <span class="required">required</span> fields.
					<br /> <br /> <span class="emphasized">Select the Channel
					</span> you would like this story to be posted to.
					<br />
					If you do not see the Channel you would like to post to, 
					it may need to be created or you may need to be associated with that Channel Group.<br/>
					<br />Next, select the <span class="emphasized">date and
						time</span> you would like this story to be <span class="emphasized">published</span>
					(visible to a subscriber). This will become visible once the story
					has been <span class="emphasized">approved AND the publish
						date-time has past</span>. <br /> <br />
					<span class="emphasized">Expiration date</span> sets the date you
					would like the story to no longer be visible to a new subscriber to
					the channel(s) the story is posted to. Default is two weeks from
					the publish date. <br /> <br />
					<span class="emphasized">Headline</span> is the title of the
					content. <br /> <br />
					<span class="emphasized">Link to full story</span> is a link or full URL
					to the complete content of the story. <br /> <br />
					<span class="emphasized">Link Name</span> is the name of the link displayed 
					on the client. <br /> <br />
					<span class="emphasized">Description</span> is a short summary of
					the content. <br /> <br />Upon completion, select '<span
						class="emphasized">Submit</span>' to submit the content to be
					reviewed and published. <c:if test="${canApprove }" > 
                    If you are a content gatekeeper, you can approve it
                    with the '<span
						class="emphasized">Submit and Approve</span>' button. If you are editing an already
                    approved story, you should use the '<span
						class="emphasized">Submit and Approve</span>' button so
                    that you don't have to re-approve it.
                    </c:if>
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
		<div id="edits">
			<form:form method="POST" id="storyForm" action="updateStory" modelAttribute="StoryForm">
				<form:hidden path="storyId" />
				<form:hidden path="storyAuthorEmail" />
				<form:hidden path="storyCategories"></form:hidden>
				<form:hidden path="storyCommentsLink"></form:hidden>
				
				<p class="required">* indicates a REQUIRED field</p>
				<div class="formElement" id="element1">
					Select Channel to post to<span class="required">*</span>:<br />
					<form:select path="channelId"> <option value="" disabled="disabled">Select a channel</option><option value="---" >---</option>
						<c:forEach var="channel" items="${Channels}">
							<form:option value="${channel.id}">${channel.title}</form:option>							
						</c:forEach>
					</form:select>
				<div class="errorHolder"></div>
				<br />
				</div>
				<div class="formElement">
					<br /> Publish Date (Date format must match: <span class="monotype">yyyy/MM/dd HH:mm</span>)<span class="required">*</span><br />
					<c:choose>
						<c:when test="${empty StoryForm.approvedBy}">
							<form:input id="pupDatePicker" path="storyPublishDate"></form:input><br />
						</c:when>
						<c:otherwise>
							<form:input id="publishDate" path="storyPublishDate" readonly="true"></form:input><br />
						</c:otherwise>
					</c:choose>
				</div>
				<div class="errorHolder"></div>
				<br/>
				<div class="formElement">
				Expiration Date (Default is 2 weeks after Publish Date, Date format must match: <span class="monotype">yyyy/MM/dd HH:mm</span>)<br />
				<form:input id="expDatePicker" path="expirationDate"></form:input>
				<br />
				</div><br/>	
				<div class="formElement">
				Headline (Max length:100 characters) * Please avoid using special characters.<span class="required">*</span><br />
				<form:input id="storyTitle" path="storyTitle"></form:input>
				<br />
				</div>
				<div id="placeholder">
						<c:set var="count" value="0" scope="page" />
						<c:set var="tempName" value="template" scope="page" />
						
						<c:choose>
							<c:when test="${not empty StoryForm.links}">
								<c:forEach items="${StoryForm.links}" var="element">
									<c:if test = "${count > 0}">
										<c:set var="tempName" value="template${count}" scope="page" />
									</c:if>
									<c:set var="count" value="${count + 1}" scope="page"/>
									<div id="${tempName}">
										<div class="formElement">
											<div class="errorHolder"></div>
											<br />Link to Full Story (Max length:500 characters)<br />
											<form:input id="storyLink" class="storyLink"
												value="${element.link}" path="storyLink"></form:input>
											<br />
										</div>
										<div class="formElement">
											<div class="errorHolder"></div>
											<br />Link Name (Max length:70 characters)<br />
											<form:input id="storyLinkName" class="storyLinkName"
												value="${element.linkName}" path="storyLinkName"></form:input>
										</div>
										<br />
									</div>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<div id="template">
									<div class="errorHolder"></div>
									<div class="formElement">
										<br />Link to Full Story (Max length:500 characters)<br />
										<form:input id="storyLink" class="storyLink" path="storyLink"></form:input>
										<br />
									</div>
									<div class="errorHolder"></div>
									<div class="formElement">
										<br />Link Name (Max length:70 characters)<br />
										<form:input id="storyLinkName" class="storyLinkName"
											path="storyLinkName"></form:input>
										<br />
									</div>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
					
				<c:if test = "${count > 2}">
					<input id="addButton" type="button" value="Add Additonal Link" onclick="addLink()" style="width:15% ;float: left;margin-right: 10px; display:none;" />
				</c:if>
				
				<c:if test = "${count <= 1}">
					<input id="addButton" type="button" value="Add Additonal Link" onclick="addLink()" style="width:15% ;float: left;margin-right: 10px;" />
				</c:if>
					
				<c:if test = "${count > 0}">
					  <input id="removeButton" type="button" value="Remove Additonal Link" onclick="removeLink()" style="width:15% ; " />
				</c:if>
				<c:if test = "${count <= 0}">
            		<input id="removeButton" type="button" value="Remove Additonal Link" onclick="removeLink()" style="width:15% ; display:none;" />
				</c:if>
				
            	<br><br/>
				<div class="errorHolder"></div>
				<div class="formElement">
                <br />Point of contact name (Max length: 100)<br />
                <c:if test = "${empty StoryForm.storyPocName}">
               		<form:input id="storyPocName" path="storyPocName" value="${currentUser.lname}, ${currentUser.fname}"></form:input>
                </c:if>
                <c:if test = "${not empty StoryForm.storyPocName}">
               		<form:input id="storyPocName" path="storyPocName" value="${StoryForm.storyPocName}"></form:input>
                </c:if>
                <br />
                </div>
                <div class="errorHolder"></div>
                <div class="formElement">
                <br />Point of contact phone number (Max length: 25)<br />
                <c:if test = "${empty StoryForm.storyPocPhNbr}">
                	<form:input id="storyPocPhNbr" path="storyPocPhNbr" class="phone" value="${currentUser.phnNbr}"></form:input>
                </c:if>
                <c:if test = "${not empty StoryForm.storyPocPhNbr}">
                	<form:input id="storyPocPhNbr" path="storyPocPhNbr" class="phone" value="${StoryForm.storyPocPhNbr}"></form:input>
                </c:if>
                <br />
                </div>
                <div class="errorHolder"></div>
                <div class="formElement">
				<br /> Description<span class="required">*</span><br />
				<form:textarea path="storyDescription" id="tinyeditor" class="descriptionTextArea"></form:textarea>
				<br />
				</div><br/>
				<div class="formElement" id="element2">
                <c:if test="${not empty StoryForm.approvedBy}"> 
                  	<span class="approvedBy">Approved By: ${StoryForm.approvedBy}</span><br /><br />
                </c:if>
				<table style="width:100%; border-style:hidden;">
				<tr style="border-style:hidden;">
				<td style="border-style:hidden; width:2%">
				<form:checkbox path="isHighPriority" value="true"></form:checkbox></td><td style="border-style:hidden;">&nbsp;Check here to flag content as High Priority to audience, initiating pop-up notification to take action now. </td>
				</tr>
				</table>
				</div><br/>
				<c:choose>
					<c:when test="${not empty StoryForm.approvedBy}">
					  	<c:if test="${canApprove}">
					    	<input type="submit" value="Republish" id="submitAndApprove" onClick="return trySubmitAndApprove()" /><br/>
					  	</c:if>
					</c:when>
					<c:otherwise>
					  	<input type="submit" value="Submit" id="submit" onClick="return trySubmit()"></input><br/><br/>
					  	<c:if test="${canApprove}">
					    	<input type="submit" value="Submit and Approve" id="submitAndApprove" onClick="return trySubmitAndApprove()" /><br/>
					  	</c:if>
					</c:otherwise>
				</c:choose>
			</form:form>
            <br/>
            <input type="button" value="Preview" onclick="showPreview()" style="width:98% !important;" />
		</div>
	</div>
</div>
</sec:authorize>

<script type="text/javascript">
	$(document).ready(function(){
		// courtesy of this helpful dude: http://stackoverflow.com/questions/8760070/how-to-format-a-phone-number-with-jquery
		$('.phone').attr('value', (function(i, text) {
			if(text.length === 10){
		    	return text.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
			}else{
				return text.replace(/(\d{1})(\d{3})(\d{3})(\d{4})/, '($1)$2-$3-$4');
			}
		}));
	});
	
	//!/Invalid|NaN/.test(new Date(value).toString())
	jQuery('#pupDatePicker').datetimepicker();
	jQuery('#expDatePicker').datetimepicker();
	
	jQuery('#pupDatePicker').datetimepicker({
		 formatDate:'yyyy/MM/dd HH:mm',
		 minDate:'0',
		});

	jQuery('#expDatePicker').datetimepicker({
		 formatDate:'yyyy/MM/dd HH:mm',
		 minDate:'0',
		});
	
jQuery.validator.addMethod("dateTime", function (value, element) {
    var stamp = value.split(" ");
    var validDate = !/Invalid|NaN/.test(new Date(stamp[0]).toString());
    var validTime = /^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])(:([0-5]?[0-9]))?$/i.test(stamp[1]);
    return this.optional(element) || (validDate && validTime);
}, "Please enter a valid date and time.");
	var win= null;
	function showPreview()
	{
		// you need this because otherwise doc.write will write to the same window. 
		// making 2 webpages on 1 wepage.
		if(win != null)
		{
			win.close();
		}
		win = window.open('',  "Preview", "width= " + screen.width * .347 + ", height=" + screen.height * .657 + ", scrollbars=1, resizable=0");
		win.document.write("<html style=\"margin:0px;\">");
		win.document.getElementsByTagName("html").innerHTML = '';
		var to_write = '';
		to_write +="<head> ";
		to_write +=" <style type=\"text/css\"> a:link{color:blue;} a:visited{color:blue;}";
		to_write +=" ::-webkit-scrollbar{width:20px;}";
		to_write +=" ::-webkit-scrollbar-track{-webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3); -webkit-border-radius: 10px; border-radius: 10px; background-color: rgba(230, 230, 230, .5)}";
		to_write +=" ::-webkit-scrollbar-thumb { -webkit-border-radius: 10px; border-radius: 10px; background: rgba(200,200,200,0.8); -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.5); }";
		to_write +="body {overflow-x: scroll;}";
		to_write +=" </style>";
		to_write +="</head> <body style=\"width: 100% ; margin:0px;\">"; 
		to_write += '<div style="margin-right: 0px; margin-left: 0px; height: 55px; width: 100%; background-color: #A5D867; font-size:24px;" align="center" > <b>' + document.getElementById("storyTitle").value + "</b> </div>";
		to_write += '<div style="margin:8px;">';
		to_write += " <div style=\"width:";
		to_write +=" " +  100 + "% !important;  font-size:Large;overflow-x:scroll;\" >";
		to_write +=tinyMCE.get('tinyeditor').getContent();
		to_write +="</div>";
		if (document.getElementById("storyLink").value == "" || document.getElementById("storyLinkName").value == "")
		{
			// we don't have a link
		}
		else
		{
			var currLink = '';
			var currLinkName = '';
			for (var i = 0; i <= counter; i++) {
				if (i !== 0) {
					$('#template' + i).children().find('input').each(
							function() {
								if (this.id === "storyLinkName") {
									currLinkName = $(this).val();
								}
								if (this.id === "storyLink") {
									currLink = $(this).val();
								}
							});
				} else {
					$('#template').children().find('input').each(function() {
						if (this.id === "storyLinkName") {
							currLinkName = $(this).val();
						}
						if (this.id === "storyLink") {
							currLink = $(this).val()
						}
					});
				}
				to_write += "<br><br> <a href=\"" + currLink + "\" style=\"\"" + " >"
						+ currLinkName + "</a>";
			}

			// we have a link
		}
		to_write += " <br> <br> <br> </div> </body> </html>";
		win.document.write(to_write);
	}
	
	var counter = "${count}" - 1;
	function addLink() {
		if(validateLinks(true)){
			if(counter <= -1){
				counter = 0;
			}
			if (!(counter > 2)) {
				counter++;
				var div = document.getElementById('template'), clone = div
						.cloneNode(true); // true means clone all childNodes and all event handlers
				clone.id = 'template' + counter;
				document.getElementById('placeholder').appendChild(clone);
				$('#template' + counter).children().find('input').each(function() {
					$(this).val("");
					
				});
				$('#template' + counter).children().find('#linkError').each(function() {
					$(this).empty();
				});
				
				$("#removeButton").show();
				if (counter > 1){
					$("#addButton").hide();
				}
			} else {
				$("#addButton").hide();
			}
		}
		
		checkSession();
		
	}

	function removeLink() {
		$("#template" + counter).remove();
		counter--
		if (counter == 0) {
			$("#removeButton").hide();
		}
		if (counter <= 1) {
			$("#addButton").show()
		}
	}
	
	function replaceCommas(){
		
		$('#template').children().find('input').each(function() {
			$(this).val($(this).val().replace(/,/g , '|@|'));
		});
		
		for (var i = 1; i <= counter; i++) {
			$('#template' + counter).children().find('input').each(function() {
				$(this).val($(this).val().replace(/,/g , '|@|'));
			});
		}
	}		

	function groupCheck(checkClass) {
		if ($("#" + checkClass).attr('checked')) {
			$("." + checkClass).attr('checked', true);
		} else {
			$("." + checkClass).attr('checked', false);
		}
	}

	function trySubmit() {
		var valid = true;
		var editorContent = tinyMCE.get('tinyeditor').getContent();
		$("div.required").remove();
		$('#editor-error-message').remove();
		$('#channel-error-message').remove();
		if ($('#editor-error-message'))
			$('#editor-error-message').remove();
		else if ($('#channel-error-message'))
			$('#channel-error-message').remove();

		if ($('select#channelId').val() == '---') {
			if ($("#channel-error-message").val() != "") {
				//generates error message if the channel is invalid
				$('<span id="channel-error-message">Invalid Channel</span>')
						.insertAfter($('select#channelId'));
			}
			$('select#channelId').focus();
			valid = false;
		}
		if ($('#storyTitle').val() == "") {
			$("<div class='required'>Headline is Required</div>").appendTo(
					$("#storyTitle").parent().next("div.errorHolder")).focus();
			valid = false;
		}
		if (editorContent == '' || editorContent == null) {
			// Add error message if not already present
			$('<span id="editor-error-message">Description required</span>')
					.insertAfter(
							$(tinyMCE.get('tinyeditor').getContainer()));
			valid = false;
		}
		
		if(!validateLinks(false)){
			valid = false;
		}
		
		if(valid){
			replaceCommas();
		}
		
		return valid;
	}

	function trySubmitAndApprove() {
		var valid = trySubmit();
		if (valid) {
			$("#storyForm").attr('action', 'updateStoryAndApprove');
		} else {
			return false;
		}
	}
	
	function validateLinks(fromAddButton){
		
		var hasLink = false;
		var hasLinkName = false;

		var linkValid = true;
		
		if(!fromAddButton){
			$('#template').children().find('input').each(function() {
				if (this.id === "storyLink") {
					if($(this).val().length > 0 && $(this).val() !== ""){
						hasLink = true;
					}
				}
				if (this.id === "storyLinkName") {
					if($(this).val().length > 0 && $(this).val() !== ""){
						hasLinkName = true;
					}
					if((!hasLinkName && hasLink) || (hasLinkName && !hasLink)){
						linkValid  = false;
						$(this).parent().prev(".errorHolder").empty();
						$("<div id='linkError' class='required'>*A link must have both a Link and Link name, or have be removed.</div>")
						.appendTo($(this).parent().prev(".errorHolder"));
						$(this).focus();
					}
				}
				
			});
		}else{
			$('#template').children().find('input').each(function() {
				if (this.id === "storyLink") {
					if($(this).val().length > 0 && $(this).val() !== ""){
						hasLink = true;
					}
				}
				if (this.id === "storyLinkName") {
					if($(this).val().length > 0 && $(this).val() !== ""){
						hasLinkName = true;
					}
					if((!hasLink || !hasLinkName)){
						linkValid  = false;
						$(this).parent().prev(".errorHolder").empty();
						$("<div id='linkError' class='required'>*A link must have both a Link and Link name before adding an additional link.</div>")
						.appendTo($(this).parent().prev(".errorHolder"));
						$(this).focus();
					}
				}
				
			});
		}
		
		for (var i = 1; i <= counter; i++) {
			hasLink = false;
			hasLinkName = false;
			$('#template' + counter).children().find('input').each(function() {
				if (this.id === "storyLink") {
					if($(this).val().length > 0 && $(this).val() !== ""){
						hasLink = true;
					}
				}
				if (this.id === "storyLinkName") {
					if($(this).val().length > 0 && $(this).val() !== ""){
						hasLinkName = true;
					}
					if(!hasLink || !hasLinkName){
						linkValid  = false;
						$(this).parent().prev(".errorHolder").empty();
						$("<div id='linkError' class='required'>*A link must have both a Link and Link name, or have be removed.</div>")
						.appendTo($(this).parent().prev(".errorHolder"));
						$(this).focus();
					}
				}
				
			});
		}
		return linkValid;
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

</script>
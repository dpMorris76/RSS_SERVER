<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div>
	<div id="viewtitle">
		<span id="titleheader">Welcome to CenturyLink RSS Subscriptions <input type="button" class="btn help" value="?" id="helpButton" /></span>
		<br/><br/>
		<form:form method="post" action="updateFeeds" modelAttribute="feedForm"><label for="tags">Submissions Displayed: </label>
	    		<select name="selection" id="combobox"><option value="" disabled="disabled">Select a Channel Group to display its Channels</option>
	    		<option value="allGroups">All</option>
	    		<c:forEach var="grp" items="${AllGroups}"><option value="${grp.id}">${grp.title}</option></c:forEach>
	    		</select><input class="fixedwidthbtn" type="submit" name="submit" value="Submit"></form:form>
		<br/><br/>
		<!-- Instructions popup -->
		<div id="instructionPopup" style="display: none;"
				title="Instructions" align="center" tabindex="-1">
				<div id="instructions">
					Click the <span class="fakeLink">name of each channel</span>
					whose stories you would like to view. You may right-click the <span
						class="fakeLink">name of the channel</span> and click 'Copy shortcut' 
						if you need the URL for an external RSS program.
						
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
	<!-- End of instructions popup -->
		<div id="feeds">
		<sec:authorize	access="hasRole('Content_Review')">
		<!--  -->
		<span class="groupHeading">ADMIN APPROVAL CHANNEL</span>
		<table class="channelTable">
			<tr>
				<td class="channelHeading">
				-<a href="${fullUrl}${adminChannel.id}.xml">${adminChannel.title}</a>:
				</td>
				<td class="channelDescription">${adminChannel.description}</td>
			</tr>
		</table>
		<br />
		<!--  -->
		</sec:authorize>
		<c:forEach var="group" items="${selectedGroups}">
			<c:if test="${group.title != null}">
				<span class="groupHeading">${group.title}</span>
				<table class="channelTable">
				<c:forEach var="channel" items="${group.channels}">
					<c:if test="${channel != null}">
					<tr>
						<td class="channelHeading">-<a href="${fullUrl}${channel.id}.xml">${channel.title}</a>:</td>
						<td class="channelDescription">${channel.description}</td>
					</tr>
					</c:if>
				</c:forEach>
				</table>
				<br/>
			</c:if>
		</c:forEach>
		</div>
	</div>
</div>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%-- <sec:authorize access="hasAnyRole('Subscription','Content_Submission','Content_Review','Channel_Administration','Channel_Group_Administration','User_Administration')"> --%>
<sec:authorize access="hasRole('Subscription')">
  <sec:authorize access="hasRole('Content_Submission')">
    <sec:authorize access="hasRole('Content_Review')">
      <sec:authorize access="hasRole('Channel_Administration')">
        <sec:authorize access="hasRole('Channel_Group_Administration')">
          <sec:authorize access="hasRole('User_Administration')">
            <div id="welcomecontainer">
              <div id="viewtitle">
                <span id="titleheader"> Auto-Selected Channels <input
                  type="button" class="btn help" value="?"
                  id="helpButton" /></span>
                <!--       Instructions popup -->
                <div id="instructionPopup" style="display: none;"
                  title="Instructions" align="center" tabindex="-1">
                  <div id="instructions">
                  Select a group to edit the auto selected channels 
                  of that group. Once you've chosen your group,
                  select the channels which you would like to add or remove
                  and hit the appropriate button. If you are adding a channel 
                  to that groups auto-selected channels, it will be assigned
                  to all the users of that group. If you are removing a channel,
                  then it will not un-assign the users of that group from the 
                  selected channels.
                  </div>
                </div>

                <div>
                  <form:form method="POST"
                    modelAttribute="autoSelectChannelsForm"
                    id="autoSelectChannelsForm"
                    action="autoSelectChannels">
                    <form:hidden path="switcher" id="switcher" value=""></form:hidden>
                    <label for="tags">Select Group:</label> ${selectedGroupTitle}
                    <form:select path="groupId" onchange="changeGroup()" >
                      <form:option value="${group.getId()}">${group.getTitle()}</form:option>
                      <c:forEach var="grp" items="${groups}">
                        <form:option value="${grp.getId()}">${grp.getTitle()}</form:option>
                      </c:forEach>
                    </form:select>

                    <%-- 			<form:hidden path="removeChannelId" value="" id="removeChannelId"/> --%>
                    <!-- 			<label for="tags">Add Channel:</label> -->
                    <%-- 			<form:select path="channelId"> --%>
                    <%-- 				<c:forEach var="chan" items="${channels}"> --%>
                    <%-- 					<form:option value="${chan.getId()}">${chan.getTitle()}</form:option> --%>
                    <%-- 				</c:forEach> --%>
                    <%-- 			</form:select>			 --%>
                    <!-- 		</div> -->
                    <div style="margin-top: 20px; margin-bottom: 20px;">
                      <table class="borderless">
                        <tr class="borderless">
                          <td width="250px" class="borderless align-top">

                            <table class="permissionsBlock" style="float:top;">
                              <tr>
                                <td colspan="2" class="blockHeader">Add
                                  Channels</td>
                              </tr>
                              <c:forEach var="chan" items="${channels}">
                                <tr>
                                  <td class="permCheck"><form:checkbox
                                      value="${chan.id}"
                                      path="channelIds"
                                      id="checkbox${chan.title}" />&nbsp;</td>
                                  <td class="permName">${chan.title}</td>
                                </tr>
                              </c:forEach>
                            </table>
                          </td>
                          <td width="250px" class="borderless align-top">
                            <table class="permissionsBlock">
                              <tr style="border:0px solid #000000 !important;">
                                <td colspan="2" class="blockHeader">Remove
                                  Channels</td>
                              </tr>
                              <c:forEach var="chan"
                                items="${group.getAutoChannels()}">
                                <tr>
                                  <td class="permCheck"><form:checkbox
                                      value="${chan.id}"
                                      path="removeChannelIds"
                                      id="checkbox${chan.title}" />&nbsp;</td>
                                  <td class="permName">${chan.title}</td>
                                </tr>
                              </c:forEach>
                            </table>

                          </td>
                        </tr>
                      </table>
                    </div>
                  </form:form>

                  <table class="borderless">
                    <tr class="borderless">
                      <td class="borderless"><input type="button"
                        class="fixedwidthbtn" value="Add"
                        onclick="addAutoSelectedChannels()" /></td>
                      <td class="borderless"><input type="button"
                        class="fixedwidthbtn" value="Remove"
                        onClick="removeAutoSelectedChannels()" /></td>
                      <td class="borderless"><form:form action="autoSelectChannels">
                          <input type="submit" class="fixedwidthbtn"
                            value="Cancel" onclick="this.form.reset()" />
                        </form:form></td>
                    </tr>
                  </table>
                  <!--           Takes user back to the 'Welcome' page -->


                </div>
              </div>
            </div>
            <script
              src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
            <link rel="stylesheet"
              href="css/jquery-ui-1.11.4.custom/jquery-ui.css"
              type="text/css">
            <script type="text/javascript">
													$(function() {
														$("#helpButton")
																.click(
																		function(
																				event) {
																			//Initial settings for dialogue
																			$(
																					"#instructionPopup")
																					.dialog(
																							{
																								autoOpen : false,
																								modal : true,
																								buttons : {
																									'OK' : function() {
																										$(
																												this)
																												.dialog(
																														"close");
																									}
																								}
																							});

																			//Open the dialogue
																			$(
																					"#instructionPopup")
																					.dialog(
																							"open");
																			event
																					.preventDefault();
																		});
													});
													$(document).ready(
															function() {

															});
												</script>

            <script type="text/javascript">
													function addAutoSelectedChannels() {
														if (confirm("Are you sure you want to add selected channel(s)?")) {
															document
																	.getElementById("switcher").value = "add";
															document
																	.getElementById(
																			"autoSelectChannelsForm")
																	.submit();
														} else {
															// do nothing, pass.
														}
														;
													}

													function removeAutoSelectedChannels() {
														if (confirm("Are you sure you want to remove selected channel(s)?")) {
															document
																	.getElementById("switcher").value = "remove";
															document
																	.getElementById(
																			"autoSelectChannelsForm")
																	.submit();
														} else {

														}
													}
													
													function changeGroup()
													{
														document.getElementById("switcher").value = "group";
														document.getElementById("autoSelectChannelsForm").submit();
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
          </sec:authorize>
        </sec:authorize>
      </sec:authorize>
    </sec:authorize>
  </sec:authorize>
</sec:authorize>
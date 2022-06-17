<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="hasRole('User_Administration')">
  <div id="welcomecontainer">
    <div id="viewtitle">
      <span id="titleheader">Bulk Edit <input type="button"
        class="btn help" value="?" id="helpButton" /></span>
      <!-- Instructions popup -->
      <div id="instructionPopup" style="display: none;"
        title="Instructions" align="center" tabindex="-1">
        <div id="instructions">
          Please select the users you wish to change, and the channels
          that you wish to assign to them. <br /> <br /> Upon
          completion, select '<span class="emphasized">Submit</span>' to
          save your changes. You will then be redirected to this same
          page, to finish assigning your users. If you are done
          assigning your users or want to get some users back, 
          click on 'Home' on the left side. 
          <br/> 
          <br/>
          If you wish to edit someone else's reports, use the top part of
          the form to select them by ADID.
        </div>
      </div>
      <link rel="stylesheet"
        href="css/jquery-ui-1.11.4.custom/jquery-ui.css" type="text/css">
      <script
        src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
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
      <div style="margin-left: 25px;">
        <c:if test="${changeBulkEditFormError != null}"> 
          <p class="error">${changeBulkEditFormError}</p> 
        </c:if>
        <table class="borderless">
          <tr class="borderless">
            <td class="borderless" style="font-size: 16px;">
              <h5>Edit a different manager's users by ADID:</h5>
            </td>
            <td class="borderless"><form:form action="changeBulkEditUsers"
                method="post" id="changeBulkEditUsersForm"
                modelAttribute="changeBulkEditUsersForm">
                <form:input path="newADID" id="newADID" value=""
                  style="width: 150px !important;" maxlength="10" />
                <input type="submit" value="Edit" style="width: 150px !important;"/>
              </form:form></td>
          </tr>
        </table>
        <h3>
          Editing Reports of: <u class="modUser">${selectedUser.fname}
            ${selectedUser.lname}</u>
        </h3>
        <form:form action="bulkEdit" id="bulkEditForm"
          modelAttribute="bulkEditForm">
          <input class="fixedwidthbtn" type="submit" value="Submit" />
          </br>
          </br>
          <div style="float: left; margin-right: 25px;">
            <td id="usersBlock">
              <table class="permissionsBlock">
                <tr>
                  <td colspan="2" class="blockHeader">Users</td>
                </tr>
                <c:forEach var="user" items="${users}">
                  <tr>
                    <td class="permCheck"><form:checkbox
                        value="${user.id}" path="userIds"
                        id="checkbox${user.getFullName()}" />&nbsp;</td>
                    <td class="permName">${user.getFullName()}</td>
                  </tr>
                </c:forEach>
              </table>
          </div>
          <div>
            <!--       Style="float:left; -->
            <table class="permissionsBlock">
              <tr>
                <td colspan="2" class="blockHeader">Subscribed
                  Channels</td>
              </tr>
              <c:forEach var="chan" items="${channels}">
                <tr>
                  <c:choose>
                    <c:when
                      test="${ autoChannelsIds.contains(chan.getId()) }">
                      <td class="permCheck">
                        <div class="alwaysCheck">
                          <form:checkbox style="display:none;"
                            value="${chan.id}" path="channelIds"
                            id="checkbox${chan.title }" checked="true" />
                          &nbsp;
                        </div>
                      </td>
                      <td class="alwaysName">${chan.title }</td>
                    </c:when>
                    <c:otherwise>
                      <td class="permCheck"><form:checkbox
                          value="${chan.getId()}" path="channelIds"
                          id="checkbox${chan.title}" />&nbsp;</td>
                      <td class="permName">${chan.title}</td>
                    </c:otherwise>
                  </c:choose>
                </tr>
              </c:forEach>
            </table>
          </div>
          <!-- make a hidden thing of all the previous users, so that we don't need to re-find them every time.  -->
          <!-- this dummy data is used so that we can check if we actually had been on this page before -->

        </form:form>
      </div>
    </div>
  </div>
</sec:authorize>

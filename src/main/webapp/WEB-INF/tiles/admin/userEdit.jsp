<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="hasRole('User_Administration')">
  <div id="welcomecontainer">
    <div id="viewtitle">
      <span id="titleheader">Single Edit <input type="button"
        class="btn help" value="?" id="helpButton" /></span>
      <!-- Instructions popup -->
      <div id="instructionPopup" style="display: none;"
        title="Instructions" align="center" tabindex="-1">
        <div id="instructions">
          Please select the <b><u>Permissions</u></b> this user should
          have by checking the appropriate boxes. For predefined sets of
          permissions, you may choose from the <b><u>Roles</u></b> to
          autopopulate the boxes. <br /> <br />If this does not
          exactly match your needs, you may also create a custom set of
          permissions by manually checking which permission(s) on the
          right you would like this user to have. <br /> <br /> You
          must also associate the user with an existing group by
          selecting the name of the group from the dropdown. <br /> <br />
          You may also pick channels that are important to a user, this
          makes it so that if they are using the CTL RSS Client any
          story from a selected channel will force them to acknowledge
          the story and make it visible in the client. <br /> <br />
          Keep in mind that to Submit or Review content, you will ALSO
          need Content Provider and/or Content Reviewer checked for the
          group that contains the channel(s) you would like to interact
          with. <br /> <br /> Upon completion, select '<span
            class="emphasized">Submit</span>' to save your change(s) to
          this user.
        </div>
      </div>
      <script
        src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
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
      <h5>
        Editing Permissions for: <u class="modUser">${user.fname}
          ${user.lname}</u>
      </h5>
      <form:form action="updateUser"
              id="userPermissionsForm"
              modelAttribute="userPermissionsForm">
      <table id="userPermissionsButtons">
        <tr>
          <!-- Pseudo roles -->
          <td>
            <table>
              <tr>
                <td class="blockHeader">Roles</td>
              </tr>
              <tr>
                <td><input type="button" value="Basic User"
                  class="btn" onClick="basicUser();" /></td>
              </tr>
              <tr>
                <td><input type="button" value="Content Provider"
                  class="btn" onClick="contentProvider();" /></td>
              </tr>
              <tr>
                <td><input type="button" value="Content Gatekeeper"
                  class="btn" onClick="contentGatekeeper();" /></td>
              </tr>
              <tr>
                <td><input type="button"
                  value="Channel Administrator" class="btn"
                  onClick="channelAdministrator();" /></td>
              </tr>
              <tr>
                <td><input type="button"
                  value="Super Administrator" class="btn"
                  onClick="superAdministrator();" /></td>
              </tr>
              <tr>
                <td><input type="button" value="Supervisor"
                  class="btn" onClick="apsUser();" /></td>
              </tr>
            </table>
          </td>
          <!-- Separation -->
          <td>&nbsp;</td>
          <td>
            <div style="border-left: 1px solid #8CC63F; height: 125px;"></div>
          </td>
          <td>&nbsp;</td>
          <!-- Permissions -->
          <td id="permissionsBlock">
              
              <form:hidden path="id" />
              <table class="permissionsBlock">
                <tr>
                  <td colspan="2" class="blockHeader">Permissions</td>
                </tr>
                <c:forEach var="permission" items="${permissions}">
                  <tr>
                    <td class="permCheck"><form:checkbox
                        value="${permission.id}" path="permissionIds"
                        id="checkbox${permission.permissionName}" />&nbsp;</td>
                    <td class="permName">${permission.permissionName}</td>
                  </tr>
                </c:forEach>
              </table></td>
        </tr>
      </table>
      <br /> <label for="tags">Associated Group: </label> <select
        name="selection" id="combobox" onchange="userPermissionsForm.submit()"><option value=""
          disabled="disabled">Associate user with a Channel
          Group</option>
        <c:forEach var="grp" items="${AllGroups}">
          <c:choose>
            <c:when test="${UsersGroup eq grp.id}">
              <option value="${grp.id}" selected="selected">${grp.title}</option>
            </c:when>
            <c:otherwise>
              <option value="${grp.id}">${grp.title}</option>
            </c:otherwise>
          </c:choose>
        </c:forEach>
      </select> <br /> <br />
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

<!--       <table class="permissionsBlock" margin-right: 25px;"> -->
<!--         <tr> -->
<!--           <td colspan="2" class="blockHeader">Subscribed Channels</td> -->
<!--         </tr> -->
<%--         <c:forEach var="chan" items="${channels}"> --%>
<!--           <tr> -->
<%--             <td class="permCheck"><form:checkbox value="${chan.id}" --%>
<%--                 path="channelIds" id="checkbox${chan.title}" />&nbsp;</td> --%>
<%--             <td class="permName">${chan.title}</td> --%>
<!--           </tr> -->
<%--         </c:forEach> --%>
<!--       </table> -->

<!--       <table> -->
<!--         <tr> -->
<!--           <td colspan="2" class="blockHeader">Important Channels</td> -->
<!--         </tr> -->
<%--         <c:forEach var="chan" items="${channels}"> --%>
<!--           <tr> -->
<%--             <td class="permCheck"><form:checkbox value="${chan.id}" --%>
<%--                 path="importantChannelIds" id="checkbox${chan.title}" />&nbsp;</td> --%>
<%--             <td class="permName">${chan.title}</td> --%>
<!--           </tr> -->
<%--         </c:forEach> --%>
<!--       </table> -->
      </form:form>
      <br />
      <table id="userPermissionsButtons" >
        <tr>
          <td><input type="button" class="btn" value="Submit"
            onClick="$('#userPermissionsForm').submit();" /></td>
          <td><form:form action="userGrid">
              <input type="submit" class="btn" value="Cancel" />
            </form:form></td>
        </tr>
      </table>
    </div>
  </div>

  <script type="text/javascript">
			function clearPermissions() {
				<c:forEach var="perm" items="${permissions}">
				$("#checkbox${perm.permissionName}").attr('checked', false);
				</c:forEach>
			}

			function basicUser() {
				clearPermissions();
				<c:forEach var="perm" items="${basicUserPermissions}">
				$("#checkbox${perm.permissionName}").attr('checked', true);
				</c:forEach>
			}

			function contentProvider() {
				clearPermissions();
				<c:forEach var="perm" items="${contentProviderPermissions}">
				$("#checkbox${perm.permissionName}").attr('checked', true);
				</c:forEach>
			}

			function contentGatekeeper() {
				clearPermissions();
				<c:forEach var="perm" items="${contentGatekeeperPermissions}">
				$("#checkbox${perm.permissionName}").attr('checked', true);
				</c:forEach>
			}

			function channelAdministrator() {
				clearPermissions();
				<c:forEach var="perm" items="${channelAdministratorPermissions}">
				$("#checkbox${perm.permissionName}").attr('checked', true);
				</c:forEach>
			}

			function superAdministrator() {
				clearPermissions();
				<c:forEach var="perm" items="${superAdministratorPermissions}">
				$("#checkbox${perm.permissionName}").attr('checked', true);
				</c:forEach>
			}

			function apsUser() {
				clearPermissions();
				<c:forEach var="perm" items="${apsUserPermissions}">
				$("#checkbox${perm.permissionName}").attr('checked', true);
				</c:forEach>
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
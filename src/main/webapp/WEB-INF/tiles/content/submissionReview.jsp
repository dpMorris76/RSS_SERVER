<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<sec:authorize
  access="hasAnyRole('Content_Submission', 'Content_Review')">
  <div>
    <div id="viewtitle">
      <span id="titleheader">Content Submission Form</span>
      <!-- Instructions popup -->
      <script
        src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
      <link rel="stylesheet"
        href="css/jquery-ui-1.11.4.custom/jquery-ui.css" type="text/css">
      <script type="text/javascript">
							$(function() {
								$("#helpButton").click(function(event) {
									//Initial settings for dialogue
									$("#denialPopup").dialog({
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
        <div class="itemSummary">
          <span class="fieldVal">Posted To: </span> ${channels}<br />
          <span class="fieldVal">Title:</span> ${story.title}<br />
          <c:forEach items="${story.links}" var="element">
		    <div>
		      <div class="fieldVal">Link: <a href="${element.link}"
		        target="_blank">${element.linkName}</a>
		      </div>
		    </div>
		  </c:forEach>
          <span class="fieldVal">Description:</span> ${story.description}<br />
          <span class="fieldVal">Author:</span> ${story.author}<br /> 
          <span class="fieldVal">Point of Contact:</span> ${story.pocName}<br /> 
          <span class="fieldVal">Point of Contact Phone Number:</span> ${story.pocPhNbr}<br />
          <span class="fieldVal">Approved By:</span> ${story.approvedBy}<br /> 
          <span class="fieldVal">Publish Date:</span> ${story.publishDate}<br />
          <span class="fieldVal">Expiration Date:</span>
          ${story.expirationDate}<br /> <span class="fieldVal">High
            Priority:</span>
          <c:choose>
            <c:when test="${story.isHighPriority}">Yes</c:when>
            <c:otherwise>No</c:otherwise>
          </c:choose>
          <br />
        </div>
        <br />
        <div id="reviewButtons">
          <table>
            <tr>
              <c:if test="${canReview}">
                <c:if
                  test="${story.getApprovalStatus().equals(story.APPROVED_STATUS())}">
                  <td><input type="submit" style="background: #FF0000;" class="btn"
                    value="Expire" id="deleteButton" /></td>
                </c:if>
              </c:if>
              <form:form action="editSubmission"
                modelAttribute="storyIdForm">
                <form:hidden path="id" />
                <form:hidden path="denialReasons" />
                <c:if test="${canReview}">
                  <c:if
                    test="${story.getApprovalStatus().equals(story.APPROVED_STATUS())}">
                    <td><input type="submit" class="btn"
                      value="Update Submission" /></td>
                  </c:if>
                </c:if>
              </form:form>
              <form:form action="approveSubmission"
                modelAttribute="storyIdForm">
                <form:hidden path="id" />
                <form:hidden path="denialReasons" />
                <c:if test="${canReview}">
                  <c:if
                    test="${!story.getApprovalStatus().equals(story.APPROVED_STATUS())}">
                    <td><input type="submit" class="btn"
                      value="Approve" /></td>
                  </c:if>
                  <c:if
                    test="${story.getApprovalStatus().equals(story.PENDING_STATUS()) || ((story.getPublishDate() > today) && story.getApprovalStatus().equals(story.APPROVED_STATUS()))  }">
                    <td><input type="button" class="btn"
                      value="Deny" id="denialButton" /></td>
                  </c:if>
                </c:if>
              </form:form>
              <c:if test="${canEdit or canReview}">
                <c:if
                  test="${!story.getApprovalStatus().equals(story.APPROVED_STATUS()) || story.getPublishDate() > today}">
                  <td><form:form action="editSubmission"
                      modelAttribute="storyIdForm">
                      <form:hidden path="id" />
                      <form:hidden path="denialReasons" />
                      <input type="submit" class="btn"
                        value="Edit Submission" />
                  </form:form></td>
                </c:if>
              </c:if>
            </tr>
          </table>
        </div>
      </div>
    </div>
    <div id="denialPopup" style="display: none;"
      title="Deny This Publication?" align="center" tabindex="-1">
      <form:form action="denySubmission" modelAttribute="storyIdForm"
        id="denySubmission">
        <form:hidden path="id" />
			Reason:
			<form:textarea path="denialReasons" id="denialReasonsFormInput" />
        <br />Max characters: 100
		</form:form>
    </div>
    <div id="deletePopup" style="display: none;"
      title="Expire This Content?" align="center" tabindex="-1">
      <form:form action="deleteSubmission" modelAttribute="storyIdForm"
        id="deleteSubmission">
        <form:hidden path="id" />
			Reason:
			<form:textarea path="denialReasons" id="deleteReasonsFormInput" />
        <br />Max characters: 100
		</form:form>
    </div>

    <script
      src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>

    <link rel="stylesheet"
      href="css/jquery-ui-1.11.4.custom/jquery-ui.css" type="text/css">

    <script type="text/javascript">
					$(function() {
						$("#denialButton")
								.click(
										function(event) {
											//Initial settings for dialogue
											$("#denialPopup")
													.dialog(
															{
																autoOpen : false,
																modal : true,
																buttons : {
																	'Deny' : function() {
																		if ($(
																				"#denialReasonsFormInput")
																				.val() != null
																				&& $(
																						"#denialReasonsFormInput")
																						.val() != "") {
																			$(
																					"#denySubmission")
																					.submit();
																		} else {
																			alert("Please enter a reason for denying this publication.");
																		}
																	},
																	'Cancel' : function() {
																		$(this)
																				.dialog(
																						"close");
																	}
																}

															});

											//Open the dialogue
											$("#denialPopup").dialog("open");
											event.preventDefault();
										});
					});
					$(function() {
						$("#deleteButton")
								.click(
										function(event) {
											//Initial settings for dialogue
											$("#deletePopup")
													.dialog(
															{
																autoOpen : false,
																modal : true,
																buttons : {
																	'Expire' : function() {
																		if ($(
																				"#deleteReasonsFormInput")
																				.val() != null
																				&& $(
																						"#deleteReasonsFormInput")
																						.val() != "") {
																			$(
																					"#deleteSubmission")
																					.submit();
																		} else {
																			alert("Please enter a reason for deleting this submission.");
																		}
																	},
																	'Cancel' : function() {
																		$(this)
																				.dialog(
																						"close");
																	}
																}

															});

											//Open the dialogue
											$("#deletePopup").dialog("open");
											event.preventDefault();
										});
					});
				</script>
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

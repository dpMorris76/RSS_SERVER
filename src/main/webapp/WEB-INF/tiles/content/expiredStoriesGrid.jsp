<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<sec:authorize	access="hasAnyRole('Content_Submission')">
<link href="css/datatablesCustom.css" rel="stylesheet" type="text/css">
<script>
	$(document).ready(function() {
	    $('#example').dataTable( {
	     	"height": 400,
	        "scrollX": false,
	        "paging": true,
	        "pageLength": 13,
	        "deferRender": true,
	        "order": [[ 0, "asc" ]],
	        "lengthMenu": [[25, 50, 100, -1], [25, 50, 100, "All"]],
	        "dom": '<"toolbar">frtip',
	        "fnInitComplete": function() { 
	        	$(".showmefirst").addClass('removeme');
		        if ( $(this).hasClass('hideme') ) {
		            $(this).removeClass('hideme');
		        } 
		        $("table").removeClass('hideme');
	        },
	    });
	                           

	    var table = $('#example').dataTable();
		$.datepicker.regional[""].dateFormat = 'yy-mm-dd';
   		$.datepicker.setDefaults($.datepicker.regional['']);
   		$('#fromDate').datepicker();
   		$('#toDate').datepicker();
   		$("#ui-datepicker-div").css("display", "none");
		

	    $('#example tbody').on( 'click', 'tr', function () {
	        if ( $(this).hasClass('selected') ) {
	            $(this).removeClass('selected');
	        }
	        else {
	            table.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	        }
	    } );
	} );
	var win = null;
	function openExpiredStory(id){
		win = window.open("expiredStory/" + id, "Story_Details", "width= 600, height= 600, scrollbars=1, resizable=1" );
		// bring the other window to the top, I wish that I could figure out how to reload as opposed to opening a new thing..
	    win.focus();
	}
</script>

<div>
	<div id="viewtitle">
		<span id="titleheader">Expired Stories <input type="button" class="btn help" value="?" id="helpButton" /></span>
		<!-- Instructions popup -->
		<div id="instructionPopup" style="display: none;"
				title="Instructions" align="center" tabindex="-1">
				<div id="instructions">
					<br /> Select the 'Search' button
					after entering any needed criteria. This will bring you to
					a new page with the list of the expired stories displayed.</div>
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
	
	<br />
	<br />
	<form:form method="POST" action="updateExpiredGrid" modelAttribute="expiredGridForm">
        <div>
        <div style="display: inline-block;" class="expiredBoxes">
            From: <form:input id="fromDate" path="from" />
          </div>
        <div style="display: inline-block;" class="expiredBoxes">
            To: <form:input id="toDate" path="to" />
          </div>
          <div style="display: inline-block;" class="expiredBoxes">
            Channel: <form:input id="channel" path="channel" />
          </div>
          <div style="display: inline-block;" class="expiredBoxes">
            Title: <form:input id="title" path="title"/> 
          </div>
          <div style="display: inline-block;" class="expiredBoxes">
          	Author: <form:input id="author" path="author" />
          </div>
          <div style="display: inline-block;" class="expiredBoxes">
          	Approved By: <form:input id="approvedBy" path="approvedBy" />
          </div>
           <div style="display: inline-block;" class="expiredBoxes">
          		<input type="submit" value="Search">
          </div>
          </div>
    </form:form>
<!-- 	clear button -->

		<p class="showmefirst">Populating grid...</p>
		<table id="example" class="display compact hideme" cellspacing="0" width="100%">
		
			<thead>
				<tr>
					<th>Channel</th>
					<th>Title</th>
					<th>Approved By</th>
					<th>High Priority</th>
					<th>Publish Date</th>
					<th>Expiration Date</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<th>Channel</th>
					<th>Title</th>
					<th>Approved By</th>
					<th>High Priority</th>
					<th>Publish Date</th>
					<th>Expiration Date</th>
				</tr>
			</tfoot>
			<tbody>
				<!-- Looped rows go here -->
				<c:forEach var="expiredStory" items="${expiredSubmissions}">
					<tr>
						<td>${expiredStory.channelName}</td>
						<td>${expiredStory.title} <button onClick="$('#expiredStoryId').val(${expiredStory.id});$('#submissionReviewExpired').submit();">Review</button></td>
						<td>${expiredStory.approvedBy}</td>
						<td><c:choose><c:when test="${expiredStory.isHighPriority}">Yes</c:when><c:otherwise>No</c:otherwise></c:choose></td>
						<td>${expiredStory.publishDate}</td>
						<td>${expiredStory.expirationDate}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div style="display:none;">
		<form hidden="true" action="submissionReviewExpired" id="submissionReviewExpired">
			<input type="text" name="expiredStoryId" id="expiredStoryId" />
		</form>
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

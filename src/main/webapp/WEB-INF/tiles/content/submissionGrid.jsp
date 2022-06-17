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
	        "columnDefs": [
	                       {
	                           // The `data` parameter refers to the data for the cell (defined by the
	                           // `data` option, which defaults to the column being worked with, in
	                           // this case `data: 0`.
	                           "render": function ( data, type, row ) {
	                               return data +''+ row[4]+'';
	                           },
	                           "targets": 2
	                       },
	                       { "visible": false,  "targets": [ 4 ] }
	                   ],
	       	"fnDrawCallback":function() { $(".sideLink").change(function() {
	       				var url = 'AjaxGtroEdits/' + $(this).val();
	       				$.ajax({
	       					url : url,
	       					cache : false,
	       					dataType : "html",
	       					success : function(data) {
	       						$("#accountEditShell").html(data);
	       					}
	       				});
	       			});}
	    } );
	                           

	    $("div.toolbar").html('<form:form method="post" action="updateGrid" modelAttribute="storyGridForm"><label for="tags">Submissions Displayed: </label>' + 
	    		'<select name="selection" id="combobox"><option value="" disabled="disabled">Select a submission set</option><option value="------" >------</option>' +
	    		'<option value="allSubmissions">All Content Submissions</option>' +
	    		'<option value="approvedSubmissions">Approved Submissions</option>' +
	    		'<option value="pendingSubmissions">Pending Submissions</option>' +
	    		'<option value="rejectedSubmissions">Rejected Submissions</option>' +
	    		'</select>' + '<input class="fixedwidthbtn" type="submit" name="submit" value="Submit"></form:form>');
	    
	    var table = $('#example').dataTable();
		$.datepicker.regional[""].dateFormat = 'yy-mm-dd';
   		$.datepicker.setDefaults($.datepicker.regional['']);
   		
   		
		
		table.columnFilter({ 	
    	 sPlaceHolder: "head:before",
		 aoColumns: [ 	{type: "text"}, {type: "text"}, {type: "text"}, {type: "text"}, null,
		             	{ sSelector: "#dateRange", type: "date-range" }]

		});

	    $('#example tbody').on( 'click', 'tr', function () {
	        if ( $(this).hasClass('selected') ) {
	            $(this).removeClass('selected');
	        }
	        else {
	            table.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	        }
	    } );
	    $("#ui-datepicker-div").css({"display": "none"});
	} );
	    
	

</script>

<div>
	<div id="viewtitle">
		<span id="titleheader">Content Submissions <input type="button" class="btn help" value="?" id="helpButton" /></span>
		<!-- Instructions popup -->
		<div id="instructionPopup" style="display: none;"
				title="Instructions" align="center" tabindex="-1">
				<div id="instructions">
					Select a filter from the dropdown to view submissions by approval 
					status.  Select 'All' if you want to see all submissions.  
					<br /> <br />Select the 'Review Submission' button
					next to the story you would like to review. This will bring you to
					a new page with the full contents of the story displayed. Admins
					will be able to modify the approval status on this page.</div>
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
	<p>Publish Date Range</p>
	<p id="dateRange"></p>
	<br />
<!-- 	clear button -->

		<p class="showmefirst">Populating grid...</p>
		<table id="example" class="display compact hideme" cellspacing="0" width="100%">
		
			<thead>
			
				<tr>
					<th>Search Channel</th>
					<th>Search Author</th>
					<th>Search Title</th>
					<th>Search Approved By</th>
					<th>Edit</th>
					<th></th>
					<th></th>
				</tr>
				<tr>
					<th>Channel</th>
					<th>Author</th>
					<th>Title</th>
					<th>Approved By</th>
					<th>Edit</th>
					<th>High Priority</th>
					<th>Publish Date</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<th>Channel</th>
					<th>Author</th>
					<th>Title</th>
					<th>Approved By</th>
					<th>Edit</th>
					<th>High Priority</th>
					<th>Publish Date</th>
				</tr>
			</tfoot>
			<tbody>
				<!-- Looped rows go here -->
				<c:forEach var="story" items="${storySubmissions}">
					<tr>
						<td>
							<c:forEach var="channel" items="${story.channels}">
								${channel.title}
							</c:forEach>
						</td>
						<td>${story.author}</td>
						<td>${story.title}</td>
						<td>${story.approvedBy}</td>
						
						<c:set var="channels" scope="session" value="${story.channels}" />
						<td>
						<sec:authorize access="hasAnyRole('Content_Review', 'Content_Submission')">
							<c:choose>
								<c:when test="${fn:length(channels) eq 1}"> 
									<c:forEach var="channel" items="${channels}">
										&nbsp;<button type="button" onClick="$('#storyId').val(${story.id});$('#submissionReview').submit();">Review Submission</button>
<%--                                         <c:if test="${channel.id != 1}">&nbsp;<button type="button" onClick="$('#storyId').val(${story.id});$('#submissionReview').submit();">Review Submission</button></c:if> --%>
									</c:forEach>
								</c:when>
								<c:otherwise>
									&nbsp;<button type="button" onClick="$('#storyId').val(${story.id});$('#submissionReview').submit();">Review Submission</button>
								</c:otherwise>
							</c:choose>
						</sec:authorize>
						</td>
						<td><c:choose><c:when test="${story.isHighPriority}">Yes</c:when><c:otherwise>No</c:otherwise></c:choose></td>
						<td>${story.publishDate}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div style="display:none;">
		<form hidden="true" action="submissionReview" id="submissionReview">
			<input type="text" name="storyId" id="storyId" />
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

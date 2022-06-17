<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link href="css/datatablesCustom.css" rel="stylesheet" type="text/css">
<script>
	$(document).ready(function() {
	    $('#example').dataTable( {
	    	"scrollY": 400,
	        "scrollX": false,
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
	                           "targets": 0
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

	    var table = $('#example').DataTable();

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

</script>
<div>
	<div id="viewtitle">
		<span id="titleheader">Channel Groups  <input type="button" class="btn help" value="?" id="helpButton" /></span>
		<!-- Instructions popup -->
		<div id="instructionPopup" style="display: none;"
				title="Instructions" align="center" tabindex="-1">
				<div id="instructions">Select the 'Edit Group' button next to the
				group you would like to review. This will bring you to a new page with
				the full contents of the channel group displayed.</div>
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
		<p class="showmefirst">Populating grid...</p>
		<table id="example" class="display compact hideme" cellspacing="0"
			width="100%">
			<thead>
				<tr>
					<th>Title</th>
					<th>Channels</th>
					<th>Content Providers</th>
					<th>Content Reviewers</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<th>Title</th>
					<th>Channels</th>
					<th>Content Providers</th>
					<th>Content Reviewers</th>
				</tr>
			</tfoot>
			<tbody>
				<!-- Looped rows go here -->
				<c:forEach var="group" items="${groups}">
					<tr>
						<td>${group.title} (id:${group.id})</td>
						<td>
							<ul>
							<c:forEach var="channel" items="${group.channels}">
								<li>${channel.title}</li>
							</c:forEach>
							</ul>
						</td>
						<td>
							<ul>
							<c:forEach var="user" items="${group.contentProviders}">
								<li>${user.fullName}</li>
							</c:forEach>
							</ul>
						</td>
						<td>
							<ul>
							<c:forEach var="user" items="${group.contentGatekeepers}">
								<li>${user.fullName}</li>
							</c:forEach>
							</ul>
						</td>
						<td><sec:authorize access="hasRole('Channel_Group_Administration')">&nbsp;<button type="button" onClick="$('#groupId').val(${group.id});$('#editGroup').submit();">Edit Group</button></sec:authorize></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div style="display:none;">
		<form hidden="true" action="editGroup" id="editGroup">
			<input type="text" name="groupId" id="groupId" />
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

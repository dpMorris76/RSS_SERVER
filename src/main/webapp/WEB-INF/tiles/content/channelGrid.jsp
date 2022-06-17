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
	                               return data +''+ row[5]+'';
	                           },
	                           "targets": 0
	                       },
	                       { "visible": false,  "targets": [ 5 ] }
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
//   		$.datepicker.regional[""].dateFormat = 'yy-mm-dd';
// 	    $.datepicker.setDefaults($.datepicker.regional['']);
	    
// 	    table.columnFilter({ 	
// 	    	 sPlaceHolder: "head:before",
// 			 aoColumns: [ 	null, null,
// // 			             	{ sSelector: "#dateRange", type: "date-range" },
// // 			             	{ sSelector: "#dateRange", type: "date-range" },
// 							{type:"date-range"},
// 							{type:"date-range"},
// 			             	null]

// 			});
	    
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
		<span id="titleheader">Channels <input type="button" class="btn help" value="?" id="helpButton" /></span>
		<!-- Instructions popup -->
		<div id="instructionPopup" style="display: none;" title="Instructions" align="center" tabindex="-1">
			<div id="instructions">Select the 'Review Channel' button next
				to the channel you would like to review. This will bring you to a
				new page with the full contents of the channel displayed.
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
<!-- 	<p align="center">Date Range</p> -->
<!-- 	<p align="center" id="dateRange"></p> -->
		<p class="showmefirst">Populating grid...</p>
		<table id="example" class="display compact hideme" cellspacing="0"
			width="100%">
			<thead>
				<tr>
					<th>Title</th>
<!-- 					<th>Link</th> -->
					<th>Description</th>
					<th>Publish Date</th>
					<th>Last Build Date</th>
					<th>Managing Editor</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<th>Title</th>
<!-- 					<th>Link</th> -->
					<th>Description</th>
					<th>Publish Date</th>
					<th>Last Build Date</th>
					<th>Managing Editor</th>
				</tr>
			</tfoot>
			<tbody>
				<!-- Looped rows go here -->
				<c:forEach var="chan" items="${channels}">
					<tr>
						<td>${chan.title} (id:${chan.id})</td>
<%-- 						<td>${chan.link}</td> --%>
						<td>${chan.description}</td>
						<td>${chan.publishDate}</td>
						<td>${chan.lastBuildDate}</td>
						<td>${chan.managingEditor}</td>
						<td><sec:authorize access="hasAnyRole('Channel_Administration', 'Channel_Group_Administration')">&nbsp;<button type="button" onClick="$('#chanId').val(${chan.id});$('#editChannel').submit();">Review Channel</button></sec:authorize></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div style="display:none;">
		<form hidden="true" action="editChannel" id="editChannel">
			<input type="text" name="chanId" id="chanId" />
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

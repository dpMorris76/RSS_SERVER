<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="hasRole('User_Administration')">
  <link href="css/datatablesCustom.css" rel="stylesheet" type="text/css">
  <script>
	$(document).ready(function() {
	    $('#example').dataTable( {
	        "scrollY": 400,
	        "scrollX": false,
	        "deferRender": true,
	        "order": [[ 1, "asc" ]],
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
	                               return data +' '+ row[7]+'';
	                           },
	                           "targets": 0
	                       },
	                       { "visible": false,  "targets": [ 7 ] }
	                   ],
	       	"fnDrawCallback":function() { $(".sideLink").change(function() {
   				var url = 'AjaxAdminEdits/' + $(this).val();
   				$.ajax({
   					url : url,
   					cache : false,
   					dataType : "html",
   					success : function(data) {
   						$("#adminEditShell").html(data);
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
  <div id="welcomecontainer">
    <div id="viewtitle">
      <span id="titleheader">Single Edit <input type="button"
        class="btn help" value="?" id="helpButton" /></span> <br />
      <br />
      <div id="instructionPopup" style="display: none;"
        title="Instructions" align="center" tabindex="-1">
        <div id="instructions">
          Select the 'View/Edit' button next to the user you would like
          to edit. This will bring you to a new page with individual
          user information displayed. Admins will be able to modify the
          user's permissions on this page.<br /> <br />If you
          would like to search for a new user to add from LDAP, select 
          'From LDAP' on the dropdown next to the submit button.
        </div>
      </div>
      <script
        src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
      <link rel="stylesheet"
        href="css/jquery-ui-1.11.4.custom/jquery-ui.css" type="text/css">
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
      <form:form method="post" action="updateAdminGrid"
        modelAttribute="adminGridForm">
        <div>
          <div style="display: inline-block;">
            Last name: <form:input path="lname" id="lastNameInput" />
          </div>
          <div style="display: inline-block;">
            First name: <form:input path="fname" id="firstNameInput" /> 
          </div>
          <div style="display: inline-block;">
          <p style="font-weight:bold" > OR </p>
          </div>
          <div style="display: inline-block;">
            ADID: <form:input path="Adid" id="ADIDInput" />
          </div>
          <div style="display: inline-block;">
            <form:select path="ldap" id="ldapSelector">
              <form:option value="false">In RSS</form:option>
              <form:option value="true">From LDAP</form:option>
            </form:select>
          </div>
          <div style="display: inline-block;">
          <input type="submit" value="Submit">
          </div>
        </div>
      </form:form>
      <p class="showmefirst">Populating grid...</p>
      <table id="example" class="display compact hideme" cellspacing="0"
        width="100%">
        <thead>
          <tr>
            <th>Username</th>
            <!-- <th>Permission(s)</th>  -->
            <th>Last name</th>
            <th>First name</th>
            <th>Middle initial</th>
            <th>State</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Edit</th>
          </tr>
        </thead>
        <tfoot>
          <tr>
            <th>Username</th>
            <!--  <th>Permission(s)</th> -->
            <th>Last name</th>
            <th>First name</th>
            <th>Middle initial</th>
            <th>State</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Edit</th>
          </tr>
        </tfoot>
        <tbody>

          <c:forEach var="user" items="${gridUserList}">
            <c:if test="${user.username != null}">
              <tr id="${user.username}">
                <td>${user.username}</td>
                <!--  <td>${user.userPermissionDesc}</td>  -->
                <td>${user.lname}</td>
                <td>${user.fname}</td>
                <td>${user.mi}</td>
                <td>${user.stCd}</td>
                <td>${user.email}</td>
                <td>${user.phnNbr}</td>
                <td><sec:authorize
                    access="hasRole('User_Administration')">&nbsp;
								<button type="button"
                      onClick="$('#userIdInput').val(${user.id});$('#usernameInput').val('${user.username}');$('#userEditForm').submit();">View/Edit</button>
                  </sec:authorize></td>
              </tr>
            </c:if>
          </c:forEach>
        </tbody>
      </table>
    </div>
    <div style="display: none;">
      <form hidden="true" action="userEdit" id="userEditForm">
        <input type="text" name="username" id="usernameInput" /> <input
          type="text" name="userId" id="userIdInput" />
      </form>
    </div>
  </div>
</sec:authorize>

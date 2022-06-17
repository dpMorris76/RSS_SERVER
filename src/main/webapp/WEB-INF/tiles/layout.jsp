<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
  <meta http-equiv="X-UA-Compatible" content="IE=9,IE=8,chrome=1" />
<!-- load jquery from cdn to get cacheing benefits, fall back to local if necessary. -->
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript">
	if (typeof jQuery == 'undefined') {
		document
				.write(unescape("%3Cscript src='/js/jquery-1.7.2.min.js' type='text/javascript'%3E%3C/script%3E"));
	}
</script>
<script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.13.1/jquery.validate.min.js"></script>
<script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.13.1/additional-methods.min.js"></script>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible" />

<title><tiles:insertAttribute name="title" /></title>

<link href="css/jquery-ui-1.7.2.custom.css" rel="stylesheet"
	type="text/css" />
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/siteStyle.css" rel="stylesheet" type="text/css">
<link href="css/contentStyle.css" rel="stylesheet" type="text/css">

<link href="//cdn.datatables.net/1.10.4/css/jquery.dataTables.css" rel="stylesheet" type="text/css">
<link href="css/jquery-ui-timepicker-addon.css" rel="stylesheet"
	type="text/css" />
<script>
  $(function() {
    $( ".datepicker" ).datetimepicker();
  });
  </script>

</head>

<body>
	<div id="everything">
	<div>
		<div id="userInfoTitle"> Welcome ${currentUser.fname} ${currentUser.lname}</div>
    </div>
		<div id="site">
			<tiles:insertAttribute name="site" />
		</div>
		<div id="content">
			<tiles:insertAttribute name="content" />
		</div>
		<div id="footer">
			<tiles:insertAttribute name="footer" />
		</div>
	</div>
	<!-- end everything -->

	<!--  common scripts -->
	<script type="text/javascript" language="JavaScript" src="js/utils.js"></script>
	
	<script type="text/javascript" src="js/jquery.dataTables.columnFilter.js"></script>
	<script type="text/javascript" src="js/jquery.bgiframe.min.js"></script>
	<script language="javascript" src="js/jquery-ui-1.7.2.custom.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui-timepicker-addon.js" ></script>
	<script type="text/javascript" src="js/jquery-ui.js"></script>
	<script type="text/javascript" src="js/jquery-ui.min.js"></script>
</body>

</html>
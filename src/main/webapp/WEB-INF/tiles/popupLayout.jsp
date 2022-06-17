<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible" />

<title><tiles:insertAttribute name="title" /></title>

<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/jquery-ui-1.7.1.custom.css" rel="stylesheet"
	type="text/css" />
</head>

<body>
	<!-- load jquery from web to get cacheing benefits, fall back to local if necessary. -->
	<script type="text/javascript"
		src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	<script type="text/javascript">
		if (typeof jQuery == 'undefined') {
			document
					.write(unescape("%3Cscript src='/js/jquery-1.7.2.min.js' type='text/javascript'%3E%3C/script%3E"));
		}
	</script>
	<div id="everything">
		<tiles:insertAttribute name="header" />
		<tiles:insertAttribute name="body" />
		<tiles:insertAttribute name="footer" />
	</div>
	<!-- end everything -->
	
	
	<!--  common scripts -->
	<script type="text/javascript" language="JavaScript" src="js/utils.js"></script>
	
	<script type="text/javascript" src="js/jquery.bgiframe.min.js"></script>
	<script language="javascript" src="js/jquery-ui-1.7.2.custom.min.js"></script>
</body>

</html>
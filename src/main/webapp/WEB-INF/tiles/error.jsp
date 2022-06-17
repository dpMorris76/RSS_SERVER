<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="welcomecontainer">

	<div id="welcometitle">
		<h3 id="titleheader">An error occurred in the application: ${errorMessage}</h3>
		<h5 id="errorInstructions">If problem persists, please contact an administrator for assisstance.</h5>
		<a href='<c:out value="${welcomeURL}"/>'/>Try Again</a>
	</div>

</div>

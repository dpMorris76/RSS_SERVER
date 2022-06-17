<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<div>
  <div id="viewtitle">
    <h3 id="titleheader">Tools</h3>
    <p>
      <a
        href="http://library.corp.intranet/sites/default/files/article/3622/Technician%20Call%20Guide%20(All%20Markets).pdf"
        target="_blank">Tech Call Guide</a>
    </p>
    <p>
      <a href="http://toolbox.corp.intranet/techMart/" target="_blank">TechMart</a>
    </p>
    <p>
      <a href="http://library/" target="_blank">Library</a>
    </p>
    <p>
      <a href="http://techpoint/" target="_blank">Tech Point</a>
    </p>
    <sec:authorize
      access="hasAnyRole('Content_Submission', 'Content_Review', 'Channel_Administration', 'Channel_Group_Administration', 'User_Administration')">

      <p>
        <a href="tracking">Tracking Report</a>
      </p>
    </sec:authorize>
  </div>
</div>
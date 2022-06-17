<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<div id="appText">RSS Feeder</div>
<br />
<!-- Home -->
<input id="navHome" class="homebtn" type="button" value="Home"
  onclick="$(homeButton())" />
<br />
<br />
<!-- Subscriptions -->
<input id="navFeeds" class="homebtn" type="button" value="Subscriptions"
  onclick="$(feedsButton())" />
<br />
<br />

<!-- Content -->
<sec:authorize
  access="hasAnyRole('Content_Submission','Content_Review')">
  <input id="navContent" class="homebtn" type="button" value="Content"
    onclick="$(contentButton())" />
  <br />
  <sec:authorize access="hasRole('Content_Submission')">
    <input id="navContentForm" class="subhomebtn" type="button"
      value="Add Content" onclick="$(contentFormButton())" />
    <br />
  </sec:authorize>
  <input id="navContentGrid" class="subhomebtn" type="button"
    value="Review" onclick="$(contentGridButton())" />
  <br />
  <input id="navExpiredGrid" class="subhomebtn" type="button"
    value="Expired" onclick="$(contentExpiredButton())" />
   <br/>
  <br />
</sec:authorize>

<!-- Channels -->
<sec:authorize
  access="hasAnyRole('Channel_Administration', 'Channel_Group_Administration')">
  <input id="navChannel" class="homebtn" type="button" value="Channels"
    onclick="$(channelButton())" />
  <br />
  <input id="navChannelForm" class="subhomebtn" type="button"
    value="Add Channel" onclick="$(channelFormButton())" />
  <br />
  <input id="navChannelReview" class="subhomebtn" type="button"
    value="Review" onclick="$(channelGridButton())" />
  <br />
  <br />
</sec:authorize>

<!-- Groups -->
<sec:authorize access="hasRole('Channel_Group_Administration')">
  <input id="navGroup" class="homebtn" type="button" value="Groups"
    onclick="$(groupButton())" />
  <br />
  <input id="navGroupForm" class="subhomebtn" type="button"
    value="Add Group" onclick="$(groupFormButton())" />
  <br />
  <input id="navGroupReview" class="subhomebtn" type="button"
    value="Review" onclick="$(groupGridButton())" />
  <br />
  <br />
</sec:authorize>

<!-- Users -->
<sec:authorize access="hasRole('User_Administration')">
  <input id="navUser" class="homebtn" type="button" value="Users"
    onclick="$(userHomeButton())" />
  <br />
  <input id="navSingleEdit" class="subhomebtn" type="button"
    value="Single Edit" onclick="$(singleEditButton())" />
  <br />
  <input id="navBulkEdit" class="subhomebtn" type="button"
    value="Bulk Edit" onclick="$(bulkEditButton())" />
  <sec:authorize access="hasRole('Subscription')">
    <sec:authorize access="hasRole('Content_Submission')">
      <sec:authorize access="hasRole('Content_Review')">
        <sec:authorize access="hasRole('Channel_Administration')">
          <sec:authorize
            access="hasRole('Channel_Group_Administration')">
            <input id="navAutoSelectChannels" class="subhomebtn"
              type="button" value="Auto-Selected Channels"
              onclick="$(autoSelectChannelsButton())" />
          </sec:authorize>
        </sec:authorize>
      </sec:authorize>
    </sec:authorize>
  </sec:authorize>
  <br />
  <br />
</sec:authorize>

<!-- Tools -->
<input id="navTools" class="homebtn" type="button" value="Tools"
  onclick="$(toolsButton())" />
<br />
<input id="navToolsEns" class="subhomebtn" type="button"
  value="Tech Call Guide" onclick="$(toolsTechGuideButton())" />
<br />
<!-- <input id="navToolsCris" class="subhomebtn" type="button" -->
<!--   value="CRIS Call Guide" onclick="$(toolsCrisButton())" /> -->
<!-- <br /> -->
<!-- <input id="navToolsBusiness" class="subhomebtn" type="button" -->
<!--   value="BUS Call Guide" onclick="$(toolsBusinessButton())" /> -->
<!-- <br /> -->
<input id="navToolsTechMart" class="subhomebtn" type="button"
  value="techMart" onclick="$(toolsTechMartButton())" />
<br />
<input id="navToolsLibrary" class="subhomebtn" type="button"
  value="Library" onclick="$(toolsLibraryButton())" />
<br />
<input id="navToolsTechPoint" class="subhomebtn" type="button"
  value="Techpoint" onclick="$(toolsTechPointButton())" />
<br />
<sec:authorize
  access="hasAnyRole('Content_Submission', 'Content_Review', 'Channel_Administration', 'Channel_Group_Administration', 'User_Administration')">
  <input id="export" class="subhomebtn" type="button"
    value="Tracking Report" onclick="$(toolsTrackingButton())" />
</sec:authorize>
<br />
<br />

<script type="text/javascript">
	function homeButton() {
		window.location.replace("welcome");
	}
	function feedsButton() {
		window.location.replace("feeds");
	}
	function contentButton() {
		window.location.replace("contentHome");
	}
	function contentFormButton() {
		window.location.replace("submissionForm");
	}
	function contentGridButton() {
		window.location.replace("submissionGrid");
	}
	function contentExpiredButton() {
		window.location.replace("expiredStoriesGrid");
	}
	function channelButton() {
		window.location.replace("channelHome");
	}
	function channelFormButton() {
		window.location.replace("channelForm");
	}
	function channelGridButton() {
		window.location.replace("channelGrid");
	}
	function groupButton() {
		window.location.replace("groupHome");
	}
	function groupFormButton() {
		window.location.replace("groupForm");
	}
	function groupGridButton() {
		window.location.replace("groupGrid");
	}
	function userHomeButton() {
		window.location.replace("userHome");
	}
	function autoSelectChannelsButton() {
		window.location.replace("autoSelectChannels");
	}
	function singleEditButton() {
		window.location.replace("userGrid");
	}
	function bulkEditButton() {
		window.location.replace("bulkEdit");
	}
	function toolsButton() {
		window.location.replace("tools");
	}
	function toolsTechGuideButton() {
		var win = window.open("http://library.corp.intranet/sites/default/files/article/3622/Technician%20Call%20Guide%20(All%20Markets).pdf", '_blank');
		win.focus();
	}
// 	function toolsCrisButton() {
// 		var win = window.open("http://toolbox.corp.intranet/nsl_handbook/sites/default/files/article/2154/Technician-Call-Guide-CRIS-Markets.pdf", '_blank');
// 		win.focus();
// 	}
// 	function toolsBusinessButton() {
// 		var win = window.open("https://doc-share.corp.intranet/livelink/llisapi.dll/36253219/Business-CPE_Technician_Contact_List.xlsx?func=doc.Fetch&nodeid=36253219", '_blank');
// 		win.focus();
// 	}
	function toolsTechMartButton() {
		var win = window.open("http://toolbox.corp.intranet/techMart/", '_blank');
		win.focus();
	}
	function toolsLibraryButton() {
		var win = window.open("http://library/", '_blank');
		win.focus();
	}
	function toolsTechPointButton() {
		var win = window.open("http://techpoint/", '_blank');
		win.focus();
	}
	function toolsTrackingButton() {
		window.location.replace("tracking");
	}
</script>

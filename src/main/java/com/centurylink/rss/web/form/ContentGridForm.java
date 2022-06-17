package com.centurylink.rss.web.form;

public class ContentGridForm {
	public static final String MY_SUBMISSIONS = "mySubmissions";
	public static final String PENDING_SUBMISSIONS = "pendingSubmissions";
	public static final String APPROVED_SUBMISSIONS = "approvedSubmissions";
	public static final String REJECTED_SUBMISSIONS = "rejectedSubmissions";
	public static final String ALL_SUBMISSIONS = "allSubmissions";
	public static final String NO_SUBMISSIONS = "------";
	private String selection;

	public String getSelection() {
		return selection;
	}

	public void setSelection(String submissionsToDisplay) {
		this.selection = submissionsToDisplay;
	}
	
}

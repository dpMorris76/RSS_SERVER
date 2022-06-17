package com.centurylink.rss.web.form;

public class FeedForm {
	public static final String ALL_GROUPS = "allGroups";
	private String selection;

	public String getSelection() {
		return selection;
	}

	public void setSelection(String groupsToDisplay) {
		this.selection = groupsToDisplay;
	}
	
}

package com.centurylink.rss.web.form;

public class AdminDropdownForm {
	public static final String ALL_USERS = "allUsers";
	private String selection;

	public String getSelection() {
		return selection;
	}

	public void setSelection(String groupsToDisplay) {
		this.selection = groupsToDisplay;
	}
	
}

package com.centurylink.rss.web.form;

public class GroupGridForm {
	public static final String MY_GROUPS = "myGroups";
	public static final String ALL_GROUPS = "allGroups";
	public static final String NO_GROUPS = "------";
	private String selection;

	public String getSelection() {
		return selection;
	}

	public void setSelection(String groupsToDisplay) {
		this.selection = groupsToDisplay;
	}
	
}

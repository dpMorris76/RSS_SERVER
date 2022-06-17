package com.centurylink.rss.web.form;

public class GroupReviewForm {
	private long id;
	private String denialReasons;
	
	public GroupReviewForm() {}
	
	public GroupReviewForm(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDenialReasons() {
		return denialReasons;
	}

	public void setDenialReasons(String denialReasons) {
		this.denialReasons = denialReasons;
	}
	
}

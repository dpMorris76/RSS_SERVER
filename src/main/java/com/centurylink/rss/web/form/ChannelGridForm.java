package com.centurylink.rss.web.form;

public class ChannelGridForm {
	public static final String MY_CHANNELS = "myChannels";
	public static final String ALL_CHANNELS = "allChannels";
	public static final String NO_CHANNELS = "------";
	private String selection;

	public String getSelection() {
		return selection;
	}

	public void setSelection(String channelsToDisplay) {
		this.selection = channelsToDisplay;
	}
	
}

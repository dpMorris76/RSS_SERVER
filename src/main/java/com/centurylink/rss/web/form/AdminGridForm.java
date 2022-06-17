package com.centurylink.rss.web.form;

public class AdminGridForm {
	private String fname;
	private String lname;
	private String Adid;
	private String ldap;
	public String getFname() {
		return fname;
	}
	public String getLname() {
		return lname;
	}
	public String getAdid() {
		return Adid;
	}
	public String getLdap() {
		return ldap;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public void setAdid(String Adid) {
		this.Adid = Adid;
	}
	public void setLdap(String ldap) {
		this.ldap = ldap;
	}
}

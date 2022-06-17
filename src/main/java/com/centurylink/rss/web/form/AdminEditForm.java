package com.centurylink.rss.web.form;

import java.util.List;

public class AdminEditForm {
	private Boolean isBasic;
	private Boolean isAdmin;
	List<String> permissionList;
	
	public Boolean getIsBasic() {
		return isBasic;
	}
	public void setIsBasic(Boolean isBasic) {
		this.isBasic = isBasic;
	}
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public List<String> getPermissionList() {
		return permissionList;
	}
	public void setPermissionList(List<String> permissionList) {
		this.permissionList = permissionList;
	}
}

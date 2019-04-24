package com.fab.model;

import com.fab.model.user.BaseBean;

public class UniqUserResponse extends BaseBean{

	private static final long serialVersionUID = 2939046402176356813L;

	private String userName;
	private String fullName;
	private String profilePID;
	private String isFriend;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getProfilePID() {
		return profilePID;
	}
	public void setProfilePID(String profilePID) {
		this.profilePID = profilePID;
	}
	public String getIsFriend() {
		return isFriend;
	}
	public void setIsFriend(String isFriend) {
		this.isFriend = isFriend;
	}
	
	
	
	
}

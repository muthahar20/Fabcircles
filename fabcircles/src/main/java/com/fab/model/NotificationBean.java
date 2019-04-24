package com.fab.model;

import com.fab.model.user.BaseBean;

public class NotificationBean extends BaseBean{

	private static final long serialVersionUID = -5041619979393821713L;

	private String userName;
	private String friend;
	private String friendFullName;
	private String friendProfilePID;
	private String priyarity;
	private String postMsgId;
	private String readStatus;
	private String postAdmin;
	private String admineFullName;
	private String admineProfilePID;
	
	
	
	public String getFriendFullName() {
		return friendFullName;
	}
	public void setFriendFullName(String friendFullName) {
		this.friendFullName = friendFullName;
	}
	public String getFriendProfilePID() {
		return friendProfilePID;
	}
	public void setFriendProfilePID(String friendProfilePID) {
		this.friendProfilePID = friendProfilePID;
	}
	public String getAdmineFullName() {
		return admineFullName;
	}
	public void setAdmineFullName(String admineFullName) {
		this.admineFullName = admineFullName;
	}
	public String getAdmineProfilePID() {
		return admineProfilePID;
	}
	public void setAdmineProfilePID(String admineProfilePID) {
		this.admineProfilePID = admineProfilePID;
	}
	public String getFriend() {
		return friend;
	}
	public void setFriend(String friend) {
		this.friend = friend;
	}
	public String getPostAdmin() {
		return postAdmin;
	}
	public void setPostAdmin(String postAdmin) {
		this.postAdmin = postAdmin;
	}
	public String getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPriyarity() {
		return priyarity;
	}
	public void setPriyarity(String priyarity) {
		this.priyarity = priyarity;
	}
	public String getPostMsgId() {
		return postMsgId;
	}
	public void setPostMsgId(String postMsgId) {
		this.postMsgId = postMsgId;
	}
	
	
}

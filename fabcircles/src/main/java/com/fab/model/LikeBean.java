package com.fab.model;

import com.fab.model.user.BaseBean;

public class LikeBean extends BaseBean{

	private static final long serialVersionUID = 5972218912707992311L;
	private String userName;	
	private String postMsgId;
	private String fullName;
	private String profilePID;
	private String isFriend;
	
	
	public String getIsFriend() {
		return isFriend;
	}
	public void setIsFriend(String isFriend) {
		this.isFriend = isFriend;
	}
	public String getProfilePID() {
		return profilePID;
	}
	public void setProfilePID(String profilePID) {
		this.profilePID = profilePID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPostMsgId() {
		return postMsgId;
	}
	public void setPostMsgId(String postMsgId) {
		this.postMsgId = postMsgId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result
				+ ((isFriend == null) ? 0 : isFriend.hashCode());
		result = prime * result
				+ ((postMsgId == null) ? 0 : postMsgId.hashCode());
		result = prime * result
				+ ((profilePID == null) ? 0 : profilePID.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LikeBean other = (LikeBean) obj;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (isFriend == null) {
			if (other.isFriend != null)
				return false;
		} else if (!isFriend.equals(other.isFriend))
			return false;
		if (postMsgId == null) {
			if (other.postMsgId != null)
				return false;
		} else if (!postMsgId.equals(other.postMsgId))
			return false;
		if (profilePID == null) {
			if (other.profilePID != null)
				return false;
		} else if (!profilePID.equals(other.profilePID))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}	
}

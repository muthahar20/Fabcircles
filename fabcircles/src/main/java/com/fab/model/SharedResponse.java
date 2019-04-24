package com.fab.model;

public class SharedResponse extends ShareBean{

	private static final long serialVersionUID = 3792359303261950978L;
	private String profilePID;
	private String fullName;
	private long shareCount;
	

	
	public long getShareCount() {
		return shareCount;
	}
	public void setShareCount(long shareCount) {
		this.shareCount = shareCount;
	}
	public String getProfilePID() {
		return profilePID;
	}
	public void setProfilePID(String profilePID) {
		this.profilePID = profilePID;
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
				+ ((profilePID == null) ? 0 : profilePID.hashCode());
		result = prime * result + (int) (shareCount ^ (shareCount >>> 32));
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
		SharedResponse other = (SharedResponse) obj;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (profilePID == null) {
			if (other.profilePID != null)
				return false;
		} else if (!profilePID.equals(other.profilePID))
			return false;
		if (shareCount != other.shareCount)
			return false;
		return true;
	}
	
	
	
}

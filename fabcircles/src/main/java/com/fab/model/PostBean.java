package com.fab.model;

import java.util.List;

import com.fab.model.user.BaseBean;

public class PostBean extends BaseBean {

	private static final long serialVersionUID = 5431763683207428652L;

	private String userName;

	private String fullName;

	private String message;

	private String postMsgId;
	
	private String reComments;
	private String reSharing;
	private String friendsAddPhotos;
	
	private boolean postToFriends;
	private boolean postToCircles;
	private List<String> toUsers;
	private List<String> toCircles;
	private List<String> photos;
	private String link;
	private String send;
	

	
	
	
	public String getReComments() {
		return reComments;
	}

	public void setReComments(String reComments) {
		this.reComments = reComments;
	}

	public String getReSharing() {
		return reSharing;
	}

	public void setReSharing(String reSharing) {
		this.reSharing = reSharing;
	}

	public String getFriendsAddPhotos() {
		return friendsAddPhotos;
	}

	public void setFriendsAddPhotos(String friendsAddPhotos) {
		this.friendsAddPhotos = friendsAddPhotos;
	}

	public String getSend() {
		return send;
	}

	public void setSend(String send) {
		this.send = send;
	}

	public boolean isPostToFriends() {
		return postToFriends;
	}

	public void setPostToFriends(boolean postToFriends) {
		this.postToFriends = postToFriends;
	}

	public boolean isPostToCircles() {
		return postToCircles;
	}

	public void setPostToCircles(boolean postToCircles) {
		this.postToCircles = postToCircles;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public List<String> getToUsers() {
		return toUsers;
	}

	public void setToUsers(List<String> toUsers) {
		this.toUsers = toUsers;
	}

	public List<String> getToCircles() {
		return toCircles;
	}

	public void setToCircles(List<String> toCircles) {
		this.toCircles = toCircles;
	}

	
	public List<String> getPhotos() {
		return photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPostMsgId() {
		return postMsgId;
	}

	public void setPostMsgId(String postMsgId) {
		this.postMsgId = postMsgId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((friendsAddPhotos == null) ? 0 : friendsAddPhotos.hashCode());
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((photos == null) ? 0 : photos.hashCode());
		result = prime * result
				+ ((postMsgId == null) ? 0 : postMsgId.hashCode());
		result = prime * result + (postToCircles ? 1231 : 1237);
		result = prime * result + (postToFriends ? 1231 : 1237);
		result = prime * result
				+ ((reComments == null) ? 0 : reComments.hashCode());
		result = prime * result
				+ ((reSharing == null) ? 0 : reSharing.hashCode());
		result = prime * result + ((send == null) ? 0 : send.hashCode());
		result = prime * result
				+ ((toCircles == null) ? 0 : toCircles.hashCode());
		result = prime * result + ((toUsers == null) ? 0 : toUsers.hashCode());
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
		PostBean other = (PostBean) obj;
		if (friendsAddPhotos == null) {
			if (other.friendsAddPhotos != null)
				return false;
		} else if (!friendsAddPhotos.equals(other.friendsAddPhotos))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (photos == null) {
			if (other.photos != null)
				return false;
		} else if (!photos.equals(other.photos))
			return false;
		if (postMsgId == null) {
			if (other.postMsgId != null)
				return false;
		} else if (!postMsgId.equals(other.postMsgId))
			return false;
		if (postToCircles != other.postToCircles)
			return false;
		if (postToFriends != other.postToFriends)
			return false;
		if (reComments == null) {
			if (other.reComments != null)
				return false;
		} else if (!reComments.equals(other.reComments))
			return false;
		if (reSharing == null) {
			if (other.reSharing != null)
				return false;
		} else if (!reSharing.equals(other.reSharing))
			return false;
		if (send == null) {
			if (other.send != null)
				return false;
		} else if (!send.equals(other.send))
			return false;
		if (toCircles == null) {
			if (other.toCircles != null)
				return false;
		} else if (!toCircles.equals(other.toCircles))
			return false;
		if (toUsers == null) {
			if (other.toUsers != null)
				return false;
		} else if (!toUsers.equals(other.toUsers))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}


	
	
}

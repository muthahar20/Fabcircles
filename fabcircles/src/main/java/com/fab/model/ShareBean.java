package com.fab.model;

import java.util.List;

import com.fab.model.user.BaseBean;

public class ShareBean extends BaseBean {

	private static final long serialVersionUID = -358655229043833010L;

	private String userName;
	private String postMsgId;
	private List<String> toUsers;
	private String shareTo;
	private String shareId;
	private Long share;
	private String sendBy;
	
	
	public String getSendBy() {
		return sendBy;
	}
	public void setSendBy(String sendBy) {
		this.sendBy = sendBy;
	}
	public Long getShare() {
		return share;
	}
	public void setShare(Long share) {
		this.share = share;
	}
	public String getShareTo() {
		return shareTo;
	}
	public void setShareTo(String shareTo) {
		this.shareTo = shareTo;
	}
	
	public String getShareId() {
		return shareId;
	}
	public void setShareId(String shareId) {
		this.shareId = shareId;
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
	public List<String> getToUsers() {
		return toUsers;
	}
	public void setToUsers(List<String> toUsers) {
		this.toUsers = toUsers;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((postMsgId == null) ? 0 : postMsgId.hashCode());
		result = prime * result + ((sendBy == null) ? 0 : sendBy.hashCode());
		result = prime * result + ((share == null) ? 0 : share.hashCode());
		result = prime * result + ((shareId == null) ? 0 : shareId.hashCode());
		result = prime * result + ((shareTo == null) ? 0 : shareTo.hashCode());
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
		ShareBean other = (ShareBean) obj;
		if (postMsgId == null) {
			if (other.postMsgId != null)
				return false;
		} else if (!postMsgId.equals(other.postMsgId))
			return false;
		if (sendBy == null) {
			if (other.sendBy != null)
				return false;
		} else if (!sendBy.equals(other.sendBy))
			return false;
		if (share == null) {
			if (other.share != null)
				return false;
		} else if (!share.equals(other.share))
			return false;
		if (shareId == null) {
			if (other.shareId != null)
				return false;
		} else if (!shareId.equals(other.shareId))
			return false;
		if (shareTo == null) {
			if (other.shareTo != null)
				return false;
		} else if (!shareTo.equals(other.shareTo))
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

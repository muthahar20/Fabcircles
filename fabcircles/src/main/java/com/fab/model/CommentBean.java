package com.fab.model;

import com.fab.model.user.BaseBean;

public class CommentBean extends BaseBean{

	private static final long serialVersionUID = 9134100710797009330L;
	
	private String comment;
	private String userName;	
	private String postMsgId;
	private String fullName;
	private String commentId;
	private boolean hide;
	private String shareId;
	private boolean shareStatus;
	
	
	public boolean isShareStatus() {
		return shareStatus;
	}
	public void setShareStatus(boolean shareStatus) {
		this.shareStatus = shareStatus;
	}
	public String getShareId() {
		return shareId;
	}
	public void setShareId(String shareId) {
		this.shareId = shareId;
	}
	public boolean isHide() {
		return hide;
	}
	public void setHide(boolean hide) {
		this.hide = hide;
	}
	public String getPostMsgId() {
		return postMsgId;
	}
	public void setPostMsgId(String postMsgId) {
		this.postMsgId = postMsgId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
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
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}	

}

package com.fab.model;

import com.fab.model.user.BaseBean;

public class PostSendToBean extends BaseBean{

	private static final long serialVersionUID = 2862146417639531541L;
	private String userName;
	private String sendBy;
	private String postMsgId;
	private boolean shareStatus;
	private String shareBy;
	private String unFollow;
	private boolean hide;
	private String shareId;
	
	
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
	public String getUnFollow() {
		return unFollow;
	}
	public void setUnFollow(String string) {
		this.unFollow = string;
	}
	public boolean isShareStatus() {
		return shareStatus;
	}
	public void setShareStatus(boolean shareStatus) {
		this.shareStatus = shareStatus;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSendBy() {
		return sendBy;
	}
	public void setSendBy(String sendBy) {
		this.sendBy = sendBy;
	}
	public String getPostMsgId() {
		return postMsgId;
	}
	public void setPostMsgId(String postMsgId) {
		this.postMsgId = postMsgId;
	}
	public String getShareBy() {
		return shareBy;
	}
	public void setShareBy(String shareBy) {
		this.shareBy = shareBy;
	}
	
}

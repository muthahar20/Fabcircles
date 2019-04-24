package com.fab.model;

import java.util.List;

public class CommentResponse extends CommentBean{

	private static final long serialVersionUID = -6989467790065492204L;
	private List<ReplyBean> reply;
	private List<CommentLikeBean> commLikes;
	private long commLikeCount;
	private List<ReplyLikeBean> replyLikes;
	private long replyCount;
	private String profilePID;
	
	
	public String getProfilePID() {
		return profilePID;
	}
	public void setProfilePID(String profilePID) {
		this.profilePID = profilePID;
	}
	public List<ReplyLikeBean> getReplyLikes() {
		return replyLikes;
	}
	public void setReplyLikes(List<ReplyLikeBean> replyLikes) {
		this.replyLikes = replyLikes;
	}
	public long getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(long replyCount) {
		this.replyCount = replyCount;
	}
	public long getCommLikeCount() {
		return commLikeCount;
	}
	public void setCommLikeCount(long commLikeCount) {
		this.commLikeCount = commLikeCount;
	}
	public List<CommentLikeBean> getCommLikes() {
		return commLikes;
	}
	public void setCommLikes(List<CommentLikeBean> commLikes) {
		this.commLikes = commLikes;
	}
	
	public List<ReplyBean> getReply() {
		return reply;
	}
	public void setReply(List<ReplyBean> reply) {
		this.reply = reply;
	}
	
	

}

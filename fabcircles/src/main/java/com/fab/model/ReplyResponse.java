package com.fab.model;

import java.util.List;

public class ReplyResponse extends ReplyBean{
	private static final long serialVersionUID = 3492235506132343052L;
	private List<ReplyLikeBean> replyLikes;
	private long replyCount;
	
	
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (replyCount ^ (replyCount >>> 32));
		result = prime * result
				+ ((replyLikes == null) ? 0 : replyLikes.hashCode());
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
		ReplyResponse other = (ReplyResponse) obj;
		if (replyCount != other.replyCount)
			return false;
		if (replyLikes == null) {
			if (other.replyLikes != null)
				return false;
		} else if (!replyLikes.equals(other.replyLikes))
			return false;
		return true;
	}
	
	
}

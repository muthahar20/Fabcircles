package com.fab.model;

import java.util.List;
import java.util.Set;

public class PostResponse extends PostBean {

	private static final long serialVersionUID = -2633387314621841989L;
	
	private List<LikeBean> likes;
	private String profilePID;
	private long likeCount;
	private long commentCount;
	private List<SharedResponse> share;
	private List<CommentResponse> comments;
	private Set<String> commenterPublicIds;
	private boolean hide;
	private String shareBy;
	private String sendBy;
	private String unFollow;
	private String sendByFullName;
	private String shareByFullName;
	private String sendByProfilPID;
	private String shareByProfilePID;
	private PostResponse parentPost;
	
	
	
	
	
	public List<SharedResponse> getShare() {
		return share;
	}

	public void setShare(List<SharedResponse> share) {
		this.share = share;
	}

	public String getShareBy() {
		return shareBy;
	}

	public void setShareBy(String shareBy) {
		this.shareBy = shareBy;
	}

	public String getSendBy() {
		return sendBy;
	}

	public void setSendBy(String sendBy) {
		this.sendBy = sendBy;
	}

	public String getSendByFullName() {
		return sendByFullName;
	}

	public void setSendByFullName(String sendByFullName) {
		this.sendByFullName = sendByFullName;
	}

	public String getShareByFullName() {
		return shareByFullName;
	}

	public void setShareByFullName(String shareByFullName) {
		this.shareByFullName = shareByFullName;
	}

	public String getSendByProfilPID() {
		return sendByProfilPID;
	}

	public void setSendByProfilPID(String sendByProfilPID) {
		this.sendByProfilPID = sendByProfilPID;
	}

	public String getShareByProfilePID() {
		return shareByProfilePID;
	}

	public void setShareByProfilePID(String shareByProfilePID) {
		this.shareByProfilePID = shareByProfilePID;
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}

	public List<LikeBean> getLikes() {
		return likes;
	}

	public void setLikes(List<LikeBean> likes) {
		this.likes = likes;
	}

	public long getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(long likeCount) {
		this.likeCount = likeCount;
	}

	public long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(long commentCount) {
		this.commentCount = commentCount;
	}

	public List<CommentResponse> getComments() {
		return comments;
	}

	public void setComments(List<CommentResponse> comments) {
		this.comments = comments;
	}

	public String getProfilePID() {
		return profilePID;
	}

	public void setProfilePID(String profilePID) {
		this.profilePID = profilePID;
	}

	
	public String getUnFollow() {
		return unFollow;
	}

	public void setUnFollow(String unFollow) {
		this.unFollow = unFollow;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (commentCount ^ (commentCount >>> 32));
		result = prime
				* result
				+ ((commenterPublicIds == null) ? 0 : commenterPublicIds
						.hashCode());
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + (hide ? 1231 : 1237);
		result = prime * result + (int) (likeCount ^ (likeCount >>> 32));
		result = prime * result + ((likes == null) ? 0 : likes.hashCode());
		result = prime * result
				+ ((profilePID == null) ? 0 : profilePID.hashCode());
		result = prime * result + ((sendBy == null) ? 0 : sendBy.hashCode());
		result = prime * result
				+ ((sendByFullName == null) ? 0 : sendByFullName.hashCode());
		result = prime * result
				+ ((sendByProfilPID == null) ? 0 : sendByProfilPID.hashCode());
		result = prime * result + ((share == null) ? 0 : share.hashCode());
		result = prime * result + ((shareBy == null) ? 0 : shareBy.hashCode());
		result = prime * result
				+ ((shareByFullName == null) ? 0 : shareByFullName.hashCode());
		result = prime
				* result
				+ ((shareByProfilePID == null) ? 0 : shareByProfilePID
						.hashCode());
		result = prime * result
				+ ((unFollow == null) ? 0 : unFollow.hashCode());
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
		PostResponse other = (PostResponse) obj;
		if (commentCount != other.commentCount)
			return false;
		if (commenterPublicIds == null) {
			if (other.commenterPublicIds != null)
				return false;
		} else if (!commenterPublicIds.equals(other.commenterPublicIds))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (hide != other.hide)
			return false;
		if (likeCount != other.likeCount)
			return false;
		if (likes == null) {
			if (other.likes != null)
				return false;
		} else if (!likes.equals(other.likes))
			return false;
		if (profilePID == null) {
			if (other.profilePID != null)
				return false;
		} else if (!profilePID.equals(other.profilePID))
			return false;
		if (sendBy == null) {
			if (other.sendBy != null)
				return false;
		} else if (!sendBy.equals(other.sendBy))
			return false;
		if (sendByFullName == null) {
			if (other.sendByFullName != null)
				return false;
		} else if (!sendByFullName.equals(other.sendByFullName))
			return false;
		if (sendByProfilPID == null) {
			if (other.sendByProfilPID != null)
				return false;
		} else if (!sendByProfilPID.equals(other.sendByProfilPID))
			return false;
		if (share == null) {
			if (other.share != null)
				return false;
		} else if (!share.equals(other.share))
			return false;
		if (shareBy == null) {
			if (other.shareBy != null)
				return false;
		} else if (!shareBy.equals(other.shareBy))
			return false;
		if (shareByFullName == null) {
			if (other.shareByFullName != null)
				return false;
		} else if (!shareByFullName.equals(other.shareByFullName))
			return false;
		if (shareByProfilePID == null) {
			if (other.shareByProfilePID != null)
				return false;
		} else if (!shareByProfilePID.equals(other.shareByProfilePID))
			return false;
		if (unFollow == null) {
			if (other.unFollow != null)
				return false;
		} else if (!unFollow.equals(other.unFollow))
			return false;
		return true;
	}

	public Set<String> getCommenterPublicIds() {
		return commenterPublicIds;
	}

	public void setCommenterPublicIds(Set<String> commenterPublicIds) {
		this.commenterPublicIds = commenterPublicIds;
	}

	

}

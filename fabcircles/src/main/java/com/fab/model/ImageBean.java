package com.fab.model;

import com.fab.model.user.BaseBean;


public class ImageBean extends BaseBean{
	private static final long serialVersionUID = 31459815712313353L;
	private String imageDescription;
	private String albumName;
	private String userName;
	private String publicId;
	private String imageUrl;
	private String circleId;
	private String isCover;
	private String isProfile;
	private String xPosition;
	private String yPosition;
	private String xAxis;
	private String yAxis;
	private String imageId;
	private String postMsgId;
	
	
	
	public String getxAxis() {
		return xAxis;
	}
	public void setxAxis(String xAxis) {
		this.xAxis = xAxis;
	}
	public String getyAxis() {
		return yAxis;
	}
	public void setyAxis(String yAxis) {
		this.yAxis = yAxis;
	}
	public String getImageDescription() {
		return imageDescription;
	}
	public void setImageDescription(String imageDescription) {
		this.imageDescription = imageDescription;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPublicId() {
		return publicId;
	}
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getCircleId() {
		return circleId;
	}
	public void setCircleId(String circleId) {
		this.circleId = circleId;
	}
	
	public String getIsCover() {
		return isCover;
	}
	public void setIsCover(String isCover) {
		this.isCover = isCover;
	}
	
	public String getxPosition() {
		return xPosition;
	}
	public void setxPosition(String xPosition) {
		this.xPosition = xPosition;
	}
	public String getyPosition() {
		return yPosition;
	}
	public void setyPosition(String yPosition) {
		this.yPosition = yPosition;
	}
	
	public String getIsProfile() {
		return isProfile;
	}
	public void setIsProfile(String isProfile) {
		this.isProfile = isProfile;
	}
	
	
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	public String getPostMsgId() {
		return postMsgId;
	}
	public void setPostMsgId(String postMsgId) {
		this.postMsgId = postMsgId;
	}
	@Override
	public String toString() {
		return "ImageBean [imageDescription=" + imageDescription
				+ ", albumName=" + albumName + ", userName=" + userName
				+ ", publicId=" + publicId + ", imageUrl=" + imageUrl
				+ ", circleId=" + circleId + ", isCover=" + isCover
				+ ", isProfile=" + isProfile + ", xPosition=" + xPosition
				+ ", yPosition=" + yPosition + ", xAxis=" + xAxis + ", yAxis="
				+ yAxis + ", imageId=" + imageId + ", postMsgId=" + postMsgId
				+ "]";
	}
	
	
}

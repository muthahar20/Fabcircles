package com.fab.model;

public class CoverImageBean extends ImageBean{

	private static final long serialVersionUID = 8139392908722625861L;
	private String coverImageDescription;
	private String coverImagePublicId;
	private String coverImageUrl;
	private String coverImageId;
	private String coverImageXPosition;
	private String coverImageYPosition;
	
	public String getCoverImageDescription() {
		return coverImageDescription;
	}
	public void setCoverImageDescription(String coverImageDescription) {
		this.coverImageDescription = coverImageDescription;
	}
	public String getCoverImagePublicId() {
		return coverImagePublicId;
	}
	public void setCoverImagePublicId(String coverImagePublicId) {
		this.coverImagePublicId = coverImagePublicId;
	}
	public String getCoverImageUrl() {
		return coverImageUrl;
	}
	public void setCoverImageUrl(String coverImageUrl) {
		this.coverImageUrl = coverImageUrl;
	}
	public String getCoverImageId() {
		return coverImageId;
	}
	public void setCoverImageId(String coverImageId) {
		this.coverImageId = coverImageId;
	}
	
	public String getCoverImageXPosition() {
		return coverImageXPosition;
	}
	public void setCoverImageXPosition(String coverImageXPosition) {
		this.coverImageXPosition = coverImageXPosition;
	}
	public String getCoverImageYPosition() {
		return coverImageYPosition;
	}
	public void setCoverImageYPosition(String coverImageYPosition) {
		this.coverImageYPosition = coverImageYPosition;
	}
	@Override
	public String toString() {
		return "CoverImageBean [coverImageDescription=" + coverImageDescription
				+ ", coverImagePublicId=" + coverImagePublicId
				+ ", coverImageUrl=" + coverImageUrl + ", coverImageId="
				+ coverImageId + ", coverImageXPosition=" + coverImageXPosition
				+ ", coverImageYPosition=" + coverImageYPosition + "]";
	}
	
}

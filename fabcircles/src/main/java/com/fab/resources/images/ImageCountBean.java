package com.fab.resources.images;

import java.io.Serializable;

public class ImageCountBean implements Serializable{

	private static final long serialVersionUID = 854726545236782275L;

	private String userName;
	private Long imageCount;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getImageCount() {
		return imageCount;
	}
	public void setImageCount(Long imageCount) {
		this.imageCount = imageCount;
	}	
}

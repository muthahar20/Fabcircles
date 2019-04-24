package com.fab.model.userprofile;

import com.fab.model.user.BaseBean;

public class PlacesLivedBean extends BaseBean {

	private String currentCity;
	private String pastCity;
	private String homeTown;
	private String userName;
	private String privacy="public";
	
	
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	public String getCurrentCity() {
		return currentCity;
	}
	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}
	public String getPastCity() {
		return pastCity;
	}
	public void setPastCity(String pastCity) {
		this.pastCity = pastCity;
	}
	public String getHomeTown() {
		return homeTown;
	}
	public void setHomeTown(String homeTown) {
		this.homeTown = homeTown;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}

package com.fab.model;

import java.util.Date;

import com.fab.model.user.BaseBean;

public class AlbumBean extends BaseBean{

	private static final long serialVersionUID = -6504794151706558187L;
	
	private String albumName;
	
	private String albumDescription;
	
	private String location;
	
	private Date date;
	
	private String userName;

	private String privacy;	
	
	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumDescription() {
		return albumDescription;
	}

	public void setAlbumDescription(String albumDescription) {
		this.albumDescription = albumDescription;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}	
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	

	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	@Override
	public String toString() {
		return "AlbumBean [albumName=" + albumName + ", albumDescription="
				+ albumDescription + ", location=" + location + ", date="
				+ date + ", userName=" + userName + "]";
	}	

}

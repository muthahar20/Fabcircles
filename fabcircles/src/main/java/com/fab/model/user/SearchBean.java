package com.fab.model.user;

public class SearchBean extends BaseBean {

	private String userName;
	private String circleName;
	private String name;
	private String publicId;
	private String icon;
	private long members;
	private Integer friends;
	private String beanType;
	
	
	
	
	public String getPublicId() {
		return publicId;
	}
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	public long getMembers() {
		return members;
	}
	public void setMembers(long members) {
		this.members = members;
	}
	public String getBeanType() {
		return beanType;
	}
	public void setBeanType(String beanType) {
		this.beanType = beanType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCircleName() {
		return circleName;
	}
	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public Integer getFriends() {
		return friends;
	}
	public void setFriends(Integer friends) {
		this.friends = friends;
	}
	
	
}

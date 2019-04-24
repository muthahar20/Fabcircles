package com.fab.model;

import java.util.Set;

import com.fab.model.user.BaseBean;
import com.fab.model.user.FriendsBean;

public class CircleBean extends BaseBean{
	
	
	private String userName;
	private String circleName;
	private String description;
	private String cover;
	private String icon;
	private String circleId;
	private long members;
	private String privacy="public";
	private Set<FriendsBean> friendsList;
	private String joinMmeberRequest;
	

	
	
	public Set<FriendsBean> getFriendsList() {
		return friendsList;
	}
	public void setFriendsList(Set<FriendsBean> friendsList) {
		this.friendsList = friendsList;
	}
	public String getJoinMmeberRequest() {
		return joinMmeberRequest;
	}
	public void setJoinMmeberRequest(String joinMmeberRequest) {
		this.joinMmeberRequest = joinMmeberRequest;
	}
	
	public long getMembers() {
		return members;
	}
	public void setMembers(long members) {
		this.members = members;
	}
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	public String getCircleName() {
		return circleName;
	}
	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getCircleId() {
		return circleId;
	}
	public void setCircleId(String circleId) {
		this.circleId = circleId;
	}
	
	
	

}

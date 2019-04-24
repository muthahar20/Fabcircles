package com.fab.model;

import java.io.Serializable;
import java.util.List;

import com.fab.model.user.FriendsBean;

public class MyFrndsCrclsResponse implements Serializable{
	private static final long serialVersionUID = -7883423793155002503L;
	
	private List<FriendsBean> myFriends;
	private List<CircleBean> myCircles;
	public List<FriendsBean> getMyFriends() {
		return myFriends;
	}
	public void setMyFriends(List<FriendsBean> myFriends) {
		this.myFriends = myFriends;
	}
	public List<CircleBean> getMyCircles() {
		return myCircles;
	}
	public void setMyCircles(List<CircleBean> myCircles) {
		this.myCircles = myCircles;
	}
	

}

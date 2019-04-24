package com.fab.model.user;


public class FriendsBean extends BaseBean{
	
	private static final long serialVersionUID = -7879509877607101753L;



	public FriendsBean(){
		
	}
	
	private String userName;
	private String friend;
	private String name;
	private String privacy="public";
	private String friendStatus;
	private String friendRequestStatus;
	private Integer mutualfriends;
	private long friends;
	private String country ;
	private String cityTown ;
	private String publicId;
	private String circleId;
	private String status;
	private String unFollow;
	
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCircleId() {
		return circleId;
	}
	public void setCircleId(String circleId) {
		this.circleId = circleId;
	}
	public String getPublicId() {
		return publicId;
	}
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCityTown() {
		return cityTown;
	}
	public void setCityTown(String cityTown) {
		this.cityTown = cityTown;
	}

	public String getFriendRequestStatus() {
		return friendRequestStatus;
	}
	public void setFriendRequestStatus(String friendRequestStatus) {
		this.friendRequestStatus = friendRequestStatus;
	}
	public Integer getMutualfriends() {
		return mutualfriends;
	}
	public void setMutualfriends(Integer mutualfriends) {
		this.mutualfriends = mutualfriends;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFriend() {
		return friend;
	}
	public void setFriend(String friend) {
		this.friend = friend;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	public String getFriendStatus() {
		return friendStatus;
	}
	public void setFriendStatus(String friendStatus) {
		this.friendStatus = friendStatus;
	}
	public long getFriends() {
		return friends;
	}
	public void setFriends(long friends) {
		this.friends = friends;
	}
	public FriendsBean(String userName, String friend,
			String name, String privacy, String friendStatus,
			String friendRequestStatus, Integer mutualfriends,
			String country, String cityTown,long friends, String publicId) {
		super();
		this.userName = userName;
		this.friend = friend;
		this.name = name;
		this.privacy = privacy;
		this.friendStatus = friendStatus;
		this.friendRequestStatus = friendRequestStatus;
		this.mutualfriends = mutualfriends;
		this.country = country;
		this.cityTown = cityTown;
		this.friends = friends;
		this.publicId = publicId;
	}
	public String getUnFollow() {
		return unFollow;
	}
	public void setUnFollow(String unFollow) {
		this.unFollow = unFollow;
	}

}

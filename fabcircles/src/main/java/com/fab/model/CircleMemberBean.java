package com.fab.model;

import com.fab.model.user.BaseBean;

public class CircleMemberBean extends BaseBean {

	private String userName;
	private String firstName;
	private String lastName;
	private String circleId;
	private String country;
	private String cityTown ;
	private String status ;
	private String joinMmeberRequest;

	
	public String getJoinMmeberRequest() {
		return joinMmeberRequest;
	}
	public void setJoinMmeberRequest(String joinMmeberRequest) {
		this.joinMmeberRequest = joinMmeberRequest;
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
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCircleId() {
		return circleId;
	}
	public void setCircleId(String circleId) {
		this.circleId = circleId;
	}
	
	
}

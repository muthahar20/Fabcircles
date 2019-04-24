package com.fab.model.userprofile;

import com.fab.model.user.BaseBean;

public class WorkBean extends BaseBean{
	private String occupation;
	private String skills;
	private String userName;
	private String positionPresent;
	private String employerNamePresent;
	private String startEndPresent;
	private String jobDescriptionPresent;
	private String positionPrevious;
	private String employerNamePrevious;
	private String startEndPrevious;
	private String jobDescriptionPrevious;
	private String privacy="public";
	
	
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getSkills() {
		return skills;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}
	public String getPositionPresent() {
		return positionPresent;
	}
	public void setPositionPresent(String positionPresent) {
		this.positionPresent = positionPresent;
	}
	public String getEmployerNamePresent() {
		return employerNamePresent;
	}
	public void setEmployerNamePresent(String employerNamePresent) {
		this.employerNamePresent = employerNamePresent;
	}
	public String getStartEndPresent() {
		return startEndPresent;
	}
	public void setStartEndPresent(String startEndPresent) {
		this.startEndPresent = startEndPresent;
	}
	public String getJobDescriptionPresent() {
		return jobDescriptionPresent;
	}
	public void setJobDescriptionPresent(String jobDescriptionPresent) {
		this.jobDescriptionPresent = jobDescriptionPresent;
	}
	public String getPositionPrevious() {
		return positionPrevious;
	}
	public void setPositionPrevious(String positionPrevious) {
		this.positionPrevious = positionPrevious;
	}
	public String getEmployerNamePrevious() {
		return employerNamePrevious;
	}
	public void setEmployerNamePrevious(String employerNamePrevious) {
		this.employerNamePrevious = employerNamePrevious;
	}
	public String getStartEndPrevious() {
		return startEndPrevious;
	}
	public void setStartEndPrevious(String startEndPrevious) {
		this.startEndPrevious = startEndPrevious;
	}
	public String getJobDescriptionPrevious() {
		return jobDescriptionPrevious;
	}
	public void setJobDescriptionPrevious(String jobDescriptionPrevious) {
		this.jobDescriptionPrevious = jobDescriptionPrevious;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
	
	
}

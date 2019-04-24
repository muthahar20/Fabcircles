package com.fab.model;

import java.util.List;


public class NotificatonResponse {

	private List<NotificationBean> notificationList;
	private long unReadCount;
	
	public List<NotificationBean> getNotificationList() {
		return notificationList;
	}
	public void setNotificationList(List<NotificationBean> notificationList) {
		this.notificationList = notificationList;
	}
	public long getUnReadCount() {
		return unReadCount;
	}
	public void setUnReadCount(long unReadCount) {
		this.unReadCount = unReadCount;
	}
	
	
	
	
	
	

}

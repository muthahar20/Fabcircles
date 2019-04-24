package com.fab.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import com.fab.constants.FabConstants;
import com.fab.model.NotificationBean;
import com.fab.model.NotificatonResponse;
import com.fab.mongo.MongoManager;


@Component
public class NotificationDAO extends BaseDAO {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CircleDAO.class);

	@Autowired 
	MongoManager mongoManager;
	@Autowired 
	PostDAO postDAO;
	
	
	// NOTIFICATON 

	public void postNotification( NotificationBean notification) throws Exception{
		LOGGER.info(" PostDAO  : postNotification method Call");
		notification.setReadStatus("unRead");
		insertDB(FabConstants.NOTIFICATON_COLLECTION, notification);
	}


	// PUT Notification
	public List<NotificationBean> putNotification( NotificationBean notification) throws Exception{
		LOGGER.info(" PostDAO  : putNotification method Call");
	
		List<NotificationBean> notificationsList = mongoManager.getObjectsByField ( FabConstants.NOTIFICATON_COLLECTION,
				"userName", notification.getUserName(), NotificationBean.class);
		
		for( NotificationBean notificationBEAN : notificationsList){
			mongoManager.updateBy2Fields(FabConstants.NOTIFICATON_COLLECTION, 
					"userName", notification.getUserName(),"postMsgId", notificationBEAN.getPostMsgId(),  "readStatus", notification.getReadStatus());
		}
		
			return mongoManager.getObjectsByField ( FabConstants.NOTIFICATON_COLLECTION,
					"userName", notification.getUserName(), NotificationBean.class);
		
	}


	
	// get Notifications ByPageSize 
	public NotificatonResponse getNotificationByPageSize(String userName,Integer pageNumber, Integer pageSize)
					throws Exception {
			LOGGER.info(":: PostDAO : getNotificationByPageSize Method call  ::");
			String readStatus = "unRead";
			NotificatonResponse notificationRes = new NotificatonResponse();
			/*
			List<NotificationBean> notificationList = mongoManager.getObjectsByOrderAndPage(FabConstants.NOTIFICATON_COLLECTION,
					"userName",null,null, readStatus,"created", Direction.DESC, pageNumber, pageSize, 
					 NotificationBean.class);
					 */
			List<NotificationBean> notificationList = mongoManager.getObjectsByOrderAndPage( FabConstants.NOTIFICATON_COLLECTION,
					"userName",userName,null,null, "created", Direction.DESC, pageNumber, pageSize, 
					 NotificationBean.class);
			
			for(NotificationBean notification : notificationList){
				notification.setAdmineFullName(postDAO.getFullName(notification.getPostAdmin()));
				notification.setFriendFullName(postDAO.getFullName(notification.getFriend()));
				notification.setAdmineProfilePID(postDAO.getProfilePublicId(notification.getPostAdmin()));
				notification.setFriendProfilePID(postDAO.getProfilePublicId(notification.getFriend()));
			}
			
			sortNotificationDate(notificationList);
			notificationRes.setNotificationList(notificationList);
			notificationRes.setUnReadCount(mongoManager.getCountByField(FabConstants.NOTIFICATON_COLLECTION,
					"userName", userName, "readStatus", readStatus, NotificationBean.class));
			
				return notificationRes;
			}

	
	
	
	
	
	//Get Notification
	public List<NotificationBean> getNotification(String userName) throws Exception{
		LOGGER.info(" PostDAO  : getNotification method Call");
		List<NotificationBean> notificationRes = new ArrayList<NotificationBean>();
		
		List<NotificationBean> notificationFromDB = mongoManager.getObjectsByField(
				FabConstants.NOTIFICATON_COLLECTION,"userName", userName,NotificationBean.class);
		for(NotificationBean notification : notificationFromDB){
			notification.setAdmineFullName(postDAO.getFullName(notification.getPostAdmin()));
			notification.setFriendFullName(postDAO.getFullName(notification.getFriend()));
			notification.setAdmineProfilePID(postDAO.getProfilePublicId(notification.getPostAdmin()));
			notification.setFriendProfilePID(postDAO.getProfilePublicId(notification.getFriend()));
			notificationRes.add(notification);
		}	
		sortNotificationDate(notificationRes);
		return  notificationRes;
	}

	
	
	
	private void sortNotificationDate(List<NotificationBean> notificationList) {
		Collections.sort(notificationList, new Comparator<NotificationBean>() {
			@Override
			public int compare(NotificationBean n1, NotificationBean n2) {
				 if (n1.getCreated() == null && n2.getCreated() == null) {
		                return 0;
		            }
		            if (n1.getCreated() == null) {
		                return 1;
		            }
		            if (n2.getCreated() == null) {
		                return -1;
		            }
					return n2.getCreated().compareTo(n1.getCreated());
			}
		});
	}
	
	
	
	
	
}

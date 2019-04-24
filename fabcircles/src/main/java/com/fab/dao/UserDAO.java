package com.fab.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fab.constants.FabConstants;
import com.fab.constants.StatusCodes;
import com.fab.dao.userprofiledao.ContactBasicInfoDAO;
import com.fab.exception.FabException;
import com.fab.model.ResponseBean;
import com.fab.model.user.UserBean;
import com.fab.mongo.MongoManager;

@Component
public class UserDAO {

	@Autowired
	MongoManager mongoManager;

	@Autowired
	FabConstants fabConstants;

	@Autowired
	StatusCodes statusCodes;

	@Autowired
	ContactBasicInfoDAO contactBasicInfoDAO;

	@Autowired
	ImageDAO imageDAO;

	// Validate User
	public void validateUser(String userName) throws Exception {
		UserBean userBeanFromDB = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName", userName,
				UserBean.class);
		if (userBeanFromDB == null) {
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST,
					" Invalid UserName Please Register ");
		}
	}

	// user Registration
	public ResponseBean userRegister(UserBean userBean) throws Exception {
		ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		// Check whether the user is exist or not

		UserBean userBeanFromDB = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName",
				userBean.getUserName(), UserBean.class);
		if (userBeanFromDB == null) {
			mongoManager.insert(FabConstants.USER_COLLECTION, userBean);
			contactBasicInfoDAO.createContactBasicInfo(userBean.getUserName());

		} else {
			// UserId already exist
			throw new FabException(StatusCodes.USER_ID_EXIST,
					"UserName Already exist");
		}
		return response;
	}

	// update User Name
	public ResponseBean updateUser(UserBean userBean) throws Exception {
		UserBean userBeanFromDB = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName",
				userBean.getUserName(), UserBean.class);

		ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		if (userBeanFromDB != null) {
			userBeanFromDB.setFirstName(userBean.getFirstName());
			userBeanFromDB.setLastName(userBean.getLastName());
			mongoManager.update(FabConstants.USER_COLLECTION, userBeanFromDB);
		} else {
			// UserId not exist
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST,
					"UserId doesn't exist");
		}
		return response;
	}

	// User Find
	public UserBean find(UserBean userBean) throws Exception {
		UserBean userBeanFromDB = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName",
				userBean.getUserName(), UserBean.class);

		if (userBeanFromDB != null) {
			userBeanFromDB.setPassword(null);
			return userBeanFromDB;
		}
		return null;
	}

	
	
	
	// get All Friends
	public List<UserBean> findAll() throws Exception {

		List<UserBean> userBeanList = mongoManager.getAllObjects(
				FabConstants.USER_COLLECTION, UserBean.class);
		for (UserBean user : userBeanList) {
			user.setPassword(null);
		}

		System.out.println(" DATE :" + new Date().getDate());
		System.out.println(" DAy :" + new Date().toString());
		System.out.println(" Time :" + new Date().getTime());
		
		return userBeanList;
	}

}
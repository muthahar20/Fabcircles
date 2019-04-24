package com.fab.dao.userprofiledao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fab.constants.FabConstants;
import com.fab.constants.StatusCodes;
import com.fab.exception.FabException;
import com.fab.model.ResponseBean;
import com.fab.model.user.UserBean;
import com.fab.model.userprofile.ContactBasicInfoBean;
import com.fab.mongo.MongoManager;

@Component
public class ContactBasicInfoDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContactBasicInfoDAO.class);

	@Autowired
	MongoManager mongoManager;
	@Autowired
	FabConstants fabConstants;
	@Autowired
	StatusCodes statusCodes;
	
	//ContactBasicInfoBean Update
	public ResponseBean updateContactBasicInfo(ContactBasicInfoBean contactBasicInfoBean) throws Exception{
		LOGGER.info(":: ContactBasicInfo Api DAO :  createContactBasicInfo Update Method call ::");
		ResponseBean response=new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		
		ContactBasicInfoBean contactBasicInfoBeanFromDB=mongoManager.getObjectByField(FabConstants.CONTACT_BASIC_INFO_COLLECTION, "userName", contactBasicInfoBean.getUserName(), ContactBasicInfoBean.class);    
   		if(contactBasicInfoBeanFromDB==null){
   			
   			mongoManager.insert(FabConstants.CONTACT_BASIC_INFO_COLLECTION, contactBasicInfoBean);
   		}else{
   			contactBasicInfoBeanFromDB.setUserName(contactBasicInfoBean.getUserName());
			contactBasicInfoBeanFromDB.setAddress(contactBasicInfoBean.getAddress());
			contactBasicInfoBeanFromDB.setCityTown(contactBasicInfoBean.getCityTown());
			contactBasicInfoBeanFromDB.setZip(contactBasicInfoBean.getZip());
			contactBasicInfoBeanFromDB.setState(contactBasicInfoBean.getState());
			contactBasicInfoBeanFromDB.setCountry(contactBasicInfoBean.getCountry());
			contactBasicInfoBeanFromDB.setEmailAddress(contactBasicInfoBean.getEmailAddress());
			contactBasicInfoBeanFromDB.setMobilePhone(contactBasicInfoBean.getMobilePhone());
			contactBasicInfoBeanFromDB.setBirthDay(contactBasicInfoBean.getBirthDay());
			contactBasicInfoBeanFromDB.setBirthYear(contactBasicInfoBean.getBirthYear());
			contactBasicInfoBeanFromDB.setGender(contactBasicInfoBean.getGender());
			contactBasicInfoBeanFromDB.setPrivacy(contactBasicInfoBean.getPrivacy());
			contactBasicInfoBeanFromDB.setRelationship(contactBasicInfoBean.getRelationship());
			mongoManager.update(FabConstants.CONTACT_BASIC_INFO_COLLECTION, contactBasicInfoBeanFromDB);
   		}
		return response;
	}
	
	
	//ContactBasicInfoBean Find
	public ContactBasicInfoBean findContactBasicIfno(String  userName) throws Exception{
		LOGGER.info(":: ContactBasicInfo Api DAO :  createContactBasicInfo Find Method call ::");
		UserBean usreNameFromUserBean=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", userName, UserBean.class);
		if(usreNameFromUserBean==null){
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "UserName doesn't exist");
		}
		ContactBasicInfoBean contactBasicInfoBeanFromDB = mongoManager.getObjectByField(FabConstants.CONTACT_BASIC_INFO_COLLECTION, "userName", userName, ContactBasicInfoBean.class);
		return contactBasicInfoBeanFromDB;
	}
	
	
	
	
	//ContactBasicInfoBean Create
	public ResponseBean createContactBasicInfo(String userName) throws Exception{
		LOGGER.info(":: ContactBasicInfo Api DAO :  createContactBasicInfo Create Method call ::");
		ResponseBean response=new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		ContactBasicInfoBean contactBasicInfoBEAN = new ContactBasicInfoBean();
		
		UserBean userBeanfromDB = mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName",userName, UserBean.class);
	
		ContactBasicInfoBean contactBasicInfoBeanFromDB=mongoManager.getObjectByField(FabConstants.CONTACT_BASIC_INFO_COLLECTION, "userName", userName, ContactBasicInfoBean.class);    
   		if(contactBasicInfoBeanFromDB==null){
   			
   				contactBasicInfoBEAN.setUserName(userName);
   				contactBasicInfoBEAN.setEmailAddress(userBeanfromDB.getEmail());
   				contactBasicInfoBEAN.setMobilePhone(userBeanfromDB.getPhone());
   				
   				String Bday =userBeanfromDB.getBirthday();
   				String[] Blist=Bday.split("/");
   				
   				contactBasicInfoBEAN.setBirthDay(Blist[0]+"-"+Blist[1]);
   				contactBasicInfoBEAN.setBirthYear(Blist[2]);
   				contactBasicInfoBEAN.setGender(userBeanfromDB.getGender());
   				contactBasicInfoBEAN.setPrivacy("public");
   				
   				mongoManager.insert(FabConstants.CONTACT_BASIC_INFO_COLLECTION, contactBasicInfoBEAN);
   				
   		}
		return response;
	}
	
	
	
	
}

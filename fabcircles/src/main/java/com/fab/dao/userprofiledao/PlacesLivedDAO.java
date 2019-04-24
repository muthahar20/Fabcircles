package com.fab.dao.userprofiledao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fab.constants.FabConstants;
import com.fab.constants.StatusCodes;
import com.fab.exception.FabException;
import com.fab.model.ResponseBean;
import com.fab.model.user.UserBean;
import com.fab.model.userprofile.PlacesLivedBean;
import com.fab.mongo.MongoManager;



@Component
public class PlacesLivedDAO {
	
	@Autowired
	MongoManager mongoManager;
	
	@Autowired
	FabConstants fabConstants;
	
	@Autowired
	StatusCodes statusCodes;
	
	
	public ResponseBean createORUpdatePlacesLived(PlacesLivedBean placesLivedBean) throws Exception{
		ResponseBean resp=new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		PlacesLivedBean placesLivedBeanFromDB=mongoManager.getObjectByField(FabConstants.PLACES_U_LIVED_COLLECTION, "userName", placesLivedBean.getUserName(), PlacesLivedBean.class);
		if(placesLivedBeanFromDB==null){
			mongoManager.insert(FabConstants.PLACES_U_LIVED_COLLECTION, placesLivedBean);
		}else{
			placesLivedBeanFromDB.setUserName(placesLivedBean.getUserName());
			placesLivedBeanFromDB.setCurrentCity(placesLivedBean.getCurrentCity());
			placesLivedBeanFromDB.setHomeTown(placesLivedBean.getHomeTown());
			placesLivedBeanFromDB.setPastCity(placesLivedBean.getPastCity());
			placesLivedBeanFromDB.setPrivacy(placesLivedBean.getPrivacy());
			mongoManager.update(FabConstants.PLACES_U_LIVED_COLLECTION, placesLivedBeanFromDB);
		}
		
		return resp;
	}
	
	
	
	
	public PlacesLivedBean findPlacesLived(String userName) throws Exception{
		UserBean usreNameFromUserBean=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", userName, UserBean.class);
		if(usreNameFromUserBean==null){
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "UserName doesn't exist");
		}
	
		PlacesLivedBean placesLivedBeanFromDB=mongoManager.getObjectByField(FabConstants.PLACES_U_LIVED_COLLECTION, "userName", userName, PlacesLivedBean.class);

		return placesLivedBeanFromDB;
	}
}

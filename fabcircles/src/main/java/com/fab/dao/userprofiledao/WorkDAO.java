package com.fab.dao.userprofiledao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fab.constants.FabConstants;
import com.fab.constants.StatusCodes;
import com.fab.exception.FabException;
import com.fab.model.ResponseBean;
import com.fab.model.user.UserBean;
import com.fab.model.userprofile.WorkBean;
import com.fab.mongo.MongoManager;


@Component
public class WorkDAO {

	@Autowired
	MongoManager mongoManager;
	
	@Autowired
	FabConstants fabConstants;
	
	@Autowired
	StatusCodes statusCodes;
	
	public ResponseBean postWork(WorkBean workBean) throws Exception{
		ResponseBean response=new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		WorkBean workBeanFromDB=mongoManager.getObjectByField(FabConstants.WORK_COLLECTION, "userName", workBean.getUserName(), WorkBean.class);
		
		if(workBeanFromDB==null){
			mongoManager.insert(FabConstants.WORK_COLLECTION, workBean);
		}else{
			workBeanFromDB.setOccupation(workBean.getOccupation());
			workBeanFromDB.setSkills(workBean.getSkills());
			workBeanFromDB.setUserName(workBean.getUserName());
			workBeanFromDB.setPositionPresent(workBean.getPositionPresent());
			workBeanFromDB.setEmployerNamePresent(workBean.getEmployerNamePresent());
			workBeanFromDB.setStartEndPresent(workBean.getStartEndPresent());
			workBeanFromDB.setJobDescriptionPresent(workBean.getJobDescriptionPresent());
			workBeanFromDB.setPositionPrevious(workBean.getPositionPrevious());
			workBeanFromDB.setEmployerNamePrevious(workBean.getEmployerNamePrevious());
			workBeanFromDB.setStartEndPrevious(workBean.getStartEndPrevious());
			workBeanFromDB.setJobDescriptionPrevious(workBean.getJobDescriptionPrevious());
			workBeanFromDB.setPrivacy(workBean.getPrivacy());
				mongoManager.update(FabConstants.WORK_COLLECTION, workBeanFromDB);
		}
		return response;
	}
	public WorkBean findWork(String userName) throws Exception{
		UserBean usreNameFromUserBean=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", userName, UserBean.class);
		if(usreNameFromUserBean==null){
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "UserName doesn't exist");
		}
		return mongoManager.getObjectByField(FabConstants.WORK_COLLECTION, "userName", userName, WorkBean.class);

	}
	
	
	
	
}

package com.fab.dao.userprofiledao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fab.constants.FabConstants;
import com.fab.constants.StatusCodes;
import com.fab.exception.FabException;
import com.fab.model.ResponseBean;
import com.fab.model.user.UserBean;
import com.fab.model.userprofile.EducationBean;
import com.fab.mongo.MongoManager;


@Component
public class EducationDAO {

	@Autowired
	MongoManager mongoManager;
	
	@Autowired
	FabConstants fabConstants;
	
	@Autowired
	StatusCodes statusCodes;
	
	public ResponseBean createOrUpdateEducation(EducationBean educationBean) throws Exception{
		ResponseBean response=new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		EducationBean educationBeanFromDB=mongoManager.getObjectByField(FabConstants.EDUCATION_COLLECTION, "userName", educationBean.getUserName(), EducationBean.class);
		if(educationBeanFromDB==null){
			mongoManager.insert(FabConstants.EDUCATION_COLLECTION, educationBean);
		}else{
			educationBeanFromDB.setCollege(educationBean.getCollege());
			educationBeanFromDB.setAreaOfStudyCollege(educationBean.getAreaOfStudyCollege());
			educationBeanFromDB.setStartEndCollege(educationBean.getStartEndCollege());
			educationBeanFromDB.setCourseDescriptionCollege(educationBean.getCourseDescriptionCollege());
			
			educationBeanFromDB.setGraduateSchool(educationBean.getGraduateSchool());
			educationBeanFromDB.setAreaOfStudyGraduate(educationBean.getAreaOfStudyGraduate());
			educationBeanFromDB.setStartEndGraduate(educationBean.getStartEndGraduate());
			educationBeanFromDB.setCourseDescriptionGraduate(educationBean.getCourseDescriptionGraduate());
			
			educationBeanFromDB.setHighSchool(educationBean.getHighSchool());
			educationBeanFromDB.setAreaOfStudyHighSchool(educationBean.getAreaOfStudyHighSchool());
			educationBeanFromDB.setStartEndHighSchool(educationBean.getStartEndHighSchool());
			educationBeanFromDB.setCourseDescriptionHighSchool(educationBean.getCourseDescriptionHighSchool());
			educationBeanFromDB.setUserName(educationBean.getUserName());
			educationBeanFromDB.setPrivacy(educationBean.getPrivacy());
			mongoManager.update(FabConstants.EDUCATION_COLLECTION, educationBeanFromDB);
		}
		return response;
	}
	
	
	
	
	public EducationBean findEducationBean(String userName) throws Exception{
		UserBean usreNameFromUserBean=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", userName, UserBean.class);
		if(usreNameFromUserBean==null){
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "UserName doesn't exist");
		}
		return mongoManager.getObjectByField(FabConstants.EDUCATION_COLLECTION, "userName", userName, EducationBean.class);

	}
}

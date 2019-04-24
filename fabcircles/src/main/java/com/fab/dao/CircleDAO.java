package com.fab.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fab.constants.FabConstants;
import com.fab.constants.StatusCodes;
import com.fab.exception.FabException;
import com.fab.model.CircleBean;
import com.fab.model.CircleMemberBean;
import com.fab.model.ImageBean;
import com.fab.model.ResponseBean;
import com.fab.model.user.FriendsBean;
import com.fab.model.user.UserBean;
import com.fab.model.userprofile.ContactBasicInfoBean;
import com.fab.mongo.MongoManager;
import com.mongodb.DB;


@Component
public class CircleDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(CircleDAO.class);

	@Autowired
	MongoManager mongoManager;
	@Autowired
	FabConstants fabConstants;
	@Autowired
	StatusCodes statusCodes;
	@Autowired
	FriendsDAO friendsDAO;


		//Create Circle
		public CircleBean createCircle(CircleBean circleBean) throws Exception{
			LOGGER.info(":: createCircle DAO : createCircle Method call  ::");
			//Check User is exist or not
			UserBean userBeanFromDB=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", circleBean.getUserName(), UserBean.class);
			if(userBeanFromDB==null){
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "UserName doesn't exist");
			}
			try{
			DB db = mongoManager.getMongoDB();
			String circleId = MongoManager.getCircleNextId(db, "circle_seq");
			circleId = circleId.replaceAll("\\.0*$", "");
			circleBean.setCircleId(circleId);
			mongoManager.insert(FabConstants.CIRCLE_COLLECTION, circleBean);
				CircleMemberBean circleMemberBean = new CircleMemberBean();
				circleMemberBean.setCircleId(circleBean.getCircleId());
				circleMemberBean.setUserName(circleBean.getUserName());
				circleMemberBean.setStatus("A");
				mongoManager.insert(FabConstants.CIRCLE_MEMBER_COLLECTION, circleMemberBean);
				circleBean.setMembers(countMembers(circleId));
			}catch(Exception e){
				throw new FabException(StatusCodes.CIRCLE_NOT_CREATED, "Circle not created");
			}
			return circleBean;
		}

  
	//Update Circle
	public CircleBean updateCircle(CircleBean circleBean) throws Exception{
		LOGGER.info(":: createCircle DAO : updateCircle Method call  ::");

		CircleBean circleBEAN = mongoManager.findBy2Fields(FabConstants.CIRCLE_COLLECTION, "CircleId",circleBean.getCircleId(), "userName", circleBean.getUserName(), CircleBean.class);
		if(circleBEAN!=null){
			circleBEAN.setCircleId(circleBean.getCircleId());
			circleBEAN.setCircleName(circleBean.getCircleName());
			circleBEAN.setCover(circleBean.getCover());
			circleBEAN.setDescription(circleBean.getDescription());
			circleBEAN.setIcon(circleBean.getIcon());
			circleBEAN.setUserName(circleBean.getUserName());
			mongoManager.update(FabConstants.CIRCLE_COLLECTION, circleBEAN);
			circleBEAN.setMembers(countMembers(circleBean.getCircleId()));
		}else{
			throw new FabException(StatusCodes.CIRCLE_NOT_FOUND, "CircleId OR UserName doesn't exist");	
		}
		return circleBEAN;
	}

	
	
	

  public List<CircleBean> getCircles(String userName)throws Exception
  {
	  LOGGER.info(":: createCircle DAO : getCircles Method call  ::");   
	  
	  List<CircleBean> circleBEAN = new ArrayList<CircleBean>();
    Set<FriendsBean> friendsList = new HashSet<FriendsBean>();

    validateUser(userName);

    List<CircleBean> circlesByUserName = mongoManager.getObjectsByField(FabConstants.CIRCLE_COLLECTION, "userName", userName, CircleBean.class);
    if(circlesByUserName == null){
        throw new FabException(StatusCodes.CIRCLE_NOT_FOUND, " Circle Not Found  ");
    }
    
      for (CircleBean circle : circlesByUserName){
    	  friendsList = getMembersList(circle.getCircleId());
    	  circle.setFriendsList(friendsList);
    	  circle.setMembers(countMembers(circle.getCircleId()));
    	  circleBEAN.add(circle);
      }
    return circleBEAN;
  }
  
  
	

	
	

  public ResponseBean deleteCircle(CircleMemberBean circleMemberBean)
    throws Exception
  {
    ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
    validateMemberBean(circleMemberBean);

    CircleMemberBean circleMemberFromDB = mongoManager.findBy2Fields(FabConstants.CIRCLE_MEMBER_COLLECTION, "circleId", circleMemberBean.getCircleId(), "userName", circleMemberBean.getUserName(), CircleMemberBean.class);
    if (circleMemberFromDB == null) {
      throw new FabException(StatusCodes.CIRCLEMEMBER_NOT_FOUND, " CIRCLEMEMBER NOT FOUND");
    }
    if (circleMemberFromDB.getStatus().equals(null)) {
      throw new FabException(StatusCodes.CIRCLE_NOT_FOUND, "Your Not a Circle Admin to Delete the Circle ");
    }
    if (circleMemberFromDB != null) {
      if (circleMemberFromDB.getStatus().equals("A")) {
        mongoManager.deleteBy2Field(FabConstants.CIRCLE_COLLECTION, "circleId", circleMemberBean.getCircleId(), "userName", circleMemberBean.getUserName());
        mongoManager.deleteByField(FabConstants.CIRCLE_MEMBER_COLLECTION, "circleId", circleMemberBean.getCircleId());
      } else if (circleMemberFromDB.getStatus().equals(null)) {
        throw new FabException(StatusCodes.CIRCLE_NOT_FOUND, "Your Not a Circle Admin to Delete the Circle ");
      }
    }
    return response;
  }
  
  
  

  public ResponseBean addMembers(CircleMemberBean circleMember)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : addMembers Method call  ::");
	  
	  validateUser(circleMember.getUserName());

    CircleBean circleBeanFromDB = mongoManager.getObjectByField(FabConstants.CIRCLE_COLLECTION, "circleId", circleMember.getCircleId(), CircleBean.class);

    if (circleBeanFromDB == null) {
      throw new FabException(StatusCodes.CIRCLE_NOT_FOUND, "Circle doesn't exist", Response.Status.CONFLICT);
    }

    CircleMemberBean circleMemberFromDB = mongoManager.findBy2Fields(FabConstants.CIRCLE_MEMBER_COLLECTION, "circleId", circleMember.getCircleId(), "userName", circleMember.getUserName(), CircleMemberBean.class);
    if (circleMemberFromDB != null) {
      throw new FabException(StatusCodes.CIRCLE_MEMBER_ALREADY_EXIST, "Circle member already exist", Response.Status.CONFLICT);
    }
    ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
    try {
      circleMember.setStatus("M");
      mongoManager.insert(FabConstants.CIRCLE_MEMBER_COLLECTION, circleMember);
    } catch (Exception e) {
      throw new FabException(StatusCodes.CIRCLE_NOT_CREATED, "Circle not created");
    }
    return response;
  }

  


  
  public ResponseBean acceptRequest(CircleMemberBean circleMemberBean)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : acceptRequest Method call  ::");
	  
	  ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
    CircleMemberBean circleMemberFromDB = mongoManager.findBy2Fields(FabConstants.CIRCLE_MEMBER_COLLECTION, "circleId", circleMemberBean.getCircleId(), "userName", circleMemberBean.getUserName(), CircleMemberBean.class);
    CircleBean beanFromJoinCircleCollection = mongoManager.findBy2Fields(FabConstants.JOIN_CIRCLE_MEMBER_COLLECTION, "circleId", circleMemberBean.getCircleId(), "joinMmeberRequest", circleMemberBean.getUserName(), CircleBean.class);
   
    if (beanFromJoinCircleCollection != null)
    {
      if (circleMemberFromDB == null) {
        mongoManager.deleteCollection(FabConstants.JOIN_CIRCLE_MEMBER_COLLECTION, "circleId", circleMemberBean.getCircleId(), "joinMmeberRequest", circleMemberBean.getUserName(), CircleBean.class);
        circleMemberBean.setStatus("M");
        mongoManager.insert(FabConstants.CIRCLE_MEMBER_COLLECTION, circleMemberBean);
      }
      else {
        mongoManager.deleteCollection(FabConstants.JOIN_CIRCLE_MEMBER_COLLECTION, "circleId", circleMemberBean.getCircleId(), "joinMmeberRequest", circleMemberBean.getUserName(), CircleBean.class);

        throw new FabException(StatusCodes.CIRCLE_MEMBER_ALREADY_EXIST, "Circle member already exist", Response.Status.CONFLICT);
      }
    }
    else {
      throw new FabException(StatusCodes.CIRCLEMEMBER_NOT_FOUND, " doen't have any requests ", Response.Status.CONFLICT);
    }
    return response;
  }

  
  
  
  public ResponseBean denyRequest(CircleMemberBean circleMemberBean)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : denyRequest Method call  ::");
	  
	  ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
    CircleBean beanFromJoinCircleCollection = mongoManager.findBy2Fields(FabConstants.JOIN_CIRCLE_MEMBER_COLLECTION, "circleId", circleMemberBean.getCircleId(), "joinMmeberRequest", circleMemberBean.getUserName(), CircleBean.class);

    if (beanFromJoinCircleCollection != null)
      mongoManager.deleteCollection(FabConstants.JOIN_CIRCLE_MEMBER_COLLECTION, "circleId", circleMemberBean.getCircleId(), "joinMmeberRequest", circleMemberBean.getUserName(), CircleBean.class);
    else {
      throw new FabException(StatusCodes.CIRCLEMEMBER_NOT_FOUND, " doen't have any requests ", Response.Status.CONFLICT);
    }
    return response;
  }

  
  
  
  
  
  
  //Delete Member
  public ResponseBean memberDelete(CircleMemberBean circleMember)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : memberDelete Method call  ::");
	  
	  ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
    validateMemberBean(circleMember);
    CircleMemberBean circleMemberFromDB = mongoManager.findBy2Fields(FabConstants.CIRCLE_MEMBER_COLLECTION, "circleId", circleMember.getCircleId(), "userName", circleMember.getUserName(), CircleMemberBean.class);
    if(circleMemberFromDB !=null){
    	if(circleMemberFromDB.getStatus() == "A"){
			throw new FabException(StatusCodes.CIRCLEMEMBER_EXIST, " Please Delete the Circle ", Response.Status.CONFLICT);	

    	}
    }
    mongoManager.deleteBy2Field(FabConstants.CIRCLE_MEMBER_COLLECTION, "userName", circleMember.getUserName(), "circleId", circleMember.getCircleId());
    return response;
  }

  
  
    
    
  
  public long countMembers(String circleId)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : countMembers Method call  ::");
    return mongoManager.getCountByField(FabConstants.CIRCLE_MEMBER_COLLECTION, "circleId", circleId, CircleMemberBean.class);
  }

  public CircleBean circleMemberCount(String circleId)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : circleMemberCount Method call  ::");
    CircleBean circleBean = mongoManager.getObjectByField(FabConstants.CIRCLE_COLLECTION, "circleId", circleId, CircleBean.class);
    circleBean.setMembers(countMembers(circleId));
    return circleBean;
  }

 
  
  
  public Set<CircleBean> newCircles(String userName)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : newCircles Method call  ::");

    Set<CircleBean> list = new HashSet<CircleBean>();
    List<CircleBean> circlesList = mongoManager.getObjectsByField(FabConstants.CIRCLE_COLLECTION, "userName", userName, CircleBean.class);
    for (CircleBean circleBean : circlesList) {
      if (circleBean.getUserName().equals(userName)) {
        circleBean.setMembers(countMembers(circleBean.getCircleId()));
        list.add(getCircleById(circleBean.getCircleId()));
      }
    }

    return list;
  }
  
  
  

  public Set<CircleBean> newCirclesBySize(String userName, Integer listSize)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : newCirclesBySize Method call  ::");
    Set<CircleBean> list = new HashSet<CircleBean>();
    Set<CircleBean> circlesList = getCirclesByUserName(userName);
    for (CircleBean circleBean : circlesList) {
      if (list.size() < listSize.intValue()) {
        circleBean.setMembers(countMembers(circleBean.getCircleId()));
        list.add(getCircleById(circleBean.getCircleId()));
      }
    }
    return list;
  }

  
  
  public List<CircleBean> myCircles(String userName)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : myCircles Method call  ::");
    List<CircleBean> list = new ArrayList<CircleBean>();
    List<CircleMemberBean> circlesList = mongoManager.getObjectsByField(FabConstants.CIRCLE_MEMBER_COLLECTION, "userName", userName, CircleMemberBean.class);
    for (CircleMemberBean circleMembers : circlesList) {
      list.add(getCircleById(circleMembers.getCircleId()));
    }
    return list;
  }

  
  
  public List<CircleBean> myCirclesBySize(String userName, Integer listSize)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : myCirclesBySize Method call  ::");
    List<CircleBean> list = new ArrayList<CircleBean>();
    List<CircleMemberBean> circlesList = mongoManager.getObjectsByField(FabConstants.CIRCLE_MEMBER_COLLECTION, "userName", userName, CircleMemberBean.class);
    for (CircleMemberBean circleMembers : circlesList) {
      if (list.size() < listSize.intValue()) {
        list.add(getCircleById(circleMembers.getCircleId()));
      }
    }
    return list;
  }

  
  
  
  public List<CircleBean> friendCircle(String userName)
		    throws Exception
		  {
	  LOGGER.info(":: createCircle DAO : friendCircle Method call  ::");
	   Set<String> set1 = new HashSet<String>();
	   List<CircleBean> list=new ArrayList<CircleBean>();

	   List<FriendsBean> friendsList =mongoManager.getObjectsByField(FabConstants.FRIENDS_COLLECTION, "userName", userName, FriendsBean.class);
	    if(friendsList==null){
	    	throw new FabException(StatusCodes.FRIENDS_LIST_NOT_FOUND, "Friend Circles Not Found");
	    }
	    for(FriendsBean friends:friendsList){
	    	set1.add(friends.getFriend());
	    }
	    
	    for(String circleUserName : set1){
	    List<CircleBean> circleListByUserName = mongoManager.getObjectsByField(FabConstants.CIRCLE_COLLECTION, "userName", circleUserName, CircleBean.class);
	    		list.addAll(circleListByUserName);
	    	}
	    
	    for(CircleBean circle : list){
	    	circle.setFriendsList(  getMembersList(circle.getCircleId()));
	    	circle.setMembers(countMembers(circle.getCircleId()));
	    }
	    	
	    	
		    return list;
		  }
  
  
  public List<CircleBean> friendCircleBySize(String userName, Integer listSize)
		    throws Exception
		  {
	  LOGGER.info(":: createCircle DAO : friendCircleBySize Method call  ::");
	  Set<String> set1 = new HashSet<String>();
	   List<CircleBean> list=new ArrayList<CircleBean>();

	   List<FriendsBean> friendsList =mongoManager.getObjectsByField(FabConstants.FRIENDS_COLLECTION, "userName", userName, FriendsBean.class);
	    if(friendsList==null){
	    	throw new FabException(StatusCodes.FRIENDS_LIST_NOT_FOUND, "Friend Circles Not Found");
	    }
	    for(FriendsBean friends:friendsList){
	    	set1.add(friends.getFriend());
	    }
	    
	    for(String circleUserName : set1){
	    List<CircleBean> circleListByUserName = mongoManager.getObjectsByField(FabConstants.CIRCLE_COLLECTION, "userName", circleUserName, CircleBean.class);
	    		list.addAll(circleListByUserName);
	    		if(list.size()==listSize){
	    			break;
	    		}
	    	}
	    
	    for(CircleBean circle : list){
	    	circle.setFriendsList(  getMembersList(circle.getCircleId()));
	    	circle.setMembers(countMembers(circle.getCircleId()));
	    }
	    	
	    
	    /*
	    for(String circleUserName : set1){
		    List<CircleBean> circleListByUserName = mongoManager.getObjectsByField(FabConstants.CIRCLE_COLLECTION, "userName", circleUserName, CircleBean.class);
		    		list.addAll(circleListByUserName);
		    	}
		    
		    for(CircleBean circle : list){
		    	circle.setFriendsList(  getMembersList(circle.getCircleId()));
		    	circle.setMembers(countMembers(circle.getCircleId()));
		    }
		    
		    
		    if(list.size()<listSize !! ){
		    	return list;
		    }
		    
		    */
		    return list;
		  }

  
  
  

  //Local Circles
  public Set<CircleBean> localCircle(String userName)throws Exception{
	  LOGGER.info(":: createCircle DAO : localCircle Method call  ::");
	  
	  UserBean userNameFromDB = mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", userName, UserBean.class);
	    if (userNameFromDB == null) {
	      throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "UserName doesn't Found");
	    }
	    
	    System.out.println("userNameFromDB :  "+userNameFromDB.getUserName());
	  
	  ContactBasicInfoBean contactBasicInfoFromUserName = mongoManager.getObjectByField(FabConstants.CONTACT_BASIC_INFO_COLLECTION, "userName", userNameFromDB.getUserName(), ContactBasicInfoBean.class);
	    if (contactBasicInfoFromUserName == null) {
	      throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "contactBasicInfo doesn't exist");
	    }
	    String userCity = contactBasicInfoFromUserName.getCityTown();
	    String userCountry = contactBasicInfoFromUserName.getCountry();
	    Set<String> userLocationList = new HashSet<String>();
	    
	    System.out.println(" contactBasicInfoFromUserName "+contactBasicInfoFromUserName.getUserName());
	    
	    List<ContactBasicInfoBean> cityList = mongoManager.getObjectsByField(FabConstants.CONTACT_BASIC_INFO_COLLECTION, "cityTown", userCity, ContactBasicInfoBean.class);
	    if (cityList == null) {
	      throw new FabException(StatusCodes.CONTACTBASIC_INFO_NOT_FOUND, "contactBasicInfo Not Found Based on Your cityTown ");
	    }
	    for(ContactBasicInfoBean city : cityList){
	    	userLocationList.add(city.getUserName());
	    }
	  
	    List<ContactBasicInfoBean> countryList = mongoManager.getObjectsByField(FabConstants.CONTACT_BASIC_INFO_COLLECTION, "country", userCountry, ContactBasicInfoBean.class);
	    if (countryList == null) {
	      throw new FabException(StatusCodes.CONTACTBASIC_INFO_NOT_FOUND, "contactBasicInfo Not Found Based on Your Country ");
	    }
	    for(ContactBasicInfoBean country : countryList){
	    	userLocationList.add(country.getUserName());
	    }
	    Set<CircleBean> cirlesList = new HashSet<CircleBean>();
	    Set<String> userFriendsList = new HashSet<String>();
	    userFriendsList.add(userName);
	    
	    List<FriendsBean> friendsList = mongoManager.getObjectsByField(FabConstants.FRIENDS_COLLECTION, "userName", userNameFromDB.getUserName(), FriendsBean.class);
	    if(friendsList==null){
	    	throw new FabException(StatusCodes.FRIENDS_LIST_NOT_FOUND, "Friend Circles Not Found");
	    }
	    for(FriendsBean friends:friendsList){
	    	userFriendsList.add(friends.getFriend());
	    }
	  
	    userLocationList.removeAll(userFriendsList);
	    
	   for(String names : userLocationList){
		   cirlesList.addAll(getCirclesByUserName(names));
	   }
		    return cirlesList;
		  }
  
//Local Circles By size
  public Set<CircleBean> localCircleBySize(String userName, Integer listSize)throws Exception{
	  LOGGER.info(":: createCircle DAO : localCircleBySize Method call  ::");
	  Set<CircleBean> list = new HashSet<CircleBean>();
	  list.addAll(localCircle(userName));
	  if(list.size()<listSize){
		  return list;
	  }
	  return null;
  }
  
  
  
  
  
  
  public Set<CircleBean> mutualCircles(String userName)
    throws Exception
  
  {
	  LOGGER.info(":: createCircle DAO : mutualCircles Method call  ::");
    System.out.println(" hello mtr");

    List<CircleBean> list1 = myCircles(userName);
    List<CircleBean> list2 = new ArrayList<CircleBean>();
    Set<String> mutulaCirclesId = new TreeSet<String>();
    List<FriendsBean> mutualFriendsList = friendsDAO.listOfFriends(userName);
    
    for (FriendsBean friends : mutualFriendsList) {
      list2.addAll(myCircles(friends.getFriend()));
    }

    for(CircleBean circle1 : list1){
    	System.out.println(" circle1 :::"+circle1.getCircleId());
    	for(CircleBean circle2 : list2){
    		System.out.println(" circle2 :::"+circle2.getCircleId());
    		if(circle1.getCircleId().equals(circle2.getCircleId())){
    			mutulaCirclesId.add(circle1.getCircleId());
    			System.out.println(" mutulaCircles 1:"+circle1.getUserName());
    		}
    	}
    }
    
    for(CircleBean circle3 : list2){
    	System.out.println(" circle3 :::"+circle3.getCircleId());
    	for(CircleBean circle4 : list2){
    		System.out.println(" circle4 :::"+circle4.getCircleId());
    		if(circle3.getCircleId().equals(circle4.getCircleId())){
    			mutulaCirclesId.add(circle3.getCircleId());
    			System.out.println(" mutulaCircles 1:"+circle3.getUserName());
    		}
    	}
    
    }
    Set<CircleBean> mutulaCircles = new HashSet<CircleBean>();
    	for(String ids : mutulaCirclesId){
    		System.out.println(" result 1:"+ids);
    		mutulaCircles.add(getCircleById(ids));
    	}
    
    return mutulaCircles;
  }

  
  
  
  
  
  
  public List<CircleBean> familyCircles(String userName)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : familyCircles Method call  ::");
    List<CircleBean> list = new ArrayList<CircleBean>();
    List<CircleBean> circlesList = mongoManager.getObjectsByField(FabConstants.CIRCLE_COLLECTION, "userName", userName, CircleBean.class);
    for (CircleBean circleBean : circlesList) {
      if (circleBean.getPrivacy().equals("family")) {
        circleBean.setMembers(countMembers(circleBean.getCircleId()));
        list.add(circleBean);
      }
    }
    return list;
  }

  
  
  public List<CircleBean> circles(String userName)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : circles Method call  ::");
    List<CircleBean> circleBEAN = new ArrayList<CircleBean>();

    List<CircleBean> circleBeanList = mongoManager.getObjectsByField(FabConstants.CIRCLE_COLLECTION, "userName", userName, CircleBean.class);

    for (CircleBean circle : circleBeanList) {
      if (circle == null) {
        throw new FabException(StatusCodes.CIRCLE_NOT_FOUND, "Circle not Found");
      }
      circleBEAN.add(circle);
    }

    for (CircleBean circle : circleBEAN) {
      circle.setMembers(countMembers(circle.getCircleId()));
      circleBEAN.add(getCircleById(circle.getCircleId()));
    }

    return circleBEAN;
  }

  
 
	
	
	 // Get Suggested Circles
	  public Set<CircleBean> suggestedCircles(String userName)
	    throws Exception
	  {
		  LOGGER.info(":: createCircle DAO : suggestedCircles Method call  ::");
	    Set<CircleBean> list = new HashSet<CircleBean>();
	    Set<String> circleName = new HashSet<String>();
	    List<CircleMemberBean> circlesList = mongoManager.getAllObjects(FabConstants.CIRCLE_MEMBER_COLLECTION, CircleMemberBean.class);
	    
	    
	    for(CircleMemberBean circlemember:circlesList ){
	    	if(!circlemember.getUserName().equals(userName)){
	    		circleName.add(circlemember.getUserName());
	    	}
	    }
	    for(String circleUserName : circleName){
	    	list.addAll(getCirclesByUserName(circleUserName));
	    }
	    
	   
	    return list;
	  }

	
	

  public Set<FriendsBean> getMembersList(String circleId)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : getMembersList Method call  ::");
    List<CircleMemberBean> circleMembersList = mongoManager.getObjectsByField(FabConstants.CIRCLE_MEMBER_COLLECTION, "circleId", circleId, CircleMemberBean.class);
    if (circleMembersList == null) {
      throw new FabException(StatusCodes.CIRCLE_NOT_FOUND, "Circle doesn't exist");
    }
    Set<FriendsBean> MemberBeanList = new HashSet<FriendsBean>();

    FriendsBean member = new FriendsBean();

    for (CircleMemberBean memeberbean : circleMembersList)
    {
      member = getFriendDetails(memeberbean.getUserName());
      member.setStatus(memeberbean.getStatus());
      member.setCircleId(memeberbean.getCircleId());
      MemberBeanList.add(member);
    }

    return MemberBeanList;
  }
  
  

  public CircleBean updatePrivacyStatus(CircleBean circleBean)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : updatePrivacyStatus Method call  ::");
    CircleBean circleBeanStatus = mongoManager.findBy2Fields(FabConstants.CIRCLE_COLLECTION, "userName", circleBean.getUserName(), "circleId", circleBean.getCircleId(), CircleBean.class);
    if (circleBeanStatus == null) {
      throw new FabException(StatusCodes.CIRCLE_NOT_FOUND, "Circle doesn't exist");
    }
    circleBeanStatus.setPrivacy(circleBean.getPrivacy());
    mongoManager.update(FabConstants.CIRCLE_COLLECTION, circleBeanStatus);
    circleBeanStatus.setMembers(0L);
    circleBeanStatus.setCover(null);
    circleBeanStatus.setDescription(null);
    circleBeanStatus.setIcon(null);
    return circleBeanStatus;
  }
  
  

  		//Join Circle
		public ResponseBean joinCircleRequest(CircleMemberBean circleMember) throws Exception{
			LOGGER.info(":: createCircle DAO : updateCircle Method call  ::");
			ResponseBean response=new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
			CircleBean circleBEAN = new CircleBean();
			
			circleBEAN.setJoinMmeberRequest(circleMember.getUserName());
			circleBEAN.setCircleId(circleMember.getCircleId());
			
			CircleMemberBean beanFromCircleMemberCollection=mongoManager.findBy2Fields(FabConstants.CIRCLE_MEMBER_COLLECTION, "userName", circleMember.getUserName(), "circleId", circleMember.getCircleId(),CircleMemberBean.class);
			
			CircleBean beanFromJoinMemberCollection=mongoManager.findBy2Fields(FabConstants.JOIN_CIRCLE_MEMBER_COLLECTION, "joinMmeberRequest", circleMember.getUserName(), "circleId", circleMember.getCircleId(),CircleBean.class);

		
			if( beanFromCircleMemberCollection==null && beanFromJoinMemberCollection==null ){
				
				circleMember.setStatus("A");
				CircleMemberBean circleAdmin=mongoManager.findBy2Fields(FabConstants.CIRCLE_MEMBER_COLLECTION, "circleId", circleMember.getCircleId(), "status", circleMember.getStatus() ,CircleMemberBean.class);
				if( circleAdmin != null ){
					circleBEAN.setUserName(circleAdmin.getUserName());
					mongoManager.insert(FabConstants.JOIN_CIRCLE_MEMBER_COLLECTION, circleBEAN);
				}
				
			}else if(  beanFromCircleMemberCollection==null && beanFromJoinMemberCollection!=null ){
				
				throw new FabException(StatusCodes.CIRCLEMEMBER_EXIST, " You Already Sent Request to Join this Circle ", Response.Status.CONFLICT);	
				
			}else if(  beanFromCircleMemberCollection!=null && beanFromJoinMemberCollection==null ){
				throw new FabException(StatusCodes.CIRCLEMEMBER_EXIST, " You Already Joined this Circle ", Response.Status.CONFLICT);	
			
			}else if(  beanFromCircleMemberCollection!=null && beanFromJoinMemberCollection!=null ){
				
				mongoManager.deleteBy2Field(FabConstants.CIRCLE_MEMBER_COLLECTION, "circleId", circleMember.getCircleId(), "status", circleMember.getStatus() );
				
			}else{
				throw new FabException(StatusCodes.CIRCLEMEMBER_EXIST, " You Already Joined this Circle ", Response.Status.CONFLICT);	
			}
			
			return response;
		}
		


  
  
  public List<CircleBean> getJoinCircleRequest(String userName)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : getJoinCircleRequest Method call  ::");

    Set<String> list1 = new HashSet<String>();
    Set<String> list2 = new HashSet<String>();
    Set<FriendsBean> friendsList = new HashSet<FriendsBean>();
    List<CircleBean> finalListCircle = new ArrayList<CircleBean>();
    
    List<CircleBean> listOfCirclesFromDB = mongoManager.getObjectsByField(FabConstants.CIRCLE_COLLECTION, "userName", userName, CircleBean.class);
    if ((listOfCirclesFromDB == null) || (listOfCirclesFromDB.isEmpty())) {
      throw new FabException(StatusCodes.CIRCLE_NOT_CREATED, "You doen't have Any Circles ", Response.Status.CONFLICT);
    }
    for (CircleBean ids : listOfCirclesFromDB) {
      list1.add(ids.getCircleId());
    }
    
    
    List<CircleBean> requestCircles = mongoManager.getObjectsByField(FabConstants.JOIN_CIRCLE_MEMBER_COLLECTION, "userName", userName, CircleBean.class);
    for (CircleBean ids : requestCircles) {
      list2.add(ids.getCircleId());
      friendsList.add(getFriendDetails(ids.getJoinMmeberRequest()));
    }

    list1.retainAll(list2);
    if ( list2.isEmpty() || list1.isEmpty() ) {
      throw new FabException(StatusCodes.CIRCLE_NOT_CREATED, "You doen't have any Request ", Response.Status.CONFLICT);
    }

    for (String circleID : list1) {
       finalListCircle = mongoManager.getObjectsByField(FabConstants.CIRCLE_COLLECTION, "circleId", circleID, CircleBean.class);
    }
   
    for(CircleBean cirle : finalListCircle){
    	cirle.setFriendsList(friendsList);
    }
    
    
    return finalListCircle;
  }

  
  
  
  
  
  public Set<CircleBean> getCirclesByUserName(String userName)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : getCirclesByUserName Method call  ::");
    Set<CircleBean> circlelist = new HashSet<CircleBean>();

    List<CircleBean> circleListByUserName = mongoManager.getObjectsByField(FabConstants.CIRCLE_COLLECTION, "userName", userName, CircleBean.class);
    if (circleListByUserName == null) {
      throw new FabException(StatusCodes.CIRCLE_NOT_FOUND, "Circle not Found");
    }

    for (CircleBean circleBEAN : circleListByUserName) {
      CircleBean circle = getCircleById(circleBEAN.getCircleId());
      circle.setMembers(countMembers(circleBEAN.getCircleId()));
      circlelist.add(circle);
    }

    return circlelist;
  }

  
  
  //Get Circle By Id
  public CircleBean getCircleById(String circleId)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : getCircleById Method call  ::");
    Set<FriendsBean> friendlist = new HashSet<FriendsBean>();

    CircleBean circleBean = mongoManager.getObjectByField(FabConstants.CIRCLE_COLLECTION, "circleId", circleId, CircleBean.class);
    if (circleBean == null) {
      throw new FabException(StatusCodes.CIRCLE_NOT_FOUND, "Circle not Found");
    }

    List<CircleMemberBean> circleMemberBeanFromDB = mongoManager.getObjectsByField(FabConstants.CIRCLE_MEMBER_COLLECTION, "circleId", circleId, CircleMemberBean.class);
    if (circleMemberBeanFromDB == null) {
      throw new FabException(StatusCodes.CIRCLEMEMBER_NOT_FOUND, "CircleMember not Found");
    }

    for (CircleMemberBean circleMemeberFromDB : circleMemberBeanFromDB) {
      FriendsBean friendBEAN = getFriendDetails(circleMemeberFromDB.getUserName());
      friendBEAN.setStatus(circleMemeberFromDB.getStatus());
      friendlist.add(friendBEAN);
    }
    circleBean.setMembers(countMembers(circleId));
    circleBean.setFriendsList(friendlist);

    return circleBean;
  }

  
  
  //get Friends/User Details
  public FriendsBean getFriendDetails(String friendName)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : getFriendDetails Method call  ::");
    UserBean usreNameFromUserBean = mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", friendName, UserBean.class);
    if (usreNameFromUserBean == null) {
      throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "UserName doesn't exist");
    }

    ContactBasicInfoBean contactBasicInfoBean = mongoManager.getObjectByField(FabConstants.CONTACT_BASIC_INFO_COLLECTION, "userName", friendName, ContactBasicInfoBean.class);
    if (contactBasicInfoBean == null) {
      throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "contactBasicInfo doesn't exist");
    }

    ImageBean image = new ImageBean();
    image.setUserName(friendName);
    image.setPublicId(" publicId Not Found/Please upload Profile Picture ");

    ImageBean imageBeanFromDB =  mongoManager.getObjectByField(FabConstants.IMAGE_COLLECTION, "userName", friendName, ImageBean.class);
    if (imageBeanFromDB == null) {
      imageBeanFromDB = image;
    }

    FriendsBean friendsBean = new FriendsBean();
    friendsBean.setMutualfriends(null);
    friendsBean.setFriend(friendName);
    friendsBean.setName(usreNameFromUserBean.getFirstName() + " " + usreNameFromUserBean.getLastName());
    friendsBean.setCityTown(contactBasicInfoBean.getCityTown());
    friendsBean.setCountry(contactBasicInfoBean.getCountry());
    friendsBean.setPublicId(imageBeanFromDB.getPublicId());
    return friendsBean;
  }


  
  
  
  
  //Validate User
  public void validateUser(String userName)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : validateUser Method call  ::");
    UserBean userBeanFromDB = mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", userName, UserBean.class);
    if (userBeanFromDB == null)
      throw new FabException(StatusCodes.USER_ID_NOT_EXIST, " Invalid UserName Please Register ");
  }

  //Validate Mmeber
  private void validateMemberBean(CircleMemberBean circleMember)
    throws Exception
  {
    CircleMemberBean memberBeanFromDB = mongoManager.findBy2Fields(FabConstants.CIRCLE_MEMBER_COLLECTION, "userName", circleMember.getUserName(), "circleId", circleMember.getCircleId(), CircleMemberBean.class);
    if (memberBeanFromDB == null)
      throw new FabException(StatusCodes.CIRCLEMEMBERS_FOUND, " Member doesn't exist");
  }

  
  
  public ResponseBean addDescription(String description)
    throws Exception
  {
	  LOGGER.info(":: createCircle DAO : addDescription Method call  ::");
    ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);

    return response;
  }
}
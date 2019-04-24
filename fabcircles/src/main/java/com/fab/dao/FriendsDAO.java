package com.fab.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fab.constants.FabConstants;
import com.fab.constants.StatusCodes;
import com.fab.exception.FabException;
import com.fab.filter.AuthFilter;
import com.fab.model.CircleBean;
import com.fab.model.CircleMemberBean;
import com.fab.model.FabWallBean;
import com.fab.model.ImageBean;
import com.fab.model.ResponseBean;
import com.fab.model.user.FriendsBean;
import com.fab.model.user.SearchBean;
import com.fab.model.user.UserBean;
import com.fab.model.userprofile.ContactBasicInfoBean;
import com.fab.mongo.MongoManager;

@Component
public class FriendsDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(FriendsDAO.class);
	@Autowired
	MongoManager mongoManager;
	
	@Autowired
	FabConstants fabConstants;
	
	@Autowired
	StatusCodes statusCodes;
	
	@Autowired
	LoginDAO loginDAO;
	
	@Autowired
	ImageDAO imageDAO;
	
	@Autowired
	PostDAO postDAO;
	
	
	 //FindFriend 
    public FriendsBean findFriend(FriendsBean friendsBEAN, String token)throws Exception{
    	LOGGER.info(":: FindFriend Api DAO Call  ::");
    	AuthFilter authFilter = new AuthFilter();
    	String userNameFromToken = authFilter.getUserNameFromToken(token);
    	if(userNameFromToken==null){
    		throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "token null ");
    	}
    	ContactBasicInfoBean contactBasicInfoBean = mongoManager.getObjectByField(FabConstants.CONTACT_BASIC_INFO_COLLECTION, "userName", friendsBEAN.getUserName(), ContactBasicInfoBean.class);
		if(contactBasicInfoBean==null){
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "contactBasicInfo doesn't exist");
		}
    	UserBean friendNameFromDB=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", friendsBEAN.getUserName(), UserBean.class);
    	if(friendNameFromDB==null){ return null;}
    	FriendsBean friendsBean = new FriendsBean();
    	friendsBean.setUserName(userNameFromToken);
    	friendsBean.setFriend(friendNameFromDB.getUserName());
    	friendsBean = friendStatus(friendsBean);
    	friendsBean.setName(friendNameFromDB.getFirstName()+" "+friendNameFromDB.getLastName());
    	friendsBean.setId(null);
    	//friendsBean.setProfileImg(imageDAO.getProfileImageUrl(friendNameFromDB.getUserName()));
    	friendsBean.setPrivacy(null);
    	friendsBean.setFriends(countFriends(friendNameFromDB.getUserName()));
    	friendsBean.setCityTown(contactBasicInfoBean.getCityTown());
    	friendsBean.setCountry(contactBasicInfoBean.getCountry());
    	return friendsBean;
    }
	
    
    //Count of Friends based on UserName
    public long countFriends(String userName)throws Exception{
    	return  mongoManager.getCountByField(FabConstants.FRIENDS_COLLECTION, "userName", userName, FriendsBean.class);
    }
    
 // Get Profile Image PublicId
 	public String getProfilePublicId(String userName) throws Exception {
 		LOGGER.info(":: Get : getPublicId Method call  ::");
 		ImageBean image = imageDAO.getProfileImage(userName);
 		return image.getPublicId();
 	}
 	
    
    
    //Find Friends List
    public List<FriendsBean> listOfFriends(String userName)throws Exception{
    	LOGGER.info("::FriendDAO : Find Friends List Method Call  ::");
    	List<FriendsBean> friendsBeanList =  mongoManager.getObjectsByField(FabConstants.FRIENDS_COLLECTION, "userName", userName, FriendsBean.class);
    	UserBean usreNameFromUserBean=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", userName, UserBean.class);
		if(usreNameFromUserBean==null){
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "UserName doesn't exist");
		}
		
		
		
    	List<FriendsBean> list=new ArrayList<FriendsBean>();
    	for(FriendsBean friendsBean:friendsBeanList){
    		ContactBasicInfoBean contactBasicInfoBean = mongoManager.getObjectByField(FabConstants.CONTACT_BASIC_INFO_COLLECTION, "userName", friendsBean.getFriend(), ContactBasicInfoBean.class);
    		if(contactBasicInfoBean==null){
    			throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "contactBasicInfo doesn't exist");
    		}
    		friendsBean.setMutualfriends(null);
    			friendsBean.setUserName(null);
    			friendsBean.setId(null);
    			friendsBean.setFriends(countFriends(friendsBean.getFriend()));
    			friendsBean.setCityTown(contactBasicInfoBean.getCityTown());
    			friendsBean.setCountry(contactBasicInfoBean.getCountry());
    			list.add(friendsBean);
    		}
    		return list;
    	}
    
    
    
   
    
      //Fab Page 
    public FriendsBean fabPage(FriendsBean friendsBEAN)throws Exception{
    	LOGGER.info("::FriendDAO : fabPage Method Call  ::");
    	
		FriendsBean friendsBeanfromDB=mongoManager.findBy2Fields(FabConstants.FRIENDS_COLLECTION, "userName", friendsBEAN.getUserName(), "friend", friendsBEAN.getFriend(), FriendsBean.class);
    	String friendStatus="ON";
    	if(friendsBeanfromDB==null){ friendStatus = "OFF";	}
    	FriendsBean friendsBean=new FriendsBean();
    	friendsBean = friendStatus(friendsBean);
    	UserBean userBeanFromDB=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", friendsBEAN.getFriend(), UserBean.class);
    	if(userBeanFromDB==null){ return null;}
    	friendsBean.setUserName(userBeanFromDB.getUserName());
    	friendsBean.setName(userBeanFromDB.getFirstName()+" "+userBeanFromDB.getLastName());
    	friendsBean = friendStatus(friendsBean);
    	friendsBean.setPrivacy(null);
    	return friendsBean;
    }
    
    
    
    //Friend Status and  FriendRequest Status
    public FriendsBean friendStatus(FriendsBean friendsBean)throws Exception{
    	LOGGER.info("::FriendDAO : FindStatus Method Call  ::");
		FriendsBean friendsBeanfromDB=mongoManager.findBy2Fields(FabConstants.FRIENDS_COLLECTION, "userName", friendsBean.getUserName(), "friend", friendsBean.getFriend(), FriendsBean.class);
		String friendStatus=null;
		String friendRequestStatus=null;
		FriendsBean sentFriendsRequestBeanfromDB=mongoManager.findBy2Fields(FabConstants.SENT_FRIEND_REQUEST_COLLECTION, "userName", friendsBean.getUserName(), "friend", friendsBean.getFriend(), FriendsBean.class);
		FriendsBean friendsRequestBeanfromDB=mongoManager.findBy2Fields(FabConstants.FRIEND_REQUEST_COLLECTION, "userName", friendsBean.getUserName(), "friend", friendsBean.getFriend(), FriendsBean.class);
		FriendsBean result = new FriendsBean();
		
		if(friendsBeanfromDB==null){
			if(sentFriendsRequestBeanfromDB!=null){
				friendStatus = "OFF";
				friendRequestStatus = "ON";
			}else if(friendsRequestBeanfromDB!=null){
				friendStatus = "OFF";
				friendRequestStatus = "ON";
			}else{
				friendStatus = "OFF";
				friendRequestStatus = "OFF";
			}
		}else{
			friendStatus = "ON";
			friendRequestStatus = "OFF";
		}
		result.setFriendStatus(friendStatus);
		result.setFriendRequestStatus(friendRequestStatus);
		result.setPrivacy(null);
		result.setFriends(0);
		return result;
    }
    
    
    // UnFriend
    public ResponseBean unFriend(FriendsBean friendsBean)throws Exception{
    	LOGGER.info("::FriendDAO : UnFriend Method Call  ::");
    	ResponseBean responseBean=new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		mongoManager.deleteBy2Field(FabConstants.FRIENDS_COLLECTION, "userName", friendsBean.getUserName(), "friend", friendsBean.getFriend());
		mongoManager.deleteBy2Field(FabConstants.FRIENDS_COLLECTION, "userName", friendsBean.getFriend(), "friend",  friendsBean.getUserName());
    	return responseBean;
    	}
    
	public ResponseBean unFollow(FriendsBean friendsBean) throws Exception {
		LOGGER.info("::FriendDAO : UnFollow Method Call  ::");
		ResponseBean responseBean = new ResponseBean(
				StatusCodes.SUCCESS_MESSAGE);;
		FriendsBean friendsBeanfromDB = mongoManager.findBy2Fields(
				FabConstants.FRIENDS_COLLECTION, "userName",
				friendsBean.getUserName(), "friend", friendsBean.getFriend(),
				FriendsBean.class);
		if (friendsBeanfromDB != null) {
			friendsBeanfromDB.setUnFollow(friendsBean.getUnFollow());
			mongoManager.update(FabConstants.FRIENDS_COLLECTION,
					friendsBeanfromDB);
			postDAO.UpdatePostSend(friendsBean.getUserName(),friendsBean.getFriend(), friendsBean.getUnFollow());
			
			
		} else {
			postDAO.UpdatePostSend(friendsBean.getUserName(),friendsBean.getFriend(), friendsBean.getUnFollow());
		}
		return responseBean;
	}
    
    
    //Suggested Friends
    public List<FriendsBean> suggestedFriends(String userName)throws Exception{
    	LOGGER.info("::FriendDAO : Suggested Method Call  ::");
    	List<FriendsBean> result = new ArrayList<FriendsBean>();
    	Set<String> set1 = new TreeSet<String>();
    	
    	List<UserBean> userBeanList = mongoManager.getAllObjects(FabConstants.USER_COLLECTION, UserBean.class);
    	for(UserBean userbean:userBeanList){
    		set1.add(userbean.getUserName());
    	}
    	System.out.println(" Set1 : "+set1);
    	
    	//from given userName - > friends
    	Set<String> set2 = new HashSet<String>();
    	List<FriendsBean> friendsList =mongoManager.getObjectsByField(FabConstants.FRIENDS_COLLECTION, "userName", userName, FriendsBean.class);
    	for(FriendsBean friendsbean:friendsList){
    		set2.add(friendsbean.getFriend());
    	}
    	
    	
    	//from friendsRequest list
    	Set<String> set3 = new HashSet<String>();
    	List<FriendsBean> friendsRequestList =findSentFriendRequest(userName);
    	for(FriendsBean sentFriendRequest:friendsRequestList){
    		set3.add(sentFriendRequest.getFriend());
    	}
    	
    	System.out.println(" Set3 : "+set3);
    	
    	Set<String> commonFriends = new HashSet<String>();
    	commonFriends.addAll(set1);
    	commonFriends.retainAll(set2);
    	set1.removeAll(commonFriends);// returns remaining values after deleting duplicates
    	
    	set1.remove(userName);		
    	
    	set1.removeAll(set3);
    	
    	set2.removeAll(commonFriends);//returns null  and commonFriend returns duplicate values
    	
    	for(String commonfriend:set1){
        	UserBean userBeanFromDB=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", commonfriend, UserBean.class);
        	ContactBasicInfoBean contactBasicInfoBean = mongoManager.getObjectByField(FabConstants.CONTACT_BASIC_INFO_COLLECTION, "userName", commonfriend, ContactBasicInfoBean.class);
        	if(contactBasicInfoBean!=null){
        		
        	result.add(new FriendsBean(null,userBeanFromDB.getUserName(),userBeanFromDB.getFirstName()+" "+userBeanFromDB.getLastName(),"public",null,null,null,contactBasicInfoBean.getCountry(),contactBasicInfoBean.getCityTown(),countFriends(userBeanFromDB.getUserName()),null));
        	}
    		}
    		return result;
    	}

    
    
    
    
    
    
    
    //Mutual Friends  
    public List<FriendsBean> mutualFriends(FriendsBean friendsBean)throws Exception{
    	LOGGER.info("::FriendDAO : Mutual Friend Method Call  ::");
    	List<FriendsBean> mutualFriends = new ArrayList<FriendsBean>();
    	
    	//It gives Friends list based on userName
    	Set<String> set1 = new HashSet<String>();
    	List<FriendsBean> friendsListByUserName =mongoManager.getObjectsByField(FabConstants.FRIENDS_COLLECTION, "userName", friendsBean.getUserName(), FriendsBean.class);
    	for(FriendsBean friendNameByUserName:friendsListByUserName){
    		set1.add(friendNameByUserName.getFriend());
    	}
    	LOGGER.info("::FriendDAO : Mutual Friend Method Call  ::"+set1);
    	//It gives Friends list based on friendName
    	Set<String> set2 = new HashSet<String>();
    	List<FriendsBean> friendsListByFriendName =mongoManager.getObjectsByField(FabConstants.FRIENDS_COLLECTION, "userName", friendsBean.getFriend(), FriendsBean.class);
    	for(FriendsBean friendNameByFriendsName:friendsListByFriendName){
    		set2.add(friendNameByFriendsName.getFriend());
    	}
    	LOGGER.info("::FriendDAO : Mutual Friend Method Call  ::"+set2);
    	Set<String> commonFriends = new HashSet<String>();
    	commonFriends.addAll(set1);
    	commonFriends.retainAll(set2);
    	set1.removeAll(commonFriends);
    	set2.removeAll(commonFriends);
    	
    	for(String mutualfriend:commonFriends){
    		String pubId = getProfilePublicId(mutualfriend);
        	UserBean userBeanFromDB=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", mutualfriend, UserBean.class);
        	ContactBasicInfoBean contactBasicInfoBean = mongoManager.getObjectByField(FabConstants.CONTACT_BASIC_INFO_COLLECTION, "userName", mutualfriend, ContactBasicInfoBean.class);
    		mutualFriends.add((new FriendsBean(null, userBeanFromDB.getUserName(),userBeanFromDB.getFirstName()+" "+userBeanFromDB.getLastName(),"public",null,null,null,
    				contactBasicInfoBean.getCountry(),contactBasicInfoBean.getCityTown(),countFriends(userBeanFromDB.getUserName()),pubId)));
    	}
    		return mutualFriends;
		}
    
  
    
    
    //Count of Mutual Friends
    public FriendsBean coutOfMutualFriends(FriendsBean friendsBean)throws Exception{
    	LOGGER.info("::FriendDAO : Mutual Friend Method Call  ::");
    	//List<FriendsBean> mutualFriends = new ArrayList<FriendsBean>();
    	FriendsBean friendsBEAN = new FriendsBean();
    	friendsBEAN.setMutualfriends(mutualFriends(friendsBean).size());
    	friendsBEAN.setFriend(null);
    	friendsBEAN.setUserName(null);
    	friendsBEAN.setName(null);
    	friendsBEAN.setFriendStatus(null);
    	friendsBEAN.setPrivacy(null);
    	
    	return friendsBEAN;
	}
    
	
    //Find Sent Friend Request
    public List<FriendsBean> findSentFriendRequest(String userName)throws Exception{
    	LOGGER.info("::FriendDAO : Find SentFriend Request Method Call  ::");
    	UserBean usreNameFromUserBean=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", userName, UserBean.class);
		if(usreNameFromUserBean==null){
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "UserName doesn't exist");
		}
    	//FriendsBean friendsBean=new FriendsBean();
    	List<FriendsBean> friendsBeanList =  mongoManager.getAllObjects(FabConstants.SENT_FRIEND_REQUEST_COLLECTION, FriendsBean.class);
    	List<FriendsBean> list=new ArrayList<FriendsBean>();
    	for(FriendsBean friendsBean:friendsBeanList){
    		if(friendsBean.getUserName().equals(userName)){
    			friendsBean.setUserName(null);
    			friendsBean.setId(null);
    			list.add(friendsBean);
    			}
    		}
    		return list;
    	}
    
    
    
    
   
    
  //mutual count in Integer
    public Integer mutualCount(FriendsBean friendsBean) throws Exception {
    	List<FriendsBean> mutualFriends = mutualFriends(friendsBean);
    	return mutualFriends.size();
    	}
    
    
    //FabWall api
    public FabWallBean fabWall(String userName) throws Exception{
    	LOGGER.info("::FriendDAO : FabWall Method Call  ::");
 	   UserBean usreNameFromUserBean=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", userName, UserBean.class);
		if(usreNameFromUserBean==null){
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "UserName doesn't exist");
		}
		FabWallBean fabwall=new FabWallBean();
		
		long circlesCount = mongoManager.getCountByField(FabConstants.CIRCLE_MEMBER_COLLECTION, "userName", userName, CircleBean.class);

			fabwall.setFriendsCount(countFriends(userName));
			fabwall.setPhotos(0);
			fabwall.setVideos(0);
			fabwall.setCircles(circlesCount);
    			return fabwall;
    		}

    
    
    //Count Friends
  	public FabWallBean countOfFriends(String userName ) throws Exception {
      	LOGGER.info("::FriendDAO : Count Friends Method Call  ::");
  		FabWallBean fabwall=new FabWallBean();
  		UserBean usreNameFromUserBean=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", userName, UserBean.class);
  		if(usreNameFromUserBean==null){
  			throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "UserName doesn't exist");
  		}
  			fabwall.setFriendsCount(countFriends(userName));
      			return fabwall;
  			}

  	
  
	//Count FriendsRequests
	public FabWallBean countOfFriendsRequests(String userName ) throws Exception {
	    	LOGGER.info("::FriendDAO : Count FriendsRequests Method Call  ::");
			FabWallBean fabwall=new FabWallBean();
			UserBean usreNameFromUserBean=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", userName, UserBean.class);
			if(usreNameFromUserBean==null){
				throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "UserName doesn't exist");
				}
			long friendsRequestsCount =  mongoManager.getCountByField(FabConstants.FRIEND_REQUEST_COLLECTION, "userName", userName, FriendsBean.class);
			
				fabwall.setFriendsRequest(friendsRequestsCount);
	    		return fabwall;
			}
	    
		//Find Friend Request
	    public List<FriendsBean> findFriendRequest(String userName)throws Exception{
	    	LOGGER.info("::FriendDAO : Find Friend Request Method Call  ::");
	    	List<FriendsBean> friendsBeanList =  mongoManager.getObjectsByField(FabConstants.FRIEND_REQUEST_COLLECTION, "userName", userName, FriendsBean.class);
	    	
	    	List<FriendsBean> list=new ArrayList<FriendsBean>();
	    	for(FriendsBean friendsBean:friendsBeanList){
	    		
	    		if(friendsBean.getUserName().equals(userName)){
	    	    	UserBean userBeanFromDB=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", friendsBean.getFriend(), UserBean.class);

	    			//friendsBean.setUserName(null);
	    			//friendsBean.setFriend(null);
	    			friendsBean.setId(null);
	    			friendsBean.setPrivacy(null);
	    			friendsBean.setName(userBeanFromDB.getFirstName()+" "+userBeanFromDB.getLastName());
	    			//friendsBean.setProfileImg(imageDAO.getProfileImage(userBeanFromDB.getUserName()));
	    			list.add(friendsBean);
	    			}
	    		}
	    		return list	;
	    	}
	    
	  
    	//Confirm Friend
    	public ResponseBean confirm(FriendsBean friendsBean ) throws Exception {
        	LOGGER.info("::FriendDAO : Confirm Method Call  ::");
    		ResponseBean response=new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
    		FriendsBean reverse=new FriendsBean();
        	reverse.setUserName(friendsBean.getFriend());
    		reverse.setFriend(friendsBean.getUserName());
    		
    		// UserDAO 
        	UserBean userBeanFromDB=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", friendsBean.getUserName(), UserBean.class);
        	UserBean friendUserBeanFromDB=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", friendsBean.getFriend(), UserBean.class);
        	FriendsBean FriendsBeanNew=new FriendsBean();
        	FriendsBeanNew.setUserName(friendsBean.getUserName());
        	FriendsBeanNew.setFriend(friendUserBeanFromDB.getUserName());
        	FriendsBeanNew.setName(friendUserBeanFromDB.getFirstName()+" "+friendUserBeanFromDB.getLastName());
        	reverse.setName(userBeanFromDB.getFirstName()+" "+userBeanFromDB.getLastName());
    		
    		FriendsBean friendsBeanFROMDB=mongoManager.findBy2Fields(FabConstants.FRIENDS_COLLECTION, "userName", friendsBean.getUserName(),"friend",friendsBean.getFriend(), FriendsBean.class);
    		if(friendsBeanFROMDB==null){
    			mongoManager.insert(FabConstants.FRIENDS_COLLECTION, FriendsBeanNew);
    			mongoManager.insert(FabConstants.FRIENDS_COLLECTION, reverse);
    		}else{
    			throw new FabException(StatusCodes.FRIEND_REQUEST_EXIST, " You Already added As A Friend ");
    			}
    		
    			return response;
    		}
    
    	
    	//Add Friend
    	public ResponseBean addFriendRequest(FriendsBean friendsBean) throws Exception{
        	LOGGER.info("::FriendDAO : AddFriend Method Call  ::");
        	ResponseBean response=new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
        	FriendsBean reverse=new FriendsBean();
        	reverse.setUserName(friendsBean.getFriend());
    		reverse.setFriend(friendsBean.getUserName());
    		
			FriendsBean friendsBeanFromFriendsCollections=mongoManager.findBy2Fields(FabConstants.FRIENDS_COLLECTION, "userName", friendsBean.getUserName(), "friend", friendsBean.getFriend(), FriendsBean.class);
    		
    		// UserDAO 
    		UserBean usreNameFromDB=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", friendsBean.getUserName(), UserBean.class);
        	UserBean friendNameFromDB=mongoManager.getObjectByField(FabConstants.USER_COLLECTION, "userName", friendsBean.getFriend(), UserBean.class);
    	
			FriendsBean sentFriendsRequestBeanFromDB=mongoManager.findBy2Fields(FabConstants.SENT_FRIEND_REQUEST_COLLECTION, "userName", friendsBean.getUserName(), "friend", friendsBean.getFriend(), FriendsBean.class);
			FriendsBean friendsRequestBeanFromDB=mongoManager.findBy2Fields(FabConstants.FRIEND_REQUEST_COLLECTION, "userName", reverse.getUserName(), "friend", reverse.getFriend(), FriendsBean.class);
		
			if(usreNameFromDB!=null){
				if(friendNameFromDB!=null){
					
					if(friendsBeanFromFriendsCollections==null){
						if(sentFriendsRequestBeanFromDB==null && friendsRequestBeanFromDB==null){
							
							mongoManager.insert(FabConstants.SENT_FRIEND_REQUEST_COLLECTION, friendsBean);
			    		    mongoManager.insert(FabConstants.FRIEND_REQUEST_COLLECTION,reverse );
						}else{
							throw new FabException(StatusCodes.FRIEND_REQUEST_EXIST, " Already Friend Request sent ");
						}
					}else{
						throw new FabException(StatusCodes.FRIEND_REQUEST_EXIST, " Already Added as a Friend ");
					}
					
				}else{
					throw new FabException(StatusCodes.USER_ID_NOT_EXIST, " Friend's UserName doesn't exist ");
				}
			}else{
				throw new FabException(StatusCodes.USER_ID_NOT_EXIST, "UserName doesn't exist ");
			}
    		return response;
        }
    	
    	
    
       //DELETE Friend Request 
       public ResponseBean deleteFriendRequest(FriendsBean friendsBean)throws Exception{
    	LOGGER.info(":: Delete Request FriendRequst Api DAO Call  ::");
    	ResponseBean responseBean=new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
    	// SentFriendRequest
    	FriendsBean sentFriendRequestByUserNameFromDB=mongoManager.findBy2Fields(FabConstants.SENT_FRIEND_REQUEST_COLLECTION,"userName", friendsBean.getUserName(),"friend",friendsBean.getFriend(), FriendsBean.class);
    	//AddFriendRequest
    	FriendsBean getFriendRequestByUserNameFromDB=mongoManager.findBy2Fields(FabConstants.FRIEND_REQUEST_COLLECTION, "userName", friendsBean.getFriend(),"friend",friendsBean.getUserName(), FriendsBean.class);
    	if(sentFriendRequestByUserNameFromDB!=null && getFriendRequestByUserNameFromDB!=null){
    		mongoManager.deleteBy2Field(FabConstants.SENT_FRIEND_REQUEST_COLLECTION, "userName", friendsBean.getUserName(), "friend", friendsBean.getFriend());
    		mongoManager.deleteBy2Field(FabConstants.FRIEND_REQUEST_COLLECTION, "userName", friendsBean.getFriend(), "friend", friendsBean.getUserName());
    	}else{
    		throw new FabException(StatusCodes.FRIEND_REQUEST_NOT_EXIST, "FriendReqiest Doesn't Exist ");
    				}
				return responseBean;
			}
    

       
       //Search Api Start  :::::::::::::::::::::::::::::
       public List<SearchBean> searchFriendByKeywork( String keyword)throws Exception{
           LOGGER.info(":: Friends DAO Api : Search Friend Method Call  ::");
           List<SearchBean> friendsList=new ArrayList<SearchBean>();
           List<UserBean> searchList = mongoManager.searchLikeQuery(keyword);
           
           
        for(UserBean user:searchList){
            if(user!=null){
            	SearchBean searchBean = new SearchBean();
            	searchBean.setName(user.getFirstName()+" "+user.getLastName());
            	searchBean.setUserName(user.getUserName());
            	
            	ImageBean image = getImage(user.getUserName());
            	searchBean.setPublicId(image.getPublicId());
            	
            	searchBean.setBeanType("profile");
            friendsList.add(searchBean);
            }
        }
        friendsList.addAll(searchCircleName(keyword));
             return friendsList;

       }
       public List<SearchBean> searchCircleName( String keyword)throws Exception{
        List<SearchBean> searchCirclesList=new ArrayList<SearchBean>();
           List<CircleBean> searchList = mongoManager.searchCircleName(keyword);
          
           for(CircleBean circleBean:searchList){
        	   if(searchList!=null){
        		   SearchBean searchBean = new SearchBean();
        		   searchBean.setUserName(circleBean.getUserName());
        		   long circleMemberCount = mongoManager.getCountByField(FabConstants.CIRCLE_MEMBER_COLLECTION, "circleId", circleBean.getCircleId(), CircleMemberBean.class);
        		   circleBean.setMembers(circleMemberCount);
        		   searchBean.setMembers(circleMemberCount);
        		   searchBean.setCircleName(circleBean.getCircleName());
        		   searchBean.setIcon(circleBean.getIcon());
        		   ImageBean image = getImage(circleBean.getUserName());
               	searchBean.setPublicId(image.getPublicId());
        		   searchBean.setBeanType("circle");
        		   searchCirclesList.add(searchBean);   
        	   }
           }
             return searchCirclesList;
       }
       // Search Api End ::::::::::::::::::::::::::::::::::::
       
       
       
     //Get Image publicId
       public ImageBean getImage(String userName) throws Exception{
    	   ImageBean image =new ImageBean();
   		image.setUserName(userName);
   		image.setPublicId(" publicId Not Found/Please upload Profile Picture ");
    	 ImageBean imageBeanFromDB = mongoManager.getObjectByField(FabConstants.IMAGE_COLLECTION, "userName", userName, ImageBean.class);
   			if( imageBeanFromDB == null   ){
   			imageBeanFromDB = image;
   		}
   		return imageBeanFromDB;
   		
       }     
       
}

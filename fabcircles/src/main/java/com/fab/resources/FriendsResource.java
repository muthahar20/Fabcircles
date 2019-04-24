package com.fab.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fab.constants.StatusCodes;
import com.fab.dao.CircleDAO;
import com.fab.dao.FriendsDAO;
import com.fab.exception.FabException;
import com.fab.model.FabWallBean;
import com.fab.model.MyFrndsCrclsResponse;
import com.fab.model.ResponseBean;
import com.fab.model.user.FriendsBean;
import com.fab.model.user.SearchBean;



@Path("friend")
@Produces(APPLICATION_JSON) 
public class FriendsResource extends BaseResource{

	private static final Logger LOGGER = LoggerFactory.getLogger(FriendsResource.class);
	
	@Autowired
	FriendsDAO friendsDAO;
	@Autowired
	CircleDAO circleDAO;
	
	
	@POST
	@Path("/addfriend")
	@Produces(APPLICATION_JSON) 
	public ResponseBean addSentFriendRequest(FriendsBean friendsBean) throws Exception{
		LOGGER.info("SentFriendRequestResource POST call ::");
		ResponseBean response=null;
		try{
			response=friendsDAO.addFriendRequest(friendsBean);
			
		}catch(FabException fe){
			LOGGER.error("Error SentFriendRequestResource ",fe);
			buildErrorResponse(Response.Status.CONFLICT	, fe.getErrorCode(), fe.getMessage());
		}
		return response;
    }
	
	
	@POST
	@Path("/confirm")
	 @Consumes(APPLICATION_JSON)
	public ResponseBean confirmFriendRequest(FriendsBean friendsBean) throws Exception{
		LOGGER.info("FriendsResource API  POST Call ::");
		ResponseBean response=null;
		try{
			response=friendsDAO.deleteFriendRequest(friendsBean);
			if(response.getMessage().equals(StatusCodes.SUCCESS_MESSAGE)){
				response=friendsDAO.confirm(friendsBean);
			}
			
		}catch(FabException fe){
			LOGGER.error("Error FriendsResource ",fe);
			buildErrorResponse(Response.Status.CONFLICT	, fe.getErrorCode(), fe.getMessage());
		}
		return response;
	}
	
	
	
	
	@DELETE
	@Path("/deleterequest")
	 @Consumes(APPLICATION_JSON)
	public ResponseBean deleteRequest(FriendsBean friendsBean) throws Exception{
		LOGGER.info("FriendsResource API  deleteRequest DELETE Call ::");
		ResponseBean response=null;
		try{
			
			response=friendsDAO.deleteFriendRequest(friendsBean);
		}catch(FabException fe){
			LOGGER.error("Error SentFriendRequestResource ",fe);
			buildErrorResponse(Response.Status.CONFLICT	, fe.getErrorCode(), fe.getMessage());
		}
		return response;
   }
	
	
	@DELETE
	@Path("/unfriend")
	 @Consumes(APPLICATION_JSON)
	
	public ResponseBean unFriend(FriendsBean friendsBean) throws Exception{
		
		LOGGER.info("FriendsResource API  unfriend DELETE Call ::");
		ResponseBean response=null;
		try{
			response=friendsDAO.unFriend(friendsBean);
		}catch(FabException fe){
			LOGGER.error("Error SentFriendRequestResource ",fe);
			buildErrorResponse(Response.Status.CONFLICT	, fe.getErrorCode(), fe.getMessage());
		}
		return response;
   }
	@PUT
	@Path("/unfollow")
	 @Consumes(APPLICATION_JSON)
	
	public ResponseBean unFollow(FriendsBean friendsBean) throws Exception{
		
		LOGGER.info("FriendsResource API  unfollow Call ::");
		ResponseBean response=null;
		try{
			response=friendsDAO.unFollow(friendsBean);
		}catch(FabException fe){
			LOGGER.error("Error unFollow ",fe);
			buildErrorResponse(Response.Status.CONFLICT	, fe.getErrorCode(), fe.getMessage());
		}
		return response;
   }
	
	
	
		@POST
	    @Path("/mutualfriends")
	    @Produces(APPLICATION_JSON)
	    public List<FriendsBean> mutualFriends(FriendsBean friendsBean) throws Exception{
		 List<FriendsBean> friendsBean1 = null;
	    	try{
	    		friendsBean1 = friendsDAO.mutualFriends(friendsBean);
	    	if(friendsBean == null){
	    		buildErrorResponse(Response.Status.CONFLICT, StatusCodes.USER_ID_NOT_EXIST, "UserId doesn't exist");
	    	}
	    	}catch(FabException fe){
	    		LOGGER.error("findByUserName error ",fe);
	    		buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
	    	}
	    	return friendsBean1;
	    }
	
	

		@GET
	    @Path("/suggestedfriends")
	    @Produces(APPLICATION_JSON)
	    public List<FriendsBean> suggestedFriends(@QueryParam ("userName")String userName) throws Exception{
		 List<FriendsBean> friendsBean = null;
	    	try{
	    		friendsBean = friendsDAO.suggestedFriends(userName);
	    	if(friendsBean == null){
	    		buildErrorResponse(Response.Status.CONFLICT, StatusCodes.USER_ID_NOT_EXIST, "UserId doesn't exist");
	    	}
	    	}catch(FabException fe){
	    		LOGGER.error("findByUserName error ",fe);
	    		buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
	    	}
	    	return friendsBean;
	    }
	 
		@POST
	    @Path("/find")  
	    @Produces(APPLICATION_JSON)
	    public FriendsBean findByUserName(@HeaderParam("token") String token, FriendsBean friendsBEAN) throws Exception{
		 FriendsBean friendsBean = null;
	    	try{
	    		friendsBean = friendsDAO.findFriend(friendsBEAN,token);
	    	if(friendsBean == null){
	    		buildErrorResponse(Response.Status.NOT_FOUND, StatusCodes.USER_ID_NOT_EXIST, "UserName doesn't Registered");
	    	}
	    	}catch(FabException fe){
	    		LOGGER.error("findByUserName error ",fe);
	    		buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
	    	}
	    	return friendsBean;
	    }
		
	 	@POST
	    @Path("/friendstatus")  
	    @Produces(APPLICATION_JSON)
	    public FriendsBean friendStatus(FriendsBean friendsBEAN) throws Exception{
		 FriendsBean friendsBean = null;
	    	try{
	    		friendsBean = friendsDAO.friendStatus(friendsBEAN);
	    	if(friendsBean == null){
	    		buildErrorResponse(Response.Status.NOT_FOUND, StatusCodes.USER_ID_NOT_EXIST, "UserName doesn't Registered");
	    	}
	    	}catch(FabException fe){
	    		LOGGER.error("findByUserName error ",fe);
	    		buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
	    	}
	    	return friendsBean;
	    }
	
	 
	 	@GET
	    @Path("/sentfriendsrequest")
	    @Produces(APPLICATION_JSON)
	    public List<FriendsBean> findSentFriendRequest(@QueryParam ("userName") String userName) throws Exception{
	 		List<FriendsBean> friendsBean = null;
	    	try{
	    		friendsBean = friendsDAO.findSentFriendRequest(userName);
	    	if(friendsBean.isEmpty()){
	    		buildErrorResponse(Response.Status.NOT_FOUND, StatusCodes.FRIEND_REQUEST_NOT_EXIST, "Sent Friend Request doesn't exist");
	    	}
	    	}catch(FabException fe){
	    		LOGGER.error("findByUserName error ",fe);
	    		buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
	    	}
	    	return friendsBean;
	    }
	 
	 
	 
	 
	 	@GET
	    @Path("/friendsrequest")
	    @Produces(APPLICATION_JSON)
	    public List<FriendsBean> findFriendRequest(@QueryParam ("userName") String userName) throws Exception{
	 		List<FriendsBean> friendsBean = null;
	    	try{
	    		friendsBean = friendsDAO.findFriendRequest(userName);
	    	if(friendsBean.isEmpty()){
	    		buildErrorResponse(Response.Status.NOT_FOUND, StatusCodes.FRIEND_REQUEST_NOT_EXIST, "Friend Request doesn't exist");
	    	}
	    	}catch(FabException fe){
	    		LOGGER.error("findByUserName error ",fe);
	    		buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
	    	}
	    	return friendsBean;
	    }
	 
	 	@GET
	    @Path("/friends")
	    @Produces(APPLICATION_JSON)
	    public List<FriendsBean> listofFriends(@QueryParam ("userName") String userName) throws Exception{
	 		List<FriendsBean> friendsBean = null;
	    	try{
	    		friendsBean = friendsDAO.listOfFriends(userName);
	    	if(friendsBean.isEmpty()){
	    		buildErrorResponse(Response.Status.NOT_FOUND, StatusCodes.FRIEND_REQUEST_NOT_EXIST, "Friends doesn't exist");
	    	}
	    	}catch(FabException fe){
	    		LOGGER.error("findByUserName error ",fe);
	    		buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
	    	}
	    	return friendsBean;
	    }
	 
	 	@GET
	    @Path("/friendscount")
	    @Produces(APPLICATION_JSON)
	    public FabWallBean countOfFriends(@QueryParam ("userName") String userName) throws Exception{
	 		FabWallBean fabwall = null;
	    	try{
	    		fabwall = friendsDAO.countOfFriends(userName);
	    		
	    	}catch(FabException fe){
	    		LOGGER.error("findByUserName error ",fe);
	    		buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
	    	}
	    	return fabwall;
	    }
	 
	 	
	 	@GET
	    @Path("/friendsrequestcount")
	    @Produces(APPLICATION_JSON)
	    public FabWallBean countOfFriendsRequests(@QueryParam ("userName") String userName) throws Exception{
	 		FabWallBean fabwall = null;
	    	try{
	    		fabwall = friendsDAO.countOfFriendsRequests(userName);
	    		
	    	}catch(FabException fe){
	    		LOGGER.error("findByUserName error ",fe);
	    		buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
	    	}
	    	return fabwall;
	    }
	 
	 
	 	
	 	@GET
	    @Path("/fabwall")
	    @Produces(APPLICATION_JSON)
	    public FabWallBean fabWall(@QueryParam ("userName") String userName) throws Exception{
	 		FabWallBean fabwall = null;
	    	try{
	    		fabwall = friendsDAO.fabWall(userName);
	    		
	    	}catch(FabException fe){
	    		LOGGER.error("findByUserName error ",fe);
	    		buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
	    	}
	    	return fabwall;
	    }
	 
	 	  @GET
	        @Path("/search")   
	        @Produces(APPLICATION_JSON)
	        public List<SearchBean> searchByUserName(@QueryParam ("keyword") String keyword) throws Exception{
	             List<SearchBean> friendsBean = null;
	            try{
	                friendsBean = friendsDAO.searchFriendByKeywork(keyword);

	            if(friendsBean.size()==0){

	                buildErrorResponse(Response.Status.NOT_FOUND, StatusCodes.USER_ID_NOT_EXIST, " Search Result Not Found");
	            }
	            }catch(FabException fe){
	                LOGGER.error("findByUserName error ",fe);
	                buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
	            }
	            return friendsBean;
	        }
	 	  
	 	  
	 	  
	 	 @GET
	        @Path("/searchcircle")   
	        @Produces(APPLICATION_JSON)
	        public List<SearchBean> searchCircle(@QueryParam ("keyword") String keyword) throws Exception{
	             List<SearchBean> friendsBean = null;
	            try{
	                friendsBean = friendsDAO.searchCircleName(keyword);

	            if(friendsBean.size()==0){

	                buildErrorResponse(Response.Status.NOT_FOUND, StatusCodes.USER_ID_NOT_EXIST, " Search Result Not Found");
	            }
	            }catch(FabException fe){
	                LOGGER.error("findByUserName error ",fe);
	                buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
	            }
	            return friendsBean;
	        }

	 
	 	 @GET
	        @Path("/searchfrndscircles")   
	        @Produces(APPLICATION_JSON)
	        public MyFrndsCrclsResponse searchFriendsAndCircles(@QueryParam ("userName") String userName) throws Exception{
	 		MyFrndsCrclsResponse searchResponse = new MyFrndsCrclsResponse();
	            try{
	            	searchResponse.setMyFriends(friendsDAO.listOfFriends(userName));
	            	searchResponse.setMyCircles(circleDAO.getCircles(userName));
	            
	            }catch(FabException fe){
	                LOGGER.error("findByUserName error ",fe);
	                buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
	            }
	            return searchResponse;
	        }
}

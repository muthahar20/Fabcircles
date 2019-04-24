package com.fab.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.fab.dao.CircleDAO;
import com.fab.exception.FabException;
import com.fab.model.CircleMemberBean;
import com.fab.model.ResponseBean;
import com.fab.model.CircleBean;
import com.fab.model.user.FriendsBean;
import com.fab.resources.BaseResource;


@Path("circle")
@Produces(APPLICATION_JSON) 
public class CircleResource extends BaseResource{
	private static final Logger LOGGER = LoggerFactory.getLogger(CircleResource.class);
	@Autowired
	CircleDAO circleDAO;
	
	@POST
	@Consumes(APPLICATION_JSON)
	//Accept CircleBean as a RequestBody
	public CircleBean createCircle(CircleBean circleBean) throws Exception{
		LOGGER.info("createCircle POST call ::");
		CircleBean response=null;
		try{
			response=circleDAO.createCircle(circleBean);
		}catch(FabException fe){
			LOGGER.error("Error Circle API POST ",fe);
			buildErrorResponse(Response.Status.CONFLICT	, fe.getErrorCode(), fe.getMessage());
		}
		return response;
	}
	


 	@GET
 	@Path("/list")
    @Produces(APPLICATION_JSON)
    public List<CircleBean>  getCircles(@QueryParam ("userName") String userName) throws Exception{
 		List <CircleBean> circles  = null;
    	try{
    		 circles = circleDAO.getCircles(userName);

    	}catch(FabException fe){
    		LOGGER.error("getCircles error ",fe);
    		buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
    	}
    	return circles;
    }	

 	
 	@GET
 	@Path("/id")
    @Produces(APPLICATION_JSON)
    public CircleBean  getCirclesById(@QueryParam ("circleId") String circleId) throws Exception{
 		CircleBean circles  = null;
    	try{
    		 circles = circleDAO.getCircleById(circleId);
    	

    	}catch(FabException fe){
    		LOGGER.error("getCirclesbyid error ",fe);
    		buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
    	}
    	return circles;
    }	
	
 	
 	/*
 	
 	@GET
 	@Path("/mutualcircles")
    @Produces(APPLICATION_JSON)
    public List<CircleBean>    getMutualCircles(@QueryParam ("userName") String userName) throws Exception{
 		List<CircleBean>   mutualCircles  = null;
    	try{
    		mutualCircles = circleDAO.mutualCircles(userName);
    	}catch(FabException fe){
    		LOGGER.error("getCirclesbyid error ",fe);
    		buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
    	}
    	return mutualCircles;
    }	
*/ 	

 	
 	@GET
	@Path("/localcircles")
	@Produces(APPLICATION_JSON)
	public Set<CircleBean> localCircle(@QueryParam ("userName") String userName,  @QueryParam ("listSize") Integer listSize) throws Exception{
		LOGGER.info("add member POST call ::");
		Set<CircleBean> circleBean = new HashSet<CircleBean>();
		try{
			if(listSize==null ){
				circleBean=circleDAO.localCircle(userName);
				return circleBean;
			}
			circleBean=circleDAO.localCircleBySize(userName, listSize);
		}catch(FabException fe){
			LOGGER.error("Error Circle API POST ",fe);
			buildErrorResponse(fe.getStatus(), fe.getErrorCode(), fe.getMessage());
		}
		return circleBean;
	}
 	
 	
 	
 	
 	/*
	@DELETE
	 @Consumes(APPLICATION_JSON)
	public ResponseBean deleteCircle(@QueryParam("circleId") String circleId,@QueryParam ("userName") String userName ) throws Exception{
		LOGGER.info(" deleteCircle api Call ::");
		ResponseBean response=null;
		try{
			
			response=circleDAO.deleteCircle(circleId, userName);
		}catch(FabException fe){
			LOGGER.error("Error SentFriendRequestResource ",fe);
			buildErrorResponse(Response.Status.CONFLICT	, fe.getErrorCode(), fe.getMessage());
		}
		return response;
   }
	*/
	
	 @PUT
	 @Consumes(APPLICATION_JSON)
	public CircleBean updateCircle(CircleBean circleBean) throws Exception{
		LOGGER.info(" updateCircle  api Call ::");
		CircleBean circleBEAN=null;
		try{
			circleBEAN=circleDAO.updateCircle(circleBean);
		}catch(FabException fe){
			LOGGER.error("Error SentFriendRequestResource ",fe);
			buildErrorResponse(Response.Status.CONFLICT	, fe.getErrorCode(), fe.getMessage());
		}
		return circleBEAN;
  }
	
	
	 @PUT
	 @Path("/updateprivacy")
	 @Consumes(APPLICATION_JSON)
	public CircleBean updatePrivacyStatus(CircleBean circleBean) throws Exception{
		LOGGER.info(" updatePrivacyStatus  api Call method ::");
		CircleBean circleBEAN=null;
		try{
			circleBEAN=circleDAO.updatePrivacyStatus(circleBean);
		}catch(FabException fe){
			LOGGER.error("Error SentFriendRequestResource ",fe);
			buildErrorResponse(Response.Status.CONFLICT	, fe.getErrorCode(), fe.getMessage());
		}
		return circleBEAN;
  }
	 
	
	@DELETE
	@Consumes(APPLICATION_JSON)
	public ResponseBean unfollowCircle(CircleMemberBean circleMemberBean ) throws Exception{
		System.out.println("1 unfollowCircle method called");
		LOGGER.info(" deleteCircle api Call ::");
		ResponseBean response=null;
		try{
			
			response=circleDAO.deleteCircle(circleMemberBean);
		}catch(FabException fe){
			LOGGER.error("Error SentFriendRequestResource ",fe);
			buildErrorResponse(Response.Status.CONFLICT	, fe.getErrorCode(), fe.getMessage());
		}
		return response;
  }
	
	

	@DELETE
	@Path("/member")
	@Consumes(APPLICATION_JSON)
	public ResponseBean deleteMemeber(CircleMemberBean circleMemberBean ) throws Exception{
		LOGGER.info(" delete Member api method Call ::");
		ResponseBean response=null;
		try{
			response=circleDAO.memberDelete(circleMemberBean);
		}catch(FabException fe){
			LOGGER.error("Error SentFriendRequestResource ",fe);
			buildErrorResponse(Response.Status.CONFLICT	, fe.getErrorCode(), fe.getMessage());
		}
		return response;
  }
	
	
	
	@POST
	@Path("/member")
	@Consumes(APPLICATION_JSON)
	public ResponseBean addMembers(CircleMemberBean circleMemberBean) throws Exception{
		LOGGER.info("add member POST call ::");
		ResponseBean response=null;
		try{
			response=circleDAO.addMembers(circleMemberBean);
		}catch(FabException fe){
			LOGGER.error("Error Circle API POST ",fe);
			buildErrorResponse(fe.getStatus(), fe.getErrorCode(), fe.getMessage());
		}
		return response;
	}
	
	
	@POST
	@Path("/accept")
	@Consumes(APPLICATION_JSON)
	public ResponseBean acceptRequest(CircleMemberBean circleMemberBean) throws Exception{
		LOGGER.info("add member POST call ::");
		ResponseBean response=null;
		try{
			response=circleDAO.acceptRequest(circleMemberBean);
		}catch(FabException fe){
			LOGGER.error("Error Circle API POST ",fe);
			buildErrorResponse(fe.getStatus(), fe.getErrorCode(), fe.getMessage());
		}
		return response;
	}
	
	
	@DELETE
	@Path("/deny")
	@Consumes(APPLICATION_JSON)
	public ResponseBean denyRequest(CircleMemberBean circleMemberBean) throws Exception{
		LOGGER.info("add member POST call ::");
		ResponseBean response=null;
		try{
			response=circleDAO.denyRequest(circleMemberBean);
		}catch(FabException fe){
			LOGGER.error("Error Circle API POST ",fe);
			buildErrorResponse(fe.getStatus(), fe.getErrorCode(), fe.getMessage());
		}
		return response;
	}
	
	
	
	@POST
	@Path("/joincircle")
	@Consumes(APPLICATION_JSON)
	public ResponseBean joinCircle(CircleMemberBean circleMemberBean) throws Exception{
		LOGGER.info("add member POST call ::");
		ResponseBean response=null;
		try{
			response=circleDAO.joinCircleRequest(circleMemberBean);
		}catch(FabException fe){
			LOGGER.error("Error Circle API POST ",fe);
			buildErrorResponse(fe.getStatus(), fe.getErrorCode(), fe.getMessage());
		}
		return response;
	}
	
	
	
	@GET
	@Path("/memberscount")
	@Consumes(APPLICATION_JSON)
	public CircleBean membersCount(@QueryParam ("circleId") String circleId) throws Exception{
		LOGGER.info("add member POST call ::");
		CircleBean circleBEAN = null;
		try{
			circleBEAN=circleDAO.circleMemberCount(circleId);
		}catch(FabException fe){
			LOGGER.error("Error Circle API POST ",fe);
			buildErrorResponse(fe.getStatus(), fe.getErrorCode(), fe.getMessage());
		}
		return circleBEAN;
	}
	
	
	
	@GET
	@Path("/suggested")
	@Consumes(APPLICATION_JSON)
	public Set<CircleBean> suggestedCircles(@QueryParam ("userName") String userName) throws Exception{
		LOGGER.info("add member POST call ::");
		Set<CircleBean> circleBean=null;
		try{
			circleBean=circleDAO.suggestedCircles(userName);
		}catch(FabException fe){
			LOGGER.error("Error Circle API POST ",fe);
			buildErrorResponse(fe.getStatus(), fe.getErrorCode(), fe.getMessage());
		}
		return circleBean;
	}
	
	
	
	@GET
	@Path("/mutualcircles")
	@Produces(APPLICATION_JSON)
	public Set<CircleBean> mutualFriendCircles(@QueryParam ("userName") String userName ) throws Exception{
		LOGGER.info("add member POST call ::");
		Set<CircleBean> circleBean=null;
		try{
			circleBean=circleDAO.mutualCircles(userName);
		}catch(FabException fe){
			LOGGER.error("Error Circle API POST ",fe);
			buildErrorResponse(fe.getStatus(), fe.getErrorCode(), fe.getMessage());
		}
		return circleBean;
	}
	
	
	
	
	@GET
	@Path("/newcircles")
	@Produces(APPLICATION_JSON)
	public Set<CircleBean> newCircles(@QueryParam ("userName") String userName,  @QueryParam ("listSize") Integer listSize) throws Exception{
		LOGGER.info("add member POST call ::");
		Set<CircleBean> circleBean=null;
		try{
			if(listSize==null){
				circleBean=circleDAO.newCircles(userName);
				return circleBean;
			}
			circleBean=circleDAO.newCirclesBySize(userName, listSize);
		}catch(FabException fe){
			LOGGER.error("Error Circle API POST ",fe);
			buildErrorResponse(fe.getStatus(), fe.getErrorCode(), fe.getMessage());
		}
		return circleBean;
	}
	
	
	@GET
	@Path("/mycircles")
	@Produces(APPLICATION_JSON)
	public List<CircleBean> myCircles(@QueryParam ("userName") String userName,  @QueryParam ("listSize") Integer listSize) throws Exception{
		LOGGER.info("add member POST call ::");
		List<CircleBean> circleBean=null;
		try{
			if(listSize==null){
				circleBean=circleDAO.myCircles(userName);
				return circleBean;
			}
			circleBean=circleDAO.myCirclesBySize(userName, listSize);
		}catch(FabException fe){
			LOGGER.error("Error Circle API POST ",fe);
			buildErrorResponse(fe.getStatus(), fe.getErrorCode(), fe.getMessage());
		}
		return circleBean;
	}
	
	@GET
	@Path("/friendcircles")
	@Produces(APPLICATION_JSON)
	public List<CircleBean> friendCircles(@QueryParam ("userName") String userName, @QueryParam ("listSize") Integer listSize) throws Exception{
		LOGGER.info("add member POST call ::");
		List<CircleBean> circleBean=null;
		try{
			if(listSize==null){
				circleBean=circleDAO.friendCircle(userName);
				return circleBean;
			}
			circleBean=circleDAO.friendCircleBySize(userName, listSize);
		}catch(FabException fe){
			LOGGER.error("Error Circle API POST ",fe);
			buildErrorResponse(fe.getStatus(), fe.getErrorCode(), fe.getMessage());
		}
		return circleBean;
	}
	
	
	
	
	@GET
	@Path("/familycircles")
	@Consumes(APPLICATION_JSON)
	public List<CircleBean> familyCircles(@QueryParam ("userName") String userName) throws Exception{
		LOGGER.info("add member POST call ::");
		List<CircleBean> circleBean=null;
		try{
			circleBean=circleDAO.familyCircles(userName);
		}catch(FabException fe){
			LOGGER.error("Error Circle API POST ",fe);
			buildErrorResponse(fe.getStatus(), fe.getErrorCode(), fe.getMessage());
		}
		return circleBean;
	}
	
	
	
	
	
	
	@GET
	@Path("/memberslist")
	@Produces(APPLICATION_JSON)
	public Set<FriendsBean> getMemberList(@QueryParam ("circleId") String circleId) throws Exception{
		LOGGER.info("add member POST call ::");
		Set<FriendsBean> circleMemberBeanList=null;
		try{
			circleMemberBeanList=circleDAO.getMembersList(circleId);
		}catch(FabException fe){
			LOGGER.error("Error Circle API POST ",fe);
			buildErrorResponse(fe.getStatus(), fe.getErrorCode(), fe.getMessage());
		}
		return circleMemberBeanList;
	}
	
	
	
	@GET
	@Path("/joincirclerequests")
	@Consumes(APPLICATION_JSON)
	public List<CircleBean> joinCircleRequest(@QueryParam ("userName") String userName) throws Exception{
		LOGGER.info("add member POST call ::");
		List<CircleBean> circleBean=null;
		try{
			circleBean=circleDAO.getJoinCircleRequest(userName);
		}catch(FabException fe){
			LOGGER.error("Error Circle API POST ",fe);
			buildErrorResponse(fe.getStatus(), fe.getErrorCode(), fe.getMessage());
		}
		return circleBean;
	}
	
	
	
}
	
	


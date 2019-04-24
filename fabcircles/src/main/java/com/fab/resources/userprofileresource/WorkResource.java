package com.fab.resources.userprofileresource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fab.constants.StatusCodes;
import com.fab.dao.userprofiledao.WorkDAO;
import com.fab.exception.FabException;
import com.fab.model.ResponseBean;
import com.fab.model.userprofile.WorkBean;
import com.fab.resources.BaseResource;

@Path("work")
@Produces(APPLICATION_JSON) 
public class WorkResource extends BaseResource{
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkResource.class);
	@Autowired
	WorkDAO workDAO;
	
	@POST
	 @Consumes(APPLICATION_JSON)
	public ResponseBean createWork(WorkBean workBean) throws Exception{
		LOGGER.info("userBasicInfo Register POST call ::");
		ResponseBean response=null;
		try{
			response=workDAO.postWork(workBean);
		}catch(FabException fe){
			LOGGER.error("Error UserBasicInfo ",fe);
			buildErrorResponse(Response.Status.CONFLICT	, fe.getErrorCode(), fe.getMessage());
		}
		return response;
	}
	
	
	@GET
    @Path("/find")
    @Produces(APPLICATION_JSON)
    public WorkBean findWork(@QueryParam ("userName") String userName) throws Exception{
		WorkBean workBeanFromDB=null;
		try{
			workBeanFromDB=workDAO.findWork(userName);
			if(workBeanFromDB==null){
				
	    		buildErrorResponse(Response.Status.NOT_FOUND, StatusCodes.WORK_INFO_NOT_FOUND, "Work resource Not Found");
			}
		}catch(FabException fe){
			LOGGER.error("Error WorkAPI GET ",fe);
			buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
		}
		
		return workBeanFromDB;
	}
	
}

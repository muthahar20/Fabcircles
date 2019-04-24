package com.fab.resources.userprofileresource;


import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
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

import com.fab.constants.StatusCodes;
import com.fab.dao.userprofiledao.EducationDAO;
import com.fab.exception.FabException;
import com.fab.model.ResponseBean;
import com.fab.model.userprofile.EducationBean;
import com.fab.resources.BaseResource;

@Path("education")
@Produces(APPLICATION_JSON) 
public class EducationResource extends BaseResource{
	private static final Logger LOGGER = LoggerFactory.getLogger(EducationResource.class);
	@Autowired
	EducationDAO educationDAO;
	
	@POST
	 @Consumes(APPLICATION_JSON)
	public ResponseBean createWork(EducationBean educationBean) throws Exception{
		LOGGER.info("EducationBean RegisterAPI POST call ::");
		ResponseBean response=null;
		try{
			response=educationDAO.createOrUpdateEducation(educationBean);
		}catch(FabException fe){
			LOGGER.error("Error Education API POST ",fe);
			buildErrorResponse(Response.Status.CONFLICT	, fe.getErrorCode(), fe.getMessage());
		}
		return response;
	}
	
	
	
	@GET
    @Path("/find")
    @Produces(APPLICATION_JSON)
    public EducationBean findWork(@QueryParam ("userName") String userName) throws Exception{
		LOGGER.info("EducationBean FindAPI GET call ::");
		EducationBean educationBeanFromDB=null;
		try{
			educationBeanFromDB=educationDAO.findEducationBean(userName);
			if(educationBeanFromDB==null){
	    		buildErrorResponse(Response.Status.NOT_FOUND, StatusCodes.EDUCATION_INFO_NOT_FOUND, "Education resource Not Found");
			}
		}catch(FabException fe){
			LOGGER.error("Error Education API GET ",fe);
			buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
		}
		
		return educationBeanFromDB;
	}
	
}

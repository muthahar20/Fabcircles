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
import com.fab.dao.userprofiledao.PlacesLivedDAO;
import com.fab.exception.FabException;
import com.fab.model.ResponseBean;
import com.fab.model.userprofile.PlacesLivedBean;
import com.fab.resources.BaseResource;


@Path("/placeslived")
@Produces(APPLICATION_JSON) 
public class PlacesLivedResource extends BaseResource{

	private static final Logger LOGGER=LoggerFactory.getLogger(PlacesLivedBean.class);
	
	@Autowired
	PlacesLivedDAO placesLivedDAO;
	
	
	
	@POST
	@Consumes(APPLICATION_JSON)
	public ResponseBean  createPlacesLived(PlacesLivedBean placesLivedBean) throws Exception {
		LOGGER.info("PlacesLived API  Register POST call ::");
		ResponseBean resp=null;
		try{
			resp=placesLivedDAO.createORUpdatePlacesLived(placesLivedBean);
		}catch(FabException fe){
			LOGGER.error("placesLived POST api error ",fe);
    		buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
		}
		
		return resp;
	}
	

	@GET
	@Path("/find")
	@Produces(APPLICATION_JSON) 
	public PlacesLivedBean findPlacesLived(@QueryParam ("userName") String userName) throws Exception{
		PlacesLivedBean	placesLivedBeanFromDB=null;
		try{
			placesLivedBeanFromDB=placesLivedDAO.findPlacesLived(userName);
			if(placesLivedBeanFromDB==null){
				buildErrorResponse(Response.Status.NOT_FOUND, StatusCodes.PLACESLIVED_INFO_NOT_FOUND, " PlacesLived resource Not Found");			}

		}catch(FabException fe){
			buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
		}
		
		return placesLivedBeanFromDB;
	}

	
	
	
}

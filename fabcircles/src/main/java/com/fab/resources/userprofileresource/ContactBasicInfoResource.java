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
import com.fab.dao.userprofiledao.ContactBasicInfoDAO;
import com.fab.exception.FabException;
import com.fab.model.ResponseBean;
import com.fab.model.userprofile.ContactBasicInfoBean;
import com.fab.resources.BaseResource;


@Path("contactbasic")
@Produces(APPLICATION_JSON) 
public class ContactBasicInfoResource extends BaseResource {

	 private static final Logger LOGGER = LoggerFactory.getLogger(ContactBasicInfoResource.class);
	
	@Autowired
	ContactBasicInfoDAO contactBasicInfoDAO;
	
	@POST
	 @Consumes(APPLICATION_JSON)
	public ResponseBean createContactBasicInfo(ContactBasicInfoBean contactBasicInfoBean) throws Exception{
		LOGGER.info(":: ContactBasicInfo Resource Api :  Update Method call ::");
		ResponseBean res=null;
		try{
			res=contactBasicInfoDAO.updateContactBasicInfo(contactBasicInfoBean);
		}catch(FabException fe){
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(), fe.getMessage());
		}
		return res;
	}
	
	
	@GET
	@Path("/find")
	@Produces(APPLICATION_JSON)
	public ContactBasicInfoBean findContactBasicInfo(@QueryParam ("userName") String userName) throws Exception{
		LOGGER.info(":: ContactBasicInfo Resource Api :  Find Method call ::");
		ContactBasicInfoBean result=null;
		try{
			result=contactBasicInfoDAO.findContactBasicIfno(userName);
			if(result==null){
				buildErrorResponse(Response.Status.NOT_FOUND, StatusCodes.CONTACTBASIC_INFO_NOT_FOUND, "ContactBasicInfo resource Not Found");
			}
			
		}catch(FabException fe){
			buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR,fe.getErrorCode(),fe.getMessage());
		}
		return result;
	}
	
	
	
	
}

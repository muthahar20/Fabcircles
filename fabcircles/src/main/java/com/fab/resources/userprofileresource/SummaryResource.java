package com.fab.resources.userprofileresource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fab.constants.StatusCodes;
import com.fab.dao.userprofiledao.ContactBasicInfoDAO;
import com.fab.dao.userprofiledao.EducationDAO;
import com.fab.dao.userprofiledao.PlacesLivedDAO;
import com.fab.dao.userprofiledao.WorkDAO;
import com.fab.exception.FabException;
import com.fab.model.userprofile.ContactBasicInfoBean;
import com.fab.model.userprofile.EducationBean;
import com.fab.model.userprofile.PlacesLivedBean;
import com.fab.model.userprofile.Summary;
import com.fab.model.userprofile.WorkBean;
import com.fab.resources.BaseResource;


@Path("summary")
@Produces(APPLICATION_JSON) 
public class SummaryResource extends BaseResource {
	private static final Logger LOGGER=LoggerFactory.getLogger(SummaryResource.class);
	
	
	@Autowired
	PlacesLivedDAO placesLivedDAO;
	@Autowired
	ContactBasicInfoDAO contactBasicInfoDAO;
	@Autowired
	EducationDAO educationDAO;
	@Autowired
	WorkDAO workDAO;
	
	
	@GET
	@Path("/find")
    @Produces(APPLICATION_JSON)
    public Summary findSummary(@QueryParam ("userName") String userName) throws Exception{
		Summary summary = null;
		ContactBasicInfoBean contactBasicInfoBean = null;
		PlacesLivedBean	placesLivedBean = null;
		EducationBean educationBean = null;
		WorkBean workBean = null;
		
try{
			summary=new Summary();
			contactBasicInfoBean = contactBasicInfoDAO.findContactBasicIfno(userName);
			placesLivedBean = placesLivedDAO.findPlacesLived(userName);
			educationBean = educationDAO.findEducationBean(userName);
			workBean = workDAO.findWork(userName);
			
			if(summary != null ){
				if(contactBasicInfoBean!=null || placesLivedBean!=null || educationBean!=null || workBean!=null){
				summary.setContactBasicInfo(contactBasicInfoBean);
				summary.setPlacesLived(placesLivedBean);
				summary.setEducation(educationBean);
				summary.setWork(workBean);
				
				}else{
					buildErrorResponse(Response.Status.NOT_FOUND, StatusCodes.SUMMARY_INFO_NOT_FOUND, "Summary resources Not Found");
				}
			}
			
		}catch(FabException fe){
			LOGGER.error("find UserAboutInfo error ",fe);
			buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, fe.getErrorCode(), fe.getMessage());
		}
		
		return summary;
	}
	
	
	
	
	
}

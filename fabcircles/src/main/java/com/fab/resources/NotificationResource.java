package com.fab.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.fab.dao.NotificationDAO;
import com.fab.dao.PostDAO;
import com.fab.exception.FabException;
import com.fab.model.CommentResponse;
import com.fab.model.NotificationBean;
import com.fab.model.NotificatonResponse;

@Path("/notifications")
@Produces(APPLICATION_JSON)
public class NotificationResource extends BaseResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PostResource.class);

	@Autowired
	PostDAO postDAO;
	
	@Autowired
	NotificationDAO notificationDAO;

	
	@PUT
	@Produces(APPLICATION_JSON)
	public List<NotificationBean> postNotification(NotificationBean notificationBean) throws Exception {
		LOGGER.info("postNotification post call ::");
		List<NotificationBean> response = null;
		try {
			response = notificationDAO.putNotification(notificationBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	@GET
	@Path("/list")
	@Produces(APPLICATION_JSON)
	public List<NotificationBean> postNotification(@QueryParam ( "userName") String userName) throws Exception {
		LOGGER.info("postNotification post call ::");
		List<NotificationBean> response = null;
		try {
			response = notificationDAO.getNotification(userName);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}

	
	

	@GET
	@Produces(APPLICATION_JSON)
	public NotificatonResponse getCommentByPageSize(
			@QueryParam("userName") String userName, @QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("pageSize") Integer pageSize) throws Exception {
		LOGGER.info("messages post call ::");
		NotificatonResponse response = null;
		try {
			if(pageSize == null)
				pageSize = 10;
			if(pageNumber == null)
				pageNumber = 1;
			response = notificationDAO.getNotificationByPageSize(userName, pageNumber, pageSize);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	
	
	
	
}

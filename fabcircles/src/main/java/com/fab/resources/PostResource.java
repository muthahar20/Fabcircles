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
import com.fab.dao.PostDAO;
import com.fab.exception.FabException;
import com.fab.model.CommentBean;
import com.fab.model.CommentLikeBean;
import com.fab.model.CommentResponse;
import com.fab.model.LikeBean;
import com.fab.model.NewsFeedResponse;
import com.fab.model.PostBean;
import com.fab.model.PostResponse;
import com.fab.model.PostSendToBean;
import com.fab.model.ReplyBean;
import com.fab.model.ReplyLikeBean;
import com.fab.model.ResponseBean;
import com.fab.model.ShareBean;
import com.fab.model.UniqUserResponse;

@Path("post")
@Produces(APPLICATION_JSON)
public class PostResource extends BaseResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PostResource.class);

	@Autowired
	PostDAO postDAO;

	@POST
	@Produces(APPLICATION_JSON)
	public PostBean message(PostBean postBean) throws Exception {
		LOGGER.info("messages post call ::");
		PostBean response = null;
		try {
			response = postDAO.postMessage(postBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}

	@PUT
	@Produces(APPLICATION_JSON)
	public PostResponse updatePostMsg(PostBean postBean) throws Exception {
		LOGGER.info("messages post call ::");
		PostResponse response = null;
		try {
			response = postDAO.updatePostMsg(postBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}

	@DELETE
	@Consumes(APPLICATION_JSON)
	public ResponseBean deletePost(PostBean postBean) throws Exception {
		LOGGER.info("messages post call ::");
		ResponseBean response = null;
		try {
			response = postDAO.deletePostMessage(postBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}

	@GET
	@Produces(APPLICATION_JSON)
	public PostResponse getPostById(@QueryParam("postMsgId") String postMsgId)
			throws Exception {
		LOGGER.info("messages post call ::");
		PostResponse response = null;
		try {
			response = postDAO.getPostMsgById(postMsgId);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}

	@GET
	@Path("/like")
	@Produces(APPLICATION_JSON)
	public List<LikeBean> getPostLike(@HeaderParam("token") String token, @QueryParam("postMsgId") String postMsgId)
			throws Exception {
		LOGGER.info("messages post call ::");
		List<LikeBean> response = null;
		try {
			response = postDAO.getLikeList(token, postMsgId);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	@GET
	@Path("/comment/users")
	@Produces(APPLICATION_JSON)
	public List<UniqUserResponse> getCommentsUniquetLike(@HeaderParam("token") String token, @QueryParam("postMsgId") String postMsgId)
			throws Exception {
		LOGGER.info("messages post call ::");
		List<UniqUserResponse> response = null;
		try {
			response = postDAO.getUniqCommentUsers(token,postMsgId);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	@GET
	@Path("/share/users")
	@Produces(APPLICATION_JSON)
	public List<UniqUserResponse> getUniqSharedUsers(@HeaderParam("token") String token, @QueryParam("postMsgId") String postMsgId)
			throws Exception {
		LOGGER.info("messages post call ::");
		List<UniqUserResponse> response = null;
		try {
			response = postDAO.getUniqSharedUsers(token,postMsgId);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	
	
	@GET
	@Path("/comment/like")
	@Produces(APPLICATION_JSON)
	public List<CommentLikeBean> getCommentLike(@QueryParam("commentId") String commentId )
			throws Exception {
		LOGGER.info("getCommentLike GET call ::");
		List<CommentLikeBean> response = null;
		try {
			response = postDAO.getCommentLikeList(commentId);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}

	

	@GET
	@Path("/timeline")
	@Produces(APPLICATION_JSON)
	public List<PostResponse> getAllPosts(
			@QueryParam("userName") String userName) throws Exception {
		LOGGER.info("messages post call ::");
		List<PostResponse> response = null;
		try {
			response = postDAO.getAllPosts(userName);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}

	@POST
	@Path("/like")
	@Produces(APPLICATION_JSON)
	public LikeBean postLike(LikeBean likeBean) throws Exception {
		LOGGER.info("Like POST call ::");
		LikeBean response = null;
		try {
			response = postDAO.postLike(likeBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	@POST
	@Path("/comment/like")
	@Produces(APPLICATION_JSON)
	public CommentLikeBean commentLike(CommentLikeBean commentLikeBean) throws Exception {
		LOGGER.info("Like POST call ::");
		CommentLikeBean response = null;
		try {
			response = postDAO.commentLike(commentLikeBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	@PUT
	@Path("/comment/hide")
	@Produces(APPLICATION_JSON)
	public CommentBean commentHide(CommentBean commenBean) throws Exception {
		LOGGER.info("Like POST call ::");
		CommentBean response = null;
		try {
			response = postDAO.commentHide(commenBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}

	
	@PUT
	@Path("/comment/reply/hide")
	@Produces(APPLICATION_JSON)
	public ReplyBean commentReplyHide(ReplyBean replyBean) throws Exception {
		LOGGER.info("commentReplyHide PUT call ::");
		ReplyBean response = null;
		try {
			response = postDAO.replyHide(replyBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	

	
	
	@POST
	@Path("/comment/reply/like")
	@Produces(APPLICATION_JSON)
	public ReplyLikeBean commentReplyLike(ReplyLikeBean replylikeBean) throws Exception {
		LOGGER.info("Like POST call ::");
		ReplyLikeBean response = null;
		try {
			response = postDAO.commentReplyLike(replylikeBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	

	@POST
	@Path("/comment")
	@Produces(APPLICATION_JSON)
	public CommentBean comment(CommentBean commentBean) throws Exception {
		LOGGER.info("comment post call ::");
		CommentBean response = null;
		try {
			response = postDAO.postComment(commentBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	/*
	
	@GET
	@Path("/notifications")
	@Produces(APPLICATION_JSON)
	public List<NotificatonResponse> getNotifications(@QueryParam ("userName") String userName) throws Exception {
		LOGGER.info("comment post call ::");
		List<NotificatonResponse> response = null;
		try {
			response = postDAO.getNotification(userName);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	*/
	
	@POST
	@Path("/comment/reply")
	@Produces(APPLICATION_JSON)
	public ReplyBean reply(ReplyBean replyBean) throws Exception {
		LOGGER.info("reply post call ::");
		ReplyBean response = null;
		try {
			response = postDAO.reply(replyBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	@DELETE
	@Path("/comment/reply/delete")
	@Consumes(APPLICATION_JSON)
	public ResponseBean deleteReply(ReplyBean replyBean) throws Exception {
		LOGGER.info("Delete reply call ::");
		ResponseBean response = null;
		try {
			response = postDAO.deleteCommentReply(replyBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	@DELETE
	@Path("/comment/delete")
	@Consumes(APPLICATION_JSON)
	public ResponseBean deleteComment(CommentBean commentBean) throws Exception {
		LOGGER.info("Delete reply call ::");
		ResponseBean response = null;
		try {
			response = postDAO.deleteComment(commentBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}

	
	@DELETE
	@Path("/like")
	@Consumes(APPLICATION_JSON)
	public ResponseBean deleteReply(PostBean postBean) throws Exception {
		LOGGER.info("Delete reply call ::");
		ResponseBean response = null;
		try {
			response = postDAO.deletePostLike(postBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	
	
	@GET
	@Path("/newsfeed")
	@Produces(APPLICATION_JSON)
	public NewsFeedResponse getAllNewsFeedNew(
			@QueryParam("userName") String userName, @QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("pageSize") Integer pageSize) throws Exception {
		LOGGER.info("messages post call ::");
		NewsFeedResponse response = null;
		try {
			if(pageSize == null)
				pageSize = 10;
			if(pageNumber == null)
				pageNumber = 1;
			response = postDAO.getNewsFeedNew(userName, pageNumber, pageSize);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	
	@GET
	@Path("/comment")
	@Produces(APPLICATION_JSON)
	public List<CommentResponse> getCommentByPageSize(
			@QueryParam("postMsgId") String postMsgId, @QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("pageSize") Integer pageSize) throws Exception {
		LOGGER.info("messages post call ::");
		List<CommentResponse> response = null;
		try {
			if(pageSize == null)
				pageSize = 10;
			if(pageNumber == null)
				pageNumber = 1;
			response = postDAO.getCommentByPageSize(postMsgId, pageNumber, pageSize);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	
	@PUT
	@Produces(APPLICATION_JSON)
	@Path("/unfollow")
	public ResponseBean updatePostFollow(
			@QueryParam("userName") String userName, @QueryParam("friendName") String friendName, @QueryParam("unFollow") String unFollow) throws Exception {
		LOGGER.info("UpdateHidePostMsg call ::");
		ResponseBean response = null;
		try {
			response = postDAO.UpdatePostSend(userName, friendName, unFollow);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	

	@POST
	@Path("/share")
	@Produces(APPLICATION_JSON)
	@Consumes(APPLICATION_JSON)
	public ShareBean message(ShareBean shareBean) throws Exception {
		LOGGER.info("share post call ::");
		ShareBean response = null;
		try {
			response = postDAO.sharePost(shareBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}

	@DELETE
	@Path("/share")
	@Consumes(APPLICATION_JSON)
	public ResponseBean deleteSharedPost(ShareBean shareBean) throws Exception {
		LOGGER.info("Delete Share post call ::");
		ResponseBean response = null;
		try {
			response = postDAO.deleteSharedPost(shareBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	@PUT
	@Produces(APPLICATION_JSON)
	@Path("/hidepost")
	public PostSendToBean updateHidePostMsg(PostSendToBean postSendToBean) throws Throwable {
		LOGGER.info("UpdateHidePostMsg call ::");
		PostSendToBean response = null;
		try {
			response = postDAO.updateHidePost(postSendToBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	@PUT
	@Produces(APPLICATION_JSON)
	@Path("/hideall")
	public ResponseBean updateHideAllPostMsg(PostSendToBean postSendToBean) throws Throwable {
		LOGGER.info("UpdateHidePostMsg for all posts call ::");
		ResponseBean response = null;
		try {
			response = postDAO.updateHideAllPostMsgs(postSendToBean);
		} catch (FabException fe) {
			LOGGER.error("Error message", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}

}

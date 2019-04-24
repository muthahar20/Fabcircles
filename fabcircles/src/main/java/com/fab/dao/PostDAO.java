package com.fab.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.fab.constants.FabConstants;
import com.fab.constants.StatusCodes;
import com.fab.exception.FabException;
import com.fab.filter.AuthFilter;
import com.fab.model.CommentBean;
import com.fab.model.CommentLikeBean;
import com.fab.model.CommentResponse;
import com.fab.model.ImageBean;
import com.fab.model.LikeBean;
import com.fab.model.NewsFeedResponse;
import com.fab.model.NotificationBean;
import com.fab.model.PostBean;
import com.fab.model.PostResponse;
import com.fab.model.PostSendToBean;
import com.fab.model.ReplyBean;
import com.fab.model.ReplyLikeBean;
import com.fab.model.ResponseBean;
import com.fab.model.ShareBean;
import com.fab.model.SharedResponse;
import com.fab.model.UniqUserResponse;
import com.fab.model.user.FriendsBean;
import com.fab.model.user.UserBean;
import com.fab.mongo.MongoManager;
import com.mongodb.DB;

@Component
public class PostDAO extends BaseDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostDAO.class);

	@Autowired
	MongoManager mongoManager;
	@Autowired
	FabConstants fabConstants;
	@Autowired
	StatusCodes statusCodes;
	@Autowired
	FriendsDAO friendsDAO;
	@Autowired
	ImageDAO imageDAO;
	@Autowired
	NotificationDAO notificationDAO;

	public PostBean postMessage(PostBean postBean) throws Exception {
		LOGGER.info(":: POST DAO : postMsg Method call  ::");
		PostBean postBEAN = new PostBean();
		try {
			DB db = mongoManager.getMongoDB();
			String postMsgId = MongoManager.getNextSeqId(db, "postmsg_seq");
			postMsgId = postMsgId.replaceAll("\\.0*$", "");
			postBEAN.setPostMsgId(postMsgId);
			postBEAN.setLink(postBean.getLink());
			postBEAN.setMessage(postBean.getMessage());
			postBEAN.setUserName(postBean.getUserName());
			postBEAN.setPostToFriends(postBean.isPostToFriends());
			postBEAN.setPostToCircles(postBean.isPostToCircles());
			postBEAN.setReComments(postBean.getReComments());
			postBEAN.setReSharing(postBean.getReSharing());
			postBEAN.setFriendsAddPhotos(postBean.getFriendsAddPhotos());
			List<String> userList = new ArrayList<String>();
			userList.add(postBean.getUserName());
			if (postBean.isPostToFriends() == false
					&& postBean.isPostToCircles() == true) {
				postBEAN.setSend("only me");
				userList.addAll(postBean.getToUsers());
				postBEAN.setToUsers(userList);
				postBEAN.setToCircles(postBean.getToCircles());
			} else if (postBean.isPostToFriends() == true
					&& postBean.isPostToCircles() == true) {
				postBEAN.setSend("friends");
				userList.addAll(shareFriends(postBean.getUserName()));
				postBEAN.setToUsers(userList);
				postBEAN.setToCircles(postBean.getToCircles());
			} else if (postBean.isPostToFriends() == true
					&& postBean.isPostToCircles() == false) {
				postBEAN.setSend("friends");
				userList.addAll(shareFriends(postBean.getUserName()));
				postBEAN.setToUsers(userList);
			} else {
				// default
				postBEAN.setSend("public");
				postBEAN.setToUsers(sharePublic(postBean.getUserName()));
			}
			postBEAN.setPhotos(postBean.getPhotos());
			insertDB(FabConstants.POST_MESSAGE_COLLECTION, postBEAN);

			// Friend Post
			for (String frnd : postBEAN.getToUsers()) {
				PostSendToBean frndPost = new PostSendToBean();
				frndPost.setUserName(frnd);
				frndPost.setSendBy(postBean.getUserName());
				FriendsBean friendsBeanfromDB = mongoManager.findBy2Fields(
						FabConstants.FRIENDS_COLLECTION, "userName",
						postBean.getUserName(), "friend", frnd,
						FriendsBean.class);
				if (friendsBeanfromDB != null) {
					frndPost.setUnFollow(StringUtils.isEmpty(friendsBeanfromDB
							.getUnFollow()) ? "no" : friendsBeanfromDB
							.getUnFollow());
				} else {
					frndPost.setUnFollow("no");
				}
				frndPost.setPostMsgId(postMsgId);
				if (frndPost.getUserName() != null) {
					insertDB(FabConstants.POST_SEND_TO_COLLECTION, frndPost);
				}
			}
		} catch (Exception e) {
			LOGGER.info(":: POST DAO : postMsg Method call  ::" + e);
			throw new FabException(StatusCodes.CIRCLE_NOT_CREATED,
					"SENT_MESSAGE_COLLECTION and POST_MESSAGE_COLLECTION not created");
		}
		return getPostMsgById(postBEAN.getPostMsgId());
	}

	// Delete Post Message
	public ResponseBean deletePostMessage(PostBean postBean) throws Exception {
		ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		LOGGER.info(":: PostDAO : deletePostMessage Method call  ::");
		PostBean postMsg = mongoManager.findBy2Fields(
				FabConstants.POST_MESSAGE_COLLECTION, "postMsgId",
				postBean.getPostMsgId(), "userName", postBean.getUserName(),
				PostBean.class);

		List<LikeBean> likes = mongoManager.getObjectsByField(
				FabConstants.LIKE_COLLECTION, "postMsgId",
				postBean.getPostMsgId(), LikeBean.class);

		List<CommentBean> commentFromDB = mongoManager.getObjectsByField(
				FabConstants.COMMENT_COLLECTION, "postMsgId",
				postBean.getPostMsgId(), CommentBean.class);

		List<CommentLikeBean> commLikeFromDB = mongoManager.getObjectsByField(
				FabConstants.COMMENT_LIKE_COLLECTION, "postMsgId",
				postBean.getPostMsgId(), CommentLikeBean.class);

		List<ReplyLikeBean> replyMsg = mongoManager.getObjectsByField(
				FabConstants.COMMENT_COLLECTION, "postMsgId",
				postBean.getPostMsgId(), ReplyLikeBean.class);

		List<ReplyLikeBean> replyMsgLike = mongoManager.getObjectsByField(
				FabConstants.COMM_REPLY_LIKE, "postMsgId",
				postBean.getPostMsgId(), ReplyLikeBean.class);

		if (postMsg != null) {
			if (commentFromDB != null) {
				if (replyMsg != null) {
					if (replyMsgLike != null) {
						mongoManager.deleteByField(
								FabConstants.COMM_REPLY_LIKE, "postMsgId",
								postBean.getPostMsgId());
					}
					mongoManager.deleteByField(FabConstants.REPLY_COLLECTION,
							"postMsgId", postBean.getPostMsgId());
				}
				if (commLikeFromDB != null) {
					mongoManager.deleteByField(
							FabConstants.COMMENT_LIKE_COLLECTION, "postMsgId",
							postBean.getPostMsgId());
				}
				mongoManager.deleteByField(FabConstants.COMMENT_COLLECTION,
						"postMsgId", postBean.getPostMsgId());
			}
			if (likes != null) {
				mongoManager.deleteByField(FabConstants.LIKE_COLLECTION,
						"postMsgId", postBean.getPostMsgId());
			}
			mongoManager.deleteByField(FabConstants.POST_SEND_TO_COLLECTION,
					"postMsgId", postBean.getPostMsgId());
			mongoManager.deleteByField(FabConstants.POST_MESSAGE_COLLECTION,
					"postMsgId", postBean.getPostMsgId());
		} else {
			throw new FabException(StatusCodes.REPLY_ID_NOT_FOUND,
					" postMsgId Not Found ");
		}
		return response;
	}

	// Get Post Messages by PostMsgId
	public PostResponse getPostMsgById(String postMsgId) throws Exception {
		LOGGER.info(":: Get : getPostMsgById Method call  ::");

		PostResponse postResp = null;
		PostBean postBeansFromDB = mongoManager.getObjectByField(
				FabConstants.POST_MESSAGE_COLLECTION, "postMsgId", postMsgId,
				PostBean.class);
		UserBean userBean = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName",
				postBeansFromDB.getUserName(), UserBean.class);
		if (postBeansFromDB != null) {
			postResp = getPostElements(userBean, postBeansFromDB);

		}
		return postResp;
	}

	// Get All Post Messages
	public List<PostResponse> getAllPosts(String userName) throws Exception {
		LOGGER.info(":: Get : getAllPosts Method call  ::");
		List<PostResponse> posts = new ArrayList<PostResponse>();
		posts.addAll(getPosts(userName));
		sortPostResponseDate(posts);
		return posts;
	}

	// Get Full Name
	public String getFullName(String userName) throws Exception {
		LOGGER.info(":: Get : getFullName Method call  ::");
		UserBean userBean = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName", userName,
				UserBean.class);
		return userBean.getFirstName() + " " + userBean.getLastName();
	}

	private List<PostResponse> getPosts(String userName) throws Exception {
		List<PostResponse> tempPostRespList = new ArrayList<PostResponse>();
		List<PostBean> postBeansFromDB = mongoManager.getObjectsByField(
				FabConstants.POST_MESSAGE_COLLECTION, "userName", userName,
				PostBean.class);
		UserBean userBean = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName", userName,
				UserBean.class);

		if (CollectionUtils.isNotEmpty(postBeansFromDB)) {
			for (PostBean post : postBeansFromDB) {
				PostResponse postResp = getPostElements(userBean, post);
				tempPostRespList.add(postResp);
				LOGGER.info("POST date:" + post.getCreated());
				postResp.setTimeDiff(calculateTimeDiff(post.getCreated()));
			}
		}
		return tempPostRespList;
	}

	private PostResponse getPostElements(UserBean userBean, PostBean post)
			throws IllegalAccessException, InvocationTargetException, Exception {
		PostResponse postResp = new PostResponse();
		post.setFullName(userBean.getFirstName() + " " + userBean.getLastName());
		BeanUtils.copyProperties(postResp, post);
		postResp.setProfilePID(getProfilePublicId(userBean.getUserName()));
		postResp.setTimeDiff(calculateTimeDiff(post.getCreated()));
		// postResp.setShare(countOfShared(post.getPostMsgId()));
		List<ShareBean> sharedList = mongoManager.getObjectsByField(
				FabConstants.SHARE_COLLECTION, "postMsgId",
				post.getPostMsgId(), ShareBean.class);
		List<SharedResponse> shareRes = new ArrayList<SharedResponse>();
		if (CollectionUtils.isNotEmpty(sharedList)) {
			for (ShareBean share : sharedList) {
				SharedResponse shareRES = new SharedResponse();
				shareRES.setUserName(share.getUserName());
				shareRES.setFullName(getFullName(share.getUserName()));
				shareRES.setProfilePID(getProfilePublicId(share.getUserName()));
				shareRES.setShareCount(countOfShared(share.getPostMsgId()));
				shareRes.add(shareRES);
			}
		}
		postResp.setShare(shareRes);
		List<LikeBean> likes = mongoManager.getObjectsByField(
				FabConstants.LIKE_COLLECTION, "postMsgId", post.getPostMsgId(),
				LikeBean.class);
		if (CollectionUtils.isNotEmpty(likes)) {
			postResp.setLikes(likes);
			postResp.setLikeCount(likes.size());
		}
		/*
		 * List<CommentBean> comments = mongoManager.getObjectsByField(
		 * FabConstants.COMMENT_COLLECTION, "postMsgId", post.getPostMsgId(),
		 * CommentBean.class);
		 */
		List<CommentBean> comments = mongoManager.getObjectsByOrderAndPage(
				FabConstants.COMMENT_COLLECTION, "postMsgId",
				post.getPostMsgId(), "created", Direction.DESC, 1, 3,
				"commentId", CommentBean.class);

		if (CollectionUtils.isNotEmpty(comments)) {
			sortPostCommentsDate(comments);
			List<CommentResponse> commentRespList = new ArrayList<CommentResponse>();
			Set<String> commenterPublicIds = new LinkedHashSet<String>();
			for (CommentBean comment : comments) {
				String profId = getProfilePublicId(comment.getUserName());
				if (commenterPublicIds.size() <= 5) {
					commenterPublicIds.add(profId);
				}
				CommentResponse commResp = new CommentResponse();
				BeanUtils.copyProperties(commResp, comment);

				commResp.setProfilePID(profId);
				List<CommentLikeBean> commLikes = mongoManager
						.getObjectsByField(
								FabConstants.COMMENT_LIKE_COLLECTION,
								"commentId", commResp.getCommentId(),
								CommentLikeBean.class);
				commResp.setCommLikes(commLikes);
				commResp.setCommLikeCount(commLikes.size());
				commResp.setTimeDiff(calculateTimeDiff(comment.getCreated()));
				// Reply
				List<ReplyLikeBean> replyBeanFromDB = mongoManager
						.getObjectsByField(FabConstants.REPLY_COLLECTION,
								"commentId", commResp.getCommentId(),
								ReplyLikeBean.class);

				commResp.setReplyLikes(replyBeanFromDB);
				commResp.setReplyCount(replyBeanFromDB.size());

				List<ReplyBean> replyBEAN = mongoManager.getObjectsByField(
						FabConstants.REPLY_COLLECTION, "commentId",
						comment.getCommentId(), ReplyBean.class);
				for (ReplyBean reply : replyBEAN) {
					reply.setProfilePID(getProfilePublicId(reply.getUserName()));
					reply.setTimeDiff(calculateTimeDiff(reply.getCreated()));
				}
				commResp.setReply(replyBEAN);
				commentRespList.add(commResp);
			}
			postResp.setComments(commentRespList);
			postResp.setCommentCount(commentRespList.size());
			postResp.setCommenterPublicIds(commenterPublicIds);
		}
		return postResp;
	}

	// Get Profile Image PublicId
	public String getProfilePublicId(String userName) throws Exception {
		LOGGER.info(":: Get : getPublicId Method call  ::");
		ImageBean image = imageDAO.getProfileImage(userName);
		return image.getPublicId();
	}

	// Post Like
	public LikeBean postLike(LikeBean likeBean) throws Exception {
		LOGGER.info(":: POST DAO : postLike Method call  ::");
		UserBean userBean = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName",
				likeBean.getUserName(), UserBean.class);
		LikeBean likesFromDB = mongoManager.findBy2Fields(
				FabConstants.LIKE_COLLECTION, "postMsgId",
				likeBean.getPostMsgId(), "userName", likeBean.getUserName(),
				LikeBean.class);
		String priyarity = "liked";
		NotificationBean notificationFromDB = mongoManager.findBy3Fields(
				FabConstants.NOTIFICATON_COLLECTION, "postMsgId",
				likeBean.getPostMsgId(), "priyarity", priyarity, "friend",
				likeBean.getUserName(), NotificationBean.class);

		if (likesFromDB == null && notificationFromDB == null) {
			likeBean.setFullName(userBean.getFirstName() + " "
					+ userBean.getLastName());
			likeBean.setCreated(likeBean.getCreated());
			insertDB(FabConstants.LIKE_COLLECTION, likeBean);

			// for notification
			NotificationBean notificaton = new NotificationBean();
			notificaton
					.setUserName(getUserNameByPostID(likeBean.getPostMsgId()));
			notificaton.setPostAdmin(notificaton.getUserName());
			notificaton.setFriend(likeBean.getUserName());
			notificaton.setPostMsgId(likeBean.getPostMsgId());
			notificaton.setPriyarity(priyarity);
			notificationDAO.postNotification(notificaton);
		} else {
			if (notificationFromDB != null) {
				mongoManager.deleteBy3Field(
						FabConstants.NOTIFICATON_COLLECTION, "postMsgId",
						likeBean.getPostMsgId(), "priyarity", priyarity,
						"friend", likeBean.getUserName());
			}
			mongoManager.deleteBy2Field(FabConstants.LIKE_COLLECTION,
					"postMsgId", likeBean.getPostMsgId(), "userName",
					likeBean.getUserName());

		}
		LikeBean likeResult = mongoManager.findBy2Fields(
				FabConstants.LIKE_COLLECTION, "postMsgId",
				likeBean.getPostMsgId(), "userName", likeBean.getUserName(),
				LikeBean.class);
		if (likeResult == null) {
			throw new FabException(StatusCodes.DIS_LIKE, "UNLIKE SUCCESS");
		}

		return likeResult;
	}

	// Update Post Messages
	public PostResponse updatePostMsg(PostBean postBean) throws Exception {
		LOGGER.info(":: PostDAO : updatePostMsg Method call  ::");
		PostBean postBeanFromDB = mongoManager.findBy2Fields(
				FabConstants.POST_MESSAGE_COLLECTION, "postMsgId",
				postBean.getPostMsgId(), "userName", postBean.getUserName(),
				PostBean.class);
		if (postBeanFromDB == null) {
			throw new FabException(StatusCodes.POST_MSG_ID_NOT_FOUND,
					" postMsgId Not Found ");
		} else {
			mongoManager.updateByField(FabConstants.POST_MESSAGE_COLLECTION,
					"postMsgId", postBean.getPostMsgId(), "message",
					postBean.getMessage());
		}
		return getPostMsgById(postBean.getPostMsgId());
	}

	// validate user from PostMsg
	public void validateUserFromPostMsg(String userName) throws Exception {
		List<PostBean> postBeanfromPostMsgColl = mongoManager
				.getObjectsByField(FabConstants.POST_MESSAGE_COLLECTION,
						"userName", userName, PostBean.class);
		if (postBeanfromPostMsgColl == null) {
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST,
					"You Don't have any Messages :");
		}
	}

	// Count of Likes
	public long countOfLikes(String postMsgId) throws Exception {
		String like = "CHECK";
		return mongoManager.getCountByField(FabConstants.LIKE_COLLECTION,
				"postMsgId", postMsgId, "like", like, PostBean.class);
	}

	// Get userName By PostMsgId
	public String getUserNameByPostID(String postMsgId) throws Exception {
		PostBean postBeanFromDB = mongoManager.getObjectByField(
				FabConstants.POST_MESSAGE_COLLECTION, "postMsgId", postMsgId,
				PostBean.class);
		if (postBeanFromDB == null) {
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST,
					"postMsgId not Found :");
		}
		return postBeanFromDB.getUserName();
	}

	// Get Post Messages by PostMsgId
	public PostResponse getPostById(String postMsgId) throws Exception {
		LOGGER.info(":: Get : getPostMsgById Method call  ::");
		PostResponse postResp = null;
		PostBean postBeansFromDB = mongoManager.getObjectByField(
				FabConstants.POST_MESSAGE_COLLECTION, "postMsgId", postMsgId,
				PostBean.class);
		UserBean userBean = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName",
				postBeansFromDB.getUserName(), UserBean.class);
		if (postBeansFromDB != null) {
			postResp = getPostElements(userBean, postBeansFromDB);
		}
		return postResp;
	}

	// Get CommentLikeBean Likes
	public List<CommentLikeBean> getCommentLikeList(String commentId)
			throws Exception {
		LOGGER.info(":: Get : getLikeList Method call  ::");
		List<CommentLikeBean> commentLikeList = new ArrayList<CommentLikeBean>();

		List<CommentLikeBean> listFromDB = mongoManager.getObjectsByField(
				FabConstants.COMMENT_LIKE_COLLECTION, "commentId", commentId,
				CommentLikeBean.class);

		for (CommentLikeBean like : listFromDB) {
			CommentLikeBean likeBean = new CommentLikeBean();
			UserBean userBean = mongoManager.getObjectByField(
					FabConstants.USER_COLLECTION, "userName",
					like.getUserName(), UserBean.class);
			likeBean.setUserName(like.getUserName());
			likeBean.setFullName(userBean.getFirstName() + " "
					+ userBean.getLastName());
			likeBean.setProfilePID(getProfilePublicId(like.getUserName()));

			commentLikeList.add(likeBean);
		}

		return commentLikeList;
	}

	// Comment Like
	public CommentLikeBean commentLike(CommentLikeBean commLikeBean)
			throws Exception {
		LOGGER.info(":: POST DAO :commentLike Method call  ::");
		UserBean userBean = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName",
				commLikeBean.getUserName(), UserBean.class);
		CommentLikeBean commLikeFromDB = mongoManager.findBy2Fields(
				FabConstants.COMMENT_LIKE_COLLECTION, "userName",
				commLikeBean.getUserName(), "commentId",
				commLikeBean.getCommentId(), CommentLikeBean.class);

		if (commLikeFromDB == null) {
			commLikeBean.setFullName(userBean.getFirstName() + " "
					+ userBean.getLastName());
			insertDB(FabConstants.COMMENT_LIKE_COLLECTION, commLikeBean);
		} else {
			mongoManager.deleteBy2Field(FabConstants.COMMENT_LIKE_COLLECTION,
					"commentId", commLikeBean.getCommentId(), "userName",
					commLikeBean.getUserName());
		}
		return mongoManager.findBy2Fields(FabConstants.COMMENT_LIKE_COLLECTION,
				"userName", commLikeBean.getUserName(), "commentId",
				commLikeBean.getCommentId(), CommentLikeBean.class);
	}

	// Comment Reply Like
	public ReplyLikeBean commentReplyLike(ReplyLikeBean commReplyLikeBean)
			throws Exception {
		LOGGER.info(":: POST DAO :commentLike Method call  ::");
		UserBean userBean = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName",
				commReplyLikeBean.getUserName(), UserBean.class);

		ReplyLikeBean replylikeBean = mongoManager.findBy2Fields(
				FabConstants.COMM_REPLY_LIKE, "userName",
				commReplyLikeBean.getUserName(), "replyId",
				commReplyLikeBean.getReplyId(), ReplyLikeBean.class);
		if (replylikeBean == null) {
			commReplyLikeBean.setFullName(userBean.getFirstName() + " "
					+ userBean.getLastName());
			insertDB(FabConstants.COMM_REPLY_LIKE, commReplyLikeBean);
		} else {
			mongoManager.deleteBy2Field(FabConstants.COMM_REPLY_LIKE,
					"commentId", commReplyLikeBean.getReplyId(), "userName",
					commReplyLikeBean.getUserName());
		}
		return mongoManager.findBy2Fields(FabConstants.COMM_REPLY_LIKE,
				"userName", commReplyLikeBean.getUserName(), "replyId",
				commReplyLikeBean.getReplyId(), ReplyLikeBean.class);
	}

	// Post Comment
	public CommentBean postComment(CommentBean commentBean) throws Exception {
		LOGGER.info(":: POST DAO : postComment Method call  ::");
		/*
		 * ShareBean share = mongoManager.getObjectByField (
		 * FabConstants.SHARE_COLLECTION, "postMsgId",
		 * commentBean.getPostMsgId(), ShareBean.class);
		 */
		UserBean userBean = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName",
				commentBean.getUserName(), UserBean.class);

		CommentBean commentFromDB = null;

		if (StringUtils.isNotEmpty(commentBean.getCommentId())) {
			commentFromDB = mongoManager.findBy3Fields(
					FabConstants.COMMENT_COLLECTION, "postMsgId",
					commentBean.getPostMsgId(), "userName",
					commentBean.getUserName(), "commentId",
					commentBean.getCommentId(), CommentBean.class);
		}
		if (commentFromDB != null) {
			mongoManager.updateByField(FabConstants.COMMENT_COLLECTION,
					"replyId", commentBean.getCommentId(), "comemnt",
					commentBean.getComment());
		} else {
			commentBean.setFullName(userBean.getFirstName() + " "
					+ userBean.getLastName());
			DB db = mongoManager.getMongoDB();
			String commentId = MongoManager.getNextSeqId(db, "comment_seq");
			commentId = commentId.replaceAll("\\.0*$", "");
			commentBean.setCommentId(commentId);
			insertDB(FabConstants.COMMENT_COLLECTION, commentBean);

			/*
			 * 
			 * //for Notification NotificationBean notification = new
			 * NotificationBean();
			 * notification.setUserName(commentBean.getUserName());
			 * notification.setPostMsgId(commentBean.getPostMsgId());
			 * notification
			 * .setPostAdmin(getUserNameByPostID(commentBean.getPostMsgId()));
			 * notification.setPriyarity("commented");
			 * postNotification(notification);
			 */
		}
		commentBean.setTimeDiff(calculateTimeDiff(commentBean.getCreated()));
		return commentBean;
	}

	// comment delete
	public ResponseBean deleteComment(CommentBean commentBean) throws Exception {
		LOGGER.info(":: PostDAO : getLikesById Method call  ::");
		ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);

		PostBean postMsg = mongoManager.findBy2Fields(
				FabConstants.POST_MESSAGE_COLLECTION, "postMsgId",
				commentBean.getPostMsgId(), "userName",
				commentBean.getUserName(), PostBean.class);

		CommentBean commentFromDB = mongoManager.findBy2Fields(
				FabConstants.COMMENT_COLLECTION, "userName",
				commentBean.getUserName(), "commentId",
				commentBean.getCommentId(), CommentBean.class);

		if (postMsg != null) {
			if (commentFromDB != null) {
				mongoManager.deleteByField(FabConstants.COMMENT_COLLECTION,
						"commentId", commentBean.getCommentId());
			} else {
				throw new FabException(StatusCodes.USER_ID_NOT_EXIST,
						"Comment Admin will delete :");
			}
		} else {
			if (commentFromDB != null) {
				mongoManager.deleteByField(FabConstants.COMMENT_COLLECTION,
						"commentId", commentBean.getCommentId());
			} else {
				throw new FabException(StatusCodes.USER_ID_NOT_EXIST,
						"Comment Admin will delete :");
			}
		}

		return response;
	}

	/*
	 * 
	 * // comment delete public ResponseBean deleteComment(CommentBean
	 * commentBean) throws Exception {
	 * LOGGER.info(":: PostDAO : getLikesById Method call  ::"); ResponseBean
	 * response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
	 * 
	 * PostBean postMsg = mongoManager.findBy2Fields(
	 * FabConstants.POST_MESSAGE_COLLECTION, "postMsgId",
	 * commentBean.getPostMsgId(), "userName", commentBean.getUserName(),
	 * PostBean.class);
	 * 
	 * CommentBean commentFromDB = mongoManager.findBy2Fields(
	 * FabConstants.COMMENT_COLLECTION, "userName", commentBean.getUserName(),
	 * "commentId", commentBean.getCommentId(), CommentBean.class);
	 * 
	 * if( postMsg != null){
	 * 
	 * if( commentFromDB != null){
	 * 
	 * mongoManager.deleteByField(FabConstants.COMMENT_COLLECTION, "commentId",
	 * commentBean.getCommentId()); }else{ throw new
	 * FabException(StatusCodes.POST_MSG_ID_NOT_FOUND,
	 * "Comment Admin will delete  :"); } }else{ throw new
	 * FabException(StatusCodes.POST_MSG_ID_NOT_FOUND,
	 * "Comment Admin will delete  :"); } return response; }
	 */

	// Get Likes Messages by PostMsgId
	public List<LikeBean> getLikesById(String postMsgId) throws Exception {
		LOGGER.info(":: PostDAO : getLikesById Method call  ::");
		List<LikeBean> likeBeanFromDB = mongoManager.getObjectsByField(
				FabConstants.LIKE_COLLECTION, "postMsgId", postMsgId,
				LikeBean.class);

		return likeBeanFromDB;
	}

	// POST Reply
	public ReplyBean reply(ReplyBean replyBean) throws Exception {
		LOGGER.info(":: POST DAO : reply Method call  ::");
		UserBean userBean = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName",
				replyBean.getUserName(), UserBean.class);
		ReplyBean replyBeanDB = null;
		if (StringUtils.isNotEmpty(replyBean.getReplyId())) {
			replyBeanDB = mongoManager.findBy2Fields(
					FabConstants.COMMENT_COLLECTION, "commentId",
					replyBean.getCommentId(), "replyId",
					replyBean.getReplyId(), ReplyBean.class);
		}

		if (replyBeanDB != null) {
			mongoManager.updateByField(FabConstants.REPLY_COLLECTION,
					"replyId", replyBean.getReplyId(), "reply",
					replyBean.getReply());
		} else {
			replyBean.setCreated(new Date());
			replyBean.setFullName(userBean.getFirstName() + " "
					+ userBean.getLastName());
			DB db = mongoManager.getMongoDB();
			String replyId = MongoManager.getNextSeqId(db, "comment_reply_seq");
			replyId = replyId.replaceAll("\\.0*$", "");
			replyBean.setReplyId(replyId);
			insertDB(FabConstants.REPLY_COLLECTION, replyBean);
		}
		replyBean.setTimeDiff(calculateTimeDiff(replyBean.getCreated()));
		return replyBean;
	}

	public ResponseBean deleteCommentReply(ReplyBean replyBean)
			throws FabException, Exception {
		ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		LOGGER.info(":: DELETE DAO : deleteCommentReply Method call  ::");
		ReplyBean replyBeanFromDB = mongoManager.findBy2Fields(
				FabConstants.REPLY_COLLECTION, "commentId",
				replyBean.getCommentId(), "replyId", replyBean.getReplyId(),
				ReplyBean.class);
		if (replyBeanFromDB != null) {
			mongoManager.deleteBy2Field(FabConstants.REPLY_COLLECTION,
					"commentId", replyBean.getCommentId(), "replyId",
					replyBean.getReplyId());

		} else {
			throw new FabException(StatusCodes.REPLY_ID_NOT_FOUND,
					" postMsgId Not Found ");
		}

		return response;
	}

	public List<PostResponse> getAllNewsFeed(String userName) throws Exception {
		LOGGER.info(":: Get : getAllNewsFeed Method call  ::");
		List<PostResponse> posts = new ArrayList<PostResponse>();
		List<FriendsBean> friendList = friendsDAO.listOfFriends(userName);
		for (FriendsBean friendBean : friendList) {
			if (StringUtils.isEmpty(friendBean.getUnFollow())
					|| !friendBean.getUnFollow().equalsIgnoreCase("yes")) {
				posts.addAll(getPosts(friendBean.getFriend()));
			}
		}
		sortPostResponseDate(posts);
		return posts;
	}

	private void sortPostResponseDate(List<PostResponse> posts) {
		Collections.sort(posts, new Comparator<PostResponse>() {
			@Override
			public int compare(PostResponse o1, PostResponse o2) {
				if (o1.getCreated() == null && o2.getCreated() == null) {
					return 0;
				}
				if (o1.getCreated() == null) {
					return 1;
				}
				if (o2.getCreated() == null) {
					return -1;
				}
				return o2.getCreated().compareTo(o1.getCreated());
			}
		});
	}

	private void sortPostCommentsDate(List<CommentBean> posts) {
		Collections.sort(posts, new Comparator<CommentBean>() {
			@Override
			public int compare(CommentBean o1, CommentBean o2) {
				if (o1.getCreated() == null && o2.getCreated() == null) {
					return 0;
				}
				if (o1.getCreated() == null) {
					return 1;
				}
				if (o2.getCreated() == null) {
					return -1;
				}
				return o2.getCreated().compareTo(o1.getCreated());
			}
		});
	}

	public List<String> shareFriends(String userName) throws Exception {
		List<String> users = new ArrayList<String>();
		List<FriendsBean> friendsBeanList = mongoManager.getObjectsByField(
				FabConstants.FRIENDS_COLLECTION, "userName", userName,
				FriendsBean.class);

		if (friendsBeanList != null) {
			for (FriendsBean frnds : friendsBeanList) {
				users.add(frnds.getFriend());
			}
		} else {
			throw new FabException(StatusCodes.FRIENDS_LIST_NOT_FOUND,
					" Friend list Not Found ");
		}
		return users;
	}

	public ResponseBean deletePostLike(PostBean postBean) throws Exception {
		LOGGER.info(":: Get : getAllPosts Method call  ::");
		ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		List<LikeBean> likes = mongoManager.getObjectsByField(
				FabConstants.LIKE_COLLECTION, "postMsgId",
				postBean.getPostMsgId(), LikeBean.class);
		if (likes != null) {
			mongoManager.deleteByField(FabConstants.LIKE_COLLECTION,
					"postMsgId", postBean.getPostMsgId());
		} else {
			throw new FabException(StatusCodes.REPLY_ID_NOT_FOUND,
					" postMsgId Not Found ");
		}
		return response;
	}

	public List<String> sharePublic(String userName) throws Exception {
		List<String> users = new ArrayList<String>();
		List<UserBean> userFromDB = mongoManager.getAllObjects(
				FabConstants.USER_COLLECTION, UserBean.class);
		for (UserBean user : userFromDB) {
			users.add(user.getUserName());
		}
		return users;
	}

	// share post
	public ShareBean sharePost(ShareBean shareBean) throws Exception {
		LOGGER.info(":: Get : sharePost Method call  ::");
		String priyarity = "shared";
		DB db = mongoManager.getMongoDB();
		String shareId = MongoManager.getNextSeqId(db, "share_seq");
		shareId = shareId.replaceAll("\\.0*$", "");
		shareBean.setShareId(shareId);

		if (shareBean.getShareTo().equals("public")) {
			shareBean.setToUsers(sharePublic(shareBean.getUserName()));
		} else if (shareBean.getShareTo().equals("friends")) {
			shareBean.setToUsers(shareFriends(shareBean.getUserName()));
		} else if (shareBean.getShareTo().equals("onlyme")) {
			List<String> user = new ArrayList<String>();
			user.add(shareBean.getUserName());
			shareBean.setToUsers(user);
		} else {
			shareBean.setToUsers(shareBean.getToUsers());
		}
		insertDB(FabConstants.SHARE_COLLECTION, shareBean);

		// for Notification
		for (String user : shareBean.getToUsers()) {
			NotificationBean notificaton = new NotificationBean();
			if (user != shareBean.getUserName()) {
				notificaton.setUserName(user);
				notificaton.setFriend(shareBean.getUserName());
				notificaton.setPostMsgId(shareBean.getPostMsgId());
				notificaton.setPostAdmin(getUserNameByPostID(shareBean
						.getPostMsgId()));
				notificaton.setPriyarity(priyarity);
				notificaton.setReadStatus("unRead");
				notificationDAO.postNotification(notificaton);

				/*
				 * NotificationBean notificationFromDB =
				 * mongoManager.findBy3Fields(
				 * FabConstants.NOTIFICATON_COLLECTION,
				 * "postMsgId",shareBean.getPostMsgId(), "priyarity",
				 * priyarity,"userName", shareBean.getUserName(),
				 * NotificationBean.class);
				 */
			}
		}

		for (String user : shareBean.getToUsers()) {
			PostSendToBean share = new PostSendToBean();
			share.setUserName(user);
			share.setShareStatus(true);
			share.setShareId(shareBean.getShareId());
			share.setShareBy(shareBean.getUserName());
			share.setPostMsgId(shareBean.getPostMsgId());
			FriendsBean friendsBeanfromDB = mongoManager.findBy2Fields(
					FabConstants.FRIENDS_COLLECTION, "userName",
					shareBean.getUserName(), "friend", user, FriendsBean.class);

			if (friendsBeanfromDB != null) {
				share.setUnFollow(StringUtils.isEmpty(friendsBeanfromDB
						.getUnFollow()) ? "no" : friendsBeanfromDB
						.getUnFollow());
			} else {
				share.setUnFollow("no");
			}
			// share.setUnFollow(StringUtils.isEmpty(friendsBeanfromDB.getUnFollow())?"no":friendsBeanfromDB.getUnFollow());

			insertDB(FabConstants.POST_SEND_TO_COLLECTION, share);
		}

		return mongoManager.findBy2Fields(FabConstants.SHARE_COLLECTION,
				"postMsgId", shareBean.getPostMsgId(), "userName",
				shareBean.getUserName(), ShareBean.class);
	}

	// Count of Shares
	public long countOfShared(String postMsgId) throws Exception {
		return mongoManager.getCountByField(FabConstants.SHARE_COLLECTION,
				"postMsgId", postMsgId, ShareBean.class);
	}

	// delete Shared Post
	public ResponseBean deleteSharedPost(ShareBean shareBean) throws Exception {
		LOGGER.info(":: Get : getAllPosts Method call  ::");
		ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);

		ShareBean shared = mongoManager.getObjectByField(
				FabConstants.SHARE_COLLECTION, "shareId",
				shareBean.getShareId(), ShareBean.class);
		if (shared != null) {
			mongoManager.deleteByField(FabConstants.SHARE_COLLECTION,
					"shareId", shareBean.getShareId());
		} else {
			throw new FabException(StatusCodes.REPLY_ID_NOT_FOUND,
					" postMsgId Not Found ");
		}
		return response;
	}

	// Get Comment Messages by PostMsgId
	public List<CommentResponse> getCommentById(String postMsgId)
			throws Exception {
		LOGGER.info(":: PostDAO : getCommentById Method call  ::");
		List<CommentResponse> commentBeanFromDB = mongoManager
				.getObjectsByField(FabConstants.COMMENT_COLLECTION,
						"postMsgId", postMsgId, CommentResponse.class);

		return commentBeanFromDB;
	}

	// Get unique comment users
	public List<UniqUserResponse> getUniqCommentUsers(String token,
			String postMsgId) throws Exception {
		AuthFilter authFilter = new AuthFilter();
		String userNameFromToken = authFilter.getUserNameFromToken(token);
		LOGGER.info(":: PostDAO : getUniqCommentUsers Method call  ::");
		List<CommentBean> comments = mongoManager.getObjectsByField(
				FabConstants.COMMENT_COLLECTION, "postMsgId", postMsgId,
				CommentBean.class);
		Set<String> uniqUsers = new TreeSet<String>();
		for (CommentBean uniquser : comments) {

			uniqUsers.add(uniquser.getUserName());
		}

		List<UniqUserResponse> commentRespList = new ArrayList<UniqUserResponse>();
		for (String user : uniqUsers) {
			UniqUserResponse res = new UniqUserResponse();
			res.setUserName(user);
			res.setFullName(getFullName(user));
			res.setProfilePID(getProfilePublicId(user));
			if (userNameFromToken == user) {
				res.setIsFriend("0");
			}
			res.setIsFriend(checkFriendStatus(userNameFromToken, user));

			commentRespList.add(res);
		}
		return commentRespList;
	}

	// Get Post Likes
	public List<LikeBean> getLikeList(String token, String postMsgId)
			throws Exception {
		LOGGER.info(":: Get : getLikeList Method call  ::");
		AuthFilter authFilter = new AuthFilter();
		String userNameFromToken = authFilter.getUserNameFromToken(token);

		List<LikeBean> likeList = new ArrayList<LikeBean>();
		List<LikeBean> listFromDB = mongoManager.getObjectsByField(
				FabConstants.LIKE_COLLECTION, "postMsgId", postMsgId,
				LikeBean.class);

		for (LikeBean like : listFromDB) {
			LikeBean likeBean = new LikeBean();
			UserBean userBean = mongoManager.getObjectByField(
					FabConstants.USER_COLLECTION, "userName",
					like.getUserName(), UserBean.class);
			likeBean.setUserName(like.getUserName());
			likeBean.setFullName(userBean.getFirstName() + " "
					+ userBean.getLastName());
			likeBean.setProfilePID(getProfilePublicId(like.getUserName()));

			if (userNameFromToken == like.getUserName()) {
				likeBean.setIsFriend("0");
			}
			likeBean.setIsFriend(checkFriendStatus(userNameFromToken,
					like.getUserName()));
			likeList.add(likeBean);
		}

		return likeList;
	}

	// Get post Shared unique users
	public List<UniqUserResponse> getUniqSharedUsers(String token,
			String postMsgId) throws Exception {
		AuthFilter authFilter = new AuthFilter();
		String userNameFromToken = authFilter.getUserNameFromToken(token);
		LOGGER.info(":: PostDAO : getUniqCommentUsers Method call  ::");
		List<ShareBean> sharedPost = mongoManager.getObjectsByField(
				FabConstants.SHARE_COLLECTION, "postMsgId", postMsgId,
				ShareBean.class);
		Set<String> uniqUsers = new TreeSet<String>();
		for (ShareBean share : sharedPost) {

			uniqUsers.add(share.getUserName());
		}

		List<UniqUserResponse> commentRespList = new ArrayList<UniqUserResponse>();
		for (String user : uniqUsers) {
			UniqUserResponse res = new UniqUserResponse();
			res.setUserName(user);
			res.setFullName(getFullName(user));
			res.setProfilePID(getProfilePublicId(user));

			FriendsBean friendsBeanfromDB = mongoManager.findBy2Fields(
					FabConstants.FRIENDS_COLLECTION, "userName",
					userNameFromToken, "friend", user, FriendsBean.class);
			if (friendsBeanfromDB == null) {
				res.setIsFriend("0");
			} else {
				res.setIsFriend("1");
			}
			commentRespList.add(res);
		}
		return commentRespList;
	}

	// friend Status Check
	public String checkFriendStatus(String userName, String friendName)
			throws Exception {
		LOGGER.info(":: PostDAO : checkFriendStatus Method call  ::");
		FriendsBean friendsBeanfromDB = mongoManager.findBy2Fields(
				FabConstants.FRIENDS_COLLECTION, "userName", userName,
				"friend", friendName, FriendsBean.class);
		String result = null;
		if (friendsBeanfromDB == null) {
			result = "0";
		} else {
			result = "1";
		}
		return result;
	}

	// getCommentByPageSize by PostMsgId
	public List<CommentResponse> getCommentByPageSize(String postMsgId,
			Integer pageNumber, Integer pageSize) throws Exception {
		LOGGER.info(":: PostDAO : getCommentById Method call  ::");

		List<CommentResponse> postComment = mongoManager
				.getObjectsByOrderAndPage(FabConstants.COMMENT_COLLECTION,
						"postMsgId", postMsgId, "created", Direction.DESC,
						pageNumber, pageSize, "commentId",
						CommentResponse.class);

		return postComment;
	}

	/*
	 * 
	 * public NewsFeedResponse getNewsFeedNew(String userName, Integer
	 * pageNumber, Integer pageSize) throws FabException{ NewsFeedResponse
	 * newsFeedResp = new NewsFeedResponse(); LinkedList<PostResponse> postList
	 * = new LinkedList<PostResponse>(); try { String unFollow = "no";
	 * List<PostSendToBean> postSendBeans =
	 * mongoManager.getObjectsByOrderAndPage
	 * (FabConstants.POST_SEND_TO_COLLECTION, "sendBy", userName,"userName",
	 * userName,"unFollow",unFollow,"created", Direction.DESC, pageNumber,
	 * pageSize, "postMsgId", PostSendToBean.class); for(PostSendToBean post :
	 * postSendBeans){ PostResponse postResp = getPostMsgById(userName,
	 * post.getPostMsgId()); postResp.setUnFollow(post.getUnFollow());
	 * //postResp.setShare(countOfShared(post.getPostMsgId()));
	 * postResp.setSendBy(post.getSendBy());
	 * postResp.setShareBy(post.getShareBy());
	 * 
	 * if(postResp.getSendBy()!= null){
	 * postResp.setSendByFullName(getFullName(post.getSendBy()));
	 * postResp.setSendByProfilPID(getProfilePublicId(post.getSendBy())); }else{
	 * postResp.setShareByFullName(getFullName(post.getShareBy()));
	 * postResp.setShareByProfilePID(getProfilePublicId(post.getShareBy())); }
	 * postResp.setPhotos(imageDAO.getImagesBypostMsgId(post.getPostMsgId()));
	 * postList.add(postResp); } newsFeedResp.setPostList(postList);
	 * newsFeedResp
	 * .setPostCount(mongoManager.getPostCount(FabConstants.POST_SEND_TO_COLLECTION
	 * , "sendBy", userName,"userName", userName,"unFollow",unFollow)); } catch
	 * (Exception e) { LOGGER.info(":: Get : getNewsFeedNew Method call  ::",
	 * e); throw new FabException(StatusCodes.REPLY_ID_NOT_FOUND,
	 * " postMsgId Not Found "); } return newsFeedResp;
	 * 
	 * }
	 */

	public NewsFeedResponse getNewsFeedNew(String userName, Integer pageNumber,
			Integer pageSize) throws FabException {
		NewsFeedResponse newsFeedResp = new NewsFeedResponse();
		LinkedList<PostResponse> postList = new LinkedList<PostResponse>();
		try {
			List<PostSendToBean> postSendBeans = mongoManager
					.getObjectsByOrderAndPage(
							FabConstants.POST_SEND_TO_COLLECTION, "sendBy",
							userName, "userName", userName, "unFollow", "no",
							"created", Direction.DESC, pageNumber, pageSize,
							"postMsgId", PostSendToBean.class);

			for (PostSendToBean post : postSendBeans) {
				PostResponse postResp = getPostMsgById(userName,
						post.getPostMsgId());
				postResp.setUnFollow(post.getUnFollow());
				if (post.isShareStatus() == false) {
					postResp.setSendBy(post.getSendBy());
					postResp.setSendByFullName(getFullName(post.getSendBy()));
					postResp.setSendByProfilPID(getProfilePublicId(post
							.getSendBy()));
				} else {
					postResp.setShareBy(post.getShareBy());
					postResp.setShareByFullName(getFullName(post.getShareBy()));
					postResp.setShareByProfilePID(getProfilePublicId(post
							.getShareBy()));
					// postResp.setShare(countOfShared(post.getPostMsgId()));
				}
				postList.add(postResp);
			}
			newsFeedResp.setPostList(postList);
			newsFeedResp.setPostCount(mongoManager.getPostCount(
					FabConstants.POST_SEND_TO_COLLECTION, "sendBy", userName,
					"userName", userName, "unFollow", "no"));
		} catch (Exception e) {
			LOGGER.info(":: Get : getNewsFeedNew Method call  ::", e);
			throw new FabException(StatusCodes.REPLY_ID_NOT_FOUND,
					" postMsgId Not Found ");
		}
		return newsFeedResp;

	}

	private PostResponse getPostMsgById(String loginUserName, String postMsgId)
			throws Exception {
		PostResponse postResp = null;
		PostBean postBeansFromDB = mongoManager.getObjectByField(
				FabConstants.POST_MESSAGE_COLLECTION, "postMsgId", postMsgId,
				PostBean.class);
		UserBean userBean = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName",
				postBeansFromDB.getUserName(), UserBean.class);
		if (postBeansFromDB != null) {
			postResp = getPostElements(userBean, postBeansFromDB);
		}
		return postResp;
	}

	public ResponseBean UpdatePostSend(String userName, String friendName,
			String unFollow) throws Exception {
		LOGGER.info(":: Put : UpdatePostSend Method call  ::");
		ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		try {
			if (StringUtils.isEmpty(unFollow))
				unFollow = "yes";
			mongoManager.updateMulti(FabConstants.POST_SEND_TO_COLLECTION,
					"userName", friendName, "sendBy", userName, "unFollow",
					unFollow);
		} catch (Exception e) {
			LOGGER.info(":: Put : UpdatePostSend Method call  ::", e);
			throw new FabException("Error in updating hide status");
		}
		return response;
	}

	// PUT commentHide
	public CommentBean commentHide(CommentBean commentBean) throws Exception {
		LOGGER.info(":: Get : commentHide Method call  ::");
		CommentBean commentBeanFromDB = mongoManager.findBy2Fields(
				FabConstants.COMMENT_COLLECTION, "commentId",
				commentBean.getCommentId(), null, null, CommentBean.class);
		try {
			if (commentBeanFromDB.isHide() == true) {
				mongoManager.updateBy2Fields(FabConstants.COMMENT_COLLECTION,
						"userName", commentBean.getUserName(), "commentId",
						commentBean.getCommentId(), "hide", false);
			} else {
				mongoManager.updateBy2Fields(FabConstants.COMMENT_COLLECTION,
						"userName", commentBean.getUserName(), "commentId",
						commentBean.getCommentId(), "hide", true);
			}
		} catch (Exception e) {
			throw new FabException(" Error in commentHide method call: " + e);
		}
		return mongoManager.findBy2Fields(FabConstants.COMMENT_COLLECTION,
				"userName", commentBean.getUserName(), "commentId",
				commentBean.getCommentId(), CommentBean.class);
	}

	// PUT commentReplyHide
	public ReplyBean replyHide(ReplyBean replyBean) throws Exception {
		LOGGER.info(":: Get : commentReplyHide Method call  ::");
		ReplyBean reply = mongoManager.findBy2Fields(
				FabConstants.REPLY_COLLECTION, "replyId",
				replyBean.getReplyId(), null, null, ReplyBean.class);
		try {
			if (reply.isHide() == true) {
				mongoManager.updateBy2Fields(FabConstants.REPLY_COLLECTION,
						"userName", reply.getUserName(), "replyId",
						reply.getReplyId(), "hide", false);
			} else {
				mongoManager.updateBy2Fields(FabConstants.REPLY_COLLECTION,
						"userName", reply.getUserName(), "replyId",
						reply.getReplyId(), "hide", true);
			}
		} catch (Exception e) {
			throw new FabException(" Error in commentReplyHide method call: "
					+ e);
		}
		return mongoManager.findBy2Fields(FabConstants.REPLY_COLLECTION,
				"userName", reply.getUserName(), "replyId",
				reply.getCommentId(), ReplyBean.class);
	}

	public PostSendToBean updateHidePost(PostSendToBean postSendToBean)
			throws Throwable {
		try {
			PostSendToBean postSendToFromDB = mongoManager.findBy2Fields(
					FabConstants.POST_SEND_TO_COLLECTION, "userName",
					postSendToBean.getUserName(), "postMsgId",
					postSendToBean.getPostMsgId(), PostSendToBean.class);

			if (postSendToFromDB.isHide() == true) {
				mongoManager.updateBy2Fields(
						FabConstants.POST_SEND_TO_COLLECTION, "userName",
						postSendToBean.getUserName(), "postMsgId",
						postSendToBean.getPostMsgId(), "hide", false);
			} else {
				mongoManager.updateBy2Fields(
						FabConstants.POST_SEND_TO_COLLECTION, "userName",
						postSendToBean.getUserName(), "postMsgId",
						postSendToBean.getPostMsgId(), "hide", true);
			}
		} catch (Exception e) {
			LOGGER.info(":: Put : updateHidePostMsg Method call  ::", e);
			throw new FabException("Error in updating hide status");
		}

		return mongoManager.findBy2Fields(FabConstants.POST_SEND_TO_COLLECTION,
				"userName", postSendToBean.getUserName(), "postMsgId",
				postSendToBean.getPostMsgId(), PostSendToBean.class);
	}

	public ResponseBean updateHideAllPostMsgs(PostSendToBean postSendToBean)
			throws Exception {
		ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		try {
			mongoManager.updateBy2Fields(FabConstants.POST_SEND_TO_COLLECTION,
					"userName", postSendToBean.getUserName(), "sendBy",
					postSendToBean.getSendBy(), "hide", true);
		} catch (Exception e) {
			LOGGER.info(":: Put : updateHidePostMsg Method call  ::", e);
			throw new FabException("Error in updating hide status");
		}
		return response;
	}
}

package com.fab.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fab.constants.FabConstants;
import com.fab.constants.StatusCodes;
import com.fab.exception.FabException;
import com.fab.model.AlbumBean;
import com.fab.model.CircleBean;
import com.fab.model.CoverImageBean;
import com.fab.model.ImageBean;
import com.fab.model.ResponseBean;
import com.fab.model.user.UserBean;
import com.fab.mongo.MongoManager;
import com.mongodb.DB;

@Component
public class ImageDAO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImageDAO.class);

	@Autowired
	MongoManager mongoManager;
	@Autowired 
	TrashCollectionDAO trashCollectionDAO;

	public ImageBean createImage(ImageBean imageBean) throws Exception {
		LOGGER.info(":: createImage DAO call  ::");
		validateUserName(imageBean.getUserName());
		validateAlbum(imageBean.getAlbumName(), imageBean.getUserName());
		validateCircleId(imageBean.getCircleId());
		// Check this image exist or not for user or album or circle
		ImageBean imageFromDB = null;
		LOGGER.info("is cover:"+(StringUtils.isNotEmpty(imageBean.getIsCover()) && imageBean.getIsCover().equalsIgnoreCase("y")));
		if(StringUtils.isNotEmpty(imageBean.getIsCover()) && imageBean.getIsCover().equalsIgnoreCase("y")){
			imageFromDB = mongoManager.findBy2Fields(
					FabConstants.IMAGE_COLLECTION,
					 "userName", imageBean.getUserName(),
					 "isCover", imageBean.getIsCover(), ImageBean.class);
		}		else if(StringUtils.isNotEmpty(imageBean.getIsProfile()) && imageBean.getIsProfile().equalsIgnoreCase("y")){
			imageFromDB = mongoManager.findBy2Fields(
					FabConstants.IMAGE_COLLECTION,
					 "userName", imageBean.getUserName(),
					 "isProfile", imageBean.getIsProfile(), ImageBean.class);
		}else if(StringUtils.isNotEmpty(imageBean.getCircleId())){
			if(StringUtils.isNotEmpty(imageBean.getIsCover()) && imageBean.getIsCover().equalsIgnoreCase("y")){
			imageFromDB = mongoManager.findBy3Fields(
					FabConstants.IMAGE_COLLECTION,
					 "userName", imageBean.getUserName(),
					 "circleId", imageBean.getCircleId(),"isCover", imageBean.getIsCover(), ImageBean.class);
			}else{
				imageFromDB = mongoManager.findBy2Fields(
						FabConstants.IMAGE_COLLECTION,
						 "userName", imageBean.getUserName(),
						 "circleId", imageBean.getCircleId(), ImageBean.class);
			}
		}
		

		if (imageFromDB != null){
			DB db = mongoManager.getMongoDB();
			String imageId = MongoManager.getNextSeqId(db, "img_id_seq");
			imageId = imageId.replaceAll("\\.0*$", "");
			imageBean.setImageId(imageId);
			imageFromDB.setImageDescription(imageBean.getImageDescription());
			imageFromDB.setImageUrl(imageBean.getImageUrl());
			imageFromDB.setPublicId(imageBean.getPublicId());
			imageFromDB.setIsCover(imageBean.getIsCover());
			imageFromDB.setxPosition(imageBean.getxPosition());
			imageFromDB.setyPosition(imageBean.getyPosition());
			imageFromDB.setIsProfile(imageBean.getIsProfile());
			mongoManager.update(FabConstants.IMAGE_COLLECTION, imageFromDB);
		}else{
			DB db = mongoManager.getMongoDB();
			String imageId = MongoManager.getNextSeqId(db, "img_id_seq");
			imageId = imageId.replaceAll("\\.0*$", "");
			imageBean.setImageId(imageId);
			mongoManager.insert(FabConstants.IMAGE_COLLECTION, imageBean);
		} 
		return imageBean;
	}

	private void validateUserName(String userName) throws Exception,
			FabException {
		UserBean userBeanFromDB = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName", userName,
				UserBean.class);
		if (userBeanFromDB == null) {
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST,
					"UserName doesn't exist");
		}
	}

	private void validateCircleId(String circleId) throws Exception {
		if(StringUtils.isNotEmpty(circleId)){
		CircleBean circleid =   mongoManager.getObjectByField(FabConstants.CIRCLE_COLLECTION, "circleId", circleId, CircleBean.class);
		 if(circleid == null){
			 throw new FabException(StatusCodes.CIRCLE_NOT_FOUND, "Circle Id not Found");
		 }
		}
	}
	

	private void validateAlbum(String albumName, String userName)
			throws Exception {
		if (StringUtils.isNotEmpty(albumName)) {
			AlbumBean albumFromDB = mongoManager.findBy2Fields(
					FabConstants.ALBUM_COLLECTION, "albumName", albumName,
					"userName", userName, AlbumBean.class);
			// check album exist or not
			if (albumFromDB == null) {
				throw new FabException(StatusCodes.ALBUM_NOT_EXIST,
						"Album doesn't exist");
			}
		}

	}

	public List<ImageBean> getAlbumImages(String userName, String albumName)
			throws Exception {
		LOGGER.info(":: getAlbumImages DAO call  ::");
		validateUserName(userName);
		validateAlbum(albumName, userName);
		List<ImageBean> images = mongoManager.getObjectsBy2Fields(
				FabConstants.IMAGE_COLLECTION, "userName", userName,
				"albumName", albumName, ImageBean.class);
		return images;
	}

	public CoverImageBean getProfileImage(String userName) throws Exception {
		LOGGER.info(":: getProfileImage DAO call  ::");
		validateUserName(userName);
		List<ImageBean> profImage = mongoManager.getObjectsBy2Fields(
				FabConstants.IMAGE_COLLECTION, "userName", userName,
				"isProfile", "Y", ImageBean.class);
		ImageBean coverImage = mongoManager.findBy2Fields(
				FabConstants.IMAGE_COLLECTION, "userName", userName, "isCover",
				"Y", ImageBean.class);
		if (coverImage != null) {
			profImage.add(coverImage);
		}
		return getCoverImage(profImage);
	}

	
	private CoverImageBean getCoverImage(List<ImageBean> imageList) {
		CoverImageBean coverImage = new CoverImageBean();
		if(CollectionUtils.isNotEmpty(imageList)){
			for(ImageBean imageBean : imageList){
				if(StringUtils.isNotEmpty(imageBean.getIsCover()) && imageBean.getIsCover().equalsIgnoreCase("y")){
					coverImage.setCoverImageDescription(imageBean.getImageDescription());
					coverImage.setCoverImagePublicId(imageBean.getPublicId());
					coverImage.setCoverImageUrl(imageBean.getImageUrl());
					coverImage.setCoverImageId(imageBean.getId());
					coverImage.setCoverImageXPosition(imageBean.getxPosition());
					coverImage.setCoverImageYPosition(imageBean.getyPosition());
				}else{
					BeanUtils.copyProperties(imageBean, coverImage);
				}
			}
		}
		return coverImage;		
	}

	//Get CoverImage
	public ImageBean getCoverImage(String userName) throws Exception {
		LOGGER.info(":: getProfileImage DAO call  ::");
		String isCover = "Y";
		ImageBean coverphoto = mongoManager.getObjectBy2Field(FabConstants.IMAGE_COLLECTION, "userName", userName, "isCover", isCover, ImageBean.class);
		if(coverphoto==null){
			 throw new FabException(StatusCodes.IMAGE_NOT_EXIST, " Cover Image Not Found ");
		}
		
		return coverphoto;
	}
	
	//Get Image
	public ImageBean getProfilePhoto(String userName ) throws Exception {
			LOGGER.info(":: getProfileImage DAO call  ::");
			String isCover = "N";
			ImageBean coverphoto = mongoManager.getObjectBy2Field(FabConstants.IMAGE_COLLECTION, "userName", userName, "isCover", isCover, ImageBean.class);
			if(coverphoto==null){
				 throw new FabException(StatusCodes.IMAGE_NOT_EXIST, " Cover Image Not Found ");
			}
			return coverphoto;
		}
		
	
	
	// POST/update Image Position
		public ImageBean postImagePosition(ImageBean imageBean ) throws Exception {
			LOGGER.info(":: getImagePosition DAO call  ::");
		ImageBean imagePosition = mongoManager.getObjectByField(FabConstants.IMAGE_POSITION, "userName", imageBean.getUserName(), ImageBean.class);
			
			if(imagePosition == null){
			mongoManager.insert(FabConstants.IMAGE_POSITION, imageBean);
			}else{
				
				mongoManager.updateMulti(FabConstants.IMAGE_POSITION, "userName", imageBean.getUserName(), "xAxis", imageBean.getxAxis(), "yAxis", imageBean.getyAxis());
			}
				return mongoManager.getObjectByField(FabConstants.IMAGE_POSITION, "userName", imageBean.getUserName(), ImageBean.class);
			}
		
		//GET Imag Position
		public ImageBean getImgPosition(String userName) throws Exception {
			LOGGER.info(":: getImgPosition DAO call  ::");
			ImageBean imgAxis = new ImageBean();
			imgAxis.setUserName(userName);
			imgAxis.setxAxis("0px");
			imgAxis.setyAxis("0px");

			ImageBean imgePosition = mongoManager.getObjectByField(
					FabConstants.IMAGE_POSITION, "userName",
					userName, ImageBean.class);
			if(imgePosition == null){
				imgePosition = imgAxis	;
			}
			return imgePosition;
		}


	
	
	public CoverImageBean getCircleImage(String circleId, String userName) throws Exception {
		LOGGER.info(":: getCircleImage DAO call  ::");
		validateUserName(userName);
		validateCircleId(circleId);
		List<ImageBean> circleImage = mongoManager.getObjectsBy2Fields(
				FabConstants.IMAGE_COLLECTION, "userName",
				userName, "circleId", circleId, ImageBean.class);
		return getCoverImage(circleImage);
	}

	public ResponseBean deleteImage(String imagePublicId, String userName)//imagId=publicUrl
			throws Exception {
		LOGGER.info(":: deleteImage call  ::");
		validateUserName(userName);

		ImageBean imageFromDB = mongoManager.findBy2Fields(
				FabConstants.IMAGE_COLLECTION, "publicId", imagePublicId, "userName",
				userName, ImageBean.class);
		// check album exist or not
		if (imageFromDB == null) {
			throw new FabException(StatusCodes.IMAGE_NOT_EXIST,
					"image doesn't exist");
		}
		ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		try {
			trashCollectionDAO.createTrashImage(imageFromDB);
			mongoManager.deleteByField(FabConstants.IMAGE_COLLECTION,
					 "publicId", imagePublicId);
		} catch (Exception e) {
			throw new FabException(StatusCodes.DELETE_IMAGE, "Image deleted");
		}
		return response;
	}
	
	public ResponseBean updateImage(ImageBean imageBean) throws Exception {
		validateUserName(imageBean.getUserName());
		ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		ImageBean imageBeanDB = mongoManager.findBy2Fields(
				FabConstants.IMAGE_COLLECTION, "id",
				imageBean.getId(), "userName", imageBean.getUserName(),
				ImageBean.class);
		// check album exist or not
		if (imageBeanDB == null) {
			throw new FabException(StatusCodes.IMAGE_NOT_EXIST,
					"Image doesn't exist");
		}
		imageBeanDB.setImageDescription(imageBean.getImageDescription());
		imageBeanDB.setImageUrl(imageBean.getImageUrl());
		imageBeanDB.setPublicId(imageBean.getPublicId());
		imageBeanDB.setIsCover(imageBean.getIsCover());
		imageBeanDB.setxPosition(imageBean.getxPosition());
		imageBeanDB.setyPosition(imageBean.getyPosition());
		mongoManager.update(FabConstants.IMAGE_COLLECTION, imageBeanDB);

		return response;
	}
	
	   public long getUserImagesCount(String userName)throws Exception{
	    	return  mongoManager.getCountByField(FabConstants.IMAGE_COLLECTION, "userName", userName, ImageBean.class);
	    }

	public List<ImageBean> getAllUserImages(String userName) throws FabException {
		List<ImageBean> images = null;
		try {
			validateUserName(userName);
			images = mongoManager.getObjectsByField(
					FabConstants.IMAGE_COLLECTION, "userName", userName,
					 ImageBean.class);
		} catch (FabException fe) {
			throw fe;
		} catch (Exception e) {
			LOGGER.error("Exception occured in getAllUserImages:",e);
		}
		return images;
	}
	
	public List<ImageBean> getAllNonAlbumImages(String userName) throws FabException {
		List<ImageBean> images = null;
		try {
			validateUserName(userName);
			images = mongoManager.getObjectsBy2Fields(
					FabConstants.IMAGE_COLLECTION, "userName", userName,"albumName", null,
					 ImageBean.class);
		} catch (FabException fe) {
			throw fe;
		} catch (Exception e) {
			LOGGER.error("Exception occured in getAllUserImages:",e);
		}
		return images;
	}

	public List<ImageBean> createImages(List<ImageBean> imageBeans) throws FabException {
		List<ImageBean> tempImgList = new ArrayList<ImageBean>();

		try {

			for (ImageBean image : imageBeans) {
				tempImgList.add(createImage(image));
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured in createImages:", e);
			throw new FabException(StatusCodes.IMAGES_POST_FAILED,
					"Unable to load images");
		}
		return tempImgList;

	}

	public List<ImageBean> getImagesBypostMsgId(String postMsgId) throws Exception {
		return mongoManager.getObjectsByField(
				FabConstants.IMAGE_COLLECTION, "postMsgId", postMsgId,
				 ImageBean.class);
	}

}

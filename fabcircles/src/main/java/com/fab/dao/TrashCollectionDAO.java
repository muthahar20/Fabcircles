package com.fab.dao;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fab.constants.FabConstants;
import com.fab.model.AlbumBean;
import com.fab.model.ImageBean;
import com.fab.mongo.MongoManager;

@Component
public class TrashCollectionDAO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TrashCollectionDAO.class);

	@Autowired
	MongoManager mongoManager;

	public ImageBean createTrashImage(ImageBean imageBean) throws Exception {
		LOGGER.info(":: createTrashImage DAO call  ::");
		// Check this image exist or not for user or album or circle
		ImageBean imageFromDB = mongoManager.findBy2Fields(FabConstants.TRASH_IMAGE_COLLECTION, "publicId", imageBean.getPublicId(), "userName", imageBean.getUserName(), ImageBean.class);
	
				

		if (imageFromDB == null || StringUtils.isNotEmpty(imageBean.getAlbumName())) {
			mongoManager.insert(FabConstants.TRASH_IMAGE_COLLECTION, imageBean);
		} else {
			imageFromDB.setImageDescription(imageBean.getImageDescription());
			imageFromDB.setImageUrl(imageBean.getImageUrl());
			imageFromDB.setPublicId(imageBean.getPublicId());
			imageFromDB.setIsCover(imageBean.getIsCover());
			imageFromDB.setxPosition(imageBean.getxPosition());
			imageFromDB.setyPosition(imageBean.getyPosition());
			mongoManager.update(FabConstants.TRASH_IMAGE_COLLECTION, imageFromDB);
		}

		return imageBean;
	}
	public AlbumBean createTrashAlbum(AlbumBean albumBean) throws Exception {
		LOGGER.info(":: createTrashAlbum DAO call  ::");
		AlbumBean albumFromDB = mongoManager.findBy2Fields(FabConstants.TRASH_ALBUM_COLLECTION, "id", albumBean.getId(), "userName", albumBean.getUserName(), AlbumBean.class);
	
				

		if (albumFromDB == null || StringUtils.isNotEmpty(albumFromDB.getAlbumName())) {
			mongoManager.insert(FabConstants.TRASH_ALBUM_COLLECTION, albumFromDB);
		} else {
			BeanUtils.copyProperties(albumFromDB, albumBean);
			
			mongoManager.update(FabConstants.TRASH_ALBUM_COLLECTION, albumFromDB);
		}

		return albumFromDB;
	}
	
	public List<ImageBean> getTrashImages(String userName) throws Exception {
		LOGGER.info(":: getTrashImages DAO call  ::");

		List<ImageBean> albums = mongoManager.getObjectsByField(
				FabConstants.TRASH_IMAGE_COLLECTION, "userName", userName,
				ImageBean.class);
		return albums;
	}

}

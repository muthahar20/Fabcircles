package com.fab.dao;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fab.constants.FabConstants;
import com.fab.constants.StatusCodes;
import com.fab.exception.FabException;
import com.fab.model.AlbumBean;
import com.fab.model.ResponseBean;
import com.fab.model.user.UserBean;
import com.fab.mongo.MongoManager;

@Component
public class AlbumDAO {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AlbumDAO.class);

	@Autowired
	MongoManager mongoManager;
	@Autowired
	TrashCollectionDAO trashCollectionDAO;

	public AlbumBean createAlbum(AlbumBean albumBean) throws Exception {
		LOGGER.info(":: createAlbum DAO call  ::");
		UserBean userBeanFromDB = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName",
				albumBean.getUserName(), UserBean.class);
		if (userBeanFromDB == null) {
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST,
					"UserName doesn't exist");
		}
		// Check this album name exist or not for user
		AlbumBean albumFromDB = mongoManager.findBy2Fields(
				FabConstants.ALBUM_COLLECTION, "albumName",
				albumBean.getAlbumName(), "userName", albumBean.getUserName(),
				AlbumBean.class);

		if (albumFromDB != null) {
			LOGGER.info("Album name" + albumBean.getAlbumName()
					+ " is already exists in system for user"
					+ albumBean.getUserName());
			throw new FabException(StatusCodes.ALBUM_ALREADY_EXISTS,
					"Album name is already exists in system");

		}
		try {
			mongoManager.insert(FabConstants.ALBUM_COLLECTION, albumBean);

		} catch (Exception e) {
			throw new FabException(StatusCodes.ALBUM_NOT_CREATED,
					"Album not created");
		}
		return albumBean;
	}

	public List<AlbumBean> getAlbums(String userName) throws Exception {
		LOGGER.info(":: getAlbums DAO call  ::");
		UserBean userBeanFromDB = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName", userName,
				UserBean.class);
		if (userBeanFromDB == null) {
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST,
					"UserName doesn't exist");
		}
		// If user exist get all the albums
		List<AlbumBean> albums = mongoManager.getObjectsByField(
				FabConstants.ALBUM_COLLECTION, "userName", userName,
				AlbumBean.class);
		return albums;
	}

	public ResponseBean deleteAlbum(String albumName, String userName)
			throws Exception {
		LOGGER.info(":: deleteAlbumDAO call  ::");
		// Check User is exist or not
		UserBean userBeanFromDB = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName", userName,
				UserBean.class);
		if (userBeanFromDB == null) {
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST,
					"UserName doesn't exist");
		}

		AlbumBean albumFromDB = mongoManager.findBy2Fields(
				FabConstants.ALBUM_COLLECTION, "albumName", albumName,
				"userName", userName, AlbumBean.class);
		// check album exist or not
		if (albumFromDB == null) {
			throw new FabException(StatusCodes.ALBUM_NOT_EXIST,
					"Album doesn't exist");
		}
		ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		try {
			trashCollectionDAO.createTrashAlbum(albumFromDB);
			mongoManager.deleteBy2Field(FabConstants.ALBUM_COLLECTION, "userName", userName, "albumName", albumName);
		} catch (Exception e) {
			throw new FabException(StatusCodes.DELETE_ALBUM, "Alubum deleted");
		}
		return response;
	}

	public ResponseBean updateAlbum(AlbumBean album) throws Exception {
		UserBean userBeanFromDB = mongoManager.getObjectByField(
				FabConstants.USER_COLLECTION, "userName", album.getUserName(),
				UserBean.class);

		ResponseBean response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE);
		if (userBeanFromDB == null) {
			throw new FabException(StatusCodes.USER_ID_NOT_EXIST,
					"UserName doesn't exist");
		}
		AlbumBean albumFromDB = mongoManager.findBy2Fields(
				FabConstants.ALBUM_COLLECTION, "id",
				album.getId(), "userName", album.getUserName(),
				AlbumBean.class);
		// check album exist or not
		if (albumFromDB == null) {
			throw new FabException(StatusCodes.ALBUM_NOT_EXIST,
					"Album doesn't exist");
		}
		BeanUtils.copyProperties(albumFromDB, album);
		mongoManager.update(FabConstants.ALBUM_COLLECTION, albumFromDB);

		return response;
	}
}

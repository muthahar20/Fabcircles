package com.fab.resources.images;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fab.dao.AlbumDAO;
import com.fab.dao.ImageDAO;
import com.fab.dao.TrashCollectionDAO;
import com.fab.exception.FabException;
import com.fab.model.AlbumBean;
import com.fab.model.AlbumImageResponse;
import com.fab.model.ImageBean;
import com.fab.model.ResponseBean;
import com.fab.resources.BaseResource;

@Path("album")
@Produces(APPLICATION_JSON)
public class AlbumResource extends BaseResource {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AlbumResource.class);
	@Autowired
	AlbumDAO albumDAO;
	@Autowired
    ImageDAO imageDAO;
	@Autowired
	TrashCollectionDAO trashCollectionDAO;

	@POST
	@Consumes(APPLICATION_JSON)
	// Accept AlbumRequest as a RequestBody
	public AlbumBean createAlbum(AlbumBean albumRequest) throws Exception {
		LOGGER.info("createAlbum POST call ::");
		AlbumBean response = null;
		try {
			response = albumDAO.createAlbum(albumRequest);
		} catch (FabException fe) {
			LOGGER.error("Error createAlbum API POST ", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}

	
	/*
	@GET
	@Path("/list")
	@Produces(APPLICATION_JSON)
	public List<AlbumBean> getAlbums(@QueryParam("userName") String userName)
			throws Exception {
		List<AlbumBean> circles = null;
		circles = albumDAO.getAlbums(userName);
		return circles;
	}
	*/
	
	
	@GET
	@Path("/list")
	@Produces(APPLICATION_JSON)
	public List<AlbumImageResponse> getAlbums(@QueryParam("userName") String userName)
			throws Exception {
		List<AlbumImageResponse> albumImageList = new ArrayList<AlbumImageResponse>();
		List<AlbumBean> albumList = null;
		try {
			albumList = albumDAO.getAlbums(userName);
			if (CollectionUtils.isNotEmpty(albumList)) {
				for (AlbumBean albumBean : albumList) {
					AlbumImageResponse albumImage = new AlbumImageResponse();
					BeanUtils.copyProperties(albumImage, albumBean);

					List<ImageBean> imageList = imageDAO.getAlbumImages(userName,albumBean.getAlbumName());
					//albumImage.setImageList(imageDAO.getAlbumImages(userName,albumBean.getAlbumName()));
					if(!imageList.isEmpty()){
					albumImage.setAlbumCoverImage(imageList.get(0));
					albumImage.setImagesCount(imageList.size());
					}else{
						albumImage.setImageList(imageList);
						albumImage.setImagesCount(0L);
					}
					 
					 albumImageList.add(albumImage);
				}
					
			}
		} catch (Exception ex) {
			LOGGER.error("Error getAllAlbumImages ", ex);
		ex.printStackTrace();
		}

		return albumImageList;
	}
	
	

	@GET
	@Path("/list/images")
	@Produces(APPLICATION_JSON)
	public List<AlbumImageResponse> getAllAlbumImages(
			@QueryParam("userName") String userName) throws Exception {
		LOGGER.info("getAllAlbumImages GET call ::");
		List<AlbumImageResponse> albumImageList = new ArrayList<AlbumImageResponse>();
		List<AlbumBean> albumList = null;
		try {
			albumList = albumDAO.getAlbums(userName);
			if (CollectionUtils.isNotEmpty(albumList)) {
				for (AlbumBean albumBean : albumList) {
					AlbumImageResponse albumImage = new AlbumImageResponse();
					BeanUtils.copyProperties(albumImage, albumBean);
//					imageMap.put(
//							albumBean,
//							imageDAO.getAlbumImages(userName,
//									albumBean.getAlbumName()));
					albumImage.setImageList(imageDAO.getAlbumImages(userName,albumBean.getAlbumName()));
					albumImageList.add(albumImage);
				}
					
			}
		} catch (Exception ex) {
			LOGGER.error("Error getAllAlbumImages ", ex);
		ex.printStackTrace();
		}

		return albumImageList;
	}

	
	
	
	@DELETE
	@Path("/delete")
	@Produces(APPLICATION_JSON)
	@Consumes(APPLICATION_JSON)
	public ResponseBean deleteAlbum(AlbumBean album) throws Exception {
		LOGGER.info(" deleteAlbum api Call ::");
		ResponseBean response = null;
		try {
			List<ImageBean> imageList = imageDAO.getAlbumImages(album.getUserName(), album.getAlbumName());
			for(ImageBean imageBean : imageList){
				imageDAO.deleteImage(imageBean.getId(), album.getUserName());
			}
			response = albumDAO.deleteAlbum(album.getAlbumName(), album.getUserName());
		} catch (FabException fe) {
			LOGGER.error("Error deleteAlbum ", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	@PUT
	@Path("/update")
	@Consumes(APPLICATION_JSON)
	public ResponseBean updateAlbum(AlbumBean album) throws Exception {
		LOGGER.info(" updateAlbum api Call ::");
		ResponseBean response = null;
		try {
			response = albumDAO.updateAlbum(album);
		} catch (FabException fe) {
			LOGGER.error("Error SentFriendRequestResource ", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
}

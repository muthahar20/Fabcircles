package com.fab.resources.images;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import com.fab.dao.ImageDAO;
import com.fab.exception.FabException;
import com.fab.model.CoverImageBean;
import com.fab.model.ImageBean;
import com.fab.model.ResponseBean;
import com.fab.resources.BaseResource;

@Path("image")
@Produces(APPLICATION_JSON)
public class ImageResource extends BaseResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImageResource.class);
	@Autowired
	ImageDAO imageDAO;

	@POST
	@Consumes(APPLICATION_JSON)
	// Accept Image Bean as a RequestBody
	public ImageBean createImage(ImageBean imageBean) throws Exception {
		LOGGER.info("createImage POST call ::");
		ImageBean response = null;
		try {
			response = imageDAO.createImage(imageBean);
		} catch (FabException fe) {
			LOGGER.error("Error Circle API POST ", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	@POST
	@Path("/listimages")
	@Consumes(APPLICATION_JSON)
	public List<ImageBean> createImages(@RequestBody List<ImageBean> imageBeans) throws Exception {
		LOGGER.info("createImage POST call ::");
		List<ImageBean> response = null;
		try {
			response = imageDAO.createImages(imageBeans);
		} catch (FabException fe) {
			LOGGER.error("Error listimages post ", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}


	@GET
	@Path("/album/list")
	@Produces(APPLICATION_JSON)
	public List<ImageBean> getAlbumImages(
			@QueryParam("userName") String userName,
			@QueryParam("albumName") String albumName) throws Exception {
		List<ImageBean> albumImages = null;
		albumImages = imageDAO.getAlbumImages(userName, albumName);
		return albumImages;
	}
	
	
	@POST
	@Path("/imageposition")
	@Produces(APPLICATION_JSON)
	public ImageBean getImagePosition( ImageBean imageBean) throws Exception {
		LOGGER.info("getImagePosition POST call ::");
		ImageBean response = null;
		try {
			response = imageDAO.postImagePosition(imageBean);
		} catch (FabException fe) {
			LOGGER.error("Error Circle API POST ", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}

	


	@GET
	@Path("/profile")
	@Produces(APPLICATION_JSON)
	public CoverImageBean getProfileImage(@QueryParam("userName") String userName)
			throws Exception {
		return imageDAO.getProfileImage(userName);		
	}
	
	

	@GET
	@Path("/circle")
	@Produces(APPLICATION_JSON)
	public CoverImageBean getCircleImage(@QueryParam("userName") String userName,
			@QueryParam("circleId") String circleId) throws Exception {
		try{
			return imageDAO.getCircleImage(circleId, userName);
		}
		
		catch (FabException fe) {
			LOGGER.error("Error Circle API POST ", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return null;
	}
	
	
	@GET
	@Path("/coverimage")
	@Produces(APPLICATION_JSON)
	public ImageBean getcoverImager(@QueryParam("userName") String userName) throws Exception {
		try{
			ImageBean circleImage = null;
			System.out.println(" coverImage");
			circleImage = imageDAO.getCoverImage(userName);
			return circleImage;
		}
		
		catch (FabException fe) {
			LOGGER.error("Error Circle API POST ", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return null;
		
	}

	@DELETE
	@Path("/delete")
	@Produces(APPLICATION_JSON)
	@Consumes(APPLICATION_JSON)
	public ResponseBean deleteImage(ImageBean imageBean) throws Exception {
		LOGGER.info(" deleteImage api Call ::");
		ResponseBean response = null;
		try {

			response = imageDAO.deleteImage(imageBean.getPublicId(),
					imageBean.getUserName());
		} catch (FabException fe) {
			LOGGER.error("Error deleteImage ", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}

	
	
	@PUT
	@Path("/update")
	@Consumes(APPLICATION_JSON)
	public ResponseBean updateImage(ImageBean imageBean) throws Exception {
		LOGGER.info(" updateImage api Call ::");
		ResponseBean response = null;
		try {
			response = imageDAO.updateImage(imageBean);
		} catch (FabException fe) {
			LOGGER.error("Error updateImage ", fe);
			buildErrorResponse(Response.Status.CONFLICT, fe.getErrorCode(),
					fe.getMessage());
		}
		return response;
	}
	
	@GET
	@Path("/list")
	@Produces(APPLICATION_JSON)
	public List<ImageBean> getImagesList(
			@QueryParam("userName") String userName) throws Exception {
		List<ImageBean> images = null;
		images = imageDAO.getAllUserImages(userName);
		return images;
	}
	@GET
	@Path("/myimages")
	@Produces(APPLICATION_JSON)
	public List<ImageBean> getNonAlbumImagesList(
			@QueryParam("userName") String userName) throws Exception {
		List<ImageBean> images = null;
		images = imageDAO.getAllNonAlbumImages(userName);
		return images;
	}
	@GET
	@Path("/count")
	@Produces(APPLICATION_JSON)
	public ImageCountBean getImagesCount(
			@QueryParam("userName") String userName) throws Exception {
		ImageCountBean imagecount = new ImageCountBean();
		imagecount.setUserName(userName);
		imagecount.setImageCount(imageDAO.getUserImagesCount(userName));
		return imagecount;
	}
}

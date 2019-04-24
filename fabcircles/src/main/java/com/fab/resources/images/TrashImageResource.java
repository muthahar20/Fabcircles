package com.fab.resources.images;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;

import com.fab.dao.TrashCollectionDAO;
import com.fab.model.ImageBean;
import com.fab.resources.BaseResource;

@Path("trashimage")
@Produces(APPLICATION_JSON)
public class TrashImageResource extends BaseResource {

	@Autowired
	TrashCollectionDAO trashCollectionDAO;

	@GET
	@Path("/list")
	@Produces(APPLICATION_JSON)
	public List<ImageBean> getTrashImages(
			@QueryParam("userName") String userName) throws Exception {
		List<ImageBean> trashImages = null;
		trashImages = trashCollectionDAO.getTrashImages(userName);
		return trashImages;
	}	
}

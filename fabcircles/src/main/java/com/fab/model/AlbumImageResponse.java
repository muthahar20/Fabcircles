package com.fab.model;

import java.util.List;

public class AlbumImageResponse extends AlbumBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4892172237920465274L;
private List<ImageBean> imageList;
private ImageBean albumCoverImage;
private long imagesCount;



public ImageBean getAlbumCoverImage() {
	return albumCoverImage;
}
public void setAlbumCoverImage(ImageBean albumCoverImage) {
	this.albumCoverImage = albumCoverImage;
}
public static long getSerialversionuid() {
	return serialVersionUID;
}
public List<ImageBean> getImageList() {
	return imageList;
}
public void setImageList(List<ImageBean> imageList) {
	this.imageList = imageList;
}
public long getImagesCount() {
	return imagesCount;
}
public void setImagesCount(long imagesCount) {
	this.imagesCount = imagesCount;
}

}

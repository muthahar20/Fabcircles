package com.fab.model;

import java.util.LinkedList;

public class NewsFeedResponse {
	
	
private LinkedList<PostResponse> postList;
private long postCount;



public LinkedList<PostResponse> getPostList() {
	return postList;
}
public void setPostList(LinkedList<PostResponse> postList) {
	this.postList = postList;
}
public long getPostCount() {
	return postCount;
}
public void setPostCount(long postCount) {
	this.postCount = postCount;
}


}

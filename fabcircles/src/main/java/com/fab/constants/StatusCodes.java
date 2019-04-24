package com.fab.constants;

import org.springframework.stereotype.Component;

@Component
public class StatusCodes {
	
	public static String SUCCESS = "200";
	public static String SUCCESS_MESSAGE = "Success";
	public static String USER_ID_EXIST = "101";
	public static String PASSWORD_INCORRECT = "103";
	public static String USER_ID_NOT_EXIST = "104";
	public static String SUMMARY_INFO_NOT_FOUND = "105";
	public static String CONTACTBASIC_INFO_NOT_FOUND = "106";
	public static String EDUCATION_INFO_NOT_FOUND = "107";
	public static String WORK_INFO_NOT_FOUND = "108";
	public static String PLACESLIVED_INFO_NOT_FOUND = "109";
	
	//Friends
	public static String FRIEND_REQUEST_EXIST = "201";
	
	public static String FRIENDS_LIST_NOT_FOUND = "207";
	
	public static String DELETE_FRIEND_REQUEST_SUCCESS = "202";
	
	public static String FRIEND_REQUEST_NOT_EXIST = "203";
	
	
	
	public static String CANCEL_SENT_FRIEND_REQUEST_DOES_NOT_EXIST = "204";
	
	public static String FRIEND_REQUEST_SENT="205";
	
	public static String FRIEND_ADDED_SUCCESSFULLY="206";
	
	//Circlces
	public static String CIRCLE_NOT_CREATED= "301";
	public static String DELETE_CIRCLE="302";
	public static String CIRCLE_NOT_FOUND="303";
	public static String CIRCLE_MEMBER_ALREADY_EXIST="304";
	public static String CIRCLEMEMBER_EXIST="306";
	public static String CIRCLEMEMBER_NOT_FOUND="307";
	public static String CIRCLEMEMBERS_FOUND="308";
	
	
	//Albums and images
	public static String ALBUM_ALREADY_EXISTS ="401";
	public static String ALBUM_NOT_CREATED= "402";
	public static String ALBUM_NOT_EXIST="403";
	public static String DELETE_ALBUM="404";
	public static String IMAGE_NOT_EXIST="405";
	public static String DELETE_IMAGE="406";
	
	//Comment and Like
	public static String COMMENT_OR_LIKE_FOUND ="500";
	public static String POST_MSG_ID_NOT_FOUND = "501";
	//Reply
	public static String REPLY_ID_NOT_FOUND = "502";
	//friend
	public static String FRIEND_ID_NOT_FOUND = "503";
	public static String DIS_LIKE = "504";
	
	public static String IMAGES_POST_FAILED = "505";
	

}

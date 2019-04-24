package com.fab.model;

import com.fab.model.user.BaseBean;

public class ResponseBean extends BaseBean{
	
	public ResponseBean(){
		
	}
	
	public ResponseBean(String message){
		this.message = message;
	}
	
	public ResponseBean(String errorCode, String message){
		this.message = message;
		this.errorCode = errorCode;
	}
	
	private String errorCode;
	private String message;
	
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	

}

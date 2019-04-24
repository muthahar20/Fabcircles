package com.fab.exception;

import java.util.Date;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  String errorCode = null;
	private Status status = null;

	private static final Logger LOGGER = LoggerFactory.getLogger(FabException.class);



    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
    /**
     * Create exception object with the default message.
     */

    public FabException(String errorCode, String message){
    	super(message);
    	this.errorCode = errorCode;
        }
    public FabException(String errorCode, String message, Status status){
    	super(message);
    	this.errorCode = errorCode;
    	this.status = status;
    	
        }

    public FabException(String errorCode){
    	super(errorCode);
        }
    
    public FabException(String code, Throwable cause){
    	super(code, cause);
        }

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}




	
    
    
    
    
}

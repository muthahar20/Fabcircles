package com.fab.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsumerCleint {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ApplicationContext ctx = new ClassPathXmlApplicationContext("rootContext.xml");
        
	    // sleep for 1 second
	    try {
	      Thread.sleep(1000);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
		
	}

}

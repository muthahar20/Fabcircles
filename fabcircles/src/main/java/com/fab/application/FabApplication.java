package com.fab.application;

import org.glassfish.jersey.server.ResourceConfig;

public class FabApplication extends ResourceConfig{

	  public FabApplication() {
		  System.out.println("application class exectuted");
	        
      packages("com.fab.resources", "com.fab.filter");
	        
	  }
	
}

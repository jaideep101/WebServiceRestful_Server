package com.service.webservice;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("rest")
public class ApplicationConfig extends Application {
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new HashSet<>();
		addRestResourcesClasses(resources);
		return resources;
	}
	
	private void addRestResourcesClasses(Set<Class<?>> resources){
		resources.add(DemoRest.class);
	}
}

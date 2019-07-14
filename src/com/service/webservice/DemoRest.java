package com.service.webservice;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.service.webservice.database.MysqlCon;

@Path("demo")
public class DemoRest {
	
	@GET
	@Path("hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello(){
		System.out.println("Hello");
		MysqlCon.getResultSet("", "sampledb");
		
		return "Name : "+MysqlCon.name+"\n "+"Age : "+MysqlCon.age+"\n "+"Place : "+MysqlCon.place;
	}
}

package com.projectthree.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class Index {

	@GET
	public String sayHello() {
		return "Hello, Speedyworld!";
	}
}

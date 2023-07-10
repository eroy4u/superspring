package com.example.superspring;

import org.springframework.stereotype.Service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("rs")
public interface RsInterface {
	@Path("")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
    String index();

}
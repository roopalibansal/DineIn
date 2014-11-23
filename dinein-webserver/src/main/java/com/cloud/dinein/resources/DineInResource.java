package com.cloud.dinein.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jersey.repackaged.com.google.common.collect.Lists;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.cloud.dinein.resources.YelpAPI.YelpAPICLI;
import com.codahale.metrics.annotation.Timed;

@Path("/dinein")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DineInResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DineInResource.class);

	private static final String CONSUMER_KEY = "n0_77dKOlQZHUZlVpZMXpg";
	private static final String CONSUMER_SECRET = "JgXp1lJITT6s15eFEsA2Ee7D0Ss";
	private static final String TOKEN = "MdS4Fw-otpzuL1sjbp7mCLK8eGD0HRWo";
	private static final String TOKEN_SECRET = "UqXd617s6O9kBAcV_oxNcGl4a4A";
	
	private YelpAPICLI yelpApiCli = new YelpAPICLI();
	private YelpAPI yelpApi = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN,
			TOKEN_SECRET);



	@GET
	@Path("get-restaurants/{dish}/{location}")
	public Object getRestaurants(@PathParam("dish") String dish,
			@PathParam("location") String location) {
		
		String[] args = new String[4];
		args[0] = "--term";
		
		args[1] = dish;
		args[2] = "--location";
		args[3] = location;
		LOGGER.error("dish" + dish);
		LOGGER.error("location" + location);
		new JCommander(yelpApiCli, args);

		JSONObject response = YelpAPI.queryAPI(yelpApi, yelpApiCli);
		LOGGER.error("Final Response: " + response);

		return response;
	}
	
	
	
	
	

}

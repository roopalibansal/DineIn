package com.cloud.dinein.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.cloud.dinein.DineInResourceInterface;
import com.cloud.dinein.db.RestaurantDAO;
import com.cloud.dinein.resources.YelpAPI.YelpAPICLI;
import com.google.common.collect.Maps;

@Path("/dinein")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DineInResource implements DineInResourceInterface {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DineInResource.class);

	private static final String CONSUMER_KEY = "n0_77dKOlQZHUZlVpZMXpg";
	private static final String CONSUMER_SECRET = "JgXp1lJITT6s15eFEsA2Ee7D0Ss";
	private static final String TOKEN = "MdS4Fw-otpzuL1sjbp7mCLK8eGD0HRWo";
	private static final String TOKEN_SECRET = "UqXd617s6O9kBAcV_oxNcGl4a4A";

	private YelpAPICLI yelpApiCli = new YelpAPICLI();
	private YelpAPI yelpApi = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN,
			TOKEN_SECRET);
	private final RestaurantDAO dao;

	public DineInResource(RestaurantDAO dao) {
		this.dao = dao;

		// test code
		dao.createRestaurantTable();

		// TODO Auto-generated constructor stub
	}

	@GET
	@Path("get-restaurants/{dish}/{location}")
	public Map<String, String> getRestaurants(@PathParam("dish") String dish,
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

		ArrayList<HashMap<String, String>> restaurantNames = (ArrayList<HashMap<String, String>>) response
				.get("businesses");

		List<String> restaurants = new ArrayList<String>();

		for (HashMap<String, String> restaurantInfo : restaurantNames) {
			restaurants.add(restaurantInfo.get("id"));
		}
		return getDeals(restaurants);
	}

	@POST
	@Path("add-deal/{restaurant}/{deal}")
	public void addDeal(@PathParam("restaurant") String restaurant,
			@PathParam("deal") String deal) {
		dao.insert(restaurant, deal);
	}

	@GET
	@Path("get-deals")
	public Map<String, String> getDeals(
			@QueryParam("restaurant-list") List<String> restaurantList) {
		Map<String, String> dealsList = Maps.newHashMap();
		// ideally send one query for all.
		for (String restaurant : restaurantList) {
			dealsList.put(restaurant, dao.getDealByName(restaurant));
		}
		return dealsList;
	}

}

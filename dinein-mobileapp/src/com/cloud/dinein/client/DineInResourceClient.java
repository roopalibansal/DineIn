package com.cloud.dinein.client;

import java.util.List;
import java.util.Map;

import javax.inject.Named;
import org.json.simple.JSONObject;

import com.cloud.dinein.DineInResourceInterface;

import feign.Headers;
import feign.RequestLine;

public interface DineInResourceClient extends DineInResourceInterface {

	@RequestLine("GET /dinein/get-restaurants/{dish}/{location}")
	Map<String,String> getRestaurants(@Named("dish") String dish,
			@Named("location") String location);

	@RequestLine("POST /dinein/add-deal/{restaurant}/{deal}")
	@Headers("Content-Type: application/json")
	public void addDeal(@Named("restaurant") String restaurant,
			@Named("deal") String deal);

	@RequestLine("GET get-deals/{restaurant-list}")
	public Map<String, String> getDeals(
			@Named("restaurant-list") List<String> restaurantList);
}
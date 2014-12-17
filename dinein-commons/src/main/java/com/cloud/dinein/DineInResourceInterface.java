package com.cloud.dinein;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public interface DineInResourceInterface {
	Map<String, String> getRestaurants(String dish, String location);

	public void addDeal(String restaurant, String deal);

	public Map<String, String> getDeals(List<String> restaurantList);
}

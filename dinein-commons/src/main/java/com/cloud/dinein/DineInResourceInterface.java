package com.cloud.dinein;

import org.json.simple.JSONObject;



public interface DineInResourceInterface {
	JSONObject getRestaurants(String dish, String location);
}

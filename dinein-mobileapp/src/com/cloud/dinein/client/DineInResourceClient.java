package com.cloud.dinein.client;

import javax.inject.Named;

import org.json.simple.JSONObject;


import com.cloud.dinein.DineInResourceInterface;

import feign.RequestLine;

public interface DineInResourceClient extends DineInResourceInterface{

	@RequestLine("GET /dinein/get-restaurants/{dish}/{location}")
	JSONObject getRestaurants(@Named("dish") String dish,
			@Named("location") String location);

}
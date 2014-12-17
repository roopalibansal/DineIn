package com.cloud.dinein;

import com.cloud.dinein.client.DineInResourceClient;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class DineinClientImpl {

	private static final String EC2_HOST = "http://ec2-54-173-191-212.compute-1.amazonaws.com:8080";
	//private static final String EC2_HOST = "http://localhost:8000";
	public static final DineInResourceInterface client = Feign.builder()
			.encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
			.target(DineInResourceClient.class, EC2_HOST);
	


}

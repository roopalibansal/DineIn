package com.cloud.dinein.client;

import com.cloud.dinein.DineInResourceInterface;

import feign.Feign;

public class DineInClientTest {

	public DineInClientTest() {
		DineInResourceInterface client = Feign.builder().target(
				DineInResourceClient.class, "http://localhost:8080/");
		Object obj = client.getRestaurants("dish", "new york");
		System.out.println("Objects: " + obj);

	}
	
	public static void main(String[] args){
		DineInClientTest test = new DineInClientTest();
	}
}

package com.cloud.dinein;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;

import com.cloud.dinein.DineInResourceInterface;
import com.cloud.dinein.client.DineInResourceClient;
import com.cloud.dinein.R;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchActivity extends Activity {

	private static final String EC2_HOST = "http://ec2-54-173-191-212.compute-1.amazonaws.com:8080";

	private final DineInResourceInterface client = Feign.builder()
			.encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
			.target(DineInResourceClient.class, EC2_HOST);

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		


		System.out.println("Reached oncreate");
		Intent intent = getIntent();
	    String dish = intent.getStringExtra(HomeActivity.DISH_MESSAGE);
	    String location = intent.getStringExtra(HomeActivity.LOCATION_MESSAGE);
	    System.out.println("Dish : " + dish);
	    System.out.println("Location : " + location);
	    
		new RetrieveRestaurantsTask().execute(Pair.create(dish, location));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class RetrieveRestaurantsTask extends
			AsyncTask<Pair<String, String>, Void, List<String>> {

		@Override
		protected List<String> doInBackground(
				Pair<String, String>... dishLocationPairs) {
			JSONObject response = SearchActivity.this.client.getRestaurants(dishLocationPairs[0].first,
					dishLocationPairs[0].second);

			@SuppressWarnings("unchecked")
			ArrayList<HashMap<String, String>> restaurantNames = (ArrayList<HashMap<String, String>>) response
					.get("businesses");

			List<String> restaurants = new ArrayList<String>();

			for (HashMap<String, String> restaurantInfo : restaurantNames) {
				restaurants.add(restaurantInfo.get("id"));
			}

			return restaurants;
		}

		@Override
		protected void onPostExecute(List<String> restaurants) {
			setContentView(R.layout.search_results);
			ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
					SearchActivity.this, android.R.layout.simple_list_item_1,
					restaurants);

			ListView lv = (ListView) SearchActivity.this
					.findViewById(R.id.restaurant_list);

			lv.setAdapter(mAdapter);
			
		}
	}
}

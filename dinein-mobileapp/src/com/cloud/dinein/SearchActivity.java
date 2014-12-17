package com.cloud.dinein;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import com.cloud.dinein.R;

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
			AsyncTask<Pair<String, String>, Void, Map<String, String>> {

		@Override
		protected Map<String, String> doInBackground(
				Pair<String, String>... dishLocationPairs) {
			System.out.println("\n\n\nPair:  " +dishLocationPairs+ "\n\n");
			
			Map<String, String> response = DineinClientImpl.client
					.getRestaurants(dishLocationPairs[0].first,
							dishLocationPairs[0].second);

			return response;
		}

		
		// TODO : we need listview of restaurant objects instead of strings. 
		// Figure out how to do this!!!
		@Override
		protected void onPostExecute(Map<String, String> restaurantDealMap) {
			setContentView(R.layout.search_results);
			List<String> restaurantDealList = new ArrayList<String>();
			for (Entry<String, String> restaurantDealPair : restaurantDealMap
					.entrySet()) {
				restaurantDealList.add(restaurantDealPair.getKey() + "Deal :"
						+ restaurantDealPair.getValue());
			}

			ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
					SearchActivity.this, android.R.layout.simple_list_item_1,
					restaurantDealList);

			ListView lv = (ListView) SearchActivity.this
					.findViewById(R.id.search_results_restaurant_list);

			lv.setAdapter(mAdapter);

		}
	}
}

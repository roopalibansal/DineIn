package com.cloud.dinein;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PushDealsActivity extends Activity {

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.push_deals);
	}

	@SuppressWarnings("unchecked")
	public void addDealActivity(View view) {
		EditText restaurantView = (EditText) findViewById(R.id.push_deals_restaurant);
		EditText dealView = (EditText) findViewById(R.id.push_deals_deal);
		String restaurant = restaurantView.getText().toString();
		String deal = dealView.getText().toString();
		new AddDealTask().execute(Pair.create(restaurant, deal));
	}

	private class AddDealTask extends
			AsyncTask<Pair<String, String>, Void, Void> {

		@Override
		protected Void doInBackground(
				Pair<String, String>... restaurantDealPairs) {
			DineinClientImpl.client.addDeal(restaurantDealPairs[0].first,
					restaurantDealPairs[0].second);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void temp) {

			Toast toast = Toast.makeText(getApplicationContext(),
					"Successfully added Deal", Toast.LENGTH_SHORT);
			// Change the positioning 
			toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
			toast.show();
		
		}

		
	}

}

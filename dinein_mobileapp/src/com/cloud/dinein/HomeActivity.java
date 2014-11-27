package com.cloud.dinein;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HomeActivity extends Activity {
	
    public final static String DISH_MESSAGE = "com.cloud.dinein.DISH";
    public final static String LOCATION_MESSAGE = "com.cloud.dinein.LOCATION";


	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// For the main activity, make sure the app icon in the action bar
			// does not behave as a button
			ActionBar actionBar = getActionBar();
			actionBar.setHomeButtonEnabled(false);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void sendSearchRequest(View view) {
		
		Intent intent = new Intent(this, SearchActivity.class);
		// Initialize member TextView so we can manipulate it later
		EditText dishTextView = (EditText) findViewById(R.id.DISH);
		EditText locationTextView  = (EditText) findViewById(R.id.LOCATION);
		String dish =  dishTextView.getText().toString();
		String location =  locationTextView.getText().toString();

		System.out.println("Sending search request " );
		System.out.println("Dish " + dish );
		System.out.println("Location " + location );
		
		intent.putExtra(DISH_MESSAGE, dish);
		intent.putExtra(LOCATION_MESSAGE, location);

	    startActivity(intent);



	    // Do something in response to button
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

}

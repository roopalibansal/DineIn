package com.example.dinein;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	private static final String DISH = "Indian";
	private static final String LOCATION = "New york";

	static {

		//
		// System.out.println("Objects: " + obj);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		ListView lv = (ListView) findViewById(R.id.restaurant_list);

		List<String> restaurantNames = new ArrayList<String>();

		// JSONObject jsonObj = new JSONObject(obj.toString());

		restaurantNames.add("Adyar");
		restaurantNames.add("Saravana");
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, restaurantNames);
		lv.setAdapter(mAdapter);

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
}

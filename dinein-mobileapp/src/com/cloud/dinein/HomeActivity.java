package com.cloud.dinein;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cloud.dinein.location.LocationUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class HomeActivity extends Activity implements LocationListener,
		AsyncResponse {

	public final static String DISH_MESSAGE = "com.cloud.dinein.DISH";
	public final static String LOCATION_MESSAGE = "com.cloud.dinein.LOCATION";

	private static final String LOG_TAG = "DineinApp";

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";

	private static final String MY_LOCATION = "My Location";

	private static final String API_KEY = "AIzaSyCADZPkUV1WVi7tyla9jJabnJVNTTINHXA";

	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Context context;

	TextView txtLat;
	String lat;
	String provider;
	protected String latitude, longitude;
	protected boolean gps_enabled, network_enabled;

	Intent currentIntent;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.activity_main_location);
		autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this,
				R.layout.locations));

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

	@SuppressWarnings("unchecked")
	public void sendSearchRequest(View view) {

		Intent intent = new Intent(this, SearchActivity.class);
		// Initialize member TextView so we can manipulate it later
		EditText dishTextView = (EditText) findViewById(R.id.activity_main_dish);
		EditText locationTextView = (EditText) findViewById(R.id.activity_main_location);
		String dish = dishTextView.getText().toString();
		String location = locationTextView.getText().toString();
		intent.putExtra(DISH_MESSAGE, dish);

		System.out.println("Location : " + location);
		
		if (false) {

			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, this);
			Location loc = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			GetAddressTask task = (new HomeActivity.GetAddressTask(this));
			Pair<Location, Intent> locIntentPair = Pair.create(loc, intent);
			task.execute(locIntentPair);
			task.delegate = this;
		} else {
			this.processFinish(Pair.create(location,intent));
		}
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

	// uncomment to get autocomplete working.
	private ArrayList<String> autocomplete(String input) {
		ArrayList<String> resultList = new ArrayList<String>();

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();

		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE
					+ TYPE_AUTOCOMPLETE + OUT_JSON);
			sb.append("?key=" + API_KEY);
			sb.append("&components=country:us");
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing Places API URL", e);
			return resultList;
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error connecting to Places API", e);
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try { // Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<String>(predsJsonArray.length());
			for (int i = 0; i < predsJsonArray.length(); i++) {
				resultList.add(predsJsonArray.getJSONObject(i).getString(
						"description"));
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Cannot process JSON results", e);
		}

		return resultList;

	}

	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String>
			implements Filterable {
		private ArrayList<String> resultList;

		public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public String getItem(int index) {
			return resultList.get(index);
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						// Retrieve the autocomplete results.
						resultList = autocomplete(constraint.toString());

						resultList.add(0, MY_LOCATION);
						// Assign the data to the FilterResults
						filterResults.values = resultList;
						filterResults.count = resultList.size();
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
			return filter;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		txtLat = (TextView) findViewById(R.id.activity_main_get_address_text);
		txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:"
				+ location.getLongitude());

	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d("Latitude", "disable");

	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d("Latitude", "enable");

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d("Latitude", "status");
	}

	protected class GetAddressTask extends
			AsyncTask<Pair<Location, Intent>, Void, Pair<String,Intent> > {

		// Store the context passed to the AsyncTask when the sfystem
		// instantiates it.
		Context localContext;

		public AsyncResponse delegate = null;

		// Constructor called by the system to instantiate the task
		public GetAddressTask(Context context) {

			// Required by the semantics of AsyncTask
			super();

			// Set a Context for the background task
			localContext = context;
		}

		/**
		 * Get a geocoding service instance, pass latitude and longitude to it,
		 * format the returned address, and return the address to the UI thread.
		 */
		@Override
		protected Pair<String,Intent> doInBackground(Pair<Location, Intent>... params) {
			/*
			 * Get a new geocoding service instance, set for localized
			 * addresses. This example uses android.location.Geocoder, but other
			 * geocoders that conform to address standards can also be used.
			 */
			Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

			// Get the current location from the input parameter list
			Location location = params[0].first;

			// Create a list to contain the result address
			List<Address> addresses = null;

			// Try to get an address for the current location. Catch IO or
			// network problems.
			try {

				/*
				 * Call the synchronous getFromLocation() method with the
				 * latitude and longitude of the current location. Return at
				 * most 1 address.
				 */
				addresses = geocoder.getFromLocation(location.getLatitude(),
						location.getLongitude(), 1);

				// Catch network or other I/O problems.
			} catch (IOException exception1) {

				// Log an error and return an error message
				Log.e(LocationUtils.APPTAG,
						getString(R.string.IO_Exception_getFromLocation));

				// print the stack trace
				exception1.printStackTrace();

				// Return an error message
				return Pair.create((getString(R.string.IO_Exception_getFromLocation)),params[0].second);

				// Catch incorrect latitude or longitude values
			} catch (IllegalArgumentException exception2) {

				// Construct a message containing the invalid arguments
				String errorString = getString(
						R.string.illegal_argument_exception,
						location.getLatitude(), location.getLongitude());
				// Log the error and print the stack trace
				Log.e(LocationUtils.APPTAG, errorString);
				exception2.printStackTrace();

				//
				return Pair.create(errorString,params[0].second);
			}
			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {

				// Get the first address
				Address address = addresses.get(0);

				// Format the first line of address
				String addressText = getString(
						R.string.address_output_string,

						// If there's a street address, add it
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "",

						// Locality is usually a city
						address.getLocality(),

						// The country of the address
						address.getCountryName());

				// Return the text
				return Pair.create(addressText,params[0].second);

				// If there aren't any addresses, post a message
			} else {
				
				return Pair.create(getString(R.string.no_address_found),params[0].second);
			}
		}

		/**
		 * A method that's called once doInBackground() completes. Set the text
		 * of the UI element that displays the address. This method runs on the
		 * UI thread.
		 */
		@Override
		protected void onPostExecute(Pair<String,Intent> address) {

			// Turn off the progress bar
			TextView Location = (TextView) findViewById(R.id.activity_main_get_address_text);
			// Set the address in the UI
			Location.setText(address.first);
			delegate.processFinish(address);

		}
	}

	@Override
	public void processFinish(Pair<String, Intent> locIntentPair) {
		System.out.println("In process finish"  + locIntentPair.first);

		locIntentPair.second.putExtra(LOCATION_MESSAGE, locIntentPair.first);

		startActivity(locIntentPair.second);

		// Set the address in the UI

	}

}

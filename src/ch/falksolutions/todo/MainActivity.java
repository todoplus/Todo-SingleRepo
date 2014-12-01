/** MainActivity wird bei App Start aufgerufen, und initialisiert die Liste
 * GetContent und PutContent als asynchrone Tasks, die für die Dateiuebertragung benoetigt werden
 * GetContent urspruenglich von Tutorial "http://www.androidhive.info/2012/01/android-json-parsing-tutorial/"
 * uebernommen und modifiziert
 */

package ch.falksolutions.todo;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	//URL + user & pass
	private static String url = DataHandler.getUrl();
	private static String user = DataHandler.getUser();
	private static boolean firstStart = true;
	
	//Setzten der Url mit den notwendigen Parametern, für die Synchronisation
	public static void setUrl(String url) {
		MainActivity.url = url;
	}

	
	// JSON Node names
	private static final String TAG_ID = "_id";
	private static final String TAG_DATE = "Date";
	private static final String TAG_NAME = "name";
		
	// content JSONArray
	JSONArray content = null;

	// Hashmap fuer ListView
	ArrayList<HashMap<String, String>> eventList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		eventList = new ArrayList<HashMap<String, String>>();
	
		ListView lv = getListView();
		
		checkUser();
		
		Intent in = getIntent();
		Boolean autoSync = in.getBooleanExtra("SYNC", false);
		Log.d("MainAC", "autoSync = " + autoSync);
		Log.d("MainAC", "firstStart = " + firstStart);
		
		if (autoSync == true) {
			Log.d("MainAC","processRemove/Put= " + url);
			new PutContent().execute();
			
		}
		if (firstStart != false) {
			if (user != null) {
			DataHandler.getData();
			new GetContent().execute();
			firstStart = false;
			}
		}
		
		
		
		lv.setOnItemClickListener(new OnItemClickListener() { // ListView on item click listener

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("MAIN AC", "ListID" + id);
				
				String name = ((TextView) view.findViewById(R.id.name))
						.getText().toString();
				String date = ((TextView) view.findViewById(R.id.description))
						.getText().toString();
				String _id = ((TextView) view.findViewById(R.id.id))
						.getText().toString();

				// Starting single contact activity
				Intent in = new Intent(getApplicationContext(),
						SingleEventActivity.class);
				in.putExtra(TAG_NAME, name);
				in.putExtra(TAG_DATE, date);
				in.putExtra(TAG_ID, _id);
				startActivity(in);

			
		}
    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_synchronisieren:
	            DataHandler.getData();
	            new GetContent().execute();
	            
	            Context context = getApplicationContext();
	    		CharSequence text = "Synchronisation gestartet!";
	    		int duration = Toast.LENGTH_SHORT;

	    		Toast toast = Toast.makeText(context, text, duration);
	    		toast.show();
	            
	            return true;
	            
	        case R.id.action_settings:
	            //Todo
	            return true;
	            
	        case R.id.action_hinzufuegen:
	        	Intent in = new Intent(MainActivity.this,AddEventActivity.class);
	        	startActivity(in);
	        	
	        	return true;
	        	
	        case R.id.action_logOut:
	        	DataHandler.logOutUser();
	        	Intent logOut = new Intent(MainActivity.this,LogInActivity.class);
	        	startActivity(logOut);
	        	
	        	return true;
	        	
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public void autoGET() {
		DataHandler.getData();
		Log.d("Main AC", "autoGet= " + url);

		new GetContent().execute();
	}
	public void checkUser() { //Check, ob schon ein User besteht
		user = DataHandler.getUser();

		if (user==null) {
			Intent in = new Intent(MainActivity.this,LogInActivity.class);
			startActivity(in);
		}
	}
	
	/**
	 * Async task class to get json by making HTTP call
	 * */
	public class GetContent extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			Context context = getApplicationContext();
			CharSequence text = "Synchronisation gestartet!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// URL Request
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);
			
			if (jsonStr != null) {
				try {
					JSONArray content = new JSONArray(jsonStr);
					

					// looping through content
					for (int i = 0; i < content.length(); i++) {
						JSONObject c = content.getJSONObject(i);
						
						String _id = c.getString(TAG_ID);
						String name = c.getString(TAG_NAME);
						String date = c.getString(TAG_DATE);	

						// tmp hashmap for single contact
						HashMap<String, String> singleEvent = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						singleEvent.put(TAG_NAME, name);
						singleEvent.put(TAG_ID, _id);
						singleEvent.put(TAG_DATE, date);

						// adding contact to contact list
						eventList.add(singleEvent);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

	
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(
					MainActivity.this, eventList,
					R.layout.list_item, new String[] { TAG_NAME, TAG_DATE,
							TAG_ID }, new int[] { R.id.name,
							R.id.description, R.id.id });

			setListAdapter(adapter);
			
		}

	}

	public class PutContent extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);
			

			if (jsonStr != null) {
				try {
					JSONObject sC = new JSONObject(jsonStr);
						
						String _id = sC.getString(TAG_ID);
						String name = sC.getString(TAG_NAME);
						String date = sC.getString(TAG_DATE);
						
						// tmp hashmap for single contact
						HashMap<String, String> singleEvent = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						singleEvent.put(TAG_NAME, name);
						singleEvent.put(TAG_ID, _id);
						singleEvent.put(TAG_DATE, date);
						
						//eventList.add(singleEvent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			DataHandler.getData();
			new GetContent().execute();
			
			/* ListAdapter adapter = new SimpleAdapter(
					MainActivity.this, eventList,
					R.layout.list_item, new String[] { TAG_NAME, TAG_DATE,
							TAG_ID }, new int[] { R.id.name,
							R.id.description, R.id.id });

			setListAdapter(adapter); */
		}

	}
}



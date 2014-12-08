/** MainActivity wird bei App Start aufgerufen, und initialisiert die Liste
 * GetContent und PutContent als asynchrone Tasks, die für die Dateiuebertragung benoetigt werden
 * GetContent urspruenglich von Tutorial "http://www.androidhive.info/2012/01/android-json-parsing-tutorial/"
 * uebernommen und modifiziert
 */

package ch.falksolutions.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

	// URL + user & pass
	private static String url = DataHandler.getUrl();
	private static String user = DataHandler.getUser();
	private static boolean firstStart = true;
	private static boolean autoSync = false;
	private static int method; // POST = 2, PUT = 3, DELETE = 4, vgl.
								// ServiceHandler
	private static boolean pSync = false;
	
	final Handler handler = new Handler();
	Timer timer = new Timer();
	
	
	public static void setMethod(int pMethod) {
		MainActivity.method = pMethod;
	}

	// Setzten der Url mit den notwendigen Parametern, für die Synchronisation
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
	static ArrayList<HashMap<String, String>> eventList;
	static ArrayList<HashMap<String, String>> compareList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		eventList = ListHandler.getEventList();
		compareList = ListHandler.getCompareList();
		ListView lv = getListView();

		checkUser();
		
		ListAdapter adapter = new SimpleAdapter(MainActivity.this, eventList,
				R.layout.list_item,
				new String[] { TAG_NAME, TAG_DATE, TAG_ID }, new int[] {
						R.id.name, R.id.description, R.id.id });
		
		setListAdapter(adapter);

		Intent in = getIntent();
		autoSync = in.getBooleanExtra("SYNC", false);
		Log.d("MainAC", "autoSync = " + autoSync);
		

		if (autoSync == true) {
			new PutContent().execute();
			autoSync = false;

		}
		// Start Synchronisationsintervall
		if (checkUser() == true) {
			pSync = true;
			if (firstStart == true) {
				callAsyncTask();
				firstStart = false;
			}
			Log.d("Main AC","checkUser pSync: " + pSync);
			
		}

		lv.setOnItemClickListener(new OnItemClickListener() { // ListView on
																// item click
																// listener

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("MAIN AC", "ListID" + id);
				String stringID = Long.toString(id);

				String name = ((TextView) view.findViewById(R.id.name))
						.getText().toString();
				String date = ((TextView) view.findViewById(R.id.description))
						.getText().toString();
				String _id = ((TextView) view.findViewById(R.id.id)).getText()
						.toString();

				// Starting single event activity
				Intent in = new Intent(getApplicationContext(),
						SingleEventActivity.class);
				in.putExtra(TAG_NAME, name);
				in.putExtra(TAG_DATE, date);
				in.putExtra(TAG_ID, _id);
				in.putExtra("list_id", stringID);
				Log.d("MainAC", "list_id" + stringID);
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
			Log.d("MainAC", "updatedURL= " + url);
			new GetContent().execute();

			Context context = getApplicationContext();
			CharSequence text = "Synchronisation gestartet!";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();

			return true;

		case R.id.action_settings:
			// Todo
			return true;

		case R.id.action_hinzufuegen:
			Intent in = new Intent(MainActivity.this, AddEventActivity.class);
			startActivity(in);

			return true;

		case R.id.action_logOut:
			DataHandler.logOutUser();
			pSync = false;
			Intent logOut = new Intent(MainActivity.this, LogInActivity.class);
			startActivity(logOut);

			return true;

		case R.id.action_seturl:
			Intent setUrl = new Intent(MainActivity.this,
					ServerUrlSet_Debug.class);
			startActivity(setUrl);

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		Log.d("MainAC", "onPause ausgefuehrt");
		pSync = false;
		new GetContent().cancel(true);
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		Log.d("MainAC","onResume ausgeführt");
		Intent in = getIntent();
		boolean pAutoSync = in.getBooleanExtra("SYNC", false);
		if (pAutoSync != true) {
			if (checkUser() == true) {
				pSync = true;
				Log.d("MainAC","onResume callAsync");
			}
			
		}
		
		super.onResume();
	}

	public void callAsyncTask() {
		
		// Automatische Synchronisierung
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						if (pSync == true) {
							DataHandler.getData();
							new GetContent().execute();
							Log.d("MainAC","timerTask calld getC");
						}
					}
				});
				
			}
		};
		timer.schedule(task, 0, 7000);
	}
	public void newAdapter() {
		ListAdapter adapter = new SimpleAdapter(MainActivity.this,
				eventList, R.layout.list_item, new String[] { TAG_NAME,
						TAG_DATE, TAG_ID }, new int[] { R.id.name,
						R.id.description, R.id.id });
		
		setListAdapter(adapter);
	}

	public boolean checkUser() { // Check, ob schon ein User besteht
		user = DataHandler.getUser();
		boolean check = false;
		if (user == null) {
			Intent in = new Intent(MainActivity.this, LogInActivity.class);
			startActivity(in);
			check = false;
		} else if (user != null) {
			check = true;
		}
		return check;
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

						// adding todo to event list
						ListHandler.addToCompareList(singleEvent);
						if (eventList.contains(singleEvent) == true) {
							Log.d("MainAC", "eventList contains: "
									+ singleEvent);
						} else if (eventList.contains(singleEvent) != true) {
							ListHandler.addToEventList(singleEvent);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}
		
		
	

		@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
		protected void onCancelled(Void result) {
			Log.d("MainAC","AsyncTask cancelled");
			super.onCancelled(result);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			// Überprüfung, ob von einem anderen Ort etwas gelöscht wurde
			for (int i = 0; i < eventList.size(); i++) {

				if (compareList.contains(eventList.get(i)) != true) {
					Log.d("MainAC", "con FALSE: " + compareList.get(i));
					ListHandler.deleteFromEventList(i);
				}
			}
			/**
			 * Überprüfung ob alles gelöscht wurde (falls die for Schleife nicht
			 * ausgelöst wird da size() == 0.
			 */
			if (compareList.size() == 0) {
				ListHandler.clearEventList();
			}

			/**
			 * Updating parsed JSON data into ListView
			 * */
			newAdapter();
			

		}

	}

	public class PutContent extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			
			new GetContent().cancel(true);
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = null;
			if (method == 2) {
				jsonStr = sh.makeServiceCall(url, ServiceHandler.POST,
						ListHandler.getParamList());
			} else if (method == 3) {
				jsonStr = sh.makeServiceCall(url, ServiceHandler.PUT,
						ListHandler.getParamList());
			} else if (method == 4) {
				jsonStr = sh.makeServiceCall(url, ServiceHandler.DELETE);
			}
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

					if (eventList.contains(singleEvent) == true) {
						Log.d("MainAC", "eventList contains: " + singleEvent);
					} else if (eventList.contains(singleEvent) != true) {
						ListHandler.addToEventList(singleEvent);
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
			
			newAdapter();
			pSync = true;
			Log.d("MainAC","PutContent call Async");
		}

	}
}

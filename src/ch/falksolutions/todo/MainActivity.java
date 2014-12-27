/** MainActivity wird bei App Start aufgerufen, und initialisiert die Liste
 * GetContent und PutContent als asynchrone Tasks, die für die Dateiuebertragung benoetigt werden
 * GetContent urspruenglich von Tutorial "http://www.androidhive.info/2012/01/android-json-parsing-tutorial/"
 * uebernommen und modifiziert
 */

package ch.falksolutions.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	// URL + user & pass
	private static String url;
	private static String user;
	private static boolean firstStart = true;
	private static boolean autoSync = false;
	private static int method; // POST = 2, PUT = 3, DELETE = 4, vgl.
								// ServiceHandler
	private static boolean pSync = false;
	private static boolean error;
	private static int errorCode;

	public static void setAutoSync(boolean autoSync) {
		MainActivity.autoSync = autoSync;
	}

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
	private static final String TAG_SHARED = "sharedw";
	private static final String TAG_USER = "user";

	// content Array, ListView, adapter
	JSONArray content = null;
	ListView lv;
	BaseAdapter adapter;

	// Hashmap fuer ListView
	static ArrayList<HashMap<String, String>> eventList;
	static ArrayList<HashMap<String, String>> compareList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		eventList = ListHandler.getEventList();
		compareList = ListHandler.getCompareList();
		lv = getListView();
		
		// Initialisieren eines Adapters für die Anzeige
		adapter = new SimpleAdapter(MainActivity.this, eventList,
				R.layout.list_item,
				new String[] { TAG_NAME, TAG_DATE, TAG_USER }, new int[] {
						R.id.name, R.id.date, R.id.user }); 
		
		setListAdapter(adapter);
		
		
		// User schon eingeloggt?
		checkUser();
		
		if (autoSync == true) {
			new PutContent().execute();
			Log.d("MainAC","autoSync1 " + autoSync);
			setAutoSync(false);
			Log.d("MainAC","autoSync " + autoSync);

		}
		// Start Synchronisationsintervall
		if (checkUser() == true) {
			// pSync = true;
			if (firstStart == true) {
				DataHandler.setSsid(UserHandler.getSsid());
				//callAsyncTask();
				DataHandler.getData();
				new GetContent().execute();
				firstStart = false;
			}
			Log.d("Main AC", "checkUser pSync: " + pSync);

		}

		lv.setOnItemClickListener(new OnItemClickListener() { // ListView on
																// item click
																// listener

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("MAIN AC", "ListID" + id);
				DataHandler.saveListID(id);

				// Starting single event activity
				Intent in = new Intent(getApplicationContext(),
						SingleEventActivity.class);
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
			if (pSync == true) {
				new GetContent().cancel(true);
				pSync = false;
			} else if (pSync == false) {
				pSync = true;
			}
			return true;

		case R.id.action_hinzufuegen:
			Intent in = new Intent(MainActivity.this, AddEventActivity.class);
			startActivity(in);

			return true;

		case R.id.action_logOut:
			ListHandler.clearEventList();
			UserHandler uH = new UserHandler(getBaseContext());
			uH.logOut();
			pSync = false;
			firstStart = true;
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
		Log.d("MainAC", "onResume ausgeführt");
		Intent in = getIntent();
		boolean pAutoSync = in.getBooleanExtra("SYNC", false);
		if (pAutoSync != true) {
			if (checkUser() == true) {
				pSync = true;
				Log.d("MainAC", "onResume callAsync");
			}

		}

		super.onResume();
	}

	public boolean checkErrorCodes(String jsonStr) {
		String analyze = "999";
		errorCode = 888;
		error = false;
		if (jsonStr != null) {
			if (jsonStr.length() > 2) {
				analyze = jsonStr.substring(1, 4);
			}
		}

		if (analyze.equals("001") == true) {
			error = true;
			errorCode = 001;
		} else if (analyze.equals("002") == true) {
			error = true;
			errorCode = 002;
		} else if (analyze.equals("003") == true) {
			error = true;
			errorCode = 003;
		} else if (analyze.equals("004") == true) {
			error = true;
			errorCode = 004;
		} else if (analyze.equals("999") == true) {
			error = true;
			errorCode = 999;
		}
		Log.d("MainAC", "error Code: " + errorCode);
		return error;
	}

	public void makeToast(int errorCode) {
		String toastText = "";
		if (MainActivity.error == true) {
			if (errorCode == 001) {
				toastText = "Fehler: Bitte logge dich erneut ein!";
			} else if (errorCode == 002) {
				toastText = "Fehler: Bitte logge dich erneut ein!";
			} else if (errorCode == 003) {
				toastText = "ToDo wurde gelöscht";
				ListHandler.deleteFromEventList(DataHandler.getListID());
			} else if (errorCode == 004) {
				toastText = "Fehler: Bitte versuche es erneut!";
			} else if (errorCode == 999) {
				toastText = "Verbindung zum Server nicht möglich!";
			}
			if (toastText.equals("") != true) {
				Context context = getApplicationContext();
				CharSequence text = toastText;
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
			Log.d("MainAC", "toast: " + toastText);
		}

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
							Log.d("MainAC", "timerTask calld getC");
						}
					}
				});

			}
		};
		timer.schedule(task, 0, 7000);
	}

	public synchronized void contentChanged() {
		adapter.notifyDataSetChanged();
		
	}

	public boolean checkUser() { // Check, ob schon ein User besteht
		UserHandler userHandler = new UserHandler(getBaseContext());
		user = userHandler.getUser();
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
			ListHandler.clearCompareList();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// URL Request
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (checkErrorCodes(jsonStr) == true) {
				// ToDo
			} else if (jsonStr != null) {
				try {
					JSONArray content = new JSONArray(jsonStr);

					// looping through content
					for (int i = 0; i < content.length(); i++) {
						JSONObject c = content.getJSONObject(i);

						String _id = c.getString(TAG_ID);
						String name = c.getString(TAG_NAME);
						String date = c.getString(TAG_DATE);
						String shared = c.getString(TAG_SHARED);
						String createdbyUser = c.getString(TAG_USER);

						String year = date.substring(0, 4);
						String month = date.substring(5, 7);
						String day = date.substring(8, 10);
						String time = date.substring(11, 16);
						date = day + "." + month + "." + year + " " + time;

						// tmp hashmap for single contact
						HashMap<String, String> singleEvent = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						singleEvent.put(TAG_NAME, name);
						singleEvent.put(TAG_ID, _id);
						singleEvent.put(TAG_DATE, date);
						singleEvent.put(TAG_SHARED, shared);
						singleEvent.put(TAG_USER, createdbyUser);

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

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		protected void onCancelled(Void result) {
			Log.d("MainAC", "AsyncTask cancelled");
			super.onCancelled(result);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			makeToast(errorCode);
			List<Integer> objToDelete = new ArrayList<Integer>();
			

			// Überprüfung, ob von einem anderen Ort etwas gelöscht wurde
				eventList = ListHandler.getEventList();
				compareList = ListHandler.getCompareList();
				if (eventList.size() > 0) {
					Log.e("MainAC","eventList schleife");
					for (int i = 0; i < ListHandler.getEventListSize(); i++) {
						Log.d("MainAC","eventList Stelle: " + i);
						if (compareList.contains(eventList.get(i)) != true) {
							Log.d("MainAC", "con FALSE: " + compareList.get(i));
							objToDelete.add(i);

						}
				}
			} 
			for (int k = 0; k < objToDelete.size(); k++) {
				ListHandler.deleteFromEventList(k);
			}
			/**
			 * Überprüfung, ob alles gelöscht wurde (falls die for Schleife nicht
			 * ausgelöst wird da size() == 0.
			 */
			if (compareList.size() == 0) {
				ListHandler.clearEventList();
			}

			// Updating parsed JSON data into ListView
			contentChanged();

		}

	}

	public class PutContent extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {

			new GetContent().cancel(true);
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = "";
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

			if (checkErrorCodes(jsonStr) == true) {
				// ToDo
			} else if (jsonStr != null) {
				try {
					JSONArray content = new JSONArray(jsonStr);

					for (int i = 0; i < content.length(); i++) {
						JSONObject sC = content.getJSONObject(i);

						String _id = sC.getString(TAG_ID);
						String name = sC.getString(TAG_NAME);
						String date = sC.getString(TAG_DATE);
						String user = sC.getString(TAG_USER);
						String shared = sC.getString(TAG_SHARED);

						String year = date.substring(0, 4);
						String month = date.substring(5, 7);
						String day = date.substring(8, 10);
						String time = date.substring(11, 16);
						date = day + "." + month + "." + year + " " + time;

						// tmp hashmap for single contact
						HashMap<String, String> singleEvent = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						singleEvent.put(TAG_NAME, name);
						singleEvent.put(TAG_ID, _id);
						singleEvent.put(TAG_DATE, date);
						singleEvent.put(TAG_USER, user);
						singleEvent.put(TAG_SHARED, shared);

						if (eventList.contains(singleEvent) == true) {
							Log.d("MainAC", "eventList contains: "
									+ singleEvent);
						} else if (eventList.contains(singleEvent) != true) {
							if (method == 3) { // Bei Update einfügen in Liste an gleicher Position
								ListHandler.updateObjEventList(
										(int) DataHandler.getListID(),
										singleEvent);
							} else {
								ListHandler.addToEventList(singleEvent);
							}
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

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			makeToast(errorCode);
			contentChanged();
			pSync = true;
			Log.d("MainAC", "PutContent call Async");
		}
	}
}

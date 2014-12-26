package ch.falksolutions.todo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends Activity {
	String user;
	String SSID;
	String passwordInput;
	String url;

	private static final String TAG_USERNAME = "user";
	private static final String TAG_SESSIONID = "ssid";
	private static boolean error;
	private static int errorCode;

	String android_id;

	protected void onCreate(Bundle savedInstanceState) {
		Log.d("LogIn AC", "Started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		DeviceUuidFactory device = new DeviceUuidFactory(getBaseContext());
		android_id = device.getDeviceUuid().toString();
		Log.d("LoginAC", "onC ID: " + android_id);
		// somerandom
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
		case R.id.action_seturl:
			Intent setUrl = new Intent(LogInActivity.this,
					ServerUrlSet_Debug.class);
			startActivity(setUrl);

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void userHandler(int method) { // 1 = login, 2 = create

		EditText userET = (EditText) findViewById(R.id.editText1);
		EditText passwordET = (EditText) findViewById(R.id.editText2);

		user = userET.getText().toString();
		passwordInput = passwordET.getText().toString();

		if (method == 1) {
			Log.d("LoginAC", "androidid: " + android_id);
			DataHandler.userLogin(user, passwordInput, android_id);
			url = DataHandler.getUrl();
			new userLogin().execute();
		} else if (method == 2) {
			DataHandler.createUser(user, passwordInput, android_id);
			if (DataHandler.createUser(user, passwordInput, android_id) == true) {
				url = DataHandler.getUrl();
				new userLogin().execute();
			} else {
				Context context = getApplicationContext();
				CharSequence text = "User oder Passwort dürfen nicht leer sein!";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();

			}
		}

	}

	public void onUserCreate(View view) {
		userHandler(2);

	}

	public void onUserLogin(View view) {
		userHandler(1);

	}

	public void startMainAC() {
		UserHandler.setUser(user);
		UserHandler.setSsid(SSID);
		Log.d("LoginAC", "user= " + user);
		Log.d("LoginAC", "ssid= " + SSID);

		Intent in = new Intent(LogInActivity.this, MainActivity.class);
		startActivity(in);

	}

	public void makeToast(int errorCode) {
		String toastText = "";

		if (errorCode == 001) {
			toastText = "Fehler: User oder Passwort inkorrekt";
		} else if (errorCode == 002) {
			toastText = "Fehler: Username schon in Verwendung";
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
	}

	public boolean checkErrorCodes(String jsonStr) {
		errorCode = 888;
		String analyze = "999";
		if (jsonStr != null) {
			if (jsonStr.length() > 2) {
				analyze = jsonStr.substring(1, 4);
			}
		}
		Log.d("LoginAC", "analyze String: " + analyze);
		error = false;

		if (analyze.equals("001") == true) {
			error = true;
			errorCode = 001;
		} else if (analyze.equals("002") == true) {
			error = true;
			errorCode = 002;
		} else if (analyze.equals("999") == true) {
			error = true;
			errorCode = 999;
		}
		Log.d("LoginAC", "error Code: " + error);
		return error;
	}

	public class userLogin extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {

			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST,
					ListHandler.getParamList());

			Log.d("Response: ", "> " + jsonStr);

			if (checkErrorCodes(jsonStr) == true) {
				// ToDo
			}

			else if (jsonStr != null) {
				try {
					JSONArray content = new JSONArray(jsonStr);

					for (int i = 0; i < content.length(); i++) {
						JSONObject c = content.getJSONObject(i);

						String username = c.getString(TAG_USERNAME);
						String sessionID = c.getString(TAG_SESSIONID);

						SSID = sessionID;
						user = username;
					}

					startMainAC();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");

			}
			return null;
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			makeToast(errorCode);

		}

	}

}

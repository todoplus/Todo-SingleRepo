package ch.falksolutions.todo;

<<<<<<< Updated upstream
<<<<<<< HEAD
import org.json.JSONArray;
=======
<<<<<<< HEAD
>>>>>>> FETCH_HEAD
=======
import org.json.JSONArray;
>>>>>>> Stashed changes
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
<<<<<<< Updated upstream
<<<<<<< HEAD
=======
=======
import android.app.Activity;
import android.content.Intent;
>>>>>>> FETCH_HEAD
>>>>>>> FETCH_HEAD
=======
>>>>>>> Stashed changes
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LogInActivity extends Activity {
	String user;
	String password;
<<<<<<< Updated upstream
<<<<<<< HEAD
=======
>>>>>>> Stashed changes
	String url;
	
	private static final String TAG_ID = "_id";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_PASS = "pass";
	
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
	String url;
	boolean response = false;
	
	private static final String TAG_ID = "_id";
	private static final String TAG_USER = "user";
	private static final String TAG_PASSWORT = "password";
	
=======
	boolean response = false;
	
>>>>>>> FETCH_HEAD
>>>>>>> FETCH_HEAD
=======
>>>>>>> Stashed changes
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("LogIn AC", "Started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
<<<<<<< Updated upstream
<<<<<<< HEAD
	}
	
	public void tryUserLogin() {
=======
>>>>>>> FETCH_HEAD
=======
	}
	
	public void tryUserLogin() {
>>>>>>> Stashed changes
		EditText userET = (EditText) findViewById(R.id.editText1);
		EditText passwordET = (EditText) findViewById(R.id.editText2);
		
		user = userET.getText().toString();
		password = passwordET.getText().toString();
<<<<<<< Updated upstream
<<<<<<< HEAD
=======
>>>>>>> Stashed changes
		
		DataHandler.userLogin(user, password);
		url = DataHandler.getUrl();
		new userLogin().execute();
		
<<<<<<< Updated upstream
=======
	}
	
	public void tryUserLogin() {
<<<<<<< HEAD
		DataHandler.userLogin(user, password);
		url = DataHandler.getUrl();
		
=======
		//ToDo!
		//MD5 set
>>>>>>> FETCH_HEAD
>>>>>>> FETCH_HEAD
=======
>>>>>>> Stashed changes
	}
	
	
	public void onUserCreate(View view) {
		
		
	}
	
	public void onUserLogin(View view) {
		tryUserLogin();
<<<<<<< Updated upstream
<<<<<<< HEAD
=======
>>>>>>> Stashed changes
		
	}
	
	public void startMainAC() {
			DataHandler.setUser(user);
			DataHandler.setPassword(password);
			Log.d("LoginAC", "user= " + user);
			Log.d("LoginAC", "pass= " + password);
			
			Intent in = new Intent(LogInActivity.this,MainActivity.class);
			startActivity(in);
			
	}
<<<<<<< Updated upstream
=======
		if (response == true) {
<<<<<<< HEAD
		DataHandler.setUser(user);
		DataHandler.setPassword(password);
		Log.d("LoginAC", "user= " + user);
		Log.d("LoginAC", "pass= " + password);
=======
<<<<<<< HEAD
<<<<<<< HEAD
		DataHandler.setUser(user);
		DataHandler.setPassword(password);
=======
		MainActivity.setUser(user);
		MainActivity.setPassword(password);
>>>>>>> FETCH_HEAD
=======
		MainActivity.setUser(user);
		MainActivity.setPassword(password);
>>>>>>> FETCH_HEAD
>>>>>>> FETCH_HEAD
		
		Intent in = new Intent(LogInActivity.this,MainActivity.class);
		startActivity(in);
		}
		
		
	}
<<<<<<< HEAD
>>>>>>> FETCH_HEAD
=======
>>>>>>> Stashed changes
	
	public class userLogin extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			
			
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);
			

			if (jsonStr != null) {
				try {
<<<<<<< Updated upstream
<<<<<<< HEAD
=======
>>>>>>> Stashed changes
					JSONArray jsonArray = new JSONArray(jsonStr);
					
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c = jsonArray.getJSONObject(i);
						
						String _id = c.getString(TAG_ID);
						String username = c.getString(TAG_USERNAME);
						String pass = c.getString(TAG_PASS);
						
						password = pass;
						user = username;
						startMainAC();
					}
	
				} catch (JSONException e) {
					e.printStackTrace();
<<<<<<< Updated upstream
=======
					JSONObject sC = new JSONObject(jsonStr);
						
						String _id = sC.getString(TAG_ID);
						String username = sC.getString(TAG_USER);
						String pass = sC.getString(TAG_PASSWORT);
						
						password = pass;
						user = username;
						response = true;
						
						
						
				} catch (JSONException e) {
					e.printStackTrace();
					response = false;
>>>>>>> FETCH_HEAD
=======
>>>>>>> Stashed changes
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

	}
<<<<<<< Updated upstream
<<<<<<< HEAD
=======
=======
>>>>>>> FETCH_HEAD
>>>>>>> FETCH_HEAD
=======
>>>>>>> Stashed changes
}
	

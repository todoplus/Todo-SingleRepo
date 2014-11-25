package ch.falksolutions.todo;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LogInActivity extends Activity {
	String user;
	String password;
	String url;

	private static final String TAG_ID = "_id";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_PASS = "pass";
	

	protected void onCreate(Bundle savedInstanceState) {
		Log.d("LogIn AC", "Started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	public void tryUserLogin() {

		EditText userET = (EditText) findViewById(R.id.editText1);
		EditText passwordET = (EditText) findViewById(R.id.editText2);
		
		user = userET.getText().toString();
		password = passwordET.getText().toString();

		
		DataHandler.userLogin(user, password);
		url = DataHandler.getUrl();
		new userLogin().execute();
		

	}
		
	public void onUserCreate(View view) {
		
		
	}
	
	public void onUserLogin(View view) {
		tryUserLogin();
		
	}
	
	public void startMainAC() {
			DataHandler.setUser(user);
			DataHandler.setPassword(password);
			Log.d("LoginAC", "user= " + user);
			Log.d("LoginAC", "pass= " + password);
			
			Intent in = new Intent(LogInActivity.this,MainActivity.class);
			startActivity(in);
			
	}
	
	public class userLogin extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			
			
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);
			

			if (jsonStr != null) {
				try {
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
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			return null;
		}

	}

}

	

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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
	
	public void userHandler(int method) { // 1 = login, 2 = create

		EditText userET = (EditText) findViewById(R.id.editText1);
		EditText passwordET = (EditText) findViewById(R.id.editText2);
		
		user = userET.getText().toString();
		password = passwordET.getText().toString();

		if (method == 1) {
			DataHandler.userLogin(user, password);
			url = DataHandler.getUrl();
			new userLogin().execute();
		} else if (method == 2) {
			DataHandler.createUser(user, password);
			if (DataHandler.createUser(user, password) == true) {
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
			DataHandler.setUser(user);
			DataHandler.setPassword(password);
			Log.d("LoginAC", "user= " + user);
			Log.d("LoginAC", "pass= " + password);
			
			Intent in = new Intent(LogInActivity.this,MainActivity.class);
			startActivity(in);
			
	}
	
	public void makeToast(String toastText) {
		Context context = getApplicationContext();
		CharSequence text = toastText;
		int duration = Toast.LENGTH_SHORT;
		
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public class userLogin extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			
			
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST,ListHandler.getParamList());

			Log.d("Response: ", "> " + jsonStr);
			

			if (jsonStr != null) {
				try {
					JSONArray content = new JSONArray(jsonStr);
					
					for (int i = 0; i < content.length(); i++) {
						JSONObject c = content.getJSONObject(i);
						
						String _id = c.getString(TAG_ID);
						String username = c.getString(TAG_USERNAME);
						String pass = c.getString(TAG_PASS);
						
						password = pass;
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
		
	}

}

	

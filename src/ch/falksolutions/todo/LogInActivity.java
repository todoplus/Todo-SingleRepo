package ch.falksolutions.todo;

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
	boolean response = false;
	
	private static final String TAG_ID = "_id";
	private static final String TAG_USER = "user";
	private static final String TAG_PASSWORT = "password";
	
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("LogIn AC", "Started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		EditText userET = (EditText) findViewById(R.id.editText1);
		EditText passwordET = (EditText) findViewById(R.id.editText2);
		
		user = userET.getText().toString();
		password = passwordET.getText().toString();
	}
	
	public void tryUserLogin() {
		DataHandler.userLogin(user, password);
		url = DataHandler.getUrl();
		
	}
	
	
	public void onUserCreate(View view) {
		
		
	}
	
	public void onUserLogin(View view) {
		tryUserLogin();
		if (response == true) {
		DataHandler.setUser(user);
		DataHandler.setPassword(password);
		Log.d("LoginAC", "user= " + user);
		Log.d("LoginAC", "pass= " + password);
		
		Intent in = new Intent(LogInActivity.this,MainActivity.class);
		startActivity(in);
		}
		
		
	}
	
	public class userLogin extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			
			
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);
			

			if (jsonStr != null) {
				try {
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
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

	}
}
	

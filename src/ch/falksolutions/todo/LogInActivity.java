package ch.falksolutions.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LogInActivity extends Activity {
	String user;
	String password;
	boolean response = false;
	
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
		//ToDo!
		//MD5 set
	}
	
	
	public void onUserCreate(View view) {
		
		
	}
	
	public void onUserLogin(View view) {
		tryUserLogin();
		if (response == true) {
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
		
		Intent in = new Intent(LogInActivity.this,MainActivity.class);
		startActivity(in);
		}
		
		
	}
}
	

package ch.falksolutions.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ServerUrlSet_Debug extends Activity {
	EditText inputURL;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seturl);
		
		inputURL = (EditText) findViewById(R.id.editText1);
		inputURL.setText("https://new-todoplus.c9.io/api");
		
	}
	
	public void onFinish(View v) {
		inputURL = (EditText) findViewById(R.id.editText1);
		DataHandler.setStanUrl(inputURL.getText().toString());
		
		Intent in = new Intent(this, MainActivity.class);
		startActivity(in);
	}

}

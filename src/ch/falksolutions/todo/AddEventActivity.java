/** Actvivity um ein neues ToDo zu erstellen und dem Server hinzuzufuegen
 * 
 */

package ch.falksolutions.todo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddEventActivity extends ActionBarActivity {
	EditText inputName;
	EditText inputBeschreibung;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("AddEvent Activity", "Started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addevent);
		
		inputName = (EditText) findViewById(R.id.editText1);
		inputBeschreibung = (EditText) findViewById(R.id.editText2);
		
		
		
		
	}
	public void onButtonFinishClick(View view) {
		Log.d("AddEventAc", "Finish Button betaetigt");
		DataHandler.putData(inputName.getText().toString());
		
		Context context = getApplicationContext();
		CharSequence text = "ToDo: '" + inputName.getText().toString() + "' wird hochgeladen!";
		int duration = Toast.LENGTH_SHORT;
		
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
		Intent goToMainActivity = new Intent
				(AddEventActivity.this,MainActivity.class);
				goToMainActivity.putExtra("SYNC", true);
		startActivity(goToMainActivity);
		
	}
	
	

}

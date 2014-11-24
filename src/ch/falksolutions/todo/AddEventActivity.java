/** Actvivity um ein neues ToDo zu erstellen und dem Server hinzuzufuegen
 * 
 */

package ch.falksolutions.todo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddEventActivity extends Activity {
	EditText inputName;
	EditText inputBeschreibung;
	String updateID;
	String updateContent;
	Boolean update = false;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("AddEvent Activity", "Started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addevent);
		
		inputName = (EditText) findViewById(R.id.editText1);
		inputBeschreibung = (EditText) findViewById(R.id.editText2);
		
		Intent in = getIntent();
		update = in.getBooleanExtra("update", false);
		
		if (update == true) {
			updateContent = in.getStringExtra("content");
			updateID = in.getStringExtra("id");
			Log.d("AddEventAC", "updID= " + updateID);
			
			inputName.setText(updateContent);
		}
		
		
		
		
	}
	public void onButtonFinishClick(View view) {
		Log.d("AddEventAc", "Finish Button betaetigt");
<<<<<<< Updated upstream
		DataHandler.putData(inputName.getText().toString());
=======
		if (update == false) {
		DataHandler.putData(inputName.getText().toString());
		}
		if (update == true) {
		DataHandler.updateData(updateID);
		update = false;
		}
>>>>>>> Stashed changes
		
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

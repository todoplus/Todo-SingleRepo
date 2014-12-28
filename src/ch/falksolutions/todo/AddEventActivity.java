/** Actvivity um ein neues ToDo zu erstellen und dem Server hinzuzufuegen
 * 
 */

package ch.falksolutions.todo;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddEventActivity extends Activity {
	
	// Eingabefelder
	private static EditText inputName;
	private static EditText inputSharedWith;
	
	// Unterscheidung neu/update
	private static boolean update = false;

	// vom Objekt ausgelesene Variabeln
	private static String updateID;
	private static String updateContent;
	private static String sharedWith;
	

	// Object name keys
	private static final String TAG_SHARED = "sharedw";
	private static final String TAG_NAME = "name";
	private static final String TAG_ID = "_id";

	public static void setInputName(EditText pInputName) {
		inputName = pInputName;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("AddEvent Activity", "Started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addevent);

		inputName = (EditText) findViewById(R.id.editText1);
		inputSharedWith = (EditText) findViewById(R.id.editText2);

		Intent in = getIntent();
		update = in.getBooleanExtra("update", false);

		if (update == true) {
			HashMap<String, String> eventObj = ListHandler
					.getObjFromEventList();

			sharedWith = eventObj.get(TAG_SHARED);
			updateContent = eventObj.get(TAG_NAME);
			updateID = eventObj.get(TAG_ID);

			inputName.setText(updateContent);
			inputSharedWith.setText(sharedWith);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_addevent, menu);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case R.id.action_settings:
			// Todo
			return true;

		case R.id.action_finish:
			uploadToDo();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void makeToast(String toastText) {

		Context context = getApplicationContext();
		CharSequence text = toastText;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	public void uploadToDo() {

		Log.d("AddEventAc", "ActionBar finish");
		String todo = inputName.getText().toString();
		String shared = inputSharedWith.getText().toString();
		
		MainActivity.setAutoSync(true);
		
		Intent goToMainActivity = new Intent(AddEventActivity.this,
				MainActivity.class);
		startActivity(goToMainActivity);

		if (update == false) {
			DataHandler.postData(todo, shared);
			makeToast("ToDo: '" + inputName.getText().toString()
					+ "' wird hochgeladen!");

		} else if (update == true) {
			DataHandler.updateData(updateID, todo);
			update = false;
			if (shared.equals(sharedWith) == true) {
				Log.d("AddEAC","equals true");
				makeToast("ToDo: '" + inputName.getText().toString()
						+ "' wird hochgeladen!");
			} else if (shared.equals(sharedWith) != true) {
				Log.d("AddEAC","equals false");
				makeToast("Freigaben können nachträglich nicht geändert werden!");
			}

		}
	}

}

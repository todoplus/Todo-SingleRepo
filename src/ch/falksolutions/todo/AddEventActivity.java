/** Actvivity um ein neues ToDo zu erstellen und dem Server hinzuzufuegen
 * 
 */

package ch.falksolutions.todo;

import ch.falksolutions.todo.MainActivity.GetContent;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddEventActivity extends Activity {
	private static EditText inputName;
	private static EditText inputBeschreibung;
	private static String updateID;
	private static String updateContent;
	private static boolean update = false;
	private static boolean enthaeltSonderZeichen = false;
	private static String listID;
	
	public static void setInputName(EditText pInputName) {
		inputName = pInputName;
	}

	
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
			listID = in.getStringExtra("list_id");
			Log.d("AddEventAC", "updID= " + updateID);
			
			inputName.setText(updateContent);
		}
		
		
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_addevent, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
     
	        case R.id.action_settings:
	            //Todo
	            return true;
	            
	        case R.id.action_finish:
	        	uploadToDo();
	        	
	        	return true;
	                     
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
		
	public void uploadToDo() {
		
		Log.d("AddEventAc", "ActionBar finish");
		String analyzedString = inputName.getText().toString();
		

		if (DataHandler.analyzeString(analyzedString) == false) {
			
			analyzedString = DataHandler.replaceOutPut(analyzedString);
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
		
		else {
			
			Context context = getApplicationContext();
			CharSequence text = "Der Text darf die folgenden Zeichen nicht enthalten: % & ? + #";
			int duration = Toast.LENGTH_LONG;
			
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			
		}
		
		if (update == false) {
			DataHandler.postData(analyzedString);
		
		}
		else if (update == true) {
			DataHandler.updateData(updateID, analyzedString);
			DataHandler.deleteFromEventList(Long.valueOf(listID));
			update = false;
		
		}
	}
	
	

}

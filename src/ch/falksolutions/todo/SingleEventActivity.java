/** Darstellung eines einzelnen ToDo's mit der Moeglichkeit dieses zu loeschen
 * übergibt die notwendige ID an die MainActivity, damit diese den Server aufrufen kann
 */

package ch.falksolutions.todo;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SingleEventActivity  extends Activity {
	
	// Elemente Objekt
	private static final String TAG_NAME = "name";
	private static final String TAG_ID = "_id";
	private static final String TAG_DATE = "Date";
	private static final String TAG_SHARED = "sharedw";
	private static final String TAG_USER = "user";
	private static final String TAG_PRIORITY = "prio";
	
	// Vom Objekt ausgelesene Variablen
	private static String name;
	private static String date;
	private static String shared;
	private static String id;
	private static String user;
	private static String priority;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);
        
        ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setTitle("Einzelanzeige");
        
        HashMap<String, String> eventObj = ListHandler.getObjFromEventList();
        name = eventObj.get(TAG_NAME);
        id = eventObj.get(TAG_ID);
        date = eventObj.get(TAG_DATE);
        shared = eventObj.get(TAG_SHARED);
        user = eventObj.get(TAG_USER);
        priority = eventObj.get(TAG_PRIORITY);
        
        if (priority.equals("2")) {
        	priority = "normal";
        } else if (priority.equals("1")) {
        	priority = "hoch";
        } else if (priority.equals("3")) {
        	priority = "tief";
        }
        
        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblUser = (TextView) findViewById(R.id.user_label);
        TextView lblDate = (TextView) findViewById(R.id.date_label);
        TextView lblShared = (TextView) findViewById(R.id.share_label);
        TextView lblPrio = (TextView) findViewById(R.id.priority_label);
        
        lblName.setText(name);
        lblUser.setText(user);
        lblDate.setText(date);
        lblShared.setText(shared);
        lblPrio.setText(priority);
        
        
    }
	
	public void onUpdate(View v) {
		Intent in2 = new Intent(SingleEventActivity.this, AddEventActivity.class);
		in2.putExtra("update", true);
		startActivity(in2);
		finish();
		
	}
	
	public void onDelete(View v) {
		
		DataHandler.removeData(id);
		MainActivity.setAutoSync(true);
		Intent goToMainActivity = new Intent
				(SingleEventActivity.this,MainActivity.class);
		startActivity(goToMainActivity);
		finish();
	}
	
}

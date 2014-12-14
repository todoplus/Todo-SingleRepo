/** Darstellung eines einzelnen ToDo's mit der Moeglichkeit dieses zu loeschen
 * übergibt die notwendige ID an die MainActivity, damit diese den Server aufrufen kann
 */

package ch.falksolutions.todo;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import ch.falksolutions.todo.R;

public class SingleEventActivity  extends Activity {
	
	private static final String TAG_NAME = "name";
	private static final String TAG_ID = "_id";
	private static final String TAG_DATE = "date";
	private static final String TAG_SHARED = "sharedw";
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);
        
        
        HashMap<String, String> eventObj = ListHandler.getObjFromEventList();
        String name = eventObj.get(TAG_NAME);
        String id = eventObj.get(TAG_ID);
        String date = eventObj.get(TAG_DATE);
        String shared = eventObj.get(TAG_SHARED);
        
        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblID = (TextView) findViewById(R.id.id_label);
        TextView lblDate = (TextView) findViewById(R.id.date_label);
        
        lblName.setText(name);
        lblID.setText(id);
        lblDate.setText(date);
        
        
    }
	
	public void onUpdate(View v) {
		Intent in2 = new Intent(SingleEventActivity.this, AddEventActivity.class);
		in2.putExtra("update", true);
		startActivity(in2);
	}
	
	public void onDelete(View v) {
		Intent in = getIntent();
		String id = in.getStringExtra(TAG_ID);
		String StringListID = in.getStringExtra("list_id");
		Log.d("SingleE AC", "listid: " + StringListID);
		DataHandler.removeData(id);
		ListHandler.deleteFromEventList(Long.valueOf(StringListID));
		
		Context context = getApplicationContext();
		CharSequence text = "ToDo wird geloescht!";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
		Intent goToMainActivity = new Intent
				(SingleEventActivity.this,MainActivity.class);
				goToMainActivity.putExtra("SYNC",true);
		startActivity(goToMainActivity);
	}
	
}

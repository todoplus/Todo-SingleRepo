/** Darstellung eines einzelnen ToDo's mit der Moeglichkeit dieses zu loeschen
 * übergibt die notwendige ID an die MainActivity, damit diese den Server aufrufen kann
 */

package ch.falksolutions.todo;

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
	
	// JSON node keys
	private static final String TAG_NAME = "name";
	private static final String TAG_ID = "_id";
	private static final String TAG_DATE = "date";
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        String name = in.getStringExtra(TAG_NAME);
        Log.d("SingleE AC","tagname: " + TAG_NAME);
        String id = in.getStringExtra(TAG_ID);
        String date = in.getStringExtra(TAG_DATE);
        Log.d("SingleEvent AC", "tagid= " + id);
        
        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblID = (TextView) findViewById(R.id.id_label);
        TextView lblDate = (TextView) findViewById(R.id.date_label);
        
        lblName.setText(name);
        lblID.setText(id);
        lblDate.setText(date);
        
        
    }
	
	public void onDelete(View v) {
		Intent in = getIntent();
		String id = in.getStringExtra(TAG_ID);
		DataHandler.removeData(id);
		
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

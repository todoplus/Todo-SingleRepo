/** Actvivity um ein neues ToDo zu erstellen und dem Server hinzuzufuegen
 * 
 */

package ch.falksolutions.todo;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
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
	private static String groupSharing;

	// Object name keys
	private static final String TAG_SHARED = "sharedw";
	private static final String TAG_NAME = "name";
	private static final String TAG_ID = "_id";
	private static final String TAG_GROUPNAME = "groupname";
	
	private static ListView lv;
	private static BaseAdapter adapter;
	private static String priority = "2"; //Standardpriorität
	
	RadioButton rBLow;
	RadioButton rBMedium;
	RadioButton rBHigh;

	public static void setInputName(EditText pInputName) {
		inputName = pInputName;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("AddEvent Activity", "Started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addevent);
		//Standardwerte setzten
		groupSharing = null;
		priority = "2";
		
		//Initalisieren ListView + benötigte Buttons
		lv = (ListView) findViewById(R.id.groupListView);
		new GetGroups().execute();
		
		rBMedium = (RadioButton) findViewById(R.id.mediumPriority);
		rBMedium.setChecked(true);
		
		rBLow = (RadioButton) findViewById(R.id.lowPriority);
		rBHigh = (RadioButton) findViewById(R.id.highPriority);
		

		inputName = (EditText) findViewById(R.id.editText1);
		inputSharedWith = (EditText) findViewById(R.id.editText2);

		//Überprüfung Update
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
	//ClickListener um Gruppen hinzuzufügen
	lv.setOnItemClickListener(new OnItemClickListener() { 

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String singleGroup = GroupHandler.getItemFromGroupList(id);
				if (groupSharing != null) {
					groupSharing += singleGroup + ";";
				} else {
					groupSharing = singleGroup + ";";
				}
				makeToast("Gruppe " + singleGroup + " hinzugefügt");
				GroupHandler.deleteFromGroupList((int) id);
				adapter.notifyDataSetChanged();
				
				
			}
			
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_addevent, menu);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		if (update == false) {
			actionBar.setTitle("ToDo hinzufügen");
		} else if (update == true) {
			actionBar.setTitle("ToDo bearbeiten");
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case R.id.action_settings:
			return true;

		case R.id.action_finish:
			uploadToDo();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	//Priorität setzen
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.lowPriority:
	            if (checked) {
	            	priority = "3";
	            	rBMedium.setChecked(false);
	            	rBHigh.setChecked(false);
	            }
	            break;
	        case R.id.mediumPriority:
	            if (checked) {
	            	priority = "2";
	            	rBLow.setChecked(false);
	            	rBHigh.setChecked(false);
	            }
	              
	            break;
	        case R.id.highPriority:
	        	if (checked) {
	        		priority = "1";
	        		rBMedium.setChecked(false);
	            	rBLow.setChecked(false);
	        	}
	        		break;
	    }
	}
	//Toast asugeben
	public void makeToast(String toastText) {

		Context context = getApplicationContext();
		CharSequence text = toastText;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	//ToDo mit den erforderlichen Parametern an DataHandler übergeben
	public void uploadToDo() {

		String todo = inputName.getText().toString();
		String shared = inputSharedWith.getText().toString();
		

		MainActivity.setAutoSync(true);

		if (update == false) {
			if (shared.equals("") == false) {
				shared = shared + ';';
			}
			
			DataHandler.postData(todo, shared, groupSharing, priority);
			makeToast("ToDo: '" + inputName.getText().toString()
					+ "' wird hochgeladen!");

		} else if (update == true) {
			DataHandler.updateData(updateID, todo);
			update = false;
			if (shared.equals(sharedWith) == true) {
				Log.d("AddEAC", "equals true");
				makeToast("ToDo: '" + inputName.getText().toString()
						+ "' wird hochgeladen!");
			} else if (shared.equals(sharedWith) != true) {
				Log.d("AddEAC", "equals false");
				makeToast("Freigaben können nachträglich nicht geändert werden!");
			}

		}
		Intent goToMainActivity = new Intent(AddEventActivity.this,
				MainActivity.class);
		
		startActivity(goToMainActivity);
		finish();
	}
	//AsyncTask um Gruppenliste abzurufen
	public class GetGroups extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			GroupHandler.clearGroupList();
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(DataHandler.getGroups(), ServiceHandler.GET);
			
			if (jsonStr != null) {
				try {
					JSONArray content = new JSONArray(jsonStr);

					// looping through content
					for (int i = 0; i < content.length(); i++) {
						JSONObject c = content.getJSONObject(i);

						String groupName = c.getString(TAG_GROUPNAME);
						
						HashMap<String, String> singleGroup = new HashMap<String, String>();
						singleGroup.put(TAG_GROUPNAME, groupName);
						GroupHandler.addToGroupList(singleGroup);
					}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Log.e("ServiceHandler", "Couldn't get any data from the url");
				}
				return null;		
			}

		@Override
		protected void onPostExecute(Void result) {
			
			
			adapter = new SimpleAdapter(AddEventActivity.this, GroupHandler.getGroupList(),
					R.layout.grouplist_item,
					new String[] { TAG_GROUPNAME, }, new int[] {
							R.id.name, });
			lv.setAdapter(adapter);
			
			super.onPostExecute(result);
		}
		
	}

}

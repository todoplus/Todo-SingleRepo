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
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class CreateGroupActivity extends Activity {
	
	private static final String TAG_GROUPNAME = "groupname";
	
	private static ListView lv;
	private static BaseAdapter adapter;
	
	private static int method; // 1 GET, 2 DELETE
	private static String delURL;
	private static boolean rmvOK;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creategroup);
		
		//Initalisieren ListView + Parsen der Gruppen
		lv = (ListView) findViewById(R.id.groupListView);
		method = 1;
		new GetGroups().execute();
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Gruppe erstellen");
		super.onCreate(savedInstanceState);
		
		lv.setOnItemClickListener(new OnItemClickListener() { 

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
					String groupName = GroupHandler.getItemFromGroupList(id);
					method = 2;
					delURL = DataHandler.deleteGroup(groupName);
					new GetGroups().execute();
					GroupHandler.deleteFromGroupList((int) id);
					adapter.notifyDataSetChanged();
					
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_creategroup, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_finish:
			createGroup();
			MainActivity.setAutoSync(true);
			Intent in = new Intent(CreateGroupActivity.this, MainActivity.class);
			startActivity(in);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	public void createGroup() {
		EditText groupName = (EditText) findViewById(R.id.groupname);
		EditText groupMember = (EditText) findViewById(R.id.groupmember);
		
		String gName = groupName.getText().toString();
		String gMember = groupMember.getText().toString();
		gMember = gMember + ";";
		
		DataHandler.createGroupParams(gName, gMember);
	}
	//AsyncTask um Gruppenliste abzurufen
		public class GetGroups extends AsyncTask<Void, Void, Void> {

			@Override
			protected Void doInBackground(Void... params) {
				
				String jsonStr = "";
				if (method == 1) {
					GroupHandler.clearGroupList();
				}
				ServiceHandler sh = new ServiceHandler();
				if (method == 1) {
					jsonStr = sh.makeServiceCall(DataHandler.getGroups(), ServiceHandler.GET);
				} else if (method == 2) {
					jsonStr = sh.makeServiceCall(delURL, ServiceHandler.DELETE);
					Log.d("CGAC","Response > " + jsonStr);
					rmvOK = false;
					
				}
				
				
				if (jsonStr.substring(1, 4).equals("009")) {
					rmvOK = true;
				}
				else if (!jsonStr.equals("")) {
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
				
				
				if (method == 1) {
					adapter = new SimpleAdapter(CreateGroupActivity.this, GroupHandler.getGroupList(),
							R.layout.grouplist_item,
							new String[] { TAG_GROUPNAME, }, new int[] {
							R.id.name, });
					lv.setAdapter(adapter);
				} else if (rmvOK) {
					Context context = getApplicationContext();
					CharSequence text = "Gruppe entfernt";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
				
				super.onPostExecute(result);
				
			}
			
		}

		
}

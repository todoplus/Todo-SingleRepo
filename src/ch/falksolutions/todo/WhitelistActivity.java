package ch.falksolutions.todo;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class WhitelistActivity extends Activity {
	
	private static final String TAG_WHITELISTUSER = "white";
	
	private static ListView lv;
	private static BaseAdapter adapter;
	private static String whitelistString;
	private static EditText inputUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_whitelist);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setTitle("Whitelist");
		
		lv = (ListView) findViewById(R.id.listWhitelist);
		inputUser = (EditText) findViewById(R.id.addWhitelistEdit);
		
		new GetWhitelist().execute();
		
		
		lv.setOnItemClickListener(new OnItemClickListener() { 

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String singleUser = WhitelistHandler.getItemFromWhitelist(id);
				whitelistString = whitelistString.replace(singleUser + ";", "");
				WhitelistHandler.deleteFromWhitelist((int) id); 
				adapter.notifyDataSetChanged();
				
				
			}
			
		});

		super.onCreate(savedInstanceState);
	
	}

		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_whitelist, menu);
		
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_finish:
			MainActivity.setAutoSync(true);
			updateWhitelist();
			Intent in = new Intent(WhitelistActivity.this,MainActivity.class);
			startActivity(in);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	private void updateWhitelist() {
		String newEdit = inputUser.getText().toString();
		Log.d("WhitelistAC","input: " + newEdit);
		if (newEdit.equals("") != true) {
			if (whitelistString != null) {
				whitelistString += newEdit + ";";
			} else {
				whitelistString = newEdit + ";";
			}
		}
		whitelistString = whitelistString.replace("null;", "");
		DataHandler.updateWhitelist(whitelistString);
		
		
		
	}
	
	public class GetWhitelist extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			WhitelistHandler.clearWhitelist();
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(DataHandler.getWhitelist(), ServiceHandler.GET);
			Log.d("WhitelistAC","Response > " + jsonStr);
			
			
			if (jsonStr != null) {
				try {
					JSONArray content = new JSONArray(jsonStr);

					// looping through content
					for (int i = 0; i < content.length(); i++) {
						JSONObject c = content.getJSONObject(i);

						String whitelistMember = c.getString(TAG_WHITELISTUSER);
						
						HashMap<String, String> member = new HashMap<String, String>();
						member.put(TAG_WHITELISTUSER, whitelistMember);
						WhitelistHandler.addToWhitelist(member);
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
			
			
			adapter = new SimpleAdapter(WhitelistActivity.this, WhitelistHandler.getWhitelist(),
					R.layout.grouplist_item,
					new String[] { TAG_WHITELISTUSER, }, new int[] {
							R.id.name, });
			lv.setAdapter(adapter);
			
			for (int i=0; i<WhitelistHandler.getWhitelistSize(); i++) {
				if (whitelistString != null) {
					whitelistString += WhitelistHandler.getItemFromWhitelist(i) + ";";
				} else {
					whitelistString = WhitelistHandler.getItemFromWhitelist(i) + ";";
				}
			}
			Log.d("WhitelistAC","whString: " + whitelistString);
			
			super.onPostExecute(result);
		}
		
	}

}

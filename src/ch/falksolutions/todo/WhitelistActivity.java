package ch.falksolutions.todo;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class WhitelistActivity extends Activity {
	
	private static final String TAG_WHITELISTUSER = "white";
	
	private static ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_whitelist);
		lv = (ListView) findViewById(R.id.listWhitelist);
		
		new GetWhitelist().execute();
		
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
		
	}
	
	public class GetWhitelist extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			WhitelistHandler.clearWhitelist();
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(DataHandler.getWhitelist(), ServiceHandler.GET);
			
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
			
			
			BaseAdapter adapter = new SimpleAdapter(WhitelistActivity.this, WhitelistHandler.getWhitelist(),
					R.layout.grouplist_item,
					new String[] { TAG_WHITELISTUSER, }, new int[] {
							R.id.name, });
			lv.setAdapter(adapter);
			
			super.onPostExecute(result);
		}
		
	}

}

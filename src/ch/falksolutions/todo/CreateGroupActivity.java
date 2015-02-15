package ch.falksolutions.todo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class CreateGroupActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creategroup);
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Gruppe erstellen");
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
		
}

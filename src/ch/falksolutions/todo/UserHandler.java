package ch.falksolutions.todo;

import android.content.Context;
import android.content.SharedPreferences;

public class UserHandler {
	SharedPreferences sharedPrefs; 
	
	private static String user;
	private static String ssid;
	
	
	protected static final String PREFS_FILE = "user.xml";
    protected static final String PREFS_USER = "username";
    protected static final String PREFS_SSID = "ssid";
	
	UserHandler(Context context) {
		sharedPrefs = context.getSharedPreferences(PREFS_FILE, 0);
		user = sharedPrefs.getString(PREFS_USER, null);
		ssid = sharedPrefs.getString(PREFS_SSID, null);
	}
	
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		sharedPrefs.edit().putString(PREFS_USER, user)
        .commit();
	}
	public static String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		sharedPrefs.edit().putString(PREFS_SSID, ssid)
        .commit();
	}
	
	public void logOut() {
		sharedPrefs.edit().remove(PREFS_USER).commit();
		sharedPrefs.edit().remove(PREFS_SSID).commit();
		
	}
	
	

	
	
	
	

}

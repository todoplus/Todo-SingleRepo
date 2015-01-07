/** Die Klasse enthält Methoden um den User, sowie die SSID zu bearbeiten, zu speichern und zu löschen
 */

package ch.falksolutions.todo;

import android.content.Context;
import android.content.SharedPreferences;

public class UserHandler {
	//Shared Prefs
	SharedPreferences sharedPrefs; 
	
	// User + SSID
	private static String user;
	private static String ssid;
	
	// SharedPrefs Keys
	protected static final String PREFS_FILE = "user.xml";
    protected static final String PREFS_USER = "username";
    protected static final String PREFS_SSID = "ssid";
	
	// Konstruktor um SharedPreferences zu laden
    UserHandler(Context context) {
		sharedPrefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
		user = sharedPrefs.getString(PREFS_USER, null);
		ssid = sharedPrefs.getString(PREFS_SSID, null);
	}
	
	// Rückgabe user
	public String getUser() {
		return user;
	}
	
	// User Speichern
	public void setUser(String user) {
		sharedPrefs.edit().putString(PREFS_USER, user)
        .commit();
	}
	
	// Rückgabe SSID
	public static String getSsid() {
		return ssid;
	}
	
	// SSID Speichern
	public void setSsid(String ssid) {
		sharedPrefs.edit().putString(PREFS_SSID, ssid)
        .commit();
	}
	
	// User + SSID löschen
	public void logOut() {
		sharedPrefs.edit().remove(PREFS_USER).commit();
		sharedPrefs.edit().remove(PREFS_SSID).commit();
		
	}
	
	

	
	
	
	

}

/** Beinhaltet die Liste für die ToDos und Methoden um sie zu löschen, zu verändern, etc.
 * 
 */

package ch.falksolutions.todo;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ListHandler{
	// SharedPreferences
	protected static String PREFS_FILE = "saveList";
	protected static String PREFS_ARRAY = "jsonArray";
	
	private static SharedPreferences prefs;
	
	// JSON Node Keys
	private static final String TAG_ID = "_id";
	private static final String TAG_DATE = "Date";
	private static final String TAG_NAME = "name";
	private static final String TAG_SHARED = "sharedw";
	private static final String TAG_USER = "user";
	private static final String TAG_PRIORITY = "prio";
	
	// Lokale Liste für die Anzeige
	private static ArrayList<HashMap<String, String>> eventList = new ArrayList<HashMap<String, String>>();
	
	// Offline Speicherung als JSON String
	public static void saveList(Context context) {
		JSONArray toSave = new JSONArray(eventList);
		Log.d("ListHandler","JARRAY: " + toSave);
		
		prefs = context.getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
		prefs.edit().putString(PREFS_ARRAY, toSave.toString()).commit();
		
	}
	
	// Auslesen des JSON Strings und einfügen in eventList
	public static void readList(Context context) throws JSONException {
		prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
		String jsonStr = prefs.getString(PREFS_ARRAY, null);
		Log.d("ListHandler","readStr:" + jsonStr);
		if (jsonStr != null) {
		JSONArray toRead = new JSONArray(jsonStr);
		
		for (int i = 0; i < toRead.length(); i++) {
			JSONObject c = toRead.getJSONObject(i);

			String _id = c.getString(TAG_ID);
			String name = c.getString(TAG_NAME);
			String date = c.getString(TAG_DATE);
			String shared = c.getString(TAG_SHARED);
			String createdbyUser = c.getString(TAG_USER);
			String priority = c.getString(TAG_PRIORITY);

			HashMap<String, String> singleEvent = new HashMap<String, String>();

			// adding each child node to HashMap key => value
			singleEvent.put(TAG_NAME, name);
			singleEvent.put(TAG_ID, _id);
			singleEvent.put(TAG_DATE, date);
			singleEvent.put(TAG_SHARED, shared);
			singleEvent.put(TAG_USER, createdbyUser);
			singleEvent.put(TAG_PRIORITY, priority);

			// Objekt zu Liste hinzufügen
			addToEventList(singleEvent);
			prefs.edit().clear().commit();
		}

		}
	}
	
	// Offline Liste löschen (bei LogOut)
	public static void clearSavedList(Context context) {
		prefs = context.getSharedPreferences(PREFS_FILE, 0);
		prefs.edit().clear().commit();
	}

	// Get List
	public static ArrayList<HashMap<String, String>> getEventList() {
		return eventList;
	}

	// Objekt hinzufügen
	public static void addToEventList(HashMap<String, String> singleEvent) {
		eventList.add(singleEvent);
	}
	
	//Objekt an bestimmter Stelle hinzufügen
	public static void addToEventListAtPosition(HashMap<String, String> singleEvent, int index) {
		eventList.add(index, singleEvent);
	}
	
	// Objekt an bestimmter Stelle modifizieren
	public static void updateObjEventList(int listID, HashMap<String, String> singleEvent) {
		eventList.set(listID, singleEvent);
		
	}

	// Objekt löschen
	public static void deleteFromEventList(long id) {
		int intID = (int) id;
		eventList.remove(intID);
		Log.d("ListHandler", "eventList removed id" + id);
	}
	
	//Priorität überprüfen
	public static String getPriority(int counter) {
		final String TAG_PRIORITY = "prio";
		HashMap<String, String> singleEvent = eventList.get(counter);
		return singleEvent.get(TAG_PRIORITY);
	}
	
	// Gibt das Objekt zurück, dessen Index in der Methode DataHandler.saveListID temporär gespeichert wird
	public static HashMap<String, String> getObjFromEventList() {
		int intID = (int) DataHandler.getListID();
		eventList.get(intID);
		
		return eventList.get(intID);
	}
	
	// Gibt die ID des Objekts an der angegebenen Stelle an
	public static String getIDFromEventList(int intID) {
		final String TAG_ID = "_id";
		HashMap<String, String> singleEvent = eventList.get(intID);
		String testID = singleEvent.get(TAG_ID);
		
		return testID;
	}
	
	// Gibt die Grösse der eventList zurück
	public static int getEventListSize() {
		int size = eventList.size();
		
		return size;
	}
	
	// Löscht die Liste (bei LogOut)
	public static void clearEventList() {
		eventList.clear();
	}
	
	// Liste, so wie sie aktuell auf dem Server vorhanden ist
	private static ArrayList<HashMap<String, String>> compareList = new ArrayList<HashMap<String, String>>();

	// Get
	public static ArrayList<HashMap<String, String>> getCompareList() {
		return compareList;
	}
	
	// add
	public static void addToCompareList(HashMap<String, String> singleEvent) {
		compareList.add(singleEvent);
	}
	
	// löschen
	public static void clearCompareList() {
		compareList.clear();
	}
	
	// Rückgabe Objekt an Stelle intID
	public static HashMap<String, String> getObjFromCompareList(int intID) {
		return compareList.get(intID);
	}
	
	// NameValuePair Liste für RequestBody
	private static ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();

	// Rückgabe Parameter Liste
	public static ArrayList<NameValuePair> getParamList() {
		return paramList;
	}
	
	// Parameter hinzufügen
	public static void addToParamList(NameValuePair nameValuePair) {
		paramList.add(nameValuePair);
		Log.d("ListHandler","params: " + nameValuePair);
	}
	
	// Liste löschen
	public static void clearParamList() {
		paramList.clear();
	}

}

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
	protected static String PREFS_FILE = "saveList";
	protected static String PREFS_ARRAY = "jsonArray";
	
	private static SharedPreferences prefs;
	
	private static final String TAG_ID = "_id";
	private static final String TAG_DATE = "Date";
	private static final String TAG_NAME = "name";
	private static final String TAG_SHARED = "sharedw";
	private static final String TAG_USER = "user";
	
	private static ArrayList<HashMap<String, String>> eventList = new ArrayList<HashMap<String, String>>();
	
	public static void saveList(Context context) {
		JSONArray toSave = new JSONArray(eventList);
		Log.d("ListHandler","JARRAY: " + toSave);
		
		prefs = context.getApplicationContext().getSharedPreferences(PREFS_FILE, 0);
		prefs.edit().putString(PREFS_ARRAY, toSave.toString()).commit();
		
	}
	
	public static void readList(Context context) throws JSONException {
		prefs = context.getSharedPreferences(PREFS_FILE, 0);
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

			HashMap<String, String> singleEvent = new HashMap<String, String>();

			// adding each child node to HashMap key => value
			singleEvent.put(TAG_NAME, name);
			singleEvent.put(TAG_ID, _id);
			singleEvent.put(TAG_DATE, date);
			singleEvent.put(TAG_SHARED, shared);
			singleEvent.put(TAG_USER, createdbyUser);

			// Objekt zu Liste hinzufügen
			addToEventList(singleEvent);
			prefs.edit().clear().commit();
		}

		}
	}

	public static ArrayList<HashMap<String, String>> getEventList() {
		return eventList;
	}

	public static void addToEventList(HashMap<String, String> singleEvent) {
		eventList.add(singleEvent);
	}
	public static void updateObjEventList(int listID, HashMap<String, String> singleEvent) {
		eventList.set(listID, singleEvent);
		
	}

	public static void deleteFromEventList(long id) {
		int intID = (int) id;
		eventList.remove(intID);
		Log.d("ListHandler", "eventList removed id" + id);
	}
	public static HashMap<String, String> getObjFromEventList() {
		int intID = (int) DataHandler.getListID();
		eventList.get(intID);
		
		return eventList.get(intID);
	}
	
	public static String getIDFromEventList(int intID) {
		final String TAG_ID = "_id";
		HashMap<String, String> singleEvent = eventList.get(intID);
		String testID = singleEvent.get(TAG_ID);
		
		return testID;
	}
	
	public static int getEventListSize() {
		int size = eventList.size();
		
		return size;
	}
	

	public static void clearEventList() {
		eventList.clear();
	}
	
	private static ArrayList<HashMap<String, String>> compareList = new ArrayList<HashMap<String, String>>();

	public static ArrayList<HashMap<String, String>> getCompareList() {
		return compareList;
	}

	public static void addToCompareList(HashMap<String, String> singleEvent) {
		compareList.add(singleEvent);
	}
	
	public static void clearCompareList() {
		compareList.clear();
	}
	
	public static HashMap<String, String> getObjFromCompareList(int intID) {
		Log.d("ListHandler","compObj:" + compareList.get(intID));
		return compareList.get(intID);
	}
	
	private static ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();

	public static ArrayList<NameValuePair> getParamList() {
		return paramList;
	}
	
	public static void addToParamList(NameValuePair nameValuePair) {
		paramList.add(nameValuePair);
		Log.d("ListHandler","params: " + nameValuePair);
	}

	public static void clearParamList() {
		paramList.clear();
	}

}

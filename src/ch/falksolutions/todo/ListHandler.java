/** Beinhaltet die Liste für die ToDos und Methoden um sie zu löschen verändern etc.
 * 
 */

package ch.falksolutions.todo;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;

import android.util.Log;

public class ListHandler {
	
	private static ArrayList<HashMap<String, String>> eventList = new ArrayList<HashMap<String, String>>();

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
		Log.d("DataHandler", "eventList removed id" + id);
	}
	public static HashMap<String, String> getObjFromEventList() {
		int intID = (int) DataHandler.getListID();
		eventList.get(intID);
		
		return eventList.get(intID);
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

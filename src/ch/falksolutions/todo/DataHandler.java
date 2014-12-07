
/** Die Klasse ist fuer das Handling mit den Parametern verantwortlich und speichert den User und das Passwort
 * 
 */

package ch.falksolutions.todo;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class DataHandler {
	
	private static String STANURL = "http://192.168.178.162:8080/api";
	private static String url = STANURL;
	private static String user = null;
	private static String password = null;
	private static String[] sonderzeichen = {"&","?","%","+","#"};
	private static boolean enthaeltSonderzeichen;
	
	private static ArrayList<HashMap<String, String>> eventList = new ArrayList<HashMap<String, String>>();
	
	public static ArrayList<HashMap<String, String>> getEventList() {
		return eventList;
	}
	
	public static void addToEventList(HashMap<String, String> singleEvent) {
		eventList.add(singleEvent);
	}
	public static void deleteFromEventList(long id) {
		int intID = (int) id;
		eventList.remove(intID);
		Log.d("DataHandler", "eventList removed id" + id);
	}
	public static void clearEventList() {
		eventList.clear();
	}
	
	private static ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
	
	
	
	public static ArrayList<NameValuePair> getParamList() {
		return paramList;
	}

	public static void clearParamList() {
		paramList.clear();
	}

	public static void setStanUrl(String pUrl) {
		String mUrl = "http://";
		mUrl+= pUrl;
		mUrl+= ":8080/api";
		DataHandler.STANURL = mUrl;
	}
	
	public static String getUrl() {
		return url;
	}
		
	public static void setUrl(String url) {
		DataHandler.url = url;
	}
	public static String getUser() {
		return user;
	}
	public static void setUser(String user) {
		DataHandler.user = user;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		DataHandler.password = password;
	}

	public static void postData(String name) {
		url = STANURL;
		clearParamList();
		MainActivity.setMethod(2);
		paramList.add(new BasicNameValuePair("usr",user));
		paramList.add(new BasicNameValuePair("pass",password));
		paramList.add(new BasicNameValuePair("text",name));
		Log.d("DataHandler","paramList: " + paramList);
		setUrl(url);
		MainActivity.setUrl(url);
	}
	
	public static void removeData(String id) {
		url = STANURL;
		clearParamList();
		url+= "/" + id;
		paramList.add(new BasicNameValuePair("usr", user));
		paramList.add(new BasicNameValuePair("pass", password));
		
		setUrl(url);
		MainActivity.setUrl(url);
		}
	
	public static void getData() {
		url = STANURL;
		url+= "?usr=" + user + "&pass=" + password;
		setUrl(url);
		MainActivity.setUrl(url);
		Log.d("DataHandler", "getURL= " + url);
	}
	
	public static void updateData(String id, String text) {
		url = STANURL;
		url += "/" + id;
		clearParamList();
		MainActivity.setMethod(3);
		paramList.add(new BasicNameValuePair("usr", user));
		paramList.add(new BasicNameValuePair("pass", password));
		paramList.add(new BasicNameValuePair("text", text));
		setUrl(url);
		MainActivity.setUrl(url);
	}

	public static void userLogin(String user, String passwort) {
		url = STANURL;
		url += "/login";
		
		paramList.add(new BasicNameValuePair("usr",user));
		paramList.add(new BasicNameValuePair("pass",passwort));
		setUrl(url);
		Log.d("DataHandler", "loginURL= " + url);
		}
	
	public static void createUser(String user, String passwort) {
		url = STANURL;
		url += "/create";
		
		paramList.add(new BasicNameValuePair("usr",user));
		paramList.add(new BasicNameValuePair("pass",passwort));
		setUrl(url);
		Log.d("DataHandler", "createURL= " + url);
	}
	
	public static void logOutUser() {
		setUser(null);
		setPassword(null);
	}
	
	public static boolean analyzeString(String input) {
		String inputString =  new String(input);
		enthaeltSonderzeichen = false; //Standardwert
		
		for (int i=0; i<5; i++) {
			if (inputString.contains(sonderzeichen[i])) {
				Log.d("DataHandler","String contains " + sonderzeichen[i]);
				enthaeltSonderzeichen = true;
			} 
		}
		return enthaeltSonderzeichen;	
	}
	public static String replaceOutPut(String input) {
		String inputString = new String(input);
		
		if (inputString.contains(" ")) {
			Log.d("DataHandler","oldString: " + inputString);
			inputString = inputString.replace(" ", "+");
			Log.d("DataHandler","newString: " + inputString);
			
			
		} return inputString;
	} 
	
}








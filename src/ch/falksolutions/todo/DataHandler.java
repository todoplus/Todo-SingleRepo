
/** Die Klasse ist fuer das Handling mit den Parametern verantwortlich und speichert den User und das Passwort
 * 
 */

package ch.falksolutions.todo;

import android.util.Log;

public class DataHandler {
	private static final String STANURL = "http://192.168.178.162:8080/";
	private static String url = STANURL;
	private static String user = null;
	private static String password = null;
	private static String[] sonderzeichen = {"&","?","%"};
	private static boolean enthaeltSonderzeichen;
	
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

	public static void putData(String name) {
		url = STANURL;
		url += "put";
		url += "?usr="  + user + "&pass=" + password + "&text=" + name;
		setUrl(url);
		MainActivity.setUrl(url);
	}
	
	public static void removeData(String id) {
		url = STANURL;
		url += "rmv";
		url+= "?usr=" + user + "&pass=" + password + "&id=" + id;
		setUrl(url);
		MainActivity.setUrl(url);
		}
	
	public static void getData() {
		url = STANURL;
		url+= "get";
		url+= "?usr=" + user + "&pass=" + password;
		setUrl(url);
		MainActivity.setUrl(url);
		Log.d("DataHandler", "getURL= " + url);
	}
	
	public static void updateData(String id, String text) {
		url = STANURL;
		url += "update";
		url += "?id=" + id + "&usr=" + user + "&pass=" + password + "&new=" + text;
		setUrl(url);
		MainActivity.setUrl(url);
	}

	public static void userLogin(String user, String passwort) {
		url = STANURL;
		url += "login";
		url += "?usr=" + user + "&pass=" + passwort;
		setUrl(url);
		Log.d("DataHandler", "loginURL= " + url);
		}
	
	public static void createUser(String user, String passwort) {
		url = STANURL;
		url += "createuser";
		url += "?usr=" + user + "&pass=" + passwort;
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
		
		for (int i=0; i<3; i++) {
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








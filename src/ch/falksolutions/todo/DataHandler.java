/** Die Klasse ist fuer das Handling mit den Parametern verantwortlich und speichert den User und das Passwort
 * 
 */

package ch.falksolutions.todo;

import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class DataHandler {

	private static String STANURL = "https://new-todoplus.c9.io/api";
	private static String url = STANURL;
	private static String ssid = UserHandler.getSsid();
	private static boolean response;
	private static long listID;
	
	public static void setSsid(String ssid) {
		DataHandler.ssid = ssid;
	}
	
	
	
	public static void setStanUrl(String pUrl) {
		String mUrl = "http://";
		mUrl += pUrl;
		mUrl += ":8080/api";
		DataHandler.STANURL = mUrl;
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		DataHandler.url = url;
	}


	public static void postData(String name, String shared) {
		url = STANURL;
		ListHandler.clearParamList();
		MainActivity.setMethod(2);
		ListHandler.addToParamList(new BasicNameValuePair("ssid", ssid));
		ListHandler.addToParamList(new BasicNameValuePair("text", name));
		ListHandler.addToParamList(new BasicNameValuePair("shared", shared));
		
		setUrl(url);
		MainActivity.setUrl(url);
	}

	public static void removeData(String id) {
		url = STANURL;
		MainActivity.setMethod(4);
		url += "/" + id + "/" + ssid;
		setUrl(url);
		MainActivity.setUrl(url);
	}

	public static void getData() {
		url = STANURL;
		url += "?ssid=" + ssid;
		setUrl(url);
		MainActivity.setUrl(url);
	}

	public static void updateData(String id, String text) {
		url = STANURL;
		url += "/" + id;
		ListHandler.clearParamList();
		MainActivity.setMethod(3);
		ListHandler.addToParamList(new BasicNameValuePair("ssid", ssid));
		ListHandler.addToParamList(new BasicNameValuePair("text", text));
		setUrl(url);
		MainActivity.setUrl(url);
	}

	public static void userLogin(String user, String passwort, String deviceID) {
		url = STANURL;
		url += "/login";
		ListHandler.clearParamList();
		ListHandler.addToParamList(new BasicNameValuePair("usr", user));
		ListHandler.addToParamList(new BasicNameValuePair("pass", passwort));
		ListHandler.addToParamList(new BasicNameValuePair("device", deviceID));
		setUrl(url);
		Log.d("DataHandler", "loginURL= " + url);
	}

	public static boolean createUser(String user, String passwort, String deviceID) {
		url = STANURL;
		url += "/create";
		response = false;
		ListHandler.clearParamList();
		ListHandler.addToParamList(new BasicNameValuePair("device", deviceID));
		if (user.equals("") != true) {
			ListHandler.addToParamList(new BasicNameValuePair("usr", user));
			if (passwort.equals("") != true) {
					ListHandler.addToParamList(new BasicNameValuePair("pass", passwort));
					setUrl(url);
					response = true;
			}
		}
		return response;
		
		
	}
	public static void userLogOut() {
		url = STANURL;
		url += "/logout";
		MainActivity.setMethod(2);
		ListHandler.clearParamList();
		ListHandler.addToParamList(new BasicNameValuePair("ssid",ssid));
		MainActivity.setUrl(url);
	}

	
	
	public static void saveListID(long ID) {
		DataHandler.listID = ID;
		
	}
	public static long getListID() {
		return listID;
	}

}

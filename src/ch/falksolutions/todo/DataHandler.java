/** Die Klasse ist fuer das Handling mit den Parametern verantwortlich und speichert den User und das Passwort
 * 
 */

package ch.falksolutions.todo;

import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class DataHandler {

	private static String STANURL = "http://192.168.178.162:8080/api";
	private static String url = STANURL;
	private static String user = null;
	private static String password = null;
	private static boolean response;



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

	public static void postData(String name, String shared) {
		url = STANURL;
		ListHandler.clearParamList();
		MainActivity.setMethod(2);
		ListHandler.addToParamList(new BasicNameValuePair("usr", user));
		ListHandler.addToParamList(new BasicNameValuePair("pass", password));
		ListHandler.addToParamList(new BasicNameValuePair("text", name));
		ListHandler.addToParamList(new BasicNameValuePair("shared", shared));
		
		setUrl(url);
		MainActivity.setUrl(url);
	}

	public static void removeData(String id) {
		url = STANURL;
		MainActivity.setMethod(4);
		url += "/" + id + "/" + user + "/" + password;
		setUrl(url);
		MainActivity.setUrl(url);
	}

	public static void getData() {
		url = STANURL;
		url += "?usr=" + user + "&pass=" + password;
		setUrl(url);
		MainActivity.setUrl(url);
	}

	public static void updateData(String id, String text) {
		url = STANURL;
		url += "/" + id;
		ListHandler.clearParamList();
		MainActivity.setMethod(3);
		ListHandler.addToParamList(new BasicNameValuePair("usr", user));
		ListHandler.addToParamList(new BasicNameValuePair("pass", password));
		ListHandler.addToParamList(new BasicNameValuePair("text", text));
		setUrl(url);
		MainActivity.setUrl(url);
	}

	public static void userLogin(String user, String passwort) {
		url = STANURL;
		url += "/login";
		ListHandler.clearParamList();
		ListHandler.addToParamList(new BasicNameValuePair("usr", user));
		ListHandler.addToParamList(new BasicNameValuePair("pass", passwort));
		setUrl(url);
		Log.d("DataHandler", "loginURL= " + url);
	}

	public static boolean createUser(String user, String passwort) {
		url = STANURL;
		url += "/create";
		response = false;
		ListHandler.clearParamList();
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

	public static void logOutUser() {
		setUser(null);
		setPassword(null);
		ListHandler.clearEventList();
	}

}

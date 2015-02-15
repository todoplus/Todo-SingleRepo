/** Die Klasse ist für das Handling mit den URL Parametern verantwortlich je nach Request Type
 * 
 */

package ch.falksolutions.todo;

import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class DataHandler {

	// URL Params
	private static String STANURL = "http://192.168.178.162:8080/api";
	private static String url = STANURL;
	private static String ssid = UserHandler.getSsid();
	private static boolean response;
	private static long listID;
	
	// SSID für Server Anfragen
	public static void setSsid(String ssid) {
		DataHandler.ssid = ssid;
	}
	
	
	// dev: URL für Serveranfragen setzen
	public static void setStanUrl(String pUrl) {
		String mUrl = "http://";
		mUrl += pUrl;
		mUrl += ":8080/api";
		DataHandler.STANURL = mUrl;
	}

	// Get
	public static String getUrl() {
		return url;
	}
	
	// Set
	public static void setUrl(String url) {
		DataHandler.url = url;
	}


	// Parameter für POST Request
	public static void postData(String name, String shared) {
		url = STANURL;
		ListHandler.clearParamList();
		MainActivity.setMethod(2);
		ListHandler.addToParamList(new BasicNameValuePair("ssid", ssid));
		ListHandler.addToParamList(new BasicNameValuePair("text", name));
		ListHandler.addToParamList(new BasicNameValuePair("shared", shared));
		ListHandler.addToParamList(new BasicNameValuePair("prio", "1"));
		ListHandler.addToParamList(new BasicNameValuePair("groups", ""));
		
		setUrl(url);
		MainActivity.setUrl(url);
	}
	
	// Parameter für DELETE Request
	public static void removeData(String id) {
		url = STANURL;
		MainActivity.setMethod(4);
		url += "/" + id + "/" + ssid;
		setUrl(url);
		MainActivity.setUrl(url);
	}
	
	// Parameter für GET Request
	public static void getData() {
		url = STANURL;
		url += "?ssid=" + ssid;
		setUrl(url);
		MainActivity.setUrl(url);
	}
	
	// Parameter für PUT Request
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

	// Parameter für Login (POST Request)
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

	// Parameter für Create (POST Request)
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
	
	// Parameter für Logout (POST Request)
	public static void userLogOut() {
		url = STANURL;
		url += "/logout";
		MainActivity.setMethod(2);
		ListHandler.clearParamList();
		ListHandler.addToParamList(new BasicNameValuePair("ssid",ssid));
		MainActivity.setUrl(url);
	}
	
	public static void createGroupParams(String groupName, String groupMember) {
		url = STANURL;
		url += "/group";
		ListHandler.clearParamList();
		ListHandler.addToParamList(new BasicNameValuePair("ssid",ssid));
		ListHandler.addToParamList(new BasicNameValuePair("groupname",groupName));
		ListHandler.addToParamList(new BasicNameValuePair("members",groupMember));
		MainActivity.setMethod(2);
		MainActivity.setUrl(url);
		
	}

	
	// Temporäres Speichern des Index eines Objekt's
	public static void saveListID(long ID) {
		DataHandler.listID = ID;
		
	}
	// Getter
	public static long getListID() {
		return listID;
	}
	

}

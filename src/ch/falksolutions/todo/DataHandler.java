<<<<<<< Updated upstream
<<<<<<< HEAD
=======
>>>>>>> Stashed changes
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
	
	public static String getUrl() {
		return url;
<<<<<<< Updated upstream
=======
package ch.falksolutions.todo;

public class DataHandler {
	private static final String STANURL = "http://192.168.1.220:8080/";
	private static String url = STANURL;
	private static String user = "testuser";
	private static String password = "md5hash";
	
	public static String getUrl() {
<<<<<<< HEAD
		return url;
=======
		return STANURL;
>>>>>>> FETCH_HEAD
>>>>>>> FETCH_HEAD
=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======
		MainActivity.setUrl(url);
>>>>>>> Stashed changes
	}
	
	public static void removeData(String id) {
		url = STANURL;
		url += "rmv";
		url+= "?usr=" + user + "&pass=" + password + "&id=" + id;
<<<<<<< Updated upstream
<<<<<<< HEAD
		setUrl(url);
		}
=======
<<<<<<< HEAD
		setUrl(url);
		}
=======
	}
>>>>>>> FETCH_HEAD
>>>>>>> FETCH_HEAD
=======
		setUrl(url);
		MainActivity.setUrl(url);
		}
>>>>>>> Stashed changes
	
	public static void getData() {
		url = STANURL;
		url+= "get";
		url+= "?usr=" + user + "&pass=" + password;
<<<<<<< Updated upstream
<<<<<<< HEAD
		setUrl(url);
		Log.d("DataHanlder", "getURL= " + url);
=======
<<<<<<< HEAD
		setUrl(url);
>>>>>>> FETCH_HEAD
=======
		setUrl(url);
		MainActivity.setUrl(url);
		Log.d("DataHanlder", "getURL= " + url);
	}
	
	public static void updateData(String id) {
		url = STANURL;
		url += "update";
		url += "?id=" + id + "&usr=" + user + "&pass=" + password;
		setUrl(url);
		MainActivity.setUrl(url);
>>>>>>> Stashed changes
	}

	public static void userLogin(String user, String passwort) {
		url = STANURL;
		url += "login";
<<<<<<< Updated upstream
<<<<<<< HEAD
		url += "?usr=" + user + "&pass=" + passwort;
		setUrl(url);
		Log.d("DataHandler", "loginURL= " + url);
=======
		url += "?usr=" + user + "&pass" + passwort;
		setUrl(url);
>>>>>>> FETCH_HEAD
=======
		url += "?usr=" + user + "&pass=" + passwort;
		setUrl(url);
		Log.d("DataHandler", "loginURL= " + url);
>>>>>>> Stashed changes
	}
}


<<<<<<< Updated upstream
<<<<<<< HEAD
=======
=======
		
	}
}
>>>>>>> FETCH_HEAD
>>>>>>> FETCH_HEAD
=======
>>>>>>> Stashed changes

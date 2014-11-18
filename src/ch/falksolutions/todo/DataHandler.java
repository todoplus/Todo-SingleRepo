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
	}
	
	public static void removeData(String id) {
		url = STANURL;
		url += "rmv";
		url+= "?usr=" + user + "&pass=" + password + "&id=" + id;
<<<<<<< HEAD
		setUrl(url);
		}
=======
	}
>>>>>>> FETCH_HEAD
	
	public static void getData() {
		url = STANURL;
		url+= "get";
		url+= "?usr=" + user + "&pass=" + password;
<<<<<<< HEAD
		setUrl(url);
	}

	public static void userLogin(String user, String passwort) {
		url = STANURL;
		url += "login";
		url += "?usr=" + user + "&pass" + passwort;
		setUrl(url);
	}
}


=======
		
	}
}
>>>>>>> FETCH_HEAD

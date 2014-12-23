package ch.falksolutions.todo;

public class UserHandler {
	private static String user;
	
	private static String ssid;
	private static String deviceID;
	
	
	public static String getUser() {
		return user;
	}
	public static void setUser(String user) {
		UserHandler.user = user;
	}
	public static String getSsid() {
		return ssid;
	}
	public static void setSsid(String ssid) {
		UserHandler.ssid = ssid;
	}
	public static String getDeviceID() {
		return deviceID;
	}
	public static void setDeviceID(String deviceID) {
		UserHandler.deviceID = deviceID;
	}
	

	
	
	
	

}

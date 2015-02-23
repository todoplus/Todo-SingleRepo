package ch.falksolutions.todo;

import java.util.ArrayList;
import java.util.HashMap;

public class WhitelistHandler {
private static final String TAG_WHITELISTUSER = "white";
	
	private static ArrayList<HashMap<String, String>> whitelist = new ArrayList<HashMap<String, String>>();

	
	public static ArrayList<HashMap<String, String>> getWhitelist() {
		return whitelist;
	}
	
	public static void addToWhitelist(HashMap<String, String> whitelistMember) {
		whitelist.add(whitelistMember);
	}
	
	public static void deleteFromWhitelist(int id) {
		whitelist.remove(id);
	}
	
	public static String getItemFromWhitelist(long id) {
		HashMap<String, String> group = whitelist.get((int) id);
		String groupName = group.get(TAG_WHITELISTUSER);
		
		return groupName;
	}
	public static void clearWhitelist() {
		whitelist.clear();
	}
	public static int getWhitelistSize() {
		return whitelist.size();
	}
}

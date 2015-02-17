package ch.falksolutions.todo;

import java.util.ArrayList;
import java.util.HashMap;

public class WhitelistHandler {
private static final String TAG_WHITELISTUSER = "groupname";
	
	private static ArrayList<HashMap<String, String>> whitelist = new ArrayList<HashMap<String, String>>();

	
	public static ArrayList<HashMap<String, String>> getWhitelist() {
		return whitelist;
	}
	
	public static void addToWhitelist(HashMap<String, String> whitelistMember) {
		whitelist.add(whitelistMember);
	}
	
	public static void deleteFromWhitelist(HashMap<String, String> whitelistMember) {
		int groupIndex = whitelist.indexOf(whitelistMember);
		whitelist.remove(groupIndex);
	}
	
	public static String getItemFromwhitelist(long id) {
		HashMap<String, String> group = whitelist.get((int) id);
		String groupName = group.get(TAG_WHITELISTUSER);
		
		return groupName;
	}
	public static void clearWhitelist() {
		whitelist.clear();
	}
}

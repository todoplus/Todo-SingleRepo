package ch.falksolutions.todo;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupHandler {
	
	private static final String TAG_GROUPNAME = "groupname";
	
	private static ArrayList<HashMap<String, String>> groupList = new ArrayList<HashMap<String, String>>();
	private static ArrayList<HashMap<String, String>> groupCompareList = new ArrayList<HashMap<String, String>>();
	
	public static ArrayList<HashMap<String, String>> getGroupList() {
		return groupList;
	}
	
	public static void addToGroupList(HashMap<String, String> group) {
		groupList.add(group);
	}
	
	public static void deleteFromGroupList(HashMap<String, String> group) {
		int groupIndex = groupList.indexOf(group);
		groupList.remove(groupIndex);
	}
	
	public static void addToGroupCompareList(HashMap<String, String> group) {
		groupCompareList.add(group);
	}
	
	public static void clearGroupCompareList() {
		groupCompareList.clear();
	}
	
	public static String getItemFromGroupList(long id) {
		HashMap<String, String> group = groupList.get((int) id);
		String groupName = group.get(TAG_GROUPNAME);
		
		return groupName;
	}
	public static void clearGroupList() {
		groupList.clear();
	}

}

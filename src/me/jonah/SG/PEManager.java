package me.jonah.SG;

import java.util.ArrayList;

public class PEManager {
	
	public static ArrayList<String> portal = new ArrayList<String>();
	public static ArrayList<String> flame = new ArrayList<String>();
	public static ArrayList<String> enchant = new ArrayList<String>();
	public static ArrayList<String> smoke = new ArrayList<String>();
	public static ArrayList<String> crit = new ArrayList<String>();	
	public static ArrayList<String> redstone = new ArrayList<String>();	
	
	public static void clear(String name){	
		if (portal.contains(name)) portal.remove(name);
		if (flame.contains(name)) flame.remove(name);
		if (enchant.contains(name)) enchant.remove(name);
		if (smoke.contains(name)) smoke.remove(name);
		if (crit.contains(name)) crit.remove(name);
		if (redstone.contains(name)) redstone.remove(name);
	}
	
	public static String getKit(String name){
		if (portal.contains(name)) return "Ender";
		if (flame.contains(name)) return "Flame";
		if (enchant.contains(name)) return "Enchant";
		if (smoke.contains(name)) return "Smoke";
		if (crit.contains(name)) return "Crit";
		if (redstone.contains(name)) return "Redstone";
		
		return "None Selected";
	}
	

}

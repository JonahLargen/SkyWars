package me.jonah.SG;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class ArenaManager {

	public static ArrayList<Arena> arenaList = new ArrayList<Arena>();
	public static HashMap<Integer,Arena> idToArena = new HashMap<Integer,Arena>();
	public static ArrayList<String> buildMode = new ArrayList<String>();

	public static Arena getArena(int id){
		for (Arena arena: arenaList){
			if (arena.getId() == id){
				return arena;
			}
		}
		return null;
	}

	public static String getKit(Player player){
		return EventListener.main.getConfig().getString(player.getUniqueId() + ".kit");
	}

	public static void setKit(Player player, KitType kit){
		EventListener.main.getConfig().set(player.getUniqueId() + ".kit", kit.toString());
		EventListener.main.saveConfig();
	}

	public static int getPlayerCount(int id){
		int f = 0;
		for (int i = 1; EventListener.main.getConfig().get("~Arenas." + id + ".spawn." + i + ".world") != null; i++){
			f=i;
		}
		return f;
	}

}

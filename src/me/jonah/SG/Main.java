package me.jonah.SG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("unused")
public class Main extends JavaPlugin{

	public static final Logger log = Bukkit.getLogger();
	private int tipCounter = 1;
	public static boolean doubleXP = false;

	public void onEnable() {
		for (Player p: Bukkit.getOnlinePlayers()){
			p.kickPlayer(ChatColor.AQUA + "Server Restarting. Rejoin!");
		}
		this.getConfig();
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
		log.info("Survival Games has been enabled");
		this.setUpArenas();
		this.updateSignLoop();
		this.setUpTips();
		this.updatePE();
	}

	public void onDisable() {

		for (int id = 1; this.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
			Arena arena = ArenaManager.getArena(id);
			arena.stop();
		}

		for (Player p: Bukkit.getOnlinePlayers()){
			p.kickPlayer(ChatColor.AQUA + "Server Restarting. Rejoin!");
		}

		log.info("Survival Games has been disabled");
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player){
			Player player = (Player)sender;
			if (player.isOp()){
				//Bosses Only Commands
				if (label.equalsIgnoreCase("center") && args.length == 1){
					this.getConfig().set("~Arenas." + args[0] + ".center.world", player.getLocation().getWorld().getName());
					this.getConfig().set("~Arenas." + args[0] + ".center.x", player.getLocation().getBlockX());
					this.getConfig().set("~Arenas." + args[0] + ".center.y", player.getLocation().getBlockY());
					this.getConfig().set("~Arenas." + args[0] + ".center.z", player.getLocation().getBlockZ());
					this.saveConfig();
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "Arena " + args[0] + " has center set to your location");
				}
				if (label.equalsIgnoreCase("center") && args.length != 1){
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Incorrect parameters. Usage: '/center <id>'");
				}
				if (label.equalsIgnoreCase("setwins") && args.length == 2){
					this.getConfig().set(Bukkit.getOfflinePlayer(args[0]).getUniqueId() + ".wins", Integer.valueOf(args[1]));
					this.saveConfig();
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + args[0] + "'s level was set to " + args[1]);
				}
				if (label.equalsIgnoreCase("setwins") && args.length != 2){
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Incorrect parameters. Usage: '/setwins <player> <wins>'");
				}
				if (label.equalsIgnoreCase("setcredits") && args.length == 2){
					this.getConfig().set(Bukkit.getOfflinePlayer(args[0]).getUniqueId() + ".credits", Integer.valueOf(args[1]));
					this.saveConfig();
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + args[0] + "'s credits was set to " + args[1]);
				}
				if (label.equalsIgnoreCase("setcredits") && args.length != 2){
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Incorrect parameters. Usage: '/setcredits <player> <wins>'");
				}
				if (label.equalsIgnoreCase("addcredits") && args.length == 2){
					this.getConfig().set(Bukkit.getOfflinePlayer(args[0]).getUniqueId() + ".credits", Integer.valueOf(args[1]) + this.getConfig().getInt(Bukkit.getOfflinePlayer(args[0]).getUniqueId() + ".credits"));
					this.saveConfig();
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + args[0] + "'s credits were boosted by " + args[1]);
				}
				if (label.equalsIgnoreCase("addcredits") && args.length != 2){
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Incorrect parameters. Usage: '/addcredits <player> <wins>'");
				}
				if (label.equalsIgnoreCase("createarena") && args.length == 2){
					if (!(this.getConfig().contains("~Arenas." + args[0] + ".name"))){
						this.getConfig().createSection("~Arenas." + args[0] + ".name");
						this.getConfig().set("~Arenas." + args[0] + ".name", args[1]);
						this.saveConfig();
						player.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "Arena " + args[0] + " created with the name " + args[1]);
						Arena arena = new Arena(new ArrayList<UUID>(), ArenaStatus.WAITING, Integer.valueOf(args[0]), 600, this.getConfig().getString("~Arenas." + Integer.valueOf(args[0]) + ".name"));
						ArenaManager.arenaList.add(arena);
						ArenaManager.idToArena.put(Integer.valueOf(args[0]), arena);
					}else{
						player.sendMessage(ChatColor.RED + "The arena " + args[0] + " already exists");
					}
				}
				if (label.equalsIgnoreCase("createarena") && args.length != 2){
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Incorrect parameters. Usage: '/createarena <id> <name>'");
				}
				if (label.equalsIgnoreCase("addspawn") && args.length == 2){
					this.getConfig().set("~Arenas." + args[0] + ".spawn." + args[1] + ".world", player.getLocation().getWorld().getName());
					this.getConfig().set("~Arenas." + args[0] + ".spawn." + args[1] + ".x", player.getLocation().getX());
					this.getConfig().set("~Arenas." + args[0] + ".spawn." + args[1] + ".y", player.getLocation().getY());
					this.getConfig().set("~Arenas." + args[0] + ".spawn." + args[1] + ".z", player.getLocation().getZ());
					this.getConfig().set("~Arenas." + args[0] + ".spawn." + args[1] + ".yaw", player.getLocation().getYaw());
					this.getConfig().set("~Arenas." + args[0] + ".spawn." + args[1] + ".pitch", player.getLocation().getPitch());
					this.saveConfig();
					int id = Integer.valueOf(args[0]);
					Arena arena = ArenaManager.getArena(id);
					arena.numPlayers = ArenaManager.getPlayerCount(id);
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "Arena " + args[0] + " has spawn " + args[1] + " set to your location");
				}
				if (label.equalsIgnoreCase("addspawn") && args.length != 2){
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Incorrect parameters. Usage: '/addspawn <id> <spawnNumber>'");
				}
				if (label.equalsIgnoreCase("stoparena") && args.length == 1){
					Arena arena = ArenaManager.getArena(Integer.valueOf(args[0]));
					if (arena.getStatus() == ArenaStatus.INGAME){
						arena.stop();
						player.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "Arena " + args[0] + " stopped.");
					}else{
						player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "This arena is not in game!");
					}
				}
				if (label.equalsIgnoreCase("pk") && args.length == 1){
					this.getConfig().set("~Parkour." + args[0] + ".world", player.getLocation().getWorld().getName());
					this.getConfig().set("~Parkour." + args[0] + ".x", player.getLocation().getBlockX());
					this.getConfig().set("~Parkour." + args[0] + ".y", player.getLocation().getBlockY());
					this.getConfig().set("~Parkour." + args[0] + ".z", player.getLocation().getBlockZ());
					this.saveConfig();
				}
				if (label.equalsIgnoreCase("pk") && args.length != 1){
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Incorrect parameters. Usage: '/pk <id>'");
				}
				if (label.equalsIgnoreCase("ss")){
					this.getConfig().set("~Spawn.world", player.getLocation().getWorld().getName());
					this.getConfig().set("~Spawn.x", player.getLocation().getX());
					this.getConfig().set("~Spawn.y", player.getLocation().getY());
					this.getConfig().set("~Spawn.z", player.getLocation().getZ());
					this.getConfig().set("~Spawn.yaw", player.getLocation().getYaw());
					this.getConfig().set("~Spawn.pitch", player.getLocation().getPitch());
					this.saveConfig();
				}
				if (label.equalsIgnoreCase("sgreload")){
					this.reloadConfig();
					player.sendMessage("sgreload");
				}
				if (label.equalsIgnoreCase("bm")){
					if (!ArenaManager.buildMode.contains(player.getName())){
						ArenaManager.buildMode.add(player.getName());
						player.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "Build Mode Enabled");
					}else{
						ArenaManager.buildMode.remove(player.getName());
						player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Build Mode Disabled");
					}
				}
				if (label.equalsIgnoreCase("forcestart") && args.length == 1){
					Arena arena = ArenaManager.getArena(Integer.valueOf(args[0]));
					if (arena.getStatus() != ArenaStatus.INGAME && arena.players.size() > 0){
						arena.sendAll(Methods.getDefaultPrefix() + ChatColor.BLUE + player.getName() + " has force started the game!");
						arena.stopTimer(false);
					}else{
						player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Arena cannot be in game and must have 1 person!'");
					}
				}
				if (label.equalsIgnoreCase("forcestart") && args.length != 1){
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Incorrect parameters. Usage: '/forcestart <id>'");
				}
				if (label.equalsIgnoreCase("dxp")){
					boolean b = this.getConfig().getBoolean("~DXP");
					this.saveConfig();
					b = !b;
					this.saveConfig();
					this.getConfig().set("~DXP", b);
					this.saveConfig();
					b = this.getConfig().getBoolean("~DXP");
					this.saveConfig();
					player.sendMessage(b+"");
					this.saveConfig();
				}
				if (label.equalsIgnoreCase("tlive")){
					if (this.getConfig().getBoolean("~T.live") == false){
						this.getConfig().set("~T.live", true);
						this.saveConfig();
						player.sendMessage(Methods.getDefaultPrefix() + "Live");
					}else{
						player.sendMessage(Methods.getDefaultPrefix() + "Already Live");
					}
				}
				if (label.equalsIgnoreCase("tclear")){
					this.getConfig().set("~T", null);
					this.getConfig().createSection("~T");
					this.getConfig().set("~T.live", false);
					this.saveConfig();
					player.sendMessage(Methods.getDefaultPrefix() + "Not Live");
				}
				if (label.equalsIgnoreCase("pkreset")){
					Methods.resetParkour();
				}
				if (label.equalsIgnoreCase("clearlag")){
					player.sendMessage("clearlag");

					for (World world: Bukkit.getWorlds()){
						List<Entity> list = world.getEntities();
						for (Entity e: list){
							if (e instanceof Item){
								e.remove();
							}
							if (e instanceof Arrow){
								e.remove();
							}
							if (e instanceof ExperienceOrb){
								e.remove();
							}
						}
					}
				}
			}
			/*unused
				if (label.equals("pos1") && args.length == 1){
					this.getConfig().set("~Arenas." + args[0] + ".pos1.world", player.getLocation().getWorld().getName());
					this.getConfig().set("~Arenas." + args[0] + ".pos1.x", player.getLocation().getBlockX());
					this.getConfig().set("~Arenas." + args[0] + ".pos1.y", player.getLocation().getBlockY());
					this.getConfig().set("~Arenas." + args[0] + ".pos1.z", player.getLocation().getBlockZ());
					this.saveConfig();
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "Arena " + args[0] + " has pos1 " + " set to your location");
				}
				if (label.equals("pos1") && args.length != 1){
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Incorrect parameters. Usage: '/pos1 <id>'");
				}
				if (label.equals("pos2") && args.length == 1){
					this.getConfig().set("~Arenas." + args[0] + ".pos2.world", player.getLocation().getWorld().getName());
					this.getConfig().set("~Arenas." + args[0] + ".pos2.x", player.getLocation().getBlockX());
					this.getConfig().set("~Arenas." + args[0] + ".pos2.y", player.getLocation().getBlockY());
					this.getConfig().set("~Arenas." + args[0] + ".pos2.z", player.getLocation().getBlockZ());
					this.saveConfig();
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "Arena " + args[0] + " has pos2 " + " set to your location");
				}
				if (label.equals("pos2") && args.length != 1){
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Incorrect parameters. Usage: '/pos2 <id>'");
				}
			 */

			//default commands

			if (label.equalsIgnoreCase("tleaderboard")){
				if (this.getConfig().getBoolean("~T.live") == true){
					try{
						ArrayList<T> array = new ArrayList<T>();
						for (String s: this.getConfig().getKeys(true)){
							if (s.contains("~T")){
								if (!s.contains("~T.live")){
									s=s.replace("~T", "");
									s=s.replace(".", "");
									int i = this.getConfig().getInt("~T." + s);
									if (!s.equals("")) array.add(new T(s,i));
								}
							}
						}
						array = T.getHighest(array);

						player.sendMessage(ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "================================");
						player.sendMessage(ChatColor.RED + "Leaderboard: " + ChatColor.GOLD + "Tournament");
						if (array.size() > 0){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(0).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "1. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(0).name)).getName() + ": " + ChatColor.WHITE + array.get(0).wins);}
						if (array.size() > 1){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(1).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "2. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(1).name)).getName() + ": " + ChatColor.WHITE + array.get(1).wins);}
						if (array.size() > 2){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(2).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "3. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(2).name)).getName() + ": " + ChatColor.WHITE + array.get(2).wins);}
						if (array.size() > 3){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(3).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "4. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(3).name)).getName() + ": " + ChatColor.WHITE +array.get(3).wins);}
						if (array.size() > 4){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(4).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "5. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(4).name)).getName() + ": " + ChatColor.WHITE + array.get(4).wins);}
						if (array.size() > 5){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(5).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "6. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(5).name)).getName() + ": " + ChatColor.WHITE + array.get(5).wins);}
						if (array.size() > 6){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(6).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "7. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(6).name)).getName() + ": " + ChatColor.WHITE + array.get(6).wins);}
						if (array.size() > 7){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(7).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "8. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(7).name)).getName() + ": " + ChatColor.WHITE + array.get(7).wins);}
						if (array.size() > 8){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(8).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "9. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(8).name)).getName() + ": " + ChatColor.WHITE + array.get(8).wins);}
						if (array.size() > 9){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(9).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "10. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(9).name)).getName() + ": " + ChatColor.WHITE + array.get(9).wins);}
						player.sendMessage(ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "================================");
					}catch(Exception e){
						player.sendMessage(ChatColor.RED + "Error Loading Leaderboard");
					}
				}else{
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "A tournament is not currently live!");
				}
			}
			if (label.equalsIgnoreCase("leaderboard")){
				try{
					ArrayList<T> array = new ArrayList<T>();
					for (String s: this.getConfig().getKeys(true)){
						if (!s.contains("~") && s.contains(".wins")){
							int i = this.getConfig().getInt(s);
							s=s.substring(0, s.indexOf("."));
							array.add(new T(s,i));
						}
					}
					array = T.getHighest(array);

					player.sendMessage(ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "================================");
					player.sendMessage(ChatColor.RED + "Leaderboard: " + ChatColor.GOLD + "Global");
					if (array.size() > 0){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(0).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "1. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(0).name)).getName() + ": " + ChatColor.WHITE + array.get(0).wins);}
					if (array.size() > 1){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(1).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "2. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(1).name)).getName() + ": " + ChatColor.WHITE + array.get(1).wins);}
					if (array.size() > 2){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(2).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "3. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(2).name)).getName() + ": " + ChatColor.WHITE + array.get(2).wins);}
					if (array.size() > 3){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(3).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "4. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(3).name)).getName() + ": " + ChatColor.WHITE +array.get(3).wins);}
					if (array.size() > 4){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(4).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "5. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(4).name)).getName() + ": " + ChatColor.WHITE + array.get(4).wins);}
					if (array.size() > 5){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(5).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "6. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(5).name)).getName() + ": " + ChatColor.WHITE + array.get(5).wins);}
					if (array.size() > 6){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(6).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "7. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(6).name)).getName() + ": " + ChatColor.WHITE + array.get(6).wins);}
					if (array.size() > 7){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(7).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "8. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(7).name)).getName() + ": " + ChatColor.WHITE + array.get(7).wins);}
					if (array.size() > 8){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(8).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "9. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(8).name)).getName() + ": " + ChatColor.WHITE + array.get(8).wins);}
					if (array.size() > 9){ if (Bukkit.getOfflinePlayer(UUID.fromString(array.get(9).name)).getName() != null) player.sendMessage(ChatColor.GRAY + "10. " + ChatColor.GOLD + Bukkit.getOfflinePlayer(UUID.fromString(array.get(9).name)).getName() + ": " + ChatColor.WHITE + array.get(9).wins);}
					player.sendMessage(ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "================================");
				}catch(Exception e){
					player.sendMessage(ChatColor.RED + "Error Loading Leaderboard");
				}
			}

			if (label.equalsIgnoreCase("leave")){
				for (int id = 1; getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
					Arena arena = ArenaManager.getArena(id);
					if (arena.hasPlayer(player.getName())){
						arena.removePlayer(player.getName());
						player.sendMessage(Methods.getDefaultPrefix() +  ChatColor.GRAY + "You left the arena.");
						arena.sendAll(Methods.getDefaultPrefix() + ChatColor.GOLD + player.getName() + ChatColor.GRAY + " has left.");
						return true;
					}
				}
				player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are not in an arena.");
			}
			if (label.equalsIgnoreCase("left")){
				for (int id = 1; getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
					Arena arena = ArenaManager.getArena(id);
					if (arena.hasPlayer(player.getName())){
						arena.getRemainingPlayers(player.getName());
						return true;
					}
				}
				player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are not in an arena.");
			}
			if (label.equalsIgnoreCase("credits")){
				player.sendMessage(Methods.getDefaultPrefix() + ChatColor.GRAY + "You have " + ChatColor.GOLD + Methods.getCredits(player) + ChatColor.GRAY + " credits.");
				this.clearChest();
			}
			if (label.equalsIgnoreCase("find")){
				for (int id = 1; getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
					Arena arena = ArenaManager.getArena(id);
					if (arena.hasPlayer(player.getName())){
						arena.find(player);
						return true;
					}
				}
				player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are not in an arena!");
			}
			/* unused
			if (label.equalsIgnoreCase("leaderboard")){
				Methods.printWinLeaderboard(player);
			}
			 */
			if (label.equalsIgnoreCase("stats") && args.length == 0){
				Methods.getStats(player);
			}
			if (label.equalsIgnoreCase("stats") && args.length == 1){
				OfflinePlayer player2 = Bukkit.getOfflinePlayer(args[0]);
				String s = EventListener.main.getConfig().getString(player2.getUniqueId() + ".wins");
				if (s != null){
					Methods.getStats(Bukkit.getOfflinePlayer(args[0]), player);
				}else{
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "This player does not exist.");
				}
			}
			if (label.equalsIgnoreCase("lobby")){
				boolean hasArena = false;
				for (int id = 1; this.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
					if (ArenaManager.getArena(id).players.contains(player.getUniqueId())){
						hasArena = true;
					}
				}
				if (hasArena == false){
					Methods.toSpawn(player);
				}else{
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You cannot be in an arena to do this! Use '/leave'");
				}
			}
			if (label.equalsIgnoreCase("join") && args.length == 1){
				boolean hasArena = false;
				for (int id = 1; this.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
					if (ArenaManager.getArena(id).players.contains(player.getUniqueId())){
						hasArena = true;
					}
				}
				if (hasArena == true){
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are in an arena already!");
					return true;
				}
				if (ArenaManager.getArena(Integer.valueOf(args[0])) == null){
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "This arena does not exist!");
					return true;
				}
				Arena arena = ArenaManager.getArena(Integer.valueOf(args[0]));
				if ((!(arena.getStatus() == ArenaStatus.INGAME)) && hasArena == false && arena.players.size() < arena.numPlayers){
					arena.addPlayer(player.getName());
				}else{
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Sorry, this arena is full or in game. Try another arena!");
				}
			}
			if (label.equalsIgnoreCase("join") && args.length != 1){
				player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Incorrect parameters. Usage: '/join <id>'");
			}
			if (label.equalsIgnoreCase("commands")){
				Methods.sendCommands(player);
			}
			if (label.equalsIgnoreCase("get")){
				boolean hasArena = false;
				for (int id = 1; this.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
					if (ArenaManager.getArena(id).players.contains(player.getUniqueId())){
						if (ArenaManager.getArena(id).getStatus() == ArenaStatus.INGAME) hasArena = true;
					}
				}
				if (hasArena == false){
					player.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You must be in game to use this command!");
					return true;
				}
				else{
					player.openInventory(Methods.getGetInventory(player));
				}
			}
		}
		if (sender instanceof ConsoleCommandSender){
			if (label.equalsIgnoreCase("setcredits") && args.length == 2){
				this.getConfig().set(Bukkit.getPlayer(args[0]).getUniqueId() + ".credits", Integer.valueOf(args[1]));
				this.saveConfig();
			}
			if (label.equalsIgnoreCase("addcredits") && args.length == 2){
				this.getConfig().set(Bukkit.getPlayer(args[0]).getUniqueId() + ".credits", Integer.valueOf(args[1]) + this.getConfig().getInt(Bukkit.getPlayer(args[0]).getUniqueId() + ".credits"));
				this.saveConfig();
			}
		}
		return true;

	}

	public void setUpArenas(){

		ArrayList<String> array = new ArrayList<String>(Arrays.asList(
				"doDaylightCycle",
				"doFireTick",
				"doMobSpawning",
				"doTileDrops",
				"keepInventory",
				"mobgriefing",
				"showDeathMessages"
				));
		for (World world: Bukkit.getWorlds()){
			for (String s: array){
				world.setGameRuleValue(s, "false");
			}
			world.setTime(6000);
			world.setStorm(false);
			world.setMonsterSpawnLimit(0);

			List<Entity> list = world.getEntities();
			for (Entity e: list){
				if (e instanceof Item){
					e.remove();
				}
				if (e instanceof Arrow){
					e.remove();
				}
				if (e instanceof ExperienceOrb){
					e.remove();
				}
			}
		}

		new BukkitRunnable(){
			@Override
			public void run() {
				/*
				for (int id = 1; getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
					Arena arena = ArenaManager.getArena(id);
					if (arena.dm < 0 && arena.getStatus() == ArenaStatus.INGAME){
						Location center = Methods.getCenter(arena.getId());
						for (UUID uuid: arena.players){
							Player pl = Bukkit.getPlayer(uuid);
							if (pl.getLocation().distance(center) > 35){
								if (pl.getHealth() > 1){
									pl.setHealth(pl.getHealth() - 1);
									pl.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "STAY WITHIN 35 BLOCKS!!!");
									pl.playSound(pl.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 10, 1);
								}
								else{
									Methods.addDeaths(1, pl);
									arena.sendAll(Methods.getDefaultPrefix() + ChatColor.GOLD + pl.getName() + ChatColor.GRAY + " killed by " + ChatColor.GREEN + "BORDER");
									arena.removePlayer(pl.getName());
								}
							}
						}
					}
				}
				*/
			}
		}.runTaskTimer(this, 0L, 10L);


		for (int id = 1; this.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
			Methods.sendDebugMessage("Setting up arena " + id);
			String s = Integer.toString(id);
			Arena arena = new Arena(new ArrayList<UUID>(), ArenaStatus.WAITING, id, 600, this.getConfig().getString("~Arenas." + s + ".name"));
			ArenaManager.arenaList.add(arena);
			ArenaManager.idToArena.put(id, arena);
			arena.clearSigns();
		}

		new BukkitRunnable(){
			@Override
			public void run() {
				for (int id = 1; getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
					Arena arena = ArenaManager.getArena(id);
					if (arena.getStatus() != ArenaStatus.INGAME){
						for (UUID uuid: arena.players){
							arena.semiTeleport(Bukkit.getPlayer(uuid));
						}
					}
				}
			}
		}.runTaskTimer(this, 0L, 3L);

	}

	public void updateSignLoop(){
		new BukkitRunnable(){
			@Override
			public void run() {
				for (int id = 1; getConfig().getString("~Signs." + String.valueOf(id)) != null; id++){
					String s = Integer.toString(id);
					String world = getConfig().getString("~Signs." + s + ".world");
					double x = getConfig().getDouble("~Signs." + s + ".x");
					double y = getConfig().getDouble("~Signs." + s + ".y");
					double z = getConfig().getDouble("~Signs." + s + ".z");
					Sign sign = (Sign) Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),x,y,z)).getState();

					sign.setLine(0, ChatColor.BLUE.toString() + "[SW-" + s + "]");
					ArenaStatus status = ArenaManager.getArena(id).getStatus();

					if (status == ArenaStatus.WAITING || status == ArenaStatus.STARTING){
						sign.setLine(1,ChatColor.GREEN.toString() + ChatColor.ITALIC + status.toString());
					}else if(status == ArenaStatus.INGAME){
						sign.setLine(1, ChatColor.DARK_RED.toString() + ChatColor.ITALIC + status.toString());
					}
					Arena arena = ArenaManager.getArena(id);
					sign.setLine(2, ArenaManager.getArena(id).getPlayerLength()+"/" + arena.numPlayers + " Players");
					sign.setLine(3, "Map: " + ArenaManager.getArena(id).getName());
					sign.update();
				}
			}
		}.runTaskTimer(this, 1L, 5L);
	}

	//unused
	public void clearChest(){
		for (int id = 1; this.getConfig().getString("~Arenas." + String.valueOf(id) + ".name") != null; id++){
			//Arena arena = ArenaManager.getArena(id);
			//arena.clearChests(id);
		}
	}

	public void setUpTips(){
		new BukkitRunnable(){
			@Override
			public void run() {
				sendTips();
			}		
		}.runTaskTimer(this, 10 * 20, 90 * 20);
	}

	public void sendTips(){
		Collection<? extends Player> playerList = Bukkit.getOnlinePlayers();
		HashMap<String, Boolean> inArena = new HashMap<String, Boolean>();

		for (Player p: playerList){
			inArena.put(p.getName(), false);
			for (int id = 1; getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
				Arena arena = ArenaManager.getArena(id);
				if (arena.players.contains(p.getUniqueId())){
					inArena.put(p.getName(), true);
				}
			}
		}
		/*
		String s = "";
		boolean change = true;
		if (tipCounter == 1) s = "You lose your speed at the beginning of the game if you attack!";
		if (tipCounter == 2) s = "You get 10 credits per win and 2 credits per kill.";
		if (tipCounter == 3) s = "Spend your credits on new kits!";
		if (tipCounter == 4) s = "The number in front of your name is your total wins.";
		if (tipCounter == 5){
			s = "Please be respectful! Breaking rules leads to punishments!";
			tipCounter = 1;
			change = false;
		}
		if (change) tipCounter++;
		for (Player p: playerList){
			if (inArena.get(p.getName()) == false) p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + s);
		}
		*/
	}

	public void updatePE(){
		new BukkitRunnable(){
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for (String s: PEManager.portal){
					ParticleEffect.PORTAL.display( 0, 0, 0, 1f, 40, Bukkit.getPlayer(s).getLocation().add(0, 1, 0), Methods.getSameWorldPlayers(Bukkit.getPlayer(s)));
				}
				for (String s: PEManager.flame){
					ParticleEffect.FLAME.display( 0, 0, 0, 0.05f, 15, Bukkit.getPlayer(s).getLocation().add(0, 1, 0), Methods.getSameWorldPlayers(Bukkit.getPlayer(s)));
				}	
				for (String s: PEManager.enchant){
					ParticleEffect.ENCHANTMENT_TABLE.display( 0, 0, 0, 1f, 40, Bukkit.getPlayer(s).getLocation().add(0, 1, 0), Methods.getSameWorldPlayers(Bukkit.getPlayer(s)));
				}	
				for (String s: PEManager.smoke){
					ParticleEffect.SMOKE_NORMAL.display(0, 0, 0, 0.15f, 30, Bukkit.getPlayer(s).getLocation().add(0, 0, 0), Methods.getSameWorldPlayers(Bukkit.getPlayer(s)));
				}
				for (String s: PEManager.crit){
					ParticleEffect.CRIT.display(0, 0, 0, .5f, 20, Bukkit.getPlayer(s).getLocation().add(0, 1, 0), Methods.getSameWorldPlayers(Bukkit.getPlayer(s)));
				}
				for (String s: PEManager.redstone){
					ParticleEffect.REDSTONE.display(0, 0, 0, 5f, 20, Bukkit.getPlayer(s).getLocation().add(.5, 1, 0), Methods.getSameWorldPlayers(Bukkit.getPlayer(s)));
					ParticleEffect.REDSTONE.display(0, 0, 0, 5f, 20, Bukkit.getPlayer(s).getLocation().add(-.5, 1, 0), Methods.getSameWorldPlayers(Bukkit.getPlayer(s)));
					ParticleEffect.REDSTONE.display(0, 0, 0, 5f, 20, Bukkit.getPlayer(s).getLocation().add(0, 1, .5), Methods.getSameWorldPlayers(Bukkit.getPlayer(s)));
					ParticleEffect.REDSTONE.display(0, 0, 0, 5f, 20, Bukkit.getPlayer(s).getLocation().add(0, 1, -.5), Methods.getSameWorldPlayers(Bukkit.getPlayer(s)));

					ParticleEffect.REDSTONE.display(0, 0, 0, 5f, 20, Bukkit.getPlayer(s).getLocation().add(.3, 1, .3), Methods.getSameWorldPlayers(Bukkit.getPlayer(s)));
					ParticleEffect.REDSTONE.display(0, 0, 0, 5f, 20, Bukkit.getPlayer(s).getLocation().add(.3, 1, -.3), Methods.getSameWorldPlayers(Bukkit.getPlayer(s)));
					ParticleEffect.REDSTONE.display(0, 0, 0, 5f, 20, Bukkit.getPlayer(s).getLocation().add(-.3, 1, .3), Methods.getSameWorldPlayers(Bukkit.getPlayer(s)));
					ParticleEffect.REDSTONE.display(0, 0, 0, 5f, 20, Bukkit.getPlayer(s).getLocation().add(-.3, 1, -.3), Methods.getSameWorldPlayers(Bukkit.getPlayer(s)));
				}
			}
		}.runTaskTimer(this, 0L, 5L);
	}

}

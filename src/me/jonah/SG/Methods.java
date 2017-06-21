package me.jonah.SG;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Methods {

	public static String getDefaultPrefix(){
		return ChatColor.GRAY.toString() + ChatColor.GRAY + "[" + ChatColor.AQUA + "SW" + ChatColor.GRAY + "] " + ChatColor.RESET;

	}

	public static boolean inPvP(Player damagee){
		if (damagee.getWorld().getName().equals("world") && damagee.getWorld().getName().equals("world")){
			if (damagee.getLocation().getBlockX() >= 203 && damagee.getLocation().getBlockX() <= 219){
				if (damagee.getLocation().getBlockZ() >= -5 && damagee.getLocation().getBlockZ() <= 5){
					return true;
				}
			}
		}
		return false;
	}

	public static void tLive(Player p){
		String uuid = p.getUniqueId().toString();
		if(EventListener.main.getConfig().getBoolean("~T.live")){
			EventListener.main.getConfig().set("~T." + uuid, EventListener.main.getConfig().getInt("~T." + uuid) + 1);
			EventListener.main.saveConfig();
		}
	}

	public static boolean isTLive(){
		return EventListener.main.getConfig().getBoolean("~T.live");
	}
	
	public static boolean dxpLive(){
		return EventListener.main.getConfig().getBoolean("~DXP");
	}

	//credits
	@SuppressWarnings("deprecation")
	public static void addCredits(int i, Player p){
		String s = "";
		boolean b = EventListener.main.getConfig().getBoolean("~DXP");
		if (!p.isOp()){
			if (b){
				i = i * 2;
				s = ChatColor.GOLD + "[x2 Credits]";
				if (p.hasPermission("sg.vip")){
					i = i * 2;
					s = ChatColor.GOLD + "[x4 Credits]";
				}
				else if (p.hasPermission("sg.mvp")){
					i = i * 3;
					s = ChatColor.GOLD + "[x6 Credits]";
				}
			}else{
				if (p.hasPermission("sg.vip")){
					i = i * 2;
					s = ChatColor.GOLD + "[x2 Credits]";
				}
				else if (p.hasPermission("sg.mvp")){
					i = i * 3;
					s = ChatColor.GOLD + "[x3 Credits]";
				}
			}
		}
		EventListener.main.getConfig().set(Bukkit.getOfflinePlayer(p.getName()).getUniqueId() + ".credits", i + EventListener.main.getConfig().getInt(Bukkit.getOfflinePlayer(p.getName()).getUniqueId() + ".credits"));
		EventListener.main.saveConfig();

		p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GOLD + i + ChatColor.GRAY + " credits were added to your account. " + s);
	}

	@SuppressWarnings("deprecation")
	public static void removeCredits(int i, Player p){
		EventListener.main.getConfig().set(Bukkit.getOfflinePlayer(p.getName()).getUniqueId() + ".credits", EventListener.main.getConfig().getInt(Bukkit.getOfflinePlayer(p.getName()).getUniqueId() + ".credits") - i);
		EventListener.main.saveConfig();
		p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GOLD + i + ChatColor.GRAY + " credits were removed from your account.");
	}
	@SuppressWarnings("deprecation")
	public static String getCredits(Player p){
		return EventListener.main.getConfig().getString(Bukkit.getOfflinePlayer(p.getName()).getUniqueId() + ".credits");
	}

	//wins
	@SuppressWarnings("deprecation")
	public static void addWins(int i, Player p){
		EventListener.main.getConfig().set(Bukkit.getOfflinePlayer(p.getName()).getUniqueId() + ".wins", i + EventListener.main.getConfig().getInt(Bukkit.getOfflinePlayer(p.getName()).getUniqueId() + ".wins"));
		EventListener.main.saveConfig();
	}

	@SuppressWarnings("deprecation")
	public static void removeWins(int i, Player p){
		EventListener.main.getConfig().set(Bukkit.getOfflinePlayer(p.getName()).getUniqueId() + ".wins", EventListener.main.getConfig().getInt(Bukkit.getOfflinePlayer(p.getName()).getUniqueId() + ".wins") - i);
		EventListener.main.saveConfig();
	}
	@SuppressWarnings("deprecation")
	public static String getWins(Player p){
		return EventListener.main.getConfig().getString(Bukkit.getOfflinePlayer(p.getName()).getUniqueId() + ".wins");
	}

	//kills
	public static void addKills(int i, Player p){
		EventListener.main.getConfig().set( p.getUniqueId() + ".kills", i + EventListener.main.getConfig().getInt( p.getUniqueId() + ".kills"));
		EventListener.main.saveConfig();
	}

	public static void removeKills(int i, Player p){
		EventListener.main.getConfig().set( p.getUniqueId() + ".kills", EventListener.main.getConfig().getInt( p.getUniqueId() + ".kills") - i);
		EventListener.main.saveConfig();
	}
	public static String getKills(Player p){
		return EventListener.main.getConfig().getString( p.getUniqueId() + ".kills");
	}

	//Deaths
	public static void addDeaths(int i, Player p){
		EventListener.main.getConfig().set( p.getUniqueId() + ".deaths", i + EventListener.main.getConfig().getInt( p.getUniqueId() + ".deaths"));
		EventListener.main.saveConfig();
	}

	public static void removeDeaths(int i, Player p){
		EventListener.main.getConfig().set( p.getUniqueId() + ".deaths", EventListener.main.getConfig().getInt( p.getUniqueId() + ".deaths") - i);
		EventListener.main.saveConfig();
	}
	public static String getDeaths(Player p){
		return EventListener.main.getConfig().getString( p.getUniqueId() + ".deaths");
	}

	@SuppressWarnings("deprecation")
	public static void fullHeal(final Player player){
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setFireTicks(0);
		player.setMaxHealth(20);
		for (PotionEffect pe: player.getActivePotionEffects()){
			player.removePotionEffect(pe.getType());
		}
		new BukkitRunnable(){
			@Override
			public void run() {
				player.setFireTicks(0);
			}
		}.runTaskLater(EventListener.main, 20L);
	}

	public static Location getCenter(int i){
		World world = Bukkit.getWorld(EventListener.main.getConfig().getString("~Arenas." + i + ".center.world"));
		int x = EventListener.main.getConfig().getInt("~Arenas." + i + ".center.x");
		int y = EventListener.main.getConfig().getInt("~Arenas." + i + ".center.y");
		int z = EventListener.main.getConfig().getInt("~Arenas." + i + ".center.z");
		return new Location(world,x,y,z);
	}

	public static void toSpawn(Player player){
		String world = EventListener.main.getConfig().getString("~Spawn.world");
		double x =EventListener.main.getConfig().getDouble("~Spawn.x");
		double y =EventListener.main.getConfig().getDouble("~Spawn.y");
		double z = EventListener.main.getConfig().getDouble("~Spawn.z");
		double yaw = EventListener.main.getConfig().getDouble("~Spawn.yaw");
		double pitch = EventListener.main.getConfig().getDouble("~Spawn.pitch");
		Location loc = new Location(Bukkit.getWorld(world),x,y,z,(float)yaw,(float)pitch);
		player.teleport(loc);
		player.setVelocity(new Vector(0,0,0));
		player.setLevel(0);
		Methods.fullHeal(player);
		Methods.setUpInventory(player);
	}

	public static ArrayList<Player> getSameWorldPlayers(Player pl){
		ArrayList<Player> array = new ArrayList<Player>();
		for (Player p: Bukkit.getOnlinePlayers()){
			if (pl.getWorld().getName().equals(p.getWorld().getName())){
				array.add(p);
			}
		}
		return array;
	}

	/* unused
	@SuppressWarnings("deprecation")
	public static void printWinLeaderboard(Player player){
		Map<String, Leaderboard> map = new HashMap<String, Leaderboard>();

		for (String s: EventListener.main.getConfig().getKeys(true)){
			if (!s.startsWith("~")){
				int c = s.indexOf("-");
				s = s.substring(0,c);
				String name = Bukkit.getOfflinePlayer(s).getName();
				String uuid = Bukkit.getOfflinePlayer(s).getUniqueId().toString();
				int wins = EventListener.main.getConfig().getInt(name + "-" + uuid + ".wins");
				if (!map.containsKey(name)){
					map.put(name, new Leaderboard(name,wins));
				}
			}
		}

		// not yet sorted
		List<Leaderboard> peopleByAge = new ArrayList<Leaderboard>(map.values());

		Collections.sort(peopleByAge, new Comparator<Leaderboard>() {

			public int compare(Leaderboard o1, Leaderboard o2) {
				return o2.getWins() - o1.getWins();
			}
		});

		player.sendMessage(ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "================================");
		player.sendMessage(ChatColor.RED + "Leaderboard: Total Wins");

		int i = 1;
		for (Leaderboard l : peopleByAge) {
			player.sendMessage(ChatColor.GRAY.toString() + i + ". " + l.getName() + " " + ChatColor.GOLD + l.getWins());
			if (i == 10) break;
			i++;
		}
		player.sendMessage(ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "================================");	

	}

	 */

	public static void getStats(Player player){
		DecimalFormat df = new DecimalFormat("#.##");

		double wins = Double.valueOf(EventListener.main.getConfig().getString(player.getUniqueId() + ".wins"));
		double deaths = Double.valueOf(EventListener.main.getConfig().getString(player.getUniqueId() + ".deaths"));
		double kills = Double.valueOf(EventListener.main.getConfig().getString(player.getUniqueId() + ".kills"));

		double wlr_;
		double kdr_;
		if (deaths != 0){
			wlr_ = wins/deaths;
			kdr_ = kills/deaths;
		}else{
			deaths = 1;
			wlr_ = wins/deaths;
			kdr_ = kills/deaths;
			deaths = 0;
		}
		String wlr = df.format(wlr_);
		String kdr = df.format(kdr_);

		player.sendMessage(ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "================================");
		player.sendMessage(ChatColor.RED + "Stats: " + ChatColor.GOLD + player.getName());
		player.sendMessage(ChatColor.GRAY + "Wins: " + ChatColor.GOLD + (int)wins);
		player.sendMessage(ChatColor.GRAY + "Deaths: " + ChatColor.GOLD + (int)deaths);
		player.sendMessage(ChatColor.GRAY + "Kills: " + ChatColor.GOLD + (int)kills);
		player.sendMessage(ChatColor.GRAY + "WLR: " + ChatColor.GOLD + wlr);
		player.sendMessage(ChatColor.GRAY + "KDR: " + ChatColor.GOLD + kdr);
		player.sendMessage(ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "================================");
	}

	public static void getStats(OfflinePlayer player, Player target){
		DecimalFormat df = new DecimalFormat("#.##");

		double wins = Double.valueOf(EventListener.main.getConfig().getString(player.getUniqueId() + ".wins"));
		double deaths = Double.valueOf(EventListener.main.getConfig().getString(player.getUniqueId() + ".deaths"));
		double kills = Double.valueOf(EventListener.main.getConfig().getString(player.getUniqueId() + ".kills"));

		double wlr_;
		double kdr_;
		if (deaths != 0){
			wlr_ = wins/deaths;
			kdr_ = kills/deaths;
		}else{
			deaths = 1;
			wlr_ = wins/deaths;
			kdr_ = kills/deaths;
			deaths = 0;
		}
		String wlr = df.format(wlr_);
		String kdr = df.format(kdr_);

		target.sendMessage(ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "================================");
		target.sendMessage(ChatColor.RED + "Stats: " + ChatColor.GOLD + player.getName());
		target.sendMessage(ChatColor.GRAY + "Wins: " + ChatColor.GOLD + (int)wins);
		target.sendMessage(ChatColor.GRAY + "Deaths: " + ChatColor.GOLD + (int)deaths);
		target.sendMessage(ChatColor.GRAY + "Kills: " + ChatColor.GOLD + (int)kills);
		target.sendMessage(ChatColor.GRAY + "WLR: " + ChatColor.GOLD + wlr);
		target.sendMessage(ChatColor.GRAY + "KDR: " + ChatColor.GOLD + kdr);
		target.sendMessage(ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "================================");
	}

	public static void sendAll(String s){
		for (Player p: Bukkit.getOnlinePlayers()){
			p.sendMessage(s);
		}
	}

	public static void sendDebugMessage(String s){
		Bukkit.broadcastMessage(ChatColor.DARK_RED + "DEBUG: " + ChatColor.RED + s);
	}

	public static Inventory getKit1(Player player){
		Inventory i = Bukkit.createInventory(null, 9, ChatColor.AQUA + "Rancher Kit - 100 Coins");
		i.setItem(3, getMaterial(new ItemStack(Material.EMERALD,1),ChatColor.GREEN + "Purchase", new ArrayList<String>()));
		i.setItem(5, getMaterial(new ItemStack(Material.REDSTONE,1),ChatColor.RED + "Cancel", new ArrayList<String>()));
		return i;
	}

	public static Inventory getKit2(Player player){
		Inventory i = Bukkit.createInventory(null, 9, ChatColor.AQUA + "Agile Kit - 200 Coins");
		i.setItem(3, getMaterial(new ItemStack(Material.EMERALD,1),ChatColor.GREEN + "Purchase", new ArrayList<String>()));
		i.setItem(5, getMaterial(new ItemStack(Material.REDSTONE,1),ChatColor.RED + "Cancel", new ArrayList<String>()));
		return i;
	}

	public static Inventory getKit3(Player player){
		Inventory i = Bukkit.createInventory(null, 9, ChatColor.AQUA + "Rugged Kit - 300 Coins");
		i.setItem(3, getMaterial(new ItemStack(Material.EMERALD,1),ChatColor.GREEN + "Purchase", new ArrayList<String>()));
		i.setItem(5, getMaterial(new ItemStack(Material.REDSTONE,1),ChatColor.RED + "Cancel", new ArrayList<String>()));
		return i;
	}

	public static Inventory getKit4(Player player){
		Inventory i = Bukkit.createInventory(null, 9, ChatColor.AQUA + "Racer Kit - 500 Coins");
		i.setItem(3, getMaterial(new ItemStack(Material.EMERALD,1),ChatColor.GREEN + "Purchase", new ArrayList<String>()));
		i.setItem(5, getMaterial(new ItemStack(Material.REDSTONE,1),ChatColor.RED + "Cancel", new ArrayList<String>()));
		return i;
	}

	public static Inventory getKit5(Player player){
		Inventory i = Bukkit.createInventory(null, 9, ChatColor.AQUA + "Ranger Kit - 1000 Coins");
		i.setItem(3, getMaterial(new ItemStack(Material.EMERALD,1),ChatColor.GREEN + "Purchase", new ArrayList<String>()));
		i.setItem(5, getMaterial(new ItemStack(Material.REDSTONE,1),ChatColor.RED + "Cancel", new ArrayList<String>()));
		return i;
	}

	@SuppressWarnings("deprecation")
	public static void setUpInventory(Player player){
		Inventory i = player.getInventory();
		i.clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		i.setItem(1, getMaterial(new ItemStack(Material.PAPER,1),ChatColor.RED + "Stats",new ArrayList<String>()));
		i.setItem(2, getMaterial(new ItemStack(Material.FEATHER,1),ChatColor.AQUA + "Kit Selector", new ArrayList<String>()));
		i.setItem(3, getMaterial(new ItemStack(Material.NETHER_STAR,1), ChatColor.GREEN + "Trails", new ArrayList<String>()));
		i.setItem(4, getMaterial(new ItemStack(Material.NAME_TAG,1), ChatColor.GOLD + "Voting Links", new ArrayList<String>()));
		player.updateInventory();	
	}

	public static boolean hasKit(int i, Player p){
		if (i == 1){
			if (Methods.isVIP(p)) return true;
			else return EventListener.main.getConfig().getBoolean( p.getUniqueId() + ".kit1");
		}
		if (i == 2){
			if (Methods.isVIP(p)) return true;
			else return EventListener.main.getConfig().getBoolean( p.getUniqueId() + ".kit2");
		}
		if (i == 3){
			if (Methods.isVIP(p)) return true;
			else return EventListener.main.getConfig().getBoolean( p.getUniqueId() + ".kit3");
		}
		if (i == 4){
			if (Methods.isVIP(p)) return true;
			else return EventListener.main.getConfig().getBoolean( p.getUniqueId() + ".kit4");
		}
		if (i == 5){
			if (Methods.isVIP(p)) return true;
			else return EventListener.main.getConfig().getBoolean( p.getUniqueId() + ".kit5");
		}

		return false;
	}

	public static boolean isVIP(Player p){
		if (p.hasPermission("sg.vip") || p.hasPermission("sg.mvp")) return true;
		else return false;
	}

	public static boolean isMVP(Player p){
		if (p.hasPermission("sg.mvp")) return true;
		else return false;
	}

	public static void sendCommands(Player p){
		p.sendMessage(ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "================================");
		p.sendMessage(ChatColor.RED + "Commands: " + ChatColor.GOLD + "Skywars");
		p.sendMessage(ChatColor.GOLD + "  /join #: " + ChatColor.WHITE + "Join a game");
		p.sendMessage(ChatColor.GOLD + "  /leave: " + ChatColor.WHITE + "Leave a game");
		p.sendMessage(ChatColor.GOLD + "  /lobby: " + ChatColor.WHITE + "Goto lobby");
		p.sendMessage(ChatColor.GOLD + "  /credits: " + ChatColor.WHITE + "Get your total credits");
		p.sendMessage(ChatColor.GOLD + "  /stats: " + ChatColor.WHITE + "Get your stats");
		p.sendMessage(ChatColor.GOLD + "  /stats <player>: " + ChatColor.WHITE + "Get a player's stats");
		p.sendMessage(ChatColor.GOLD + "  /get: " + ChatColor.WHITE + "Buy items in game");
		p.sendMessage(ChatColor.GOLD + "  /left: " + ChatColor.WHITE + "Get remaining players in an arena");
		p.sendMessage(ChatColor.GOLD + "  /leaderboard: " + ChatColor.WHITE + "Global Wins Leaderboard");
		p.sendMessage(ChatColor.GOLD + "  /tleaderboard: " + ChatColor.WHITE + "Tournament Wins Leaderboard");
		p.sendMessage(ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "================================");
	}

	public static Inventory getKitSelector(Player p){

		boolean kit1 = EventListener.main.getConfig().getBoolean( p.getUniqueId() + ".kit1");
		boolean kit2 = EventListener.main.getConfig().getBoolean( p.getUniqueId() + ".kit2");
		boolean kit3 = EventListener.main.getConfig().getBoolean( p.getUniqueId() + ".kit3");
		boolean kit4 = EventListener.main.getConfig().getBoolean( p.getUniqueId() + ".kit4");
		boolean kit5 = EventListener.main.getConfig().getBoolean( p.getUniqueId() + ".kit5");
		boolean vip = Methods.isVIP(p);
		boolean mvp = Methods.isMVP(p);

		Inventory i = Bukkit.createInventory(null, 54, ChatColor.AQUA + "Kit Selector");
		Material m = kitToItem(ArenaManager.getKit(p).toString().toLowerCase());
		String kit = EventListener.main.getConfig().getString( p.getUniqueId() + ".kit");

		i.setItem(4, new ItemStack(getMaterial(new ItemStack(m,1),ChatColor.BLUE + "Kit: " + ChatColor.WHITE + kit, new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Buy and select kits here!", "", ChatColor.GRAY + "Once selected your kit will be stored", ChatColor.GRAY +  "and automatically be used in every game!")))));

		@SuppressWarnings("unused")
		String k1 = "";
		@SuppressWarnings("unused")
		String k2 = "";
		String k3 = "";
		@SuppressWarnings("unused")
		String k4 = "";
		String k5 = "";
		String vip1 = "";
		String mvp1 = "";
		if (kit1 == true || vip) k1 = ChatColor.GOLD + "Purchased";
		else k1 = ChatColor.GOLD + "100 Coins";		
		if (kit2 == true || vip) k2 = ChatColor.GOLD + "Purchased";
		else k2 = ChatColor.GOLD + "200 Coins";		
		if (kit3 == true || vip) k3 = ChatColor.GOLD + "Purchased";
		else k3 = ChatColor.GOLD + "300 Coins";		
		if (kit4 == true || vip) k4 = ChatColor.GOLD + "Purchased";
		else k4 = ChatColor.GOLD + "500 Coins";	
		if (kit5 == true || vip) k5 = ChatColor.GOLD + "Purchased";
		else k5 = ChatColor.GOLD + "1000 Coins";
		if (vip == true) vip1 = ChatColor.GOLD + "Purchased";
		else vip1 = ChatColor.GOLD + "Purchase @ http://oasismcranks.buycraft.net";
		if (mvp == true) mvp1 = ChatColor.GOLD + "Purchased";
		else mvp1 = ChatColor.GOLD + "Purchase @ http://oasismcranks.buycraft.net";


		i.setItem(19, new ItemStack(getMaterial(new ItemStack(Material.APPLE,1),ChatColor.BLUE + "APPLER", new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Start the game off with 5 apples.", ChatColor.DARK_GRAY + "FREE!")))));
		i.setItem(20, new ItemStack(getMaterial(new ItemStack(Material.HAY_BLOCK,1), ChatColor.BLUE + "BOXER", new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Your fist does 1.5x damage ", ChatColor.GRAY + "to all opponents.", ChatColor.DARK_GRAY + "FREE!")))));		

		//i.setItem(21, new ItemStack(getMaterial(new ItemStack(Material.COOKED_CHICKEN), ChatColor.BLUE + "RANCHER", new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Start the game off with", ChatColor.GRAY + "5 cooked chicken.", k1)))));
		//i.setItem(22, new ItemStack(getMaterial(new ItemStack(Material.SUGAR,1),ChatColor.BLUE + "AGILE",new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "You take 2 less damage",ChatColor.GRAY + "from fall damage.", k2)))));
		i.setItem(21, new ItemStack(getMaterial(new ItemStack(Material.IRON_CHESTPLATE,1),ChatColor.BLUE + "RUGGED",new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "You take 0.5 less damage", ChatColor.GRAY + "from all enemy attacks.", k3)))));
		//i.setItem(24, new ItemStack(getMaterial(new ItemStack(Material.QUARTZ,1),ChatColor.BLUE + "RACER",new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "You get a bonus 15 seconds", ChatColor.GRAY + "of starting speed III.", k4)))));
		i.setItem(22, new ItemStack(getMaterial(new ItemStack(Material.BOW,1),ChatColor.BLUE + "RANGER",new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "You start with a wood sword,", ChatColor.GRAY + "bow, and 10 arrows.", k5)))));		

		i.setItem(23, new ItemStack(getMaterial(new ItemStack(Material.COOKED_BEEF,1), ChatColor.BLUE + "CANNIBAL",new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Every attack heals 1 ", ChatColor.GRAY + "hunger point.", ChatColor.GREEN + "Unlocked with VIP", vip1)))));		
		i.setItem(24, new ItemStack(getMaterial(new ItemStack(Material.ENDER_PEARL,1), ChatColor.BLUE + "ESCAPIST",new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Start the game off with 5", ChatColor.GRAY + "ender pearls.", ChatColor.GREEN + "Unlocked with VIP", vip1)))));	
		i.setItem(25, new ItemStack(getMaterial(new ItemStack(Material.COMPASS,1), ChatColor.BLUE + "TRACKER",new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Get the pre-damage health of your", ChatColor.GRAY + "opponent every time you hit them.", ChatColor.GREEN + "Unlocked with VIP", vip1)))));
		i.setItem(28, new ItemStack(getMaterial(new ItemStack(Material.EXP_BOTTLE,1), ChatColor.BLUE + "ENCHANTER",new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "You get an additional xp ", ChatColor.GRAY + "level when you kill an enemy.", ChatColor.GREEN + "Unlocked with VIP", vip1)))));	

		i.setItem(29, new ItemStack(getMaterial(new ItemStack(Material.GOLD_NUGGET,1), ChatColor.BLUE + "ANGEL",new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Every attack heals 1 ", ChatColor.GRAY + "health point.", ChatColor.BLUE + "Unlocked with MVP", mvp1)))));
		i.setItem(30, new ItemStack(getMaterial(new ItemStack(Material.ANVIL,1), ChatColor.BLUE + "BRUTE",new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "You have 3 extra ", ChatColor.GRAY + "hearts of health.", ChatColor.BLUE + "Unlocked with MVP", mvp1)))));
		i.setItem(31, new ItemStack(getMaterial(new ItemStack(Material.ARROW,1), ChatColor.BLUE + "SHARPSHOOTER",new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "You shoot an additional ", ChatColor.GRAY + "2 arrows without cost.", ChatColor.BLUE + "Unlocked with MVP", mvp1)))));
		i.setItem(32, new ItemStack(getMaterial(new ItemStack(Material.IRON_AXE,1), ChatColor.BLUE + "SAVAGE",new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "You do 0.5 more damage", ChatColor.GRAY + "with all attacks.", ChatColor.BLUE + "Unlocked with MVP", mvp1)))));

		return i;
	}

	public static ArrayList<String> getPlayersFromConfig(){
		ArrayList<String> list = new ArrayList<String>();		
		for (String s: EventListener.main.getConfig().getKeys(true)){
			if (!s.startsWith("~")){
				if (!s.contains(".")){
					if (!list.contains(s)) list.add(s);		
				}
			}
		}
		return list;
	}

	public static void resetParkour(){
		for (String s: Methods.getPlayersFromConfig()){
			EventListener.main.getConfig().set(s + ".pk1", 0);
			EventListener.main.getConfig().set(s + ".pk2", 0);
			EventListener.main.getConfig().set(s + ".pk3", 0);
			EventListener.main.getConfig().set(s + ".pk4", 0);
			EventListener.main.saveConfig();
		}
	}

	public static Inventory getTrailSelector(Player p){
		boolean vip = Methods.isVIP(p);
		boolean mvp = Methods.isMVP(p);

		String vip1 = "";
		String mvp1 = "";

		if (vip == true) vip1 = ChatColor.GOLD + "Purchased";
		else vip1 = ChatColor.GOLD + "Purchase @ http://oasismcranks.buycraft.net";
		if (mvp == true) mvp1 = ChatColor.GOLD + "Purchased";
		else mvp1 = ChatColor.GOLD + "Purchase @ http://oasismcranks.buycraft.net";

		Inventory i = Bukkit.createInventory(null, 36, ChatColor.GREEN + "Trails");
		i.setItem(4, new ItemStack(getMaterial(new ItemStack(Material.NETHER_STAR,1),ChatColor.GREEN + "Trail: " + ChatColor.WHITE + PEManager.getKit(p.getName()), new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Here you can summon", ChatColor.GRAY + "trails that follow you!")))));
		i.setItem(19, new ItemStack(getMaterial(new ItemStack(Material.CLAY_BALL),ChatColor.BLUE + "Clear Current Trail", new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Click me to clear", ChatColor.GRAY + "your current trail.")))));
		i.setItem(20, new ItemStack(getMaterial(new ItemStack(Material.ENCHANTED_BOOK),ChatColor.BLUE + "Enchant Trail", new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "You are gifted with", ChatColor.GRAY + "an enchanted glow.", ChatColor.GREEN + "Unlocked with VIP", vip1)))));
		i.setItem(21, new ItemStack(getMaterial(new ItemStack(Material.FLINT_AND_STEEL),ChatColor.BLUE + "Smoke Trail", new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "You are gifted with", ChatColor.GRAY + "a smoked glow.", ChatColor.GREEN + "Unlocked with VIP", vip1)))));
		i.setItem(23, new ItemStack(getMaterial(new ItemStack(Material.ENDER_PORTAL_FRAME),ChatColor.BLUE + "Ender Trail", new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "You are gifted with", ChatColor.GRAY + "an endered glow.", ChatColor.BLUE + "Unlocked with MVP", mvp1)))));
		i.setItem(24, new ItemStack(getMaterial(new ItemStack(Material.BLAZE_POWDER),ChatColor.BLUE + "Flame Trail", new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "You are gifted with", ChatColor.GRAY + "a flamed glow.", ChatColor.BLUE + "Unlocked with MVP", mvp1)))));
		i.setItem(22, new ItemStack(getMaterial(new ItemStack(Material.STRING),ChatColor.BLUE + "Crit Trail", new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "You are gifted with", ChatColor.GRAY + "a critical glow.", ChatColor.GREEN + "Unlocked with VIP", vip1)))));
		i.setItem(25, new ItemStack(getMaterial(new ItemStack(Material.REDSTONE),ChatColor.BLUE + "Redstone Trail", new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "You are gifted with", ChatColor.GRAY + "a redstone glow.", ChatColor.BLUE + "Unlocked with MVP", mvp1)))));
		return i;
	}

	public static Inventory getGetInventory(Player p){
		Inventory i = Bukkit.createInventory(null, 18, ChatColor.BLUE + "Click to purchase");
		i.setItem(0, Methods.getMaterial(new ItemStack(Material.WOOD_SWORD), ChatColor.AQUA + "10 Credits", null));
		i.setItem(1, Methods.getMaterial(new ItemStack(Material.STONE_SWORD), ChatColor.AQUA + "20 Credits", null));		
		i.setItem(2, Methods.getMaterial(new ItemStack(Material.FISHING_ROD), ChatColor.AQUA + "7 Credits", null));
		i.setItem(3, Methods.getMaterial(new ItemStack(Material.BOW), ChatColor.AQUA + "25 Credits", null));
		i.setItem(4, Methods.getMaterial(new ItemStack(Material.ARROW,8), ChatColor.AQUA + "21 Credits", null));
		i.setItem(5, Methods.getMaterial(new ItemStack(Material.IRON_HELMET), ChatColor.AQUA + "15 Credits", null));
		i.setItem(6, Methods.getMaterial(new ItemStack(Material.IRON_CHESTPLATE), ChatColor.AQUA + "18 Credits", null));
		i.setItem(7, Methods.getMaterial(new ItemStack(Material.IRON_LEGGINGS), ChatColor.AQUA + "17 Credits", null));
		i.setItem(8, Methods.getMaterial(new ItemStack(Material.IRON_BOOTS), ChatColor.AQUA + "14 Credits", null));
		i.setItem(9, Methods.getMaterial(new ItemStack(Material.LEATHER_HELMET), ChatColor.AQUA + "5 Credits", null));
		i.setItem(10, Methods.getMaterial(new ItemStack(Material.LEATHER_CHESTPLATE), ChatColor.AQUA + "8 Credits", null));
		i.setItem(11, Methods.getMaterial(new ItemStack(Material.LEATHER_LEGGINGS), ChatColor.AQUA + "7 Credits", null));
		i.setItem(12, Methods.getMaterial(new ItemStack(Material.LEATHER_BOOTS), ChatColor.AQUA + "4 Credits", null));		
		i.setItem(13, Methods.getMaterial(new ItemStack(Material.COOKED_BEEF,3), ChatColor.AQUA + "12 Credits", null));
		i.setItem(14, Methods.getMaterial(new ItemStack(Material.BREAD,3), ChatColor.AQUA + "6 Credits", null));
		i.setItem(15, Methods.getMaterial(new ItemStack(Material.ENDER_PEARL), ChatColor.AQUA + "10 Credits", null));
		i.setItem(16, Methods.getMaterial(new ItemStack(Material.EXP_BOTTLE,3), ChatColor.AQUA + "20 Credits", null));	
		return i;
	}

	public static boolean hasCredits(Player p, Material m){
		int credits = Integer.valueOf(Methods.getCredits(p));
		if (m == Material.WOOD_SWORD) return credits >= 10;
		if (m == Material.STONE_SWORD) return credits >= 20;
		if (m == Material.FISHING_ROD) return credits >= 7;
		if (m == Material.BOW) return credits >= 25;
		if (m == Material.ARROW) return credits >= 12;
		if (m == Material.IRON_HELMET) return credits >= 15;
		if (m == Material.IRON_CHESTPLATE) return credits >= 18;
		if (m == Material.IRON_LEGGINGS) return credits >= 17;
		if (m == Material.IRON_BOOTS) return credits >= 14;
		if (m == Material.LEATHER_HELMET) return credits >= 5;
		if (m == Material.LEATHER_CHESTPLATE) return credits >= 8;
		if (m == Material.LEATHER_LEGGINGS) return credits >= 7;
		if (m == Material.LEATHER_BOOTS) return credits >= 4;
		if (m == Material.COOKED_BEEF) return credits >= 12;
		if (m == Material.BREAD) return credits >= 6;
		if (m == Material.ENDER_PEARL) return credits >= 10;
		if (m == Material.EXP_BOTTLE) return credits >= 20;
		return false;
	}

	public static ItemStack getMaterial(ItemStack itemStack, String name, List<String> list){
		ItemStack i = itemStack;
		ItemMeta iM = i.getItemMeta();
		if (name != null) iM.setDisplayName(name);
		if (list != null) iM.setLore(list);
		i.setItemMeta(iM);
		return i;
	}

	public static ArrayList<Material> getKitMaterials(){
		return new ArrayList<Material>(Arrays.asList(
				Material.APPLE, Material.HAY_BLOCK,
				Material.COOKED_CHICKEN, Material.SUGAR, Material.IRON_CHESTPLATE, Material.QUARTZ, Material.BOW,
				Material.COOKED_BEEF, Material.ENDER_PEARL, Material.COMPASS, Material.EXP_BOTTLE,
				Material.GOLD_NUGGET, Material.ANVIL, Material.ARROW, Material.IRON_AXE
				));
	}

	public static Material kitToItem(String s){
		if (s.equals("appler")){
			return Material.APPLE;
		}
		if (s.equals("boxer")){
			return Material.HAY_BLOCK;
		}

		if (s.equals("rancher")){
			return Material.COOKED_CHICKEN;
		}
		if (s.equals("agile")){
			return Material.SUGAR;
		}
		if (s.equals("rugged")){
			return Material.IRON_CHESTPLATE;
		}
		if (s.equals("racer")){
			return Material.QUARTZ;
		}
		if (s.equals("ranger")){
			return Material.BOW;
		}

		if (s.equals("cannibal")){
			return Material.COOKED_BEEF;
		}
		if (s.equals("escapist")){
			return Material.ENDER_PEARL;
		}
		if (s.equals("tracker")){
			return Material.COMPASS;
		}
		if (s.equals("enchanter")){
			return Material.EXP_BOTTLE;
		}

		if (s.equals("angel")){
			return Material.GOLD_NUGGET;
		}
		if (s.equals("brute")){
			return Material.ANVIL;
		}
		if (s.equals("sharpshooter")){
			return Material.ARROW;
		}
		if (s.equalsIgnoreCase("savage")){
			return Material.IRON_AXE;
		}

		return null;
	}

	public static ItemStack getItem(){
		int r = (int)(Math.random()*175+11);
		if (r >= 11 && r <= 13) return new ItemStack(Material.STONE_SWORD);
		if (r >= 14 && r <=14) return new ItemStack(Material.IRON_SWORD);
		if (r >= 16 && r <= 21) return new ItemStack(Material.GOLD_SWORD);
		if (r >= 22 && r <= 28) return new ItemStack(Material.WOOD_SWORD);
		if (r == 29) return new ItemStack(Material.DIAMOND_HELMET);
		if (r == 30) return new ItemStack(Material.DIAMOND_BOOTS);
		if (r >= 31 && r <= 34) return new ItemStack(Material.FISHING_ROD);
		if (r >= 35 && r <= 40) return new ItemStack(Material.PORK, (41 - r)/2 + 1);
		if (r >= 41 && r <= 45) return new ItemStack(Material.BOW);
		if (r >= 46 && r <= 55) return new ItemStack(Material.ARROW, (56 - r)/3 + 1);
		if (r >= 56 && r <= 60) return new ItemStack(Material.LEATHER_HELMET);
		if (r >= 61 && r <= 65) return new ItemStack(Material.LEATHER_CHESTPLATE);
		if (r >= 66 && r <= 70) return new ItemStack(Material.LEATHER_LEGGINGS);
		if (r >= 71 && r <= 75) return new ItemStack(Material.LEATHER_BOOTS);
		if (r >= 76 && r <= 79) return new ItemStack(Material.GOLD_HELMET);
		if (r >= 80 && r <= 83) return new ItemStack(Material.GOLD_CHESTPLATE);
		if (r >= 84 && r <= 87) return new ItemStack(Material.GOLD_LEGGINGS);
		if (r >= 88 && r <= 91) return new ItemStack(Material.GOLD_BOOTS);
		if (r >= 92 && r <= 95) return new ItemStack(Material.CHAINMAIL_HELMET);
		if (r >= 96 && r <= 99) return new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		if (r >= 99 && r <= 102) return new ItemStack(Material.CHAINMAIL_LEGGINGS);
		if (r >= 103 && r <= 106) return new ItemStack(Material.CHAINMAIL_BOOTS);
		if (r >= 107 && r <= 109) return new ItemStack(Material.IRON_HELMET);
		if (r >= 110 && r <= 112) return new ItemStack(Material.IRON_CHESTPLATE);
		if (r >= 113 && r <= 115) return new ItemStack(Material.IRON_LEGGINGS);
		if (r >= 116 && r <= 118) return new ItemStack(Material.IRON_BOOTS);
		if (r >= 119 && r <= 123) return new ItemStack(Material.APPLE, (124 - r)/2 + 1);
		if (r >= 124 && r <= 128) return new ItemStack(Material.BREAD, (129 - r)/2 + 1);
		if (r >= 129 && r <= 139) return new ItemStack(Material.MELON, (140 - r)/2 + 1);
		if (r >= 140 && r <= 143) return new ItemStack(Material.COOKED_BEEF, (144 - r)/2 + 1);
		if (r >= 144 && r <= 147) return new ItemStack(Material.COOKED_CHICKEN, (148 - r)/2 + 1);
		if (r >= 148 && r <= 155) return new ItemStack(Material.CARROT_ITEM, (156 - r)/2 + 1);
		if (r >= 156 && r <= 163) return new ItemStack(Material.BAKED_POTATO, (164 - r)/2 + 1);
		if (r >= 164 && r <= 166) return new ItemStack(Material.ENDER_PEARL, (167 - r)/2 + 1);
		if (r >= 167 && r <= 169) return new ItemStack(Material.EXP_BOTTLE, 170 - r);
		if (r >= 170 && r <= 185) return new ItemStack(Material.BONE, 3);
		else return null;
	}

	public static void dropInv(Inventory inv, ItemStack[] armor, Location loc, World w){
		for (ItemStack i: inv.getContents()){
			if (i != null && i.getType() != Material.AIR) w.dropItem(loc, i);
		}
		for (ItemStack i2: armor){
			if (i2 != null && i2.getType() != Material.AIR) w.dropItem(loc, i2);
		}
	}

	public static List<Block> blocksFromTwoPoints(Location loc1, Location loc2)
	{
		List<Block> blocks = new ArrayList<Block>();

		int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
		int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

		int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
		int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

		int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
		int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

		for(int x = bottomBlockX; x <= topBlockX; x++)
		{
			for(int z = bottomBlockZ; z <= topBlockZ; z++)
			{
				for(int y = bottomBlockY; y <= topBlockY; y++)
				{
					Block block = loc1.getWorld().getBlockAt(x, y, z);

					blocks.add(block);
				}
			}
		}

		return blocks;
	}
}

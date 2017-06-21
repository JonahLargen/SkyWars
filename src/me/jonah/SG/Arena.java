package me.jonah.SG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("unused")
public class Arena implements Listener {

	//Instance Variables
	public ArrayList<UUID> players;
	public ArrayList<Location> openedChests = new ArrayList<Location>();
	private ArenaStatus status;
	private int id;
	private int endtime;
	private String name;
	public int bt;
	private int dmtimer;
	public int dm = 0;

	public HashMap<String,Integer> kills = new HashMap<String,Integer>();
	public HashMap<Location,Material> breaked_blocks = new HashMap<Location,Material>();
	public HashMap<Location,Byte> breaked_blocks_data = new HashMap<Location,Byte>();
	public HashMap<Location,Material> placed_blocks = new HashMap<Location,Material>();
	public int numPlayers;

	//Default Constructor
	public Arena(){

	}

	//Complete Constructor
	public Arena(ArrayList<UUID> players, ArenaStatus status, int id, int endtime, String name){
		this.players = players;
		this.status = status;
		this.id = id;
		this.endtime = endtime;
		this.name = name;
		this.numPlayers = ArenaManager.getPlayerCount(this.getId());
	}

	public void isReady(){
		if (this.players.size() >= 4 && this.getStatus() == ArenaStatus.WAITING){
			this.startTimer();
			this.sendAll(Methods.getDefaultPrefix() + ChatColor.GREEN + "Enough players! Game Starting.");
			Methods.sendAll(Methods.getDefaultPrefix() + ChatColor.GRAY + "Arena " + ChatColor.GOLD +  this.id + ChatColor.GRAY + "is starting!");
			this.setStatus(ArenaStatus.STARTING);
		}
	}

	@SuppressWarnings("deprecation")
	public void getRemainingPlayers(String s){
		String m = "";
		for (UUID u: players){
			String name = Bukkit.getOfflinePlayer(u).getName();
			if (!name.equals(s)){
				m = m + ChatColor.GREEN + name + ", ";
			}
		}
		if (m.equals("")){
			m = ChatColor.BLUE + "Remaining: " + ChatColor.RED + "NONE";
		}else{
			m = ChatColor.BLUE + "Remaining: "  + m.substring(0, m.length()-2);
		}
		Bukkit.getPlayer(s).sendMessage(Methods.getDefaultPrefix() + m);
	}

	public void find(Player p){
		if (this.players.size() > 1 && this.getStatus() == ArenaStatus.INGAME){
			double distance = Double.MAX_VALUE;
			int x = 0;
			int y = 0;
			int z = 0;
			String name = null;
			ArrayList<UUID> exclude = new ArrayList<UUID>();
			for (UUID u: this.players){
				exclude.add(u);
			}
			Object[] oList = exclude.toArray();
			ArrayList<UUID> newExclude = new ArrayList<UUID>();
			for (Object o: oList){
				UUID u = (UUID)o;
				newExclude.add(u);
			}
			for (UUID u: newExclude){
				if (Bukkit.getPlayer(u).getName().equals(p.getName())){
					exclude.remove(u);
				}
			}
			for (UUID u: exclude){
				Player enemy = Bukkit.getPlayer(u);
				double dis = enemy.getLocation().distance(p.getLocation());
				if (dis < distance){
					distance = dis;
					name = enemy.getName();
					x = enemy.getLocation().getBlockX();
					y = enemy.getLocation().getBlockY();
					z = enemy.getLocation().getBlockZ();
				}
			}
			p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GRAY + "Nearest Player: " + ChatColor.GOLD + name + ChatColor.GRAY + " | " + ChatColor.GOLD + (Math.round(distance * 100.0) / 100.0) + ChatColor.GRAY + " | " + ChatColor.GOLD + "[" + x + ", " + y + ", " + z + "]");
		}else{
			p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Must be in game with more than 1 player remaining!");
		}
	}

	@SuppressWarnings("deprecation")
	public void checkForWin(){
		if (this.players.size() < 2 && this.getStatus() == ArenaStatus.INGAME){
			String name = Bukkit.getPlayer(this.players.get(0)).getName();
			Methods.tLive(Bukkit.getPlayer(this.players.get(0)));
			this.removePlayer(name);
			Methods.sendAll(Methods.getDefaultPrefix() + ChatColor.GOLD + Bukkit.getPlayer(name).getName() + ChatColor.GRAY + " has won the game on arena " + ChatColor.GOLD +  this.id + ChatColor.GRAY + "!");
			Methods.addWins(1, Bukkit.getPlayer(name));
			Methods.addCredits(10, Bukkit.getPlayer(name));
			this.setStatus(ArenaStatus.WAITING);
			this.kills.clear();
			String s = EventListener.main.getConfig().getString("~Arenas." + this.id + ".name");
			World world = Bukkit.getWorld(s);
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
			for (Location l: breaked_blocks.keySet()){
				l.getBlock().setType(breaked_blocks.get(l));
			}
			for (Location l: breaked_blocks_data.keySet()){
				l.getBlock().setData(breaked_blocks_data.get(l));
			}
			for (Location l: placed_blocks.keySet()){
				l.getBlock().setType(placed_blocks.get(l));
			}
			breaked_blocks.clear();
			breaked_blocks_data.clear();
			placed_blocks.clear();
		}
	}

	public void startTimer(){
		bt = Bukkit.getScheduler().scheduleSyncRepeatingTask(EventListener.main, new Runnable(){
			int i = 46;
			@Override
			public void run() {
				if (getBroadcastNumbers().contains(i)){
					sendAll(Methods.getDefaultPrefix() + ChatColor.GRAY + "Arena starting in " + ChatColor.GOLD + i + ChatColor.GRAY + " second(s).");	
				}
				if (i == 0){
					stopTimer(false);
				}
				if (players.size() < 4){
					stopTimer(true);
				}
				i--;
			}
		}, 20L, 20L);

	}
	
	public void cancelBT(){
		if (Bukkit.getServer().getScheduler().isCurrentlyRunning(this.getBt())){
			Bukkit.getServer().getScheduler().cancelTask(this.getBt());
		}
	}

	@SuppressWarnings("deprecation")
	public void giveKits(){
		for (UUID u: this.players){
			Player player = Bukkit.getPlayer(u);
			if (EventListener.main.getConfig().get(player.getUniqueId() + ".kit").equals(KitType.APPLER.toString())){
				player.getInventory().addItem(new ItemStack(Material.APPLE,5));
			}
			/*
			if (EventListener.main.getConfig().get(player.getUniqueId() + ".kit").equals(KitType.RANCHER.toString())){
				player.getInventory().addItem(new ItemStack(Material.COOKED_CHICKEN,5));
			}
			*/
			if (EventListener.main.getConfig().get(player.getUniqueId() + ".kit").equals(KitType.RANGER.toString())){
				player.getInventory().addItem(new ItemStack(Material.WOOD_SWORD,1));
				player.getInventory().addItem(new ItemStack(Material.BOW,1));
				player.getInventory().addItem(new ItemStack(Material.ARROW,10));
			}
			if (EventListener.main.getConfig().get(player.getUniqueId() + ".kit").equals(KitType.ESCAPIST.toString())){
				player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL,5));
			}
			if (EventListener.main.getConfig().get(player.getUniqueId() + ".kit").equals(KitType.BRUTE.toString())){
				player.setMaxHealth(26);
			}
			player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE,1));
			player.getInventory().addItem(new ItemStack(Material.COBBLESTONE,32));
		}
	}

	public void addStartEffect(){
		/*
		for (UUID u: this.players){
			Player p = Bukkit.getPlayer(u);
			if (EventListener.main.getConfig().get(p.getUniqueId() + ".kit").equals(KitType.RACER.toString())) p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 30 + 1, 2));
			else p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 15 + 1, 2));
		}
		*/
	}

	public void stopTimer(boolean dueToSize){
		try{
		Bukkit.getServer().getScheduler().cancelTask(bt);
		}catch(Exception e){}
		
		this.setStatus(ArenaStatus.WAITING);
		if (dueToSize == false){
			this.kills.clear();
			for (UUID u: this.players){
				Player p = Bukkit.getPlayer(u);
				kills.put(p.getName(), 0);
			}
			this.sendAll(Methods.getDefaultPrefix() + ChatColor.GREEN.toString() + ChatColor.BOLD + "GO!!!");
			this.setStatus(ArenaStatus.INGAME);
			this.openedChests.clear();
			this.giveKits();
			this.addStartEffect();
			this.startDMTimer();
			this.checkForWin();
		}else{
			this.sendAll(Methods.getDefaultPrefix() + ChatColor.RED + "Timer cancelled because 6 players are not available.");
			this.setStatus(ArenaStatus.WAITING);
		}
		this.checkForWin();
	}

	public void startDMTimer(){
		dm = 901;
		dmtimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(EventListener.main, new Runnable(){
			@Override
			public void run() {
				if (getStatus() == ArenaStatus.STARTING || getStatus() == ArenaStatus.WAITING) stopDMTimer();
				//if (getDMNumbers().contains(dm)) sendAll(Methods.getDefaultPrefix() + ChatColor.RED + "Deathmatch starting in " + (int)(dm/60) + " minute(s).");
				//if (getDMNumbers2().contains(dm)) sendAll(Methods.getDefaultPrefix() + ChatColor.RED + "Deathmatch starting in " + dm + " second(s).");
				//if (dm == 0){
				//	int i = 1;
				//	for (UUID u: players){
				//		teleportDM(Bukkit.getPlayer(u), i);
				//		i++;
				//	}
				//}
				if (dm == 0){
					sendAll(Methods.getDefaultPrefix() + ChatColor.RED + "Game taking too long, stopping arena.");
					stop(); 
				}
				dm--;
			}
		}, 0L, 20L);
	}

	public ArrayList<Integer> getDMNumbers(){
		return new ArrayList<Integer>(Arrays.asList(900,600,300,180,120,60));
	}
	public ArrayList<Integer> getDMNumbers2(){
		return new ArrayList<Integer>(Arrays.asList(30,10,5,4,3,2,1));
	}

	public void stopDMTimer(){
		Bukkit.getServer().getScheduler().cancelTask(dmtimer);
	}

	public ArrayList<Integer> getBroadcastNumbers(){
		return new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,10,20,30,45));
	}

	public Location getSpawnOnPlayerJoin(){
		int i = this.players.size() + 1;
		String world = EventListener.main.getConfig().getString("~Arenas." + this.id + "-" + this.name + "." + i + ".world");
		double x = EventListener.main.getConfig().getDouble("~Arenas." + this.id + "-" + this.name + "." + i + ".x");
		double y = EventListener.main.getConfig().getDouble("~Arenas." + this.id + "-" + this.name + "." + i + ".y");
		double z = EventListener.main.getConfig().getDouble("~Arenas." + this.id + "-" + this.name + "." + i + ".z");
		double yaw = EventListener.main.getConfig().getDouble("~Arenas." + this.id + "-" + this.name + "." + i + ".yaw");
		double pitch = EventListener.main.getConfig().getDouble("~Arenas." + this.id + "-" + this.name + "." + i + ".pitch");
		return new Location(Bukkit.getWorld(world),x,y,z,(float)yaw,(float)pitch);
	}

	//Methods
	public void sendAll(String string){
		for (UUID u: players){
			Bukkit.getPlayer(u).sendMessage(string);
		}
	}

	@SuppressWarnings("deprecation")
	public void stop(){
		Object[] oList = this.players.toArray();
		for (Object o: oList){
			UUID u = (UUID)o;
			this.removePlayer(Bukkit.getPlayer(u).getName());
		}
		for (Location l: breaked_blocks.keySet()){
			l.getBlock().setType(breaked_blocks.get(l));
		}
		for (Location l: breaked_blocks_data.keySet()){
			l.getBlock().setData(breaked_blocks_data.get(l));
		}
		for (Location l: placed_blocks.keySet()){
			l.getBlock().setType(placed_blocks.get(l));
		}
		breaked_blocks.clear();
		breaked_blocks_data.clear();
		placed_blocks.clear();
	}

	public void teleport(final Player player){
		String id = String.valueOf(this.id);
		int i = this.players.size();
		final String world = EventListener.main.getConfig().getString("~Arenas." + id + ".spawn." + i + ".world");
		final double x = EventListener.main.getConfig().getDouble("~Arenas." + id + ".spawn." + i + ".x");
		final double y = EventListener.main.getConfig().getDouble("~Arenas." + id + ".spawn." + i + ".y");
		final double z = EventListener.main.getConfig().getDouble("~Arenas." + id + ".spawn." + i + ".z");
		final double yaw = EventListener.main.getConfig().getDouble("~Arenas." + id + ".spawn." + i + ".yaw");
		final double pitch = EventListener.main.getConfig().getDouble("~Arenas." + id + ".spawn." + i + ".pitch");
		for(UUID online : this.players) {
			Player player2 = Bukkit.getPlayer(online);
			player.showPlayer(player2);
		}

		new BukkitRunnable(){
			@Override
			public void run() {
				player.teleport(new Location(Bukkit.getWorld(world),x,y,z,(float)yaw,(float)pitch));
			}
		}.runTaskLater(EventListener.main, 2L);
	}

	public void teleportDM(final Player player, int it){
		String id = String.valueOf(this.id);
		int i = it;
		final String world = EventListener.main.getConfig().getString("~Arenas." + id + ".spawn." + i + ".world");
		final double x = EventListener.main.getConfig().getDouble("~Arenas." + id + ".spawn." + i + ".x");
		final double y = EventListener.main.getConfig().getDouble("~Arenas." + id + ".spawn." + i + ".y");
		final double z = EventListener.main.getConfig().getDouble("~Arenas." + id + ".spawn." + i + ".z");
		final double yaw = EventListener.main.getConfig().getDouble("~Arenas." + id + ".spawn." + i + ".yaw");
		final double pitch = EventListener.main.getConfig().getDouble("~Arenas." + id + ".spawn." + i + ".pitch");
		for(UUID online : this.players) {
			Player player2 = Bukkit.getPlayer(online);
			player.showPlayer(player2);
		}
		new BukkitRunnable(){
			@Override
			public void run() {
				player.teleport(new Location(Bukkit.getWorld(world),x,y,z,(float)yaw,(float)pitch));
				player.getInventory().remove(Material.ENDER_PEARL);
			}
		}.runTaskLater(EventListener.main, 2L);
	}

	public void semiTeleport(final Player player){
		String id = String.valueOf(this.id);
		int i = this.players.indexOf(player.getUniqueId()) + 1;
		final String world = EventListener.main.getConfig().getString("~Arenas." + id + ".spawn." + i + ".world");
		final double x = EventListener.main.getConfig().getDouble("~Arenas." + id + ".spawn." + i + ".x");
		final double y = EventListener.main.getConfig().getDouble("~Arenas." + id + ".spawn." + i + ".y");
		final double z = EventListener.main.getConfig().getDouble("~Arenas." + id + ".spawn." + i + ".z");
		for(UUID online : this.players) {
			Player player2 = Bukkit.getPlayer(online);
			player.showPlayer(player2);
		}
		if (Math.abs(player.getLocation().getX()) - Math.abs(x) > 0.1 || Math.abs(player.getLocation().getX()) - Math.abs(x) < -0.1){
			new BukkitRunnable(){
				@Override
				public void run() {
					player.teleport(new Location(Bukkit.getWorld(world),x,y,z,player.getLocation().getYaw(),player.getLocation().getPitch()));
				}				
			}.runTaskLater(EventListener.main, 2L);
			return;
		}
		if (Math.abs(player.getLocation().getZ()) - Math.abs(z) > 0.1 || Math.abs(player.getLocation().getZ()) - Math.abs(z) < -0.1){
			new BukkitRunnable(){
				@Override
				public void run() {
					player.teleport(new Location(Bukkit.getWorld(world),x,y,z,player.getLocation().getYaw(),player.getLocation().getPitch()));
				}				
			}.runTaskLater(EventListener.main, 2L);
		}
	}


	//Getters & Setters
	@SuppressWarnings("deprecation")
	public void addPlayer(String name){
		if (!players.contains(Bukkit.getPlayer(name).getUniqueId())){
			players.add(Bukkit.getPlayer(name).getUniqueId());
			Bukkit.getPlayer(name).getInventory().clear();
			this.isReady();
			this.teleport(Bukkit.getPlayer(name));
			this.sendAll(Methods.getDefaultPrefix() + ChatColor.GOLD + name + ChatColor.GRAY + " has joined. " + ChatColor.GOLD + "[" + this.players.size() + "/" + numPlayers +"]");
			Methods.fullHeal(Bukkit.getPlayer(name));
		}
		//Bukkit.getPlayer(name).sendMessage(Methods.getDefaultPrefix() + ChatColor.GRAY + "There are currently " + ChatColor.GOLD + this.players.size() + ChatColor.GRAY + " player(s) in the game.");
		if (this.players.size() < 4){
			int i = 4 - this.players.size();
			this.sendAll(Methods.getDefaultPrefix() + ChatColor.GOLD + i + ChatColor.GRAY + " more players needed to start the game.");
		}
		updateSignsOnAdd();
	}

	@SuppressWarnings("deprecation")
	public void removePlayer(String name){
		try{
			if (players.contains(Bukkit.getPlayer(name).getUniqueId())){
				Methods.toSpawn(Bukkit.getPlayer(name));
				players.remove(Bukkit.getOfflinePlayer(name).getUniqueId());
				if (this.players.size() > 0) this.checkForWin();
				for (PotionEffect ef: Bukkit.getPlayer(name).getActivePotionEffects()){
					Bukkit.getPlayer(name).removePotionEffect(ef.getType());
				}
				Methods.fullHeal(Bukkit.getPlayer(name));
				Methods.setUpInventory(Bukkit.getPlayer(name));
			}
		}catch(Exception e){}
		updateSignsOnRemove();
	}

	public void updateSignsOnAdd(){
		try{
			String s = Integer.toString(id);
			String world = EventListener.main.getConfig().getString("~Signs." + s + ".world");
			double x = EventListener.main.getConfig().getDouble("~Signs." + s + ".x");
			double y = EventListener.main.getConfig().getDouble("~Signs." + s + ".y");
			double z = EventListener.main.getConfig().getDouble("~Signs." + s + ".z");
			Sign sign = (Sign) Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),x,y,z)).getState();
			ArrayList<String> array = new ArrayList<String>();
			for (UUID u: this.players){
				String name = Bukkit.getPlayer(u).getName();
				array.add(name);
			}
			Block b_x1 = Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),x+1,y,z));
			Block b_x2 = Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),x-1,y,z));
			Block b_z1 = Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),z+1,y,z));
			Block b_z2 = Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),z-1,y,z));

			int b = 1;
			if (b_x1.getState() instanceof Sign){
				int count = 0;
				int max = array.size();
				while (count < max){
					int a = 0;
					sign = (Sign) Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),x+b,y,z)).getState();
					while (a < 4 && count < max){
						sign.setLine(a, array.get(count));
						sign.update();
						a++;
						count++;
					}
					b++;
				}
			}
			if (b_x2.getState() instanceof Sign){
				int count = 0;
				int max = array.size();
				while (count < max){
					int a = 0;
					sign = (Sign) Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),x-b,y,z)).getState();
					while (a < 4 && count < max){
						sign.setLine(a, array.get(count));
						sign.update();
						a++;
						count++;
					}
					b++;
				}
			}
			if (b_z1.getState() instanceof Sign){
				int count = 0;
				int max = array.size();
				while (count < max){
					int a = 0;
					sign = (Sign) Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),z+b,y,z)).getState();
					while (a < 4 && count < max){
						sign.setLine(a, array.get(count));
						sign.update();
						a++;
						count++;
					}
					b++;
				}
			}
			if (b_z2.getState() instanceof Sign){
				int count = 0;
				int max = array.size();
				while (count < max){
					int a = 0;
					sign = (Sign) Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),z-b,y,z)).getState();
					while (a < 4 && count < max){
						sign.setLine(a, array.get(count));
						sign.update();
						a++;
						count++;
					}
					b++;
				}
			}
		}catch (Exception e){
			//e.printStackTrace();
		}
	}

	public void updateSignsOnRemove(){
		clearSigns();
		updateSignsOnAdd();
	}

	public void clearSigns(){
		try{
			String s = Integer.toString(id);
			String world = EventListener.main.getConfig().getString("~Signs." + s + ".world");
			double x = EventListener.main.getConfig().getDouble("~Signs." + s + ".x");
			double y = EventListener.main.getConfig().getDouble("~Signs." + s + ".y");
			double z = EventListener.main.getConfig().getDouble("~Signs." + s + ".z");
			Sign sign = (Sign) Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),x,y,z)).getState();

			Block b_x1 = Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),x+1,y,z));
			Block b_x2 = Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),x-1,y,z));
			Block b_z1 = Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),z+1,y,z));
			Block b_z2 = Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),z-1,y,z));

			int b = 1;
			if (b_x1.getState() instanceof Sign){
				int count = 0;
				while (count < 24){
					int a = 0;
					if (Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),x+b,y,z)).getState() instanceof Sign){
						sign = (Sign) Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),x+b,y,z)).getState();
					}
					while (a < 4 && count < 24){
						sign.setLine(a, "");
						sign.update();
						a++;
						count++;
					}
					b++;
				}
				updateSignsOnAdd();
			}
			if (b_x2.getState() instanceof Sign){
				int count = 0;
				while (count < 24){
					int a = 0;
					if (Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),x+b,y,z)).getState() instanceof Sign){
						sign = (Sign) Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),x-b,y,z)).getState();
					}
					while (a < 4 && count < 24){
						sign.setLine(a, "");
						sign.update();
						a++;
						count++;
					}
					b++;
				}
				updateSignsOnAdd();
			}
			if (b_z1.getState() instanceof Sign){
				int count = 0;
				while (count < 24){
					int a = 0;
					sign = (Sign) Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),z+b,y,z)).getState();
					while (a < 4 && count < 24){
						sign.setLine(a, "");
						sign.update();
						a++;
						count++;
					}
					b++;
				}
				updateSignsOnAdd();
			}
			if (b_z2.getState() instanceof Sign){
				int count = 0;
				while (count < 24){
					int a = 0;
					sign = (Sign) Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world),z-b,y,z)).getState();
					while (a < 4 && count < 24){
						sign.setLine(a, "");
						sign.update();
						a++;
						count++;
					}
					b++;
				}
				updateSignsOnAdd();
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public boolean hasPlayer(String name){
		if (players.contains(Bukkit.getPlayer(name).getUniqueId())){
			return true;
		}else{
			return false;
		}
	}

	public int getPlayerLength(){
		return this.players.size();
	}

	public ArenaStatus getStatus(){
		return status;
	}

	public void setStatus(ArenaStatus status){
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<UUID> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<UUID> players) {
		this.players = players;
	}

	public ArrayList<Location> getOpenedChests() {
		return openedChests;
	}

	public void setOpenedChests(ArrayList<Location> openedChests) {
		this.openedChests = openedChests;
	}

	public int getBt() {
		return bt;
	}

	public void setBt(int bt) {
		this.bt = bt;
	}

	public int getDmtimer() {
		return dmtimer;
	}

	public void setDmtimer(int dmtimer) {
		this.dmtimer = dmtimer;
	}

	public int getDm() {
		return dm;
	}

	public void setDm(int dm) {
		this.dm = dm;
	}

	public HashMap<String, Integer> getKills() {
		return kills;
	}

	public void setKills(HashMap<String, Integer> kills) {
		this.kills = kills;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	
	

}

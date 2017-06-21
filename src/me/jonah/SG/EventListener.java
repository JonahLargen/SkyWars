package me.jonah.SG;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;


@SuppressWarnings({ "deprecation" })
public class EventListener implements Listener{

	static Main main;
	static ArrayList<Arrow> noArrowList = new ArrayList<Arrow>();

	public EventListener(Main plugin){
		main = plugin;
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		if (event.getMessage().startsWith("/bukkit")){
			event.getPlayer().sendMessage(ChatColor.RED + "Nice try!");
			event.setCancelled(true);
		}
		if (event.getMessage().startsWith("/help")){
			event.getPlayer().sendMessage(ChatColor.BLUE + "Use /commands for help!");
			event.setCancelled(true);
		}
		if (event.getMessage().startsWith("/spawn")){
			event.getPlayer().sendMessage(ChatColor.BLUE + "Use /lobby instead!");
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		event.getDrops().clear();
		Methods.toSpawn(event.getEntity());
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event){
		event.setJoinMessage(null);

		if (!main.getConfig().contains("~DXP")){
			main.getConfig().createSection("~DXP");
			main.getConfig().set("~DXP", false);
			main.saveConfig();
		}

		if (!main.getConfig().contains("~T")){
			main.getConfig().createSection("~T");
			main.getConfig().set("~T.live", false);
			main.saveConfig();
		}

		if (!main.getConfig().contains("~T." + event.getPlayer().getUniqueId())){
			main.getConfig().set("~T." + event.getPlayer().getUniqueId(), 0);
		}

		PEManager.clear(event.getPlayer().getName());
		if (ArenaManager.buildMode.contains(event.getPlayer().getName())) ArenaManager.buildMode.remove(event.getPlayer().getName());

		Methods.setUpInventory(event.getPlayer());
		Methods.fullHeal(event.getPlayer());
		event.getPlayer().setGameMode(GameMode.SURVIVAL);
		if (!main.getConfig().contains(event.getPlayer().getUniqueId()+"")){
			main.getConfig().createSection(event.getPlayer().getUniqueId()+"");
			main.getConfig().createSection(event.getPlayer().getUniqueId() + ".wins");
			main.getConfig().createSection(event.getPlayer().getUniqueId() + ".deaths");
			main.getConfig().createSection(event.getPlayer().getUniqueId() + ".kills");
			main.getConfig().createSection(event.getPlayer().getUniqueId() + ".kit");
			main.getConfig().createSection(event.getPlayer().getUniqueId() + ".credits");
			main.getConfig().createSection(event.getPlayer().getUniqueId() + ".creditboost");
			main.getConfig().createSection(event.getPlayer().getUniqueId() + ".pk1");
			main.getConfig().createSection(event.getPlayer().getUniqueId() + ".pk2");
			main.getConfig().createSection(event.getPlayer().getUniqueId() + ".pk3");
			main.getConfig().createSection(event.getPlayer().getUniqueId() + ".pk4");
			main.getConfig().createSection(event.getPlayer().getUniqueId() + ".kit1");
			main.getConfig().createSection(event.getPlayer().getUniqueId() + ".kit2");
			main.getConfig().createSection(event.getPlayer().getUniqueId() + ".kit3");
			main.getConfig().createSection(event.getPlayer().getUniqueId() + ".kit4");
			main.getConfig().createSection(event.getPlayer().getUniqueId() + ".kit5");
			main.getConfig().set(event.getPlayer().getUniqueId() + ".wins", 0);
			main.getConfig().set(event.getPlayer().getUniqueId() + ".deaths", 0);
			main.getConfig().set(event.getPlayer().getUniqueId() + ".kills", 0);
			main.getConfig().set(event.getPlayer().getUniqueId() + ".kit", KitType.APPLER.toString());
			main.getConfig().set(event.getPlayer().getUniqueId() + ".credits", 0);
			main.getConfig().set(event.getPlayer().getUniqueId() + ".attackboost", 0);
			main.getConfig().set(event.getPlayer().getUniqueId() + ".defenseboost", 0);
			main.getConfig().set(event.getPlayer().getUniqueId() + ".creditboost", 0);
			main.getConfig().set(event.getPlayer().getUniqueId() + ".pk1", 0);
			main.getConfig().set(event.getPlayer().getUniqueId() + ".pk2", 0);
			main.getConfig().set(event.getPlayer().getUniqueId() + ".pk3", 0);
			main.getConfig().set(event.getPlayer().getUniqueId() + ".pk4", 0);
			main.getConfig().set(event.getPlayer().getUniqueId() + ".kit1", false);
			main.getConfig().set(event.getPlayer().getUniqueId() + ".kit2", false);
			main.getConfig().set(event.getPlayer().getUniqueId() + ".kit3", false);
			main.getConfig().set(event.getPlayer().getUniqueId() + ".kit4", false);
			main.getConfig().set(event.getPlayer().getUniqueId() + ".kit5", false);
			main.saveConfig();
		}

		ArenaManager.setKit(event.getPlayer(), KitType.valueOf(EventListener.main.getConfig().getString(event.getPlayer().getUniqueId() + ".kit")));

		if (!main.getConfig().contains("~Arenas")){
			main.getConfig().createSection("~Arenas");
			EventListener.main.saveConfig();
		}
		if (!main.getConfig().contains("~Signs")){
			main.getConfig().createSection("~Signs");
			EventListener.main.saveConfig();
		}

		Methods.toSpawn(event.getPlayer());

		if (event.getPlayer().hasPermission("sg.owner")){
			event.getPlayer().setPlayerListName(ChatColor.DARK_RED + event.getPlayer().getName());
		}else if (event.getPlayer().hasPermission("sg.admin") || event.getPlayer().hasPermission("sg.youtube")){
			event.getPlayer().setPlayerListName(ChatColor.RED + event.getPlayer().getName());
		}else if (event.getPlayer().hasPermission("sg.mod")){
			event.getPlayer().setPlayerListName(ChatColor.DARK_PURPLE + event.getPlayer().getName());
		}else if (event.getPlayer().hasPermission("sg.builder")){
			event.getPlayer().setPlayerListName(ChatColor.GOLD + event.getPlayer().getName());
		}else if (event.getPlayer().hasPermission("sg.mvp")){
			event.getPlayer().setPlayerListName(ChatColor.BLUE + event.getPlayer().getName());
		}else if (event.getPlayer().hasPermission("sg.vip")){
			event.getPlayer().setPlayerListName(ChatColor.GREEN + event.getPlayer().getName());
		}else{
			//do nothing
		}

		Methods.setUpInventory(event.getPlayer());

		new BukkitRunnable(){
			@Override
			public void run() {
				if (event.getPlayer().isOp()) Methods.sendAll(ChatColor.BOLD.toString() + ChatColor.RED.toString() + ChatColor.BOLD + "Operator " + event.getPlayer().getName() + " has joined!");	
				if (Methods.isTLive()){
					event.getPlayer().sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "Tournament is live! Win as many games as you can for ranks and prizes! For more information click here:");
					event.getPlayer().sendMessage(ChatColor.GREEN + "  > " + ChatColor.GRAY + "www.swift-sg.com/tournament");
				}
				boolean b = EventListener.main.getConfig().getBoolean("~DXP");
				if (b){
					event.getPlayer().sendMessage(Methods.getDefaultPrefix() + ChatColor.BLUE + "Double Credit Event is live! Get x2 the credits on wins, kills, and parkour!");
				}
			}

		}.runTaskLater(main, 10L);

	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		event.setQuitMessage(null);

		PEManager.clear(event.getPlayer().getName());
		if (ArenaManager.buildMode.contains(event.getPlayer().getName())) ArenaManager.buildMode.remove(event.getPlayer().getName());

		for (int id = 1; main.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
			Arena arena = ArenaManager.getArena(id);
			if (arena.hasPlayer(event.getPlayer().getName())){
				arena.removePlayer(event.getPlayer().getName());
				arena.sendAll(Methods.getDefaultPrefix() + ChatColor.GOLD + event.getPlayer().getName() + ChatColor.GRAY + " has left.");
			}
		}
	}

	@EventHandler
	public void weather(WeatherChangeEvent event){
		if (event.getWorld().hasStorm() == false) event.setCancelled(true);
	}

	@EventHandler
	public void countDown(final ServerListPingEvent event){
		event.setMaxPlayers(event.getNumPlayers() + 1);
		if (Methods.dxpLive()){
			event.setMotd("          " + ChatColor.WHITE + ChatColor.STRIKETHROUGH.toString() + "----------" + ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "[ " + ChatColor.AQUA + ChatColor.BOLD.toString() + "Swift " + ChatColor.WHITE + ChatColor.BOLD.toString() + "SW" + ChatColor.DARK_GRAY + ChatColor.BOLD.toString()  + " ]" + ChatColor.RESET + ChatColor.WHITE + ChatColor.STRIKETHROUGH.toString() + "----------" + ChatColor.RESET + "\n                    " + ChatColor.GRAY + "< " + ChatColor.BLUE + "Double XP Event Live" + ChatColor.GRAY + " >");
		}else{
			event.setMotd("          " + ChatColor.WHITE + ChatColor.STRIKETHROUGH.toString() + "----------" + ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "[ " + ChatColor.AQUA + ChatColor.BOLD.toString() + "Swift " + ChatColor.WHITE + ChatColor.BOLD.toString() + "SW" + ChatColor.DARK_GRAY + ChatColor.BOLD.toString()  + " ]" + ChatColor.RESET + ChatColor.WHITE + ChatColor.STRIKETHROUGH.toString() + "----------" + ChatColor.RESET + "\n               " + ChatColor.GRAY + "< " + ChatColor.BLUE + "Fully Custom Survival Games" + ChatColor.GRAY + " >");
		}
	}


	@EventHandler
	public void bow(final EntityShootBowEvent event){
		if (event.getEntity() instanceof Player){
			final Player p = (Player)event.getEntity();
			if (ArenaManager.getKit(p) != KitType.SHARPSHOOTER.toString()){
				return;
			}else{
				if (!(event.getProjectile() instanceof Arrow)){
					return;
				}else{
					new BukkitRunnable(){
						@Override
						public void run(){
							Arrow arrow = p.launchProjectile(Arrow.class, event.getProjectile().getVelocity().add(new Vector((Math.random()-.5)/10,(Math.random()-.5)/10,(Math.random()-.5)/10)));
							Arrow arrow2 = p.launchProjectile(Arrow.class, event.getProjectile().getVelocity().add(new Vector((Math.random()-.5)/10,(Math.random()-.5)/10,(Math.random()-.5)/10)));
							noArrowList.add(arrow);
							noArrowList.add(arrow2);
						}	
					}.runTaskLater(main, 2L);
				}
			}
		}
	}

	@EventHandler
	public void onland(ProjectileHitEvent event){
		if (event.getEntity() instanceof Arrow){
			Arrow arrow = (Arrow)event.getEntity();
			if (noArrowList.contains(arrow)){
				arrow.remove();
				noArrowList.remove(arrow);
			}
		}
	}


	@EventHandler
	public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event){
		event.setFormat(ChatColor.GRAY.toString() + "[" + ChatColor.WHITE + main.getConfig().getInt(event.getPlayer().getUniqueId() + ".wins") + ChatColor.GRAY + "] " + event.getFormat());
	}
	
	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlockClicked().getRelative(event.getBlockFace());
		boolean hasArena = false;
		Arena a = null;
		for (int id = 1; main.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
			Arena arena = ArenaManager.getArena(id);
			if (arena.hasPlayer(player.getName()) && arena.getStatus() == ArenaStatus.INGAME){
				hasArena = true;
				a = arena;
			}
		}
		if (hasArena == true){
			if (!a.breaked_blocks.containsKey(block.getLocation()) && !a.placed_blocks.containsKey(block.getLocation())) a.placed_blocks.put(block.getLocation(), Material.AIR);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		Block block = event.getBlock();
		boolean hasArena = false;
		Arena a = null;
		for (int id = 1; main.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
			Arena arena = ArenaManager.getArena(id);
			if (arena.hasPlayer(player.getName()) && arena.getStatus() == ArenaStatus.INGAME){
				hasArena = true;
				a = arena;
			}
		}
		if (hasArena == true){
			if (!a.breaked_blocks.containsKey(block.getLocation()) && !a.placed_blocks.containsKey(block.getLocation())){
				a.breaked_blocks.put(block.getLocation(), block.getType());
				a.breaked_blocks_data.put(block.getLocation(), block.getData());
			}
		}else{
			if (event.getPlayer().isOp()){
				if(event.getBlock().getState() instanceof Sign){
					if (event.getPlayer().getItemInHand().getType() == Material.APPLE){
						if (((Sign)event.getBlock().getState()).getLine(0).contains("SW")){
							for (int id = 1; main.getConfig().contains("~Signs." + String.valueOf(id)) == true; id++){
								String s = Integer.toString(id);
								String world = main.getConfig().getString("~Signs." + s + ".world");
								double x = main.getConfig().getDouble("~Signs." + s + ".x");
								double y = main.getConfig().getDouble("~Signs." + s + ".y");
								double z = main.getConfig().getDouble("~Signs." + s + ".z");
								Location loc = new Location(Bukkit.getWorld(world),x,y,z);
								if (loc.equals(event.getBlock().getLocation())){
									main.getConfig().set("~Signs." + s, null);
									//main.getConfig().set("~Arenas." + s, null);
									main.saveConfig();
									event.getPlayer().sendMessage(ChatColor.GREEN + "You removed sign " + s);
								}
							}
						}
					}else{
						event.setCancelled(true);
						event.getPlayer().sendMessage(ChatColor.RED + "You must hold an apple to remove the sign.");
					}
				}else{
					for (int id = 1; main.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
						Arena arena = ArenaManager.getArena(id);
						if (arena.hasPlayer(event.getPlayer().getName())){
							event.setCancelled(true);
						}
					}
					if (!ArenaManager.buildMode.contains(event.getPlayer().getName())){
						event.setCancelled(true);
					}
				}
			}else{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event){
		if (event.getEntity() instanceof Player){
			Player player = (Player)event.getEntity();
			boolean hasArena = false;
			for (int id = 1; main.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
				Arena arena = ArenaManager.getArena(id);
				if (arena.hasPlayer(player.getName()) && arena.getStatus() == ArenaStatus.INGAME){
					hasArena = true;
				}
			}
			if (hasArena == false){
				event.setFoodLevel(20);
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event){
		try{

			boolean ok = false;

			if (event instanceof EntityDamageByEntityEvent){
				EntityDamageByEntityEvent event2 = (EntityDamageByEntityEvent)event;
				if (event2.getEntity() instanceof Player){
					Player damagee = (Player)event2.getEntity();
					Player damager = null;
					if (event2.getDamager() instanceof Player){
						damager = (Player)event2.getDamager();

						if (damagee.getWorld().getName().equals("world") && damager.getWorld().getName().equals("world")){
							if (damagee.getLocation().getBlockX() >= 203 && damagee.getLocation().getBlockX() <= 219){
								if (damager.getLocation().getBlockX() >= 203 && damager.getLocation().getBlockX() <= 219){
									if (damagee.getLocation().getBlockZ() >= -5 && damagee.getLocation().getBlockZ() <= 5){
										if (damager.getLocation().getBlockZ() >= -5 && damager.getLocation().getBlockZ() <= 5){
											if (event.getDamage() >= damagee.getHealth()){
												Methods.toSpawn(damagee);
												Methods.fullHeal(damagee);
											}
											return;
										}
									}
								}
							}
						}

					}
					if (event2.getDamager() instanceof Arrow){
						Arrow arrow = (Arrow)event2.getDamager();
						if (arrow.getShooter() instanceof Player){
							damager = (Player) arrow.getShooter();
						}else{
							//arrow shooter not a player
							event2.setCancelled(true);
						}
					}
					if (event2.getDamager() instanceof Fish){
						Fish fish = (Fish)event2.getDamager();
						if (fish.getShooter() instanceof Player){
							damager = (Player) fish.getShooter();
							return;
						}else{
							//hook shooter not a player
							event2.setCancelled(true);
						}
					}
					if (damager == null){
						//damager not set
						event2.setCancelled(true);
					}

					for (int id = 1; main.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
						Arena arena = ArenaManager.getArena(id);
						if (arena.hasPlayer(damager.getName()) && arena.hasPlayer(damagee.getName())){
							if (arena.getStatus() == ArenaStatus.INGAME){

								ok = true;

								if (damager.hasPotionEffect(PotionEffectType.SPEED)) damager.removePotionEffect(PotionEffectType.SPEED);

								double damageeHealth = Math.round(damagee.getHealth() * 100.0) / 100.0;
								if (ArenaManager.getKit(damager).equals(KitType.TRACKER.toString())) damager.sendMessage(Methods.getDefaultPrefix() + ChatColor.BLUE + damagee.getName() + " has " + damageeHealth + " health remaining.");

								double damage = event2.getDamage();


								if (ArenaManager.getKit(damager).equals(KitType.SAVAGE.toString())) damage+=0.5;

								if (ArenaManager.getKit(damagee).equals(KitType.RUGGED.toString())) damage-=0.5;

								if (ArenaManager.getKit(damager).equals(KitType.BOXER.toString()) && damager.getItemInHand().getType() == Material.AIR) damage*=1.5;

								if (ArenaManager.getKit(damager).equals(KitType.CANNIBAL.toString()) && damager.getFoodLevel() < 20) damager.setFoodLevel(damager.getFoodLevel() + 1);

								if (ArenaManager.getKit(damager).equals(KitType.ANGEL.toString()) && damager.getHealth() < 20) damager.setHealth(damager.getHealth() + 1);

								event2.setDamage(damage);							

								if (damage >= damagee.getHealth()){
									Methods.dropInv(damagee.getInventory(), damagee.getInventory().getArmorContents(), damagee.getLocation(), damagee.getWorld());
									damager.setLevel(damager.getLevel() + 1);
									if (ArenaManager.getKit(damager).equals(KitType.ENCHANTER.toString())) damager.setLevel(damager.getLevel() + 1);
									event2.setDamage(0);
									Methods.addKills(1, damager);
									Methods.addCredits(2, damager);
									Methods.addDeaths(1, damagee);
									arena.kills.put(damager.getName(), arena.kills.get(damager.getName()) + 1);
									int i = arena.kills.get(damager.getName());
									damager.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 4 * i + 5, 0));
									damager.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You received buff level " + i);
									//
									String s = damager.getItemInHand().getType().toString().replaceAll("_", " ");
									arena.sendAll(Methods.getDefaultPrefix() + ChatColor.GOLD + damagee.getName() + ChatColor.GRAY + " killed by " + ChatColor.GOLD + damager.getName() + ChatColor.GRAY + " with " + ChatColor.GREEN + s);
									arena.removePlayer(damagee.getName());
									event2.setCancelled(true);
									return;
								}else{
									//allow but not a finishing blow
									return;
								}

							}else{
								//not in game
							}
						}else{
							//not in same arena
						}
					}


				}else{
					//damagee not a player
					event2.setCancelled(true);
				}
			}else{
				if (event.getEntity() instanceof Player){			
					Player damagee = (Player)event.getEntity();
					for (int id = 1; main.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
						Arena arena = ArenaManager.getArena(id);
						if (arena.hasPlayer(damagee.getName())){
							if (arena.getStatus() == ArenaStatus.INGAME){
								ok = true;

								double damage = event.getDamage();						
								//if (ArenaManager.getKit(damagee).equals(KitType.AGILE.toString()) && event.getCause() == DamageCause.FALL) damage-=2;						
								event.setDamage(damage);

								if (event.getDamage() >= damagee.getHealth()){
									Methods.dropInv(damagee.getInventory(), damagee.getInventory().getArmorContents(), damagee.getLocation(), damagee.getWorld());
									event.setDamage(0);
									Methods.addDeaths(1, damagee);
									//
									String s = event.getCause().toString().replaceAll("_", " ");
									arena.sendAll(Methods.getDefaultPrefix() + ChatColor.GOLD + damagee.getName() + ChatColor.GRAY + " killed by " + ChatColor.GREEN + s);
									arena.removePlayer(damagee.getName());
									event.setCancelled(true);
									return;
								}else{
									//allow but not a finishing blow
									return;
								}

							}else{
								//not in game
							}
						}else{
							//not in that arena
						}
					}


				}else{
					//damagee not a player
					event.setCancelled(true);
				}
			}

			if (ok == false) event.setCancelled(true);

		}catch (NullPointerException e){
			return;
		}
	}

	@EventHandler
	public void onEntityDamageByPlayer(EntityDamageByEntityEvent event){
		//removed
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		Block block = event.getBlock();
		boolean hasArena = false;
		Arena a = null;
		for (int id = 1; main.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
			Arena arena = ArenaManager.getArena(id);
			if (arena.hasPlayer(player.getName()) && arena.getStatus() == ArenaStatus.INGAME){
				hasArena = true;
				a = arena;
			}
		}
		if (hasArena == true){
			if (!a.breaked_blocks.containsKey(block.getLocation()) && !a.placed_blocks.containsKey(block.getLocation())) a.placed_blocks.put(block.getLocation(), Material.AIR);
		}else{
			if (event.getPlayer().isOp()){
				for (int id = 1; main.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
					Arena arena = ArenaManager.getArena(id);
					if (arena.hasPlayer(event.getPlayer().getName())){
						event.setCancelled(true);
					}
				}
				if (!ArenaManager.buildMode.contains(event.getPlayer().getName())){
					event.setCancelled(true);
				}
			}
		}
	}


	@EventHandler
	public void onSignCreate(SignChangeEvent event){
		Sign sign = (Sign) event.getBlock().getState();
		if (event.getLine(0).contains("SW")){
			String s = event.getLine(0).substring(2);
			int i = Integer.valueOf(s);
			EventListener.main.getConfig().createSection("~Signs." + s);
			EventListener.main.getConfig().createSection("~Signs." + s + ".world");
			EventListener.main.getConfig().set("~Signs." + s + ".world", sign.getLocation().getWorld().getName());
			EventListener.main.getConfig().createSection("~Signs." + s + ".x");
			EventListener.main.getConfig().set("~Signs." + s + ".x", sign.getLocation().getX());
			EventListener.main.getConfig().createSection("~Signs." + s + ".y");
			EventListener.main.getConfig().set("~Signs." + s + ".y", sign.getLocation().getY());
			EventListener.main.getConfig().createSection("~Signs." + s + ".z");
			EventListener.main.getConfig().set("~Signs." + s + ".z", sign.getLocation().getZ());
			EventListener.main.saveConfig();

			sign.setLine(0, ChatColor.BLUE.toString() + "[SW-" + s + "]");
			ArenaStatus status = ArenaManager.getArena(i).getStatus();
			if (status != ArenaStatus.INGAME){
				event.setLine(1,ChatColor.GREEN.toString() + ChatColor.ITALIC + status.toString());
			}else{
				event.setLine(1, ChatColor.DARK_RED.toString() + ChatColor.ITALIC + status.toString());
			}
			Arena arena = ArenaManager.getArena(i);
			event.setLine(2, ArenaManager.getArena(i).getPlayerLength()+"/" + arena.numPlayers + " Players");
			event.setLine(3, "Map: " + ArenaManager.getArena(i).getName());
		}
		if (event.getLine(0).contains("random")){
			event.setLine(0, ChatColor.BLUE.toString() + "[SW]");
			event.setLine(2,  ChatColor.BOLD + " Join Random");
		}
	}

	@EventHandler
	public void onChestOpen(PlayerInteractEvent event){
		Player p = event.getPlayer();
		try{
			Material blockType = event.getClickedBlock().getType();
			if (blockType == Material.FURNACE || blockType == Material.ANVIL){
				event.setCancelled(true);
				return;
			}
			if (blockType == Material.CHEST && event.getAction() == Action.RIGHT_CLICK_BLOCK){
				for (int id = 1; main.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
					Arena arena = ArenaManager.getArena(id);
					if (arena.hasPlayer(p.getName()) && arena.getStatus() != ArenaStatus.INGAME){
						p.sendMessage(ChatColor.RED + "You cannot open chests yet you silly goose!");
						event.setCancelled(true);
					}else if (arena.hasPlayer(p.getName()) && arena.getStatus() == ArenaStatus.INGAME){
						if (event.getClickedBlock().getState() instanceof Chest){
							Chest chest = (Chest)event.getClickedBlock().getState();

							String world = event.getClickedBlock().getLocation().getWorld().getName();						
							int x = event.getClickedBlock().getLocation().getBlockX();
							int y = event.getClickedBlock().getLocation().getBlockY();
							int z = event.getClickedBlock().getLocation().getBlockZ();

							if (!arena.openedChests.contains(new Location(Bukkit.getWorld(world),x,y,z))){
								arena.openedChests.add(new Location(Bukkit.getWorld(world),x,y,z));
								chest.getInventory().clear();
								int rand1 = (int)(Math.random()*9), rand2 = (int)(Math.random()*9+9), rand3 = (int)(Math.random()*9+18);
								ItemStack i1 = Methods.getItem();
								ItemStack i2 = Methods.getItem();
								ItemStack i3 = Methods.getItem();
								while (i1.getType() == i2.getType()) i2 = Methods.getItem();
								while (i1.getType() == i3.getType() || i2.getType() == i3.getType()) i3 = Methods.getItem();
								chest.getInventory().setItem(rand1, i1);
								chest.getInventory().setItem(rand2, i2);
								chest.getInventory().setItem(rand3, i3);
							}
						}
					} else{
						//nothing
					}
				}
			}
		}catch (NullPointerException ex){
			//did not click a chest
		}
	}

	@EventHandler
	public void onSignInteract(PlayerInteractEvent event){
		Player p = event.getPlayer();
		Location l = p.getLocation();
		try{
			Material blockType = event.getClickedBlock().getType();
			if (blockType == Material.SIGN || blockType == Material.SIGN_POST || blockType == Material.WALL_SIGN){
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
					try{
						Sign sign = (Sign) event.getClickedBlock().getState();

						if (sign.getLine(0).contains("CLICK ME")){

							boolean k = false;

							String pk1w = main.getConfig().getString("~Parkour.1.world");
							int pk1x = main.getConfig().getInt("~Parkour.1.x");
							int pk1y = main.getConfig().getInt("~Parkour.1.y");
							int pk1z = main.getConfig().getInt("~Parkour.1.z");
							if (new Location(l.getWorld(),l.getBlockX(),l.getBlockY(),l.getBlockZ()).equals(new Location(Bukkit.getWorld(pk1w),pk1x,pk1y,pk1z))){
								Methods.toSpawn(event.getPlayer());				
								if (main.getConfig().getInt(p.getUniqueId() + ".pk1") == 0){
									Methods.addCredits(5, p);
									main.getConfig().set(p.getUniqueId() + ".pk1", 1);
								}else{
									p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You have already completed this parkour.");
								}
								event.setCancelled(true);
								k = true;
							}
							String pk2w = main.getConfig().getString("~Parkour.2.world");
							int pk2x = main.getConfig().getInt("~Parkour.2.x");
							int pk2y = main.getConfig().getInt("~Parkour.2.y");
							int pk2z = main.getConfig().getInt("~Parkour.2.z");
							if (new Location(l.getWorld(),l.getBlockX(),l.getBlockY(),l.getBlockZ()).equals(new Location(Bukkit.getWorld(pk2w),pk2x,pk2y,pk2z))){
								Methods.toSpawn(event.getPlayer());
								if (main.getConfig().getInt(p.getUniqueId() + ".pk2") == 0){
									Methods.addCredits(10, p);
									main.getConfig().set(p.getUniqueId() + ".pk2", 1);
								}else{
									p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You have already completed this parkour.");
								}
								event.setCancelled(true);
								k = true;
							}
							String pk3w = main.getConfig().getString("~Parkour.3.world");
							int pk3x = main.getConfig().getInt("~Parkour.3.x");
							int pk3y = main.getConfig().getInt("~Parkour.3.y");
							int pk3z = main.getConfig().getInt("~Parkour.3.z");
							if (new Location(l.getWorld(),l.getBlockX(),l.getBlockY(),l.getBlockZ()).equals(new Location(Bukkit.getWorld(pk3w),pk3x,pk3y,pk3z))){
								Methods.toSpawn(event.getPlayer());
								if (main.getConfig().getInt(p.getUniqueId() + ".pk3") == 0){
									Methods.addCredits(20, p);
									main.getConfig().set( p.getUniqueId() + ".pk3", 1);
								}else{
									p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You have already completed this parkour.");
								}
								event.setCancelled(true);
								k = true;
							}
							String pk4w = main.getConfig().getString("~Parkour.4.world");
							int pk4x = main.getConfig().getInt("~Parkour.4.x");
							int pk4y = main.getConfig().getInt("~Parkour.4.y");
							int pk4z = main.getConfig().getInt("~Parkour.4.z");
							if (new Location(l.getWorld(),l.getBlockX(),l.getBlockY(),l.getBlockZ()).equals(new Location(Bukkit.getWorld(pk4w),pk4x,pk4y,pk4z))){
								Methods.toSpawn(event.getPlayer());
								if (main.getConfig().getInt(p.getUniqueId() + ".pk4") == 0){
									Methods.addCredits(40, p);
									main.getConfig().set(p.getUniqueId() + ".pk4", 1);
								}else{
									p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You have already completed this parkour.");
								}
								event.setCancelled(true);
								k = true;
							}

							if (k == false) p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You must be on the finishing block!");

							event.setCancelled(true);
						}

						if (sign.getLine(2).contains("Join Random")){
							ArrayList<Integer> array = new ArrayList<Integer>();
							for (int id = 1; EventListener.main.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
								Arena arena = ArenaManager.getArena(id);
								if (arena.getStatus() == ArenaStatus.WAITING || arena.getStatus() == ArenaStatus.STARTING){
									array.add(id);
								}
							}
							if (array.size() > 0){
								Collections.shuffle(array);
								int i = array.get(0);
								Arena arena = ArenaManager.getArena(i);
								if ((!(arena.getStatus() == ArenaStatus.INGAME)) && arena.players.size() < arena.numPlayers){
									arena.addPlayer(p.getName());
								}else{
									p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Sorry, this arena is full or in game. Try another arena!");
								}
							}else{
								p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Sorry, all arenas are full.");
							}
						}

						String s = sign.getLine(0);
						s = ChatColor.stripColor(s);
						s = s.replaceAll("]","");
						s = s.substring(4);
						Arena arena = ArenaManager.getArena(Integer.valueOf(s));
						if ((!(arena.getStatus() == ArenaStatus.INGAME)) && arena.players.size() < arena.numPlayers){
							arena.addPlayer(p.getName());
						}else{
							p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Sorry, this arena is full or in game. Try another arena!");
						}
						event.setCancelled(true);

					}catch (NumberFormatException | StringIndexOutOfBoundsException e){}
				}
			}
		}catch (NullPointerException ex){
			//did not click a sign
		}
	}

	@EventHandler
	public void onFeatherClick(PlayerInteractEvent event){
		Player p = event.getPlayer();

		if (p.getWorld().getName().equalsIgnoreCase("Blank")){
			try{
				Material blockType = event.getItem().getType();
				if (blockType == Material.FEATHER){
					if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
						p.openInventory(Methods.getKitSelector(event.getPlayer()));
					}
				}
				if (blockType == Material.PAPER){
					if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
						Methods.getStats(p);
					}
				}
				/*
				if (blockType == Material.SLIME_BALL){
					if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
						p.openInventory(Methods.getUpgradeSelector(event.getPlayer()));
					}
				}
				 */
				if (blockType == Material.NETHER_STAR){
					if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
						p.openInventory(Methods.getTrailSelector(event.getPlayer()));
					}
				}
				if (blockType == Material.NAME_TAG){
					if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
						p.sendMessage(ChatColor.DARK_PURPLE + "Vote at all 3 links for 30 credits!");
						p.sendMessage(ChatColor.BLUE + "Vote 1: " + ChatColor.WHITE + "http://bit.ly/2mcxnnn");
						p.sendMessage(ChatColor.BLUE + "Vote 2: " + ChatColor.WHITE + "http://bit.ly/2nOztpw");
						p.sendMessage(ChatColor.BLUE + "Vote 3: " + ChatColor.WHITE + "http://bit.ly/2niQGev");
					}
				}
			}catch (NullPointerException ex){
				//was not a feather
			}
		}

		try{
			if (event.getClickedBlock().getType() == Material.TRAP_DOOR && !p.isOp()) event.setCancelled(true);
		}catch (NullPointerException e2){}
	}

	@EventHandler
	public void inKitSelector(final InventoryClickEvent event){
		if (event.getWhoClicked() instanceof Player){
			final Player p = (Player) event.getWhoClicked();
			try{
				if (event.getInventory().getName().equalsIgnoreCase(ChatColor.AQUA + "Kit Selector")){
					if (event.getSlot() == 4){
						event.setCancelled(true);
						return;
					}

					if (event.getCurrentItem().getType() == Material.APPLE && ArenaManager.getKit(p) != KitType.APPLER.toString()){
						ArenaManager.setKit(p, KitType.APPLER);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
					}
					else if (event.getCurrentItem().getType() == Material.APPLE && ArenaManager.getKit(p) == KitType.APPLER.toString()){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this kit!");
					}
					else if (event.getCurrentItem().getType() == Material.HAY_BLOCK && ArenaManager.getKit(p) != KitType.BOXER.toString()){
						ArenaManager.setKit(p, KitType.BOXER);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
					}
					else if (event.getCurrentItem().getType() == Material.HAY_BLOCK && ArenaManager.getKit(p) == KitType.BOXER.toString()){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this kit!");
					}
					else if (event.getCurrentItem().getType() == Material.IRON_CHESTPLATE && ArenaManager.getKit(p) != KitType.RUGGED.toString() && Methods.hasKit(3, p) == true){
						ArenaManager.setKit(p, KitType.RUGGED);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
					}
					else if (event.getCurrentItem().getType() == Material.IRON_CHESTPLATE && ArenaManager.getKit(p) != KitType.RUGGED.toString() && Methods.hasKit(3, p) == false){
						p.closeInventory();
						new BukkitRunnable(){
							@Override
							public void run() {
								p.openInventory(Methods.getKit3(p));			
							}
						}.runTaskLater(main, 5L);
						return;
					}
					else if (event.getCurrentItem().getType() == Material.IRON_CHESTPLATE && ArenaManager.getKit(p) == KitType.RUGGED.toString()){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this kit!");
					}
					/*
					else if (event.getCurrentItem().getType() == Material.SUGAR && ArenaManager.getKit(p) != KitType.AGILE.toString() && Methods.hasKit(2, p) == true){
						ArenaManager.setKit(p, KitType.AGILE);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
					}
					else if (event.getCurrentItem().getType() == Material.SUGAR && ArenaManager.getKit(p) != KitType.AGILE.toString() && Methods.hasKit(2, p) == false){
						p.closeInventory();
						new BukkitRunnable(){
							@Override
							public void run() {
								p.openInventory(Methods.getKit2(p));			
							}
						}.runTaskLater(main, 5L);
						return;
					}
					else if (event.getCurrentItem().getType() == Material.SUGAR && ArenaManager.getKit(p) == KitType.AGILE.toString()){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this kit!");
					}
					 */
					else if (event.getCurrentItem().getType() == Material.COOKED_BEEF && ArenaManager.getKit(p) != KitType.CANNIBAL.toString() && Methods.isVIP(p)){
						ArenaManager.setKit(p, KitType.CANNIBAL);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
					}
					else if (event.getCurrentItem().getType() == Material.COOKED_BEEF && ArenaManager.getKit(p) != KitType.CANNIBAL.toString() && !Methods.isVIP(p)){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have permission to use this kit.");
					}
					else if (event.getCurrentItem().getType() == Material.COOKED_BEEF && ArenaManager.getKit(p) == KitType.CANNIBAL.toString()){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this kit!");
					}
					else if (event.getCurrentItem().getType() == Material.ENDER_PEARL && ArenaManager.getKit(p) != KitType.ESCAPIST.toString() && Methods.isVIP(p)){
						ArenaManager.setKit(p, KitType.ESCAPIST);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
					}
					else if (event.getCurrentItem().getType() == Material.ENDER_PEARL && ArenaManager.getKit(p) != KitType.ESCAPIST.toString() && !Methods.isVIP(p)){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have permission to use this kit.");
					}
					else if (event.getCurrentItem().getType() == Material.ENDER_PEARL && ArenaManager.getKit(p) == KitType.ESCAPIST.toString()){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this kit!");
					}
					else if (event.getCurrentItem().getType() == Material.COMPASS && ArenaManager.getKit(p) != KitType.TRACKER.toString() && Methods.isVIP(p)){
						ArenaManager.setKit(p, KitType.TRACKER);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
					}
					else if (event.getCurrentItem().getType() == Material.COMPASS && ArenaManager.getKit(p) != KitType.TRACKER.toString() && !Methods.isVIP(p)){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have permission to use this kit.");
					}
					else if (event.getCurrentItem().getType() == Material.COMPASS && ArenaManager.getKit(p) == KitType.TRACKER.toString()){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this kit!");
					}
					else if (event.getCurrentItem().getType() == Material.EXP_BOTTLE && ArenaManager.getKit(p) != KitType.ENCHANTER.toString() && Methods.isVIP(p)){
						ArenaManager.setKit(p, KitType.ENCHANTER);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
					}
					else if (event.getCurrentItem().getType() == Material.EXP_BOTTLE && ArenaManager.getKit(p) != KitType.ENCHANTER.toString() && !Methods.isVIP(p)){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have permission to use this kit.");
					}
					else if (event.getCurrentItem().getType() == Material.EXP_BOTTLE && ArenaManager.getKit(p) == KitType.ENCHANTER.toString()){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this kit!");
					}
					else if (event.getCurrentItem().getType() == Material.GOLD_NUGGET && ArenaManager.getKit(p) != KitType.ANGEL.toString() && Methods.isMVP(p)){
						ArenaManager.setKit(p, KitType.ANGEL);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
					}
					else if (event.getCurrentItem().getType() == Material.GOLD_NUGGET && ArenaManager.getKit(p) != KitType.ANGEL.toString() && !Methods.isMVP(p)){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have permission to use this kit.");
					}
					else if (event.getCurrentItem().getType() == Material.GOLD_NUGGET && ArenaManager.getKit(p) == KitType.ANGEL.toString()){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this kit!");
					}
					else if (event.getCurrentItem().getType() == Material.ANVIL && ArenaManager.getKit(p) != KitType.BRUTE.toString() && Methods.isMVP(p)){
						ArenaManager.setKit(p, KitType.BRUTE);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
					}
					else if (event.getCurrentItem().getType() == Material.ANVIL && ArenaManager.getKit(p) != KitType.BRUTE.toString() && !Methods.isMVP(p)){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have permission to use this kit.");
					}
					else if (event.getCurrentItem().getType() == Material.ANVIL && ArenaManager.getKit(p) == KitType.BRUTE.toString()){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this kit!");
					}
					else if (event.getCurrentItem().getType() == Material.ARROW && ArenaManager.getKit(p) != KitType.SHARPSHOOTER.toString() && Methods.isMVP(p)){
						ArenaManager.setKit(p, KitType.SHARPSHOOTER);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
					}
					else if (event.getCurrentItem().getType() == Material.ARROW && ArenaManager.getKit(p) != KitType.SHARPSHOOTER.toString() && !Methods.isMVP(p)){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have permission to use this kit.");
					}
					else if (event.getCurrentItem().getType() == Material.ARROW && ArenaManager.getKit(p) == KitType.SHARPSHOOTER.toString()){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this kit!");
					}
					else if (event.getCurrentItem().getType() == Material.IRON_AXE && ArenaManager.getKit(p) != KitType.SAVAGE.toString() && Methods.isMVP(p)){
						ArenaManager.setKit(p, KitType.SAVAGE);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
					}
					else if (event.getCurrentItem().getType() == Material.IRON_AXE && ArenaManager.getKit(p) != KitType.SAVAGE.toString() && !Methods.isMVP(p)){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have permission to use this kit.");
					}
					else if (event.getCurrentItem().getType() == Material.IRON_AXE && ArenaManager.getKit(p) == KitType.SAVAGE.toString()){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this kit!");
					}
					/*
					else if (event.getCurrentItem().getType() == Material.COOKED_CHICKEN && ArenaManager.getKit(p) != KitType.RANCHER.toString() && Methods.hasKit(1, p) == true){
						ArenaManager.setKit(p, KitType.RANCHER);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
					}
					else if (event.getCurrentItem().getType() == Material.COOKED_CHICKEN && ArenaManager.getKit(p) != KitType.RANCHER.toString() && Methods.hasKit(1, p) == false){
						p.closeInventory();
						new BukkitRunnable(){
							@Override
							public void run() {
								p.openInventory(Methods.getKit1(p));			
							}
						}.runTaskLater(main, 5L);
						return;
					}
					else if (event.getCurrentItem().getType() == Material.COOKED_CHICKEN && ArenaManager.getKit(p) == KitType.RANCHER.toString()){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this kit!");
					}
					else if (event.getCurrentItem().getType() == Material.QUARTZ && ArenaManager.getKit(p) != KitType.RACER.toString() && Methods.hasKit(4, p) == true){
						ArenaManager.setKit(p, KitType.RACER);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
					}
					else if (event.getCurrentItem().getType() == Material.QUARTZ && ArenaManager.getKit(p) != KitType.RACER.toString() && Methods.hasKit(4, p) == false){
						p.closeInventory();
						new BukkitRunnable(){
							@Override
							public void run() {
								p.openInventory(Methods.getKit4(p));			
							}
						}.runTaskLater(main, 5L);
						return;
					}
					else if (event.getCurrentItem().getType() == Material.QUARTZ && ArenaManager.getKit(p) == KitType.RACER.toString()){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this kit!");
					}
					 */
					else if (event.getCurrentItem().getType() == Material.BOW && ArenaManager.getKit(p) != KitType.RANGER.toString() && Methods.hasKit(5, p) == true){
						ArenaManager.setKit(p, KitType.RANGER);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
					}
					else if (event.getCurrentItem().getType() == Material.BOW && ArenaManager.getKit(p) != KitType.RANGER.toString() && Methods.hasKit(5, p) == false){
						p.closeInventory();
						new BukkitRunnable(){
							@Override
							public void run() {
								p.openInventory(Methods.getKit5(p));			
							}
						}.runTaskLater(main, 5L);
						return;
					}
					else if (event.getCurrentItem().getType() == Material.BOW && ArenaManager.getKit(p) == KitType.RANGER.toString()){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this kit!");
					}
					p.closeInventory();
					p.openInventory(Methods.getKitSelector(p));
					event.setCancelled(true);
				}

				/*
				if (event.getInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "Upgrades")){
					if (event.getSlot() == 4){
						event.setCancelled(true);
						return;
					}

					p.sendMessage(ChatColor.RED + "Upgrades are coming soon!");
					p.closeInventory();
					p.openInventory(Methods.getUpgradeSelector(p));
					event.setCancelled(true);
				}
				 */

				if (event.getInventory().getName().equalsIgnoreCase(ChatColor.GREEN + "Trails")){
					if (event.getSlot() == 4){
						event.setCancelled(true);
						return;
					}

					if (event.getCurrentItem().getType() == Material.CLAY_BALL && PEManager.getKit(p.getName()).equals("None Selected")){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have an active trail!");
					}
					else if (event.getCurrentItem().getType() == Material.CLAY_BALL && !PEManager.getKit(p.getName()).equals("None Selected")){
						PEManager.clear(p.getName());
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Trail cleared.");
					}

					else if (event.getCurrentItem().getType() == Material.ENCHANTED_BOOK && !PEManager.enchant.contains(p.getName()) && Methods.isVIP(p)){
						PEManager.clear(p.getName());
						PEManager.enchant.add(p.getName());
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the trail - Enchant");
					}
					else if (event.getCurrentItem().getType() == Material.ENCHANTED_BOOK && !PEManager.enchant.contains(p.getName()) && !Methods.isVIP(p)){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have permission to use this trail.");
					}
					else if (event.getCurrentItem().getType() == Material.ENCHANTED_BOOK && PEManager.enchant.contains(p.getName())){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this trail!");
					}

					else if (event.getCurrentItem().getType() == Material.BLAZE_POWDER && !PEManager.flame.contains(p.getName()) && Methods.isMVP(p)){
						PEManager.clear(p.getName());
						PEManager.flame.add(p.getName());
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the trail - Flame");
					}
					else if (event.getCurrentItem().getType() == Material.BLAZE_POWDER && !PEManager.flame.contains(p.getName()) && !Methods.isMVP(p)){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have permission to use this trail.");
					}
					else if (event.getCurrentItem().getType() == Material.BLAZE_POWDER && PEManager.flame.contains(p.getName())){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this trail!");
					}

					else if (event.getCurrentItem().getType() == Material.ENDER_PORTAL_FRAME && !PEManager.portal.contains(p.getName()) && Methods.isMVP(p)){
						PEManager.clear(p.getName());
						PEManager.portal.add(p.getName());
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the trail - Portal");
					}
					else if (event.getCurrentItem().getType() == Material.ENDER_PORTAL_FRAME && !PEManager.portal.contains(p.getName()) && !Methods.isMVP(p)){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have permission to use this trail.");
					}
					else if (event.getCurrentItem().getType() == Material.ENDER_PORTAL_FRAME && PEManager.portal.contains(p.getName())){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this trail!");
					}

					else if (event.getCurrentItem().getType() == Material.FLINT_AND_STEEL && !PEManager.smoke.contains(p.getName()) && Methods.isVIP(p)){
						PEManager.clear(p.getName());
						PEManager.smoke.add(p.getName());
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the trail - Smoke");
					}
					else if (event.getCurrentItem().getType() == Material.FLINT_AND_STEEL && !PEManager.smoke.contains(p.getName()) && !Methods.isVIP(p)){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have permission to use this trail.");
					}
					else if (event.getCurrentItem().getType() == Material.FLINT_AND_STEEL && PEManager.smoke.contains(p.getName())){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this trail!");
					}

					else if (event.getCurrentItem().getType() == Material.STRING && !PEManager.crit.contains(p.getName()) && Methods.isVIP(p)){
						PEManager.clear(p.getName());
						PEManager.crit.add(p.getName());
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the trail - Crit");
					}
					else if (event.getCurrentItem().getType() == Material.STRING && !PEManager.crit.contains(p.getName()) && !Methods.isVIP(p)){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have permission to use this trail.");
					}
					else if (event.getCurrentItem().getType() == Material.STRING && PEManager.crit.contains(p.getName())){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this trail!");
					}

					else if (event.getCurrentItem().getType() == Material.REDSTONE && !PEManager.redstone.contains(p.getName()) && Methods.isMVP(p)){
						PEManager.clear(p.getName());
						PEManager.redstone.add(p.getName());
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the trail - Redstone");
					}
					else if (event.getCurrentItem().getType() == Material.REDSTONE && !PEManager.redstone.contains(p.getName()) && !Methods.isMVP(p)){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have permission to use this trail.");
					}
					else if (event.getCurrentItem().getType() == Material.REDSTONE && PEManager.redstone.contains(p.getName())){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You are already using this trail!");
					}

					p.closeInventory();
					p.openInventory(Methods.getTrailSelector(p));
					event.setCancelled(true);
				}
				/*
				if (event.getInventory().getName().equalsIgnoreCase(ChatColor.AQUA + "Rancher Kit - 100 Coins")){
					if (event.getCurrentItem().getType() == Material.EMERALD && Integer.valueOf(Methods.getCredits(p)) >= 100){
						main.getConfig().set(p.getUniqueId() + ".kit1", true);
						ArenaManager.setKit(p, KitType.RANCHER);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
						Methods.removeCredits(100, p);
						p.closeInventory();
						return;
					}
					else if (event.getCurrentItem().getType() == Material.EMERALD && Integer.valueOf(Methods.getCredits(p)) < 100){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have enough credits! (100)");
						p.closeInventory();
						return;
					}
					else if (event.getCurrentItem().getType() == Material.REDSTONE){
						p.closeInventory();
						return;
					}
					p.closeInventory();
					p.openInventory(Methods.getKit1(p));
					event.setCancelled(true);
				}
				 */
				/*
				if (event.getInventory().getName().equalsIgnoreCase(ChatColor.AQUA + "Agile Kit - 200 Coins")){
					if (event.getCurrentItem().getType() == Material.EMERALD && Integer.valueOf(Methods.getCredits(p)) >= 200){
						main.getConfig().set(p.getUniqueId() + ".kit2", true);
						ArenaManager.setKit(p, KitType.AGILE);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
						Methods.removeCredits(200, p);
						p.closeInventory();
						return;
					}
					else if (event.getCurrentItem().getType() == Material.EMERALD && Integer.valueOf(Methods.getCredits(p)) < 200){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have enough credits! (200)");
						p.closeInventory();
						return;
					}
					else if (event.getCurrentItem().getType() == Material.REDSTONE){
						p.closeInventory();
						return;
					}
					p.closeInventory();
					p.openInventory(Methods.getKit2(p));
					event.setCancelled(true);
				}
				 */
				if (event.getInventory().getName().equalsIgnoreCase(ChatColor.AQUA + "Rugged Kit - 300 Coins")){
					if (event.getCurrentItem().getType() == Material.EMERALD && Integer.valueOf(Methods.getCredits(p)) >= 300){
						main.getConfig().set(p.getUniqueId() + ".kit3", true);
						ArenaManager.setKit(p, KitType.RUGGED);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
						Methods.removeCredits(300, p);
						p.closeInventory();
						return;
					}
					else if (event.getCurrentItem().getType() == Material.EMERALD && Integer.valueOf(Methods.getCredits(p)) < 300){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have enough credits! (300)");
						p.closeInventory();
						return;
					}
					else if (event.getCurrentItem().getType() == Material.REDSTONE){
						p.closeInventory();
						return;
					}
					p.closeInventory();
					p.openInventory(Methods.getKit3(p));
					event.setCancelled(true);
				}
				/*
				if (event.getInventory().getName().equalsIgnoreCase(ChatColor.AQUA + "Racer Kit - 500 Coins")){
					if (event.getCurrentItem().getType() == Material.EMERALD && Integer.valueOf(Methods.getCredits(p)) >= 500){
						main.getConfig().set(p.getUniqueId() + ".kit4", true);
						ArenaManager.setKit(p, KitType.RACER);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
						Methods.removeCredits(500, p);
						p.closeInventory();
						return;
					}
					else if (event.getCurrentItem().getType() == Material.EMERALD && Integer.valueOf(Methods.getCredits(p)) < 500){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have enough credits! (500)");
						p.closeInventory();
						return;
					}
					else if (event.getCurrentItem().getType() == Material.REDSTONE){
						p.closeInventory();
						return;
					}
					p.closeInventory();
					p.openInventory(Methods.getKit4(p));
					event.setCancelled(true);
				}
				 */
				if (event.getInventory().getName().equalsIgnoreCase(ChatColor.AQUA + "Ranger Kit - 1000 Coins")){
					if (event.getCurrentItem().getType() == Material.EMERALD && Integer.valueOf(Methods.getCredits(p)) >= 1000){
						main.getConfig().set(p.getUniqueId() + ".kit5", true);
						ArenaManager.setKit(p, KitType.RANGER);
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "You are now using the kit - " + ArenaManager.getKit(p));
						Methods.removeCredits(1000, p);
						p.closeInventory();
						return;
					}
					else if (event.getCurrentItem().getType() == Material.EMERALD && Integer.valueOf(Methods.getCredits(p)) < 1000){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have enough credits! (1000)");
						p.closeInventory();
						return;
					}
					else if (event.getCurrentItem().getType() == Material.REDSTONE){
						p.closeInventory();
						return;
					}
					p.closeInventory();
					p.openInventory(Methods.getKit5(p));
					event.setCancelled(true);
				}

				if (event.getInventory().getName().equalsIgnoreCase(ChatColor.BLUE + "Click to purchase")){
					if (p.getInventory().firstEmpty() == -1){
						p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "Please provide room in your inventory.");
						p.closeInventory();
						return;
					}
					else if (event.getRawSlot() > 17){
						p.closeInventory();
						event.setCancelled(true);
						return;
					}
					else{
						if (Methods.hasCredits(p, event.getCurrentItem().getType())){
							ItemStack i = new ItemStack(event.getCurrentItem().getType(), event.getCurrentItem().getAmount());
							p.getWorld().dropItem(p.getLocation().add(0, 10, 0), i);
							p.sendMessage(Methods.getDefaultPrefix() + ChatColor.GREEN + "Dropping item...");
							String s = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
							s = s.substring(0, s.indexOf(" "));
							Methods.removeCredits(Integer.valueOf(s), p);
						}else{
							if (event.getCurrentItem().getType() != Material.AIR) p.sendMessage(Methods.getDefaultPrefix() + ChatColor.RED + "You do not have enough credits!");
							p.closeInventory();
							return;
						}
					}

					p.closeInventory();
					p.openInventory(Methods.getGetInventory(p));
					event.setCancelled(true);
				}			

				if (p.getWorld().getName().equals("Blank")){
					if (!ArenaManager.buildMode.contains(p.getName())){
						event.setCancelled(true);
					}
					else return;
				}
			}catch (IndexOutOfBoundsException | NullPointerException ex){
				//not kit selector
			}
		}
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent event){
		boolean hasArena = false;
		for (int id = 1; main.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
			if (ArenaManager.getArena(id).players.contains(event.getPlayer().getUniqueId())){
				hasArena = true;
			}
		}
		if (hasArena == false){
			event.setCancelled(true);
		}		
	}

	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event){
		event.setCancelled(true);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event){
		boolean hasArena = false;
		for (int id = 1; main.getConfig().getString("~Arenas." + String.valueOf(id)) != null; id++){
			if (ArenaManager.getArena(id).players.contains(event.getPlayer().getUniqueId())){
				hasArena = true;
			}
		}
		if (hasArena == false){
			event.setCancelled(true);

		}		
	}
}

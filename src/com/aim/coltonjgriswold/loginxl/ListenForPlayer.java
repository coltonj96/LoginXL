package com.aim.coltonjgriswold.loginxl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenForPlayer implements Listener {

	private final JavaPlugin plugin;
	public static File dataDir;
	public final Logger log = Logger.getLogger("Minecraft");
	public BlockPlaceEvent signcheck;
	public SignChangeEvent signchange;
	public String number;
	public String deathlocation;
	public String deathcause;
	public String line;
	public String time;
	public String date;
	public ChatColor a = ChatColor.AQUA;
	public ChatColor b = ChatColor.DARK_PURPLE;
	public ChatColor c = ChatColor.BOLD;
	public ChatColor d = ChatColor.GOLD;
	public ChatColor e = ChatColor.RESET;
	public ChatColor ff = ChatColor.DARK_RED;
	public static Integer x;
	public static Integer y;
	public static Integer z;
	
	public ListenForPlayer(final JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoinLogin(PlayerJoinEvent event) {
		// Define our variables!
		Player player = event.getPlayer();
		// Update checking!
		if (plugin.getConfig().getBoolean("settings.update-check") && player.isOp() && needsUpdate()) {
			player.sendMessage(a + "[" + b + c + "LoginXL" + a + "] " + e + ff + c + "WARNING" + d + " This Version Of LoginXL Is Out Of Date!");
			player.sendMessage(a + "[" + b + c + "LoginXL" + a + "] " + e + "Update at: " + d + "http://bit.ly/loginxl");
		}

		// Debugging!
		if (debuggling()) {
			log("Debugging: A player joined the game.");
		}

		// Define the amount of players who have joined in total.
		try {
		File f = new File(plugin.getConfig().getString("settings.main-world-name") + "/players/");
		int count = 0;
		for (File file : f.listFiles()) {
			if (file.isFile()) {
				count++;
				number = "" + count;
			}
		}
		} catch (NullPointerException ex) {
			log.severe(a+"["+b+c+"LoginXL"+a+"]"+ff+c+" WARNING"+e+" "+"The 'main-world-name' Is Set Incorrectly Or player.dat Files Cannot Be Found!");
			log.severe(a+"["+b+c+"LoginXL"+a+"]"+e+" Parts Of The Plugin May Not Work Correctly");
			log.severe(a+"["+b+c+"LoginXL"+a+"]"+e+" Set The World Spawn Using "+a+c+"/loginxl setspawn");
		}

		// Check if it's their first join.
		File file = new File(plugin.getConfig().getString("settings.main-world-name") + "/players/" + player.getName() + ".dat");
		boolean exists = file.exists();
		if (!exists) {
			// Show the first join message.
			if (plugin.getConfig().getBoolean("settings.show-first-join-message")) {
				String join = (format(plugin.getConfig().getString("messages.first-join-message"), player));
				event.setJoinMessage(join);
			}

			// Show the number of players who have joined in total.
			if (plugin.getConfig().getBoolean("settings.number-on-first-join")) {
				plugin.getServer().broadcastMessage(format(plugin.getConfig().getString("messages.number-message"), player));
			}

			// Give a player an item on their first join.
			if (plugin.getConfig().getBoolean("settings.item-on-first-join")) {
				if (debuggling()) {
					log("Debugging: Attempting to give a player an item...");
				}
				List<String> items = plugin.getConfig().getStringList("items");
				for (String itemStr : items) {
					PlayerInventory inventory = player.getInventory();
					Integer itemtogive = Integer.parseInt(itemStr.split("\\.")[0]);
					Integer amount = Integer.parseInt(itemStr.split("\\.")[1]);
					Byte data = Byte.parseByte(itemStr.split("\\.")[2]);
					ItemStack istack = new ItemStack(itemtogive, amount,
							(short) 0, (byte) data);
					inventory.addItem(istack);
					if (debuggling()) {
						log("Debugging: Gave the player the item.");
					}
				}

			}

			// Show the first join MOTD.
			if (plugin.getConfig().getBoolean("settings.show-first-join-motd")) {
				List<String> motd = plugin.getConfig().getStringList("motd");
				for (String motdStr : motd) {
					player.sendMessage(format(motdStr, player));
					if (debuggling()) {
						log("Debugging: Showing the first join MOTD.");
					}
				}
			}

			// Teleport the player to the first join spawnpoint.
			if (plugin.getConfig().getBoolean("settings.first-join-spawning")) {
				if (debuggling()) {
					log("Debugging: First join spawning enabled - teleporting the player to spawnpoint.");
				}
				final Player p = event.getPlayer();
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						teleportToFirstSpawn(p); // Delayed task to teleport them 2 ticks after any other plugins.
					}
				}, 2L);
				if (debuggling()) {
					log("Debugging: Successfully teleported.");
				}
			}

			// Show some fancy smoke!
			if (plugin.getConfig().getBoolean("settings.show-first-join-smoke")) {
				if (debuggling()) {
					log("Debugging: Someone is 'smokin on their join!");
				}
				for (int i = 0; i <= 18; i++)
					player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, i);
			}

			// Run some commands!
			if (plugin.getConfig().getBoolean("settings.commands-on-first-join")) {
				if (debuggling()) {
					log("Debugging: Running some commands.");
				}
				List<String> commands = plugin.getConfig().getStringList("commands");
				for (String command : commands) {
					player.performCommand(command);
				}
			}

		} else {
			if (plugin.getConfig().getBoolean("settings.show-join-message")) {
				if (!onlyfirstjoin()) {
					event.setJoinMessage(format(plugin.getConfig().getString("messages.join-message"), player));
				}
			} else {
				event.setJoinMessage(null);
			}
			if (plugin.getConfig().getBoolean("settings.number-on-join")) {
				Bukkit.getServer().broadcastMessage(format(plugin.getConfig().getString("messages.number-message"), player));
			}
		}
	}

	// Change quit message.
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (!onlyfirstjoin()) {
			Player player = event.getPlayer();
			if (plugin.getConfig().getBoolean("settings.show-leave-message")) {
				event.setQuitMessage(format(plugin.getConfig().getString("messages.leave-message"), player));
			} else {
				event.setQuitMessage(null);
			}
			if (debuggling()) {
				log("Debugging: A player quit the game.");
			}
		}
	}
	//Change death message
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!onlyfirstjoin()) {
			Player player = event.getEntity().getPlayer();
			if (plugin.getConfig().getBoolean("settings.show-death-message")) {
				int x = player.getLocation().getBlockX();
				int y = player.getLocation().getBlockY();
				int z = player.getLocation().getBlockZ();
				String cause = player.getLastDamageCause().getCause().toString();
				deathcause = cause;
				deathlocation = a+" X: "+ff+x+a+" Y: "+ff+y+a+ " Z: "+ff+z+e;
				event.setDeathMessage(format(plugin.getConfig().getString("messages.death-message"), player));
			} else {
				event.setDeathMessage(null);
			}
			if (debuggling()) {
				log("Debugging: A player died in-game.");
			}
		}
	}
	@EventHandler
	public void onPlayer(PlayerRespawnEvent event) {
		if (!onlyfirstjoin()) {
			Player player = event.getPlayer();
			if (plugin.getConfig().getBoolean("settings.show-respawn-message")) {
				plugin.getServer().broadcastMessage(format(plugin.getConfig().getString("messages.respawn-message"), player));
			} else {
				event.getPlayer().sendMessage("You Have Respawned");
			}
			if (debuggling()) {
				log("Debugging: A player respawned.");
			}
		}
	}
	// Change kick message.
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		if (!onlyfirstjoin()) {
			Player player = event.getPlayer();
			if (plugin.getConfig().getBoolean("settings.show-kick-message")) {
				event.setLeaveMessage(format(plugin.getConfig().getString("messages.kick-message"), player));
			} else {
				event.setLeaveMessage(null);
			}
			if (debuggling()) {
				log("Debugging: A player was kicked from the game.");
			}
		}
	}
	
	public void log(String l) {
		
		log.info(a + "[" + b + c + "LoginXL" + a + "]" + " " + e + l);
	}

	public boolean debuggling() {
		if (plugin.getConfig().getBoolean("settings.debug")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean onlyfirstjoin() {
		if (plugin.getConfig().getBoolean("settings.only-first-join")) {
			return true;
		} else {
			return false;
		}
	}

	public void teleportToFirstSpawn(Player player) {
		int x = player.getWorld().getSpawnLocation().getBlockX();
		int y = player.getWorld().getSpawnLocation().getBlockY();
		int z = player.getWorld().getSpawnLocation().getBlockZ();
		player.teleport(new Location(player.getWorld(), x, y, z, 0, 0));
	}

	public boolean needsUpdate() {
		String url = CheckForUpdates.fetch("http://www.superbuild.host-ed.me/_version/version.html");
		if(url.equalsIgnoreCase(plugin.getDescription().getVersion())) {
			return false;
		} else {
			return true;
		}
	}
	
	public String date() {
		Date d = new Date();
		SimpleDateFormat form = new SimpleDateFormat("MM/dd/yyyy");
		date = form.format(d);
		return date;
	}
	
	public String time() {
		Date d = new Date();
		SimpleDateFormat form = new SimpleDateFormat("HH:mm");
		time = form.format(d);
		return time;
	}

	public String colorize(String s){
		if(s == null) return null;
		return s.replaceAll("&([l-o0-9a-f])", "\u00A7$1");
	}

	public String format(String string, Player player) {
		String format = string.replace("%name%", player.getDisplayName()).replace("%number%", number).replace("%deathlocation%", deathlocation).replace("%deathcause%", deathcause).replace("%date%", date).replace("%time%", time);
		return colorize(format);
	}
}	
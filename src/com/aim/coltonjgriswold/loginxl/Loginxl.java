package com.aim.coltonjgriswold.loginxl;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.mcstats.MetricsLite;

public class Loginxl extends JavaPlugin {

	public static JavaPlugin plugin;
	public final Logger log = Logger.getLogger("Minecraft");
	public ChatColor a = ChatColor.AQUA;
	public ChatColor b = ChatColor.DARK_PURPLE;
	public ChatColor c = ChatColor.BOLD;
	public ChatColor d = ChatColor.GOLD;
	public ChatColor e = ChatColor.RESET;
	public ChatColor ff = ChatColor.DARK_RED;
	
	public void onDisable(){
		log.info(a + "[" + b + c + "LoginXL" + a + "] " + e + "Version " + getDescription().getVersion() + " by coltonj96 " + "Has Been Disabled!");
	}
	public void onEnable(){
		log.info(a + "[" + b + c + "LoginXL" + a + "] " + e + "Version " + getDescription().getVersion() + " By coltonj96 " + "Has Been Enabled!");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new ListenForPlayer(this), this);
		try {
		    MetricsLite metrics = new MetricsLite(this);
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :-(
		}
		try {
			getConfig().options().copyDefaults(true);
			getConfig().options().header("#LoginXL-v "+ getDescription().getVersion() + " Configuration");
			getConfig().options().copyHeader(true);
			saveConfig();
		} catch (Exception ex) {
			getLogger().log(Level.SEVERE, a + "[" + b + c + "LoginXL" + a + "] " + e + ff + c + "Error, Cannot Load Configuration!!", ex);
		}
	}
	@Override
	public boolean onCommand(CommandSender cs, Command cmnd, String string,String[] args) {
		if (cmnd.getName().equalsIgnoreCase("LoginXL")) {
			if (cs instanceof Player) {
				Player player = (Player) cs;
				if (!cs.isOp()) {
					player.sendMessage(ff + "Sorry, " + player.getDisplayName() + ff + ", You Need To Be An Op To Do That!");
					return true;
				}
				if (args.length < 1 || args.length > 5) {
					cs.sendMessage(d + "Usage:" + b + " /LoginXL " + ff + "<"+ a + "reload"+ ff + "|" + a + "setspawn" + ff + "|" + a + "spawn" + ff + "|" + a + "items" + ff + "|" + a + "motd" + ff + "|" + a + "zoomup" + ff + "|" + a + "zoom (x y z)" + ff + "|" + a + "zap (player)" + ff + "|" + a + "bam (player)" + ff + "|" + a + "troll (player)" + ff + "|" + a + "locate (player)" + ff + "|" + a + "tp <player> (player)" + ff + "|" + a + "xp (player)" + ff + "|" + a + "craft" + ff + "|" + a + "ggm <player>" + ff + "|" + a + "ride" + ff + ">");
					return true;
				}
				if (args[0].equalsIgnoreCase("reload")) {
					this.reloadConfig();
					this.saveConfig();
					cs.sendMessage(d + "Sucessfully Reloaded Config");
					return true;
				}
				if (args[0].equalsIgnoreCase("motd")) {
					String pc = player.getDisplayName();
					List<String> motd = this.getConfig().getStringList("motd");
					for (String motdStr : motd) {
						player.sendMessage(motdStr.replace("%name%", pc).replace("&", "§"));
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("setspawn")) {
					if (!getConfig().getBoolean("settings.first-join-spawning")) {
						cs.sendMessage(d + "Please Enable 'first-join-spawning' In Config");
						return true;
					}
					World world = player.getWorld();
					int x = player.getLocation().getBlockX();
					int y = player.getLocation().getBlockY();
					int z = player.getLocation().getBlockZ();
					world.setSpawnLocation(x, y, z);
					world.save();
				}
				if (args[0].equalsIgnoreCase("items")) {
					if (!getConfig().getBoolean("settings.item-on-first-join")) {
						cs.sendMessage(d + "Please Enable 'item-on-first-join' In Config");
						return true;
					}
					List<String> items = getConfig().getStringList("items");
					for (String itemStr : items) {
						PlayerInventory inventory = player.getInventory();
						Integer itemtogive = Integer.parseInt(itemStr.split("\\.")[0]);
						Integer amount = Integer.parseInt(itemStr.split("\\.")[1]);
						Byte data = Byte.parseByte(itemStr.split("\\.")[2]);
						ItemStack istack = new ItemStack(itemtogive, amount,(short) 0, (byte) data);
						inventory.addItem(istack);
					}
					cs.sendMessage(d + "Sucessfully Gave Items Defined In Config");
					return true;
				}
				if (args[0].equalsIgnoreCase("ride")) {
					if (!getConfig().getBoolean("settings.ride")){
						cs.sendMessage(d + "Please Enable 'ride' In Config");
						return true;
					}if (args.length == 1) {
						Location loc = player.getLocation();
						double y = loc.getBlockY();
						double x = loc.getBlockX();
						double z = loc.getBlockZ();
						World cw = player.getWorld();
						Location a = new Location(cw, x, y, z);
						PigZombie aa = (PigZombie) player.getWorld().spawnEntity(a, EntityType.PIG_ZOMBIE);
						aa.setPassenger(player);
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("zoomup")) {
					if (!getConfig().getBoolean("settings.zoom-up")) {
						cs.sendMessage(d + "Please Enable 'zoom-up' In Config");
						return true;
					}
					if (args.length == 1) {
					double x = 0;
    				double y = 50;
    				double z = 0;
					player.setVelocity(new Vector(x, y, z));
					cs.sendMessage(d + "Successfully Zoomed Up");
					}else if (args.length > 1){
						cs.sendMessage(d + "Too Many Arguements!");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("xp")) {
					if (!getConfig().getBoolean("settings.xp")) {
						cs.sendMessage(d + "Please Enable 'xp' In Config");
						return true;
					}
					if (args.length == 1) {
						float xp = player.getTotalExperience();
						cs.sendMessage(d + "Your Have " + xp + " EXP Total");
						return true;
					}else if (args.length == 2) {
						Player plr = Bukkit.getServer().getPlayer(args[1]);
						if (plr != null){
							float xp = plr.getTotalExperience();
							cs.sendMessage(d + plr.getDisplayName() + " Has " + xp + " EXP");
							return true;
						}else{
							 cs.sendMessage(d + " Sorry, But " + plr + " Does Not Exist!");
							 return true;
						}
					}else if (args.length > 2){
						cs.sendMessage(d + "Too Many Arguements!");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("craft")) {
					if (!getConfig().getBoolean("settings.craft")) {
						cs.sendMessage(d + "Please Enable 'craft' In Config");
						return true;
					}
					if (args.length == 1) {
						Location loc = player.getLocation();
						player.openWorkbench(loc, true);
					}else if (args.length > 1){
						cs.sendMessage(d + "Too Many Arguements!");
					}
				}
				
				if (args[0].equalsIgnoreCase("zoom")) {
					if (!getConfig().getBoolean("settings.zoom-x-y-z")) {
						cs.sendMessage(d + "Please Enable 'zoom-x-y-z' In Config");
						return true;
					}
					if (args.length == 1) {
					double x = Double.parseDouble(args[1]);
    				double y = Double.parseDouble(args[2]);
    				double z = Double.parseDouble(args[3]);
					player.setVelocity(new Vector(x, y, z));
					cs.sendMessage(d + "Successfully Zoomed X:" + x + " Y: " + y + " Z: " + z);
					return true;
					}else if (args.length > 1){
						cs.sendMessage(d + "Too Many Arguements!");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("spawn")) {
					if (!getConfig().getBoolean("settings.first-join-spawning")) {
						player.sendMessage(d + "Please Enable 'first-join-spawning' In Config");
						return true;
					}
					teleportToFirstSpawn(player);
					cs.sendMessage(d + "Sucessfully Teleported This Worlds Spawn");
					return true;
				}
				if (args[0].equalsIgnoreCase("zap")) {
					if (!getConfig().getBoolean("settings.zap-cmd")) {
						player.sendMessage(d + "Please Enable 'zap-cmd' In Config");
						return true;
					}
					if (args.length == 1){
						World w = player.getWorld();
						Location l = player.getLocation();
		                w.strikeLightning(l);
		                player.setHealth(player.getHealth() - 5);
						cs.sendMessage(d + "Sucessfully Zapped Yourself");
						return true;
					}else if (args.length == 2){
						Player tar = Bukkit.getServer().getPlayer(args[1]);
						if (tar != null){
								if (tar.isOnline()){
								World w = tar.getWorld();
								Location l = tar.getLocation();
								w.strikeLightning(l);
								tar.setHealth(tar.getHealth() - 5);
								cs.sendMessage(d + "Successfully Zapped " + tar.getDisplayName());
								tar.sendMessage(d + "You Have Been Zapped By " + cs.getName());
								return true;
							}else{
								OfflinePlayer to1 = Bukkit.getServer().getOfflinePlayer(args[2]);
								cs.sendMessage(d + "Sorry, " + to1.getPlayer().getDisplayName() + " Is Offline, Command Cancelled!");
								return true;
							}
						}else{
							cs.sendMessage(d + args[1] + " Does Not Exist, Command Cancelled");
							return true;
						}
					}
				}
				if (args[0].equalsIgnoreCase("ggm")) {
					if (!getConfig().getBoolean("settings.get-gamemode")) {
						cs.sendMessage(d + "Please Enable 'get-gamemode' In Config");
						return true;
					}
					if (args.length == 1) {
						cs.sendMessage(d + "No Player Specified, Command Cancelled");
						return true;
					}else if (args.length == 2){
						Player tar = Bukkit.getServer().getPlayer(args[1]);
						if (tar != null){
							GameMode gm = tar.getGameMode();
							cs.sendMessage(d + tar.getDisplayName() + "'s Gamemode Is Currently Set To: " + gm);
							return true;
						}else{
							cs.sendMessage(d + "Sorry, " + args[1] + " Does Not Exist, Command Cancelled!");
							return true;
						}
					}if (args.length > 2) {
						cs.sendMessage(d + "Too Many Arguements! Command Cancelled");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("bam")) {
					if (!getConfig().getBoolean("settings.bam")) {
						player.sendMessage(d + "Please Enable 'bam' In Config");
						return true;
					}
					if (args.length == 1){
						double half = player.getHealth()*0.25;
						player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 8f);
		                player.setHealth((int) (player.getHealth() - half));
						cs.sendMessage(d + "You Sucessfully Exploded");
						return true;
					}else if (args.length == 2){
						Player tar = Bukkit.getServer().getPlayer(args[1]);
						if (tar != null){
							if (tar.isOnline()) {
								double half = tar.getHealth()*0.25;
								tar.getWorld().createExplosion(tar.getLocation().getX(), tar.getLocation().getY(), tar.getLocation().getZ(), 8f);
				                tar.setHealth((int) (tar.getHealth() - half));
								cs.sendMessage(d + "Successfully Exploded " + tar.getDisplayName());
								tar.sendMessage(d + "You Have Been Exploded By " + cs.getName());
								return true;
							}else{
								OfflinePlayer to1 = Bukkit.getServer().getOfflinePlayer(args[2]);
								cs.sendMessage(d + "Sorry, " + to1.getPlayer().getDisplayName() + " Is Offline, Command Cancelled!");
								return true;
							}
						}else{
							cs.sendMessage(d + "Sorry, " + args[1] + " Does Not Exist, Command Cancelled!");
							return true;
						}
					}
				}
				if (args[0].equalsIgnoreCase("troll")) {
					if (!getConfig().getBoolean("settings.troll")) {
						player.sendMessage(d + "Please Enable 'troll' In Config");
						return true;
					}
					if (args.length == 1){
						Location loc = player.getLocation();
						double y = loc.getBlockY();
						double x = loc.getBlockX();
						double z = loc.getBlockZ();
						World cw = ((Player) cs).getPlayer().getWorld();
						Location a = new Location(cw, x + 2, y, z);
						Location b = new Location(cw, x - 2, y, z);
						Location c = new Location(cw, x, y, z + 2);
						Location d = new Location(cw, x, y, z - 2);
						PigZombie aa = (PigZombie) player.getWorld().spawnEntity(a, EntityType.PIG_ZOMBIE);
						PigZombie bb = (PigZombie) player.getWorld().spawnEntity(b, EntityType.PIG_ZOMBIE);
						PigZombie cc = (PigZombie) player.getWorld().spawnEntity(c, EntityType.PIG_ZOMBIE);
						PigZombie dd = (PigZombie) player.getWorld().spawnEntity(d, EntityType.PIG_ZOMBIE);
						aa.setTarget(player);
						aa.setAngry(true);
						bb.setTarget(player);
						bb.setAngry(true);
						cc.setTarget(player);
						cc.setAngry(true);
						dd.setTarget(player);
						dd.setAngry(true);
						cs.sendMessage(d + "You Sucessfully Trolled Yourself XD");
						return true;
					}else if (args.length == 2){
						Player tar = Bukkit.getServer().getPlayer(args[1]);
						if (tar != null){
							if (tar.isOnline()){
								Location loc = tar.getLocation();
								double y = loc.getBlockY();
								double x = loc.getBlockX();
								double z = loc.getBlockZ();
								World cw = tar.getWorld();
								Location a = new Location(cw, x + 2, y, z);
								Location b = new Location(cw, x - 2, y, z);
								Location c = new Location(cw, x, y, z + 2);
								Location d = new Location(cw, x, y, z - 2);
								PigZombie aa = (PigZombie) tar.getWorld().spawnEntity(a, EntityType.PIG_ZOMBIE);
								PigZombie bb = (PigZombie) tar.getWorld().spawnEntity(b, EntityType.PIG_ZOMBIE);
								PigZombie cc = (PigZombie) tar.getWorld().spawnEntity(c, EntityType.PIG_ZOMBIE);
								PigZombie dd = (PigZombie) tar.getWorld().spawnEntity(d, EntityType.PIG_ZOMBIE);
								aa.setTarget(tar);
								aa.setAngry(true);
								bb.setTarget(tar);
								bb.setAngry(true);
								cc.setTarget(tar);
								cc.setAngry(true);
								dd.setTarget(tar);
								dd.setAngry(true);
								cs.sendMessage(d + "Successfully Trolled " + tar.getDisplayName());
								tar.sendMessage(d + "You Have Been Trolled By " + cs.getName());
								return true;
							}else{
								OfflinePlayer to1 = Bukkit.getServer().getOfflinePlayer(args[2]);
								cs.sendMessage(d + "Sorry, " + to1.getPlayer().getDisplayName() + " Is Offline, Command Cancelled!");
								return true;
							}
						}else if (tar == null){
							cs.sendMessage(d + args[1] + " Does Not Exist, Command Cancelled!");
							return true;
						}
					}
				}
				if (args[0].equalsIgnoreCase("locate")) {
					if (!getConfig().getBoolean("settings.locate")) {
						player.sendMessage(d + "Please Enable 'locate' In Config");
						return true;
					}
					if (args.length == 1){
						int x = player.getLocation().getBlockX();
						int y = player.getLocation().getBlockY();
						int z = player.getLocation().getBlockZ();
						int cx = player.getLocation().getChunk().getX();
						int cz = player.getLocation().getChunk().getZ();
						cs.sendMessage(d + "You Are Currently Located At: X; " + x + ", Y; " + y + ", Z; " + z + ", In Chunk" + cx + ", " + cz);
						return true;
					}else if (args.length == 2){
						Player tar = Bukkit.getServer().getPlayer(args[1]);
						if (tar != null){
							if (tar.isOnline()){
								int x = tar.getLocation().getBlockX();
								int y = tar.getLocation().getBlockY();
								int z = tar.getLocation().getBlockZ();
								int cx = tar.getLocation().getChunk().getX();
								int cz = tar.getLocation().getChunk().getZ();
								cs.sendMessage(d + tar.getDisplayName() + " Is Currently Located At: X; " + x + ", Y; " + y + ", Z; " + z + ", In Chunk" + cx + ", " + cz);
								tar.sendMessage(d + "You Have Been Located By: " + cs.getName());
								return true;
							}else{
							OfflinePlayer to1 = Bukkit.getServer().getOfflinePlayer(args[1]);
							cs.sendMessage(d + to1.getPlayer().getDisplayName() + " Is Offline, Command Cancelled!");
							return true;
							}
						}else if (tar == null){
							cs.sendMessage(d + args[1] + " Does Not Exist, Command Cancelled!");
							return true;
						}
					}
				}
				if (args[0].equalsIgnoreCase("tp")) {
					if (!getConfig().getBoolean("settings.tele")) {
						player.sendMessage(d + "Please Enable 'tele' In Config");
						return true;
					}
					if (args.length == 2){
						Player from = player;
						Player to = Bukkit.getServer().getPlayer(args[1]);
						if (to != null){
							if (to.isOnline()) {
								from.teleport(to);
								from.sendMessage(d + "Successfully Teleported To " + to.getDisplayName());
								return true;
							}else if (!to.isOnline()){
								OfflinePlayer to1 = Bukkit.getServer().getOfflinePlayer(args[2]);
								from.sendMessage(d + "Sorry, " + to1.getPlayer().getDisplayName() + " Is Offline, Teleportation Cancelled!");
								return true;
							}
						}else{
							from.sendMessage(d + "Sorry, " + args[1] + " Does Not Exist, Teleportation Cancelled!");
							return true;
						}
					}else if (args.length == 3){
						Player from = Bukkit.getServer().getPlayer(args[1]);
						Player to = Bukkit.getServer().getPlayer(args[2]);
						if (from != null){
							if (!from.isOnline()){
								OfflinePlayer from1 = Bukkit.getServer().getOfflinePlayer(args[1]);
								player.sendMessage(d + "Sorry But " + from1.getPlayer().getDisplayName() + " Is Offline, Teleportation Cancelled!");
								return true;
							}
						}else if (to != null){
							if (!to.isOnline()){
								OfflinePlayer to2 = Bukkit.getServer().getOfflinePlayer(args[2]);
								player.sendMessage(d + "Sorry But " + to2.getPlayer().getDisplayName() + " Is Offline, Teleportation Cancelled!");
								return true;
							}
						}
						if (from == null){
							player.sendMessage(d + "Sorry But " + args[1] + " Does Not Exist, Teleportation Cancelled!");
							return true;
						}else if (to == null){
							player.sendMessage(d + "Sorry But " + args[2] + " Does Not Exist, Teleportation Cancelled!");
							return true;
						}
						if (from.isOnline() || to.isOnline()) {
							from.teleport(to);
							return true;
						}
					}
				}
			} else {
				cs.sendMessage("That command can only be used ingame.");
			}
			return true;
		}
		return true;
	}
	public void teleportToFirstSpawn(Player player) {
		int x = player.getWorld().getSpawnLocation().getBlockX();
		int y = player.getWorld().getSpawnLocation().getBlockY();
		int z = player.getWorld().getSpawnLocation().getBlockZ();
		player.teleport(new Location(player.getWorld(), x, y, z, 0, 0));
	}
}

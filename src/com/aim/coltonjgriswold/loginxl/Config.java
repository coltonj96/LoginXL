package com.aim.coltonjgriswold.loginxl;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	public static ChatColor a = ChatColor.AQUA;
	public static ChatColor b = ChatColor.DARK_PURPLE;
	public static ChatColor ff = ChatColor.DARK_RED;
	public static File dataDir;
	public static String worldname;
	public static Boolean debug;
	public static Boolean numberonjoin;
	public static Boolean itemonjoin;
	public static Boolean showfirstjoinmessage;
	public static Boolean showjoinmessage;
	public static Boolean showleavemessage;
	public static Boolean showkickmessage;
	public static Boolean showrespawnmessage;
	public static Boolean zoomup;
	public static Boolean zapcmd;
	public static Boolean bam;
	public static Boolean locate;
	public static Boolean showdeathmessage;
	public static Boolean troll;
	public static Boolean tele;
	public static Boolean xp;
	public static Boolean ggm;
	public static Boolean craft;
	public static Boolean zoomxyz;
	public static Boolean firstjoinspawning;
	public static Boolean firstjoinmotd;
	public static Boolean light;
	public static Boolean sign;
	public static String firstjoinmessage;
	public static String joinmessage;
	public static String deathmessage;
	public static String leavemessage;
	public static String kickmessage;
	public static String respawnmessage;
	public static String numbermessage;
	public static List<String> motdmsg;
	

	public static Integer item;
	public static Integer amount;
	public static Integer data;
	
	public static void initialize(FileConfiguration config, File pluginDir,Logger log) {
		try {
			dataDir = pluginDir;
			if (!dataDir.exists()) {
				dataDir.mkdir();
			}

			ConfigurationSection settings = config.getConfigurationSection("settings");
			ConfigurationSection messages = config.getConfigurationSection("messages");
			 ConfigurationSection signs = config.getConfigurationSection("signs");

			// Settings Section
			worldname = settings.getString("main-world-name");
			debug = settings.getBoolean("debug");
			light = settings.getBoolean("ride");
			numberonjoin = settings.getBoolean("number-on-join");
			itemonjoin = settings.getBoolean("item-on-join");
			showfirstjoinmessage = settings.getBoolean("show-first-join-message");
			showjoinmessage = settings.getBoolean("show-join-message");
			showleavemessage = settings.getBoolean("show-leave-message");
			showkickmessage = settings.getBoolean("show-kick-message");
			showdeathmessage = settings.getBoolean("show-death-message");
			showrespawnmessage = settings.getBoolean("show-respawn-message");
			zoomup = settings.getBoolean("zoom-up");
			zapcmd = settings.getBoolean("zap-cmd");
			bam = settings.getBoolean("bam");
			locate = settings.getBoolean("locate");
			troll = settings.getBoolean("troll");
			tele = settings.getBoolean("tele");
			xp = settings.getBoolean("xp");
			ggm = settings.getBoolean("get-gamemode");
			craft = settings.getBoolean("craft");
			zoomxyz = settings.getBoolean("zoom-x-y-z");
			firstjoinspawning = settings.getBoolean("first-join-spawning");
			firstjoinmotd = settings.getBoolean("first-join-motd");

			// Messages Section
			firstjoinmessage = messages.getString("first-join-message").replace("&", "§");
			joinmessage = messages.getString("join-message").replace("&", "§");
			leavemessage = messages.getString("leave-message").replace("&", "§");
			kickmessage = messages.getString("kick-message").replace("&", "§");
			numbermessage = messages.getString("number-message").replace("&","§");
			deathmessage = messages.getString("death-message").replace("&","§");
			respawnmessage = messages.getString("respawn-message").replace("&","§");
			// signs Section
			sign = signs.getBoolean("check-for-signs");

		} catch (Exception ex) {
			log.logp(Level.SEVERE, ff + "ERROR", b + "Config Is Unable To Load!", a + "Please Check Your Configuration!", ex);
		}
	}
}
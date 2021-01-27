package me.oreoezi.Utils;

import org.bukkit.ChatColor;

import me.oreoezi.database.Configs;

public class ConfigUtils {
	
	public static String getMessage(Configs configs, String index) {
		return ChatColor.translateAlternateColorCodes('&', configs.getConfig("language").getString(index));
	}
	public static String randomString(int size) {
		String alph = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-";
		String ret = "";
		for (int i=0;i<size;i++) {
			ret += alph.charAt((int) Math.floor(Math.random() * alph.length()));
		}
		return ret;
	}
}

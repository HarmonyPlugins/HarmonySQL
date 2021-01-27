package me.oreoezi.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.oreoezi.Utils.GUI.HarmonyGUI;
import me.oreoezi.Utils.GUI.HarmonyGUIListener;
import me.oreoezi.Utils.GUI.Events.GUIClickEvent;
import me.oreoezi.database.Database;
import me.oreoezi.main.Main;

public class Layouts {
	public static HarmonyGUI createMainScreen(Main main, Player player) {
		HarmonyGUI mainscreen = new HarmonyGUI(main, 27, "Main Window", player);
		mainscreen.createGuiItem(Material.GLOWSTONE, 10, "§3Show Databases", "§bList all databases.");
		mainscreen.createGuiItem(Material.REDSTONE_BLOCK, 13, "§3Create Database", "§bCreate a new Database.");
		mainscreen.createGuiItem(Material.BARRIER, 16, "§3Remove Database", "§bDelete a Database.");
		return mainscreen;
	}
	
	public static HarmonyGUI createDatabaseSettings(Main main, Player player, String database) {
		String host = main.configs.getConfig("config").getString("mysql.host");
		String port = main.configs.getConfig("config").getString("mysql.port");
		String user = main.configs.getConfig("config").getString("mysql.username");
		String pass = main.configs.getConfig("config").getString("mysql.password");
		Database db = new Database(host,port,user,pass, database);
		HarmonyGUI listscreen = new HarmonyGUI(main, 27, database + " settings", player);
		listscreen.createGuiItem(Material.GLOWSTONE, 10, "§3Show Tables", "§bList all tables.");
		listscreen.createGuiItem(Material.REDSTONE_BLOCK, 13, "§3Create Table", "§bCreate a new table.");
		listscreen.createGuiItem(Material.BARRIER, 16, "§3Remove Table", "§bDelete a table.");
		listscreen.registerEvents(new HarmonyGUIListener() {
			public void onClick(GUIClickEvent event) {
				switch(event.getSlot()) {
				case 10: {
					HarmonyGUI tablescreen = TableCreator.createTableList(main, player, db, new HarmonyGUIListener() {
						public void onClick(GUIClickEvent event) {
							HarmonyGUI settings = TableEditor.createTableSettings(main, player, db, ChatColor.stripColor(event.getClickedItem().getItemMeta().getDisplayName()));
							listscreen.setNext(settings);
							settings.setPrevious(listscreen);
							listscreen.close();
							settings.open();
						}
					});
					tablescreen.setPrevious(listscreen);
					listscreen.setNext(tablescreen);
					tablescreen.open();
					break;
				}
				case 13: {
					listscreen.close();
					TableCreator.handleTableCreation(main, player, db, listscreen);
					break;
				}
				case 16: {
					HarmonyGUI tablescreen = TableCreator.createTableRemoveList(main, player, db, listscreen);
					tablescreen.open();
					break;
				}
				}
			}
		});
		return listscreen;
	}
	
}

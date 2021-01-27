package me.oreoezi.gui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.oreoezi.Utils.ConfigUtils;
import me.oreoezi.Utils.GUI.HarmonyGUI;
import me.oreoezi.Utils.GUI.HarmonyGUIListener;
import me.oreoezi.Utils.GUI.Events.GUIClickEvent;
import me.oreoezi.main.Main;

public class SQLGUI {
	private Main main;
	private Player player;
	private String prefix;
	public SQLGUI(Main main, Player player) {
		this.main = main;
		this.player = player;
		this.prefix = ConfigUtils.getMessage(main.configs, "prefix");
		createMainScreen();
	}
	private void createMainScreen() {
		HarmonyGUI mainscreen = Layouts.createMainScreen(main, player);
		mainscreen.registerEvents(new HarmonyGUIListener() {
			public void onClick(GUIClickEvent event) {
				switch(event.getSlot()) {
				case 10: {
					HarmonyGUI dbscreen = DBCreator.createDBscreen(main, player, new HarmonyGUIListener() {
						public void onClick(GUIClickEvent event) {
							HarmonyGUI dbsettings = Layouts.createDatabaseSettings(main, player, ChatColor.stripColor(event.getClickedItem().getItemMeta().getDisplayName()));
							dbsettings.setPrevious(event.getGUI());
							dbsettings.open();
						}
					});
					dbscreen.setPrevious(mainscreen);
					dbscreen.open();
					break;
				}
				case 13: {
					DBCreator.handleDBCreation(main, player, mainscreen);
					player.sendMessage(prefix + " " + ConfigUtils.getMessage(main.configs, "messages.admin.create_database"));
					break;
				}
				case 16: {
					HarmonyGUI dbremoval = DBCreator.createDBRemoveScreen(main, player, mainscreen);
					mainscreen.close();
					dbremoval.open();
					break;
				}
				}
			}
		});
		mainscreen.open();
	}

}

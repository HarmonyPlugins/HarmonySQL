package me.oreoezi.gui;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import me.oreoezi.Utils.GUI.HarmonyGUI;
import me.oreoezi.Utils.GUI.HarmonyGUIListener;
import me.oreoezi.Utils.GUI.HarmonyInput;
import me.oreoezi.Utils.GUI.Events.GUIClickEvent;
import me.oreoezi.Utils.GUI.Events.GUITextEvent;
import me.oreoezi.main.Main;

public class DBCreator {
	public static HarmonyGUI createDBRemoveScreen(Main main, Player player, HarmonyGUI mainscreen) {
		HarmonyGUI dbscreen = createDBscreen(main, player,new HarmonyGUIListener() {
			public void onClick(GUIClickEvent event) {
				main.database.SetData("DROP DATABASE " + ChatColor.stripColor(event.getClickedItem().getItemMeta().getDisplayName()));
				event.getGUI().close();
				HarmonyGUI newscreen = createDBRemoveScreen(main, player, mainscreen);
				newscreen.open();
			}
		});
		dbscreen.setPrevious(mainscreen);
		return dbscreen;
	}
	public static void handleDBCreation(Main main, Player player, HarmonyGUI mainscreen) {
		mainscreen.close();
		HarmonyInput dbname = new HarmonyInput(main, player);
		dbname.registerEvents(new HarmonyGUIListener() {
			public void onSend(GUITextEvent event) {
				main.database.SetData("CREATE DATABASE " + event.getText());
				HarmonyGUI dbscreen = createDBscreen(main, player, new HarmonyGUIListener());
				dbscreen.setPrevious(mainscreen);
				dbscreen.open();
			}
		}); 
	}
	
	public static HarmonyGUI createDBscreen(Main main, Player player, HarmonyGUIListener listener) {
		JSONArray rows = main.database.GetData("SHOW DATABASES;");
		ArrayList<HarmonyGUI> pages = new ArrayList<HarmonyGUI>();
		int pageno = rows.size()/36+1;
		for (int i=0;i<pageno;i++) {
			HarmonyGUI page = new HarmonyGUI(main, 54, "Databases (Page " + (i+1) + "/" + pageno + ")",player);
			int rws = (rows.size()-36*i) > 36 ? 36 : rows.size()-36*i;
			for (int j=0;j<rws;j++) {
				JSONObject row = (JSONObject) rows.get(i*36+j);
				page.createGuiItem(Material.REDSTONE_BLOCK, 9+j, "§3" + row.get("SCHEMA_NAME"));
			}
			page.registerEvents(listener);
			pages.add(page);
		}
		if (pages.size() > 1) {
			pages.get(0).setNext(pages.get(1));
			for (int i=1;i<pages.size()-1;i++) {
				pages.get(i).setPrevious(pages.get(i-1));
				pages.get(i).setNext(pages.get(i+1));
			}
			pages.get(pages.size()-1).setPrevious(pages.get(pages.size()-2));
		}
		return pages.get(0);
	}
}

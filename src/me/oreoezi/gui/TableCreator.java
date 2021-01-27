package me.oreoezi.gui;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import me.oreoezi.Utils.ConfigUtils;
import me.oreoezi.Utils.GUI.HarmonyGUI;
import me.oreoezi.Utils.GUI.HarmonyGUIListener;
import me.oreoezi.Utils.GUI.HarmonyInput;
import me.oreoezi.Utils.GUI.Events.GUIClickEvent;
import me.oreoezi.Utils.GUI.Events.GUITextEvent;
import me.oreoezi.database.Database;
import me.oreoezi.main.Main;

public class TableCreator {
	
	public static HarmonyGUI createTableRemoveList(Main main, Player player, Database db, HarmonyGUI dbscreen) {
		HarmonyGUI rmlist = createTableList(main, player, db, new HarmonyGUIListener() {
			public void onClick(GUIClickEvent event) {
				db.SetData("DROP TABLE " + ChatColor.stripColor(event.getClickedItem().getItemMeta().getDisplayName()));
				event.getGUI().close();
				HarmonyGUI newrm = createTableRemoveList(main,player,db,dbscreen);
				newrm.open();
			}
		});
		rmlist.setPrevious(dbscreen);
		return rmlist;
	}
	
	public static HarmonyGUI createTableList(Main main, Player player, Database db, HarmonyGUIListener listener) {
		JSONArray rows = db.GetData("SELECT table_name FROM information_schema.tables WHERE table_schema=DATABASE()");
		ArrayList<HarmonyGUI> pages = new ArrayList<HarmonyGUI>();
		int pageno = rows.size()/36+1;
		for (int i=0;i<pageno;i++) {
			HarmonyGUI page = new HarmonyGUI(main, 54, "Tables (Page " + (i+1) + "/" + pageno + ")",player);
			int rws = (rows.size()-36*i) > 36 ? 36 : rows.size()-36*i;
			for (int j=0;j<rws;j++) {
				JSONObject row = (JSONObject) rows.get(i*36+j);
				page.createGuiItem(Material.REDSTONE_BLOCK, 9+j, "§3" + row.get("TABLE_NAME"));
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
	
	
	public static void handleTableCreation(Main main, Player player, Database db, HarmonyGUI back) {
		HarmonyInput table_name = new HarmonyInput(main, player);
		String prefix = ConfigUtils.getMessage(main.configs, "prefix");
		player.sendMessage(prefix + " " + ConfigUtils.getMessage(main.configs, "messages.admin.create_table"));
		table_name.registerEvents(new HarmonyGUIListener() {
			public void onSend(GUITextEvent event) {
				String name = event.getText();
				player.sendMessage(prefix + " " + ConfigUtils.getMessage(main.configs, "messages.admin.first_column"));
				player.sendMessage(prefix + " " + ConfigUtils.getMessage(main.configs, "messages.admin.create_column"));
				HarmonyInput col_name = new HarmonyInput(main, player);
				col_name.registerEvents(new HarmonyGUIListener() {
					public void onSend(GUITextEvent event) {
						String col_name = event.getText();
						HarmonyGUI type = TableEditor.createColumnSettings(main, player, new HarmonyGUIListener() {
							public void onClick(GUIClickEvent event) {
								String type = ChatColor.stripColor(event.getClickedItem().getItemMeta().getDisplayName());
								db.SetData("CREATE TABLE " + name + " (" + col_name + " " + type + ")");
								back.open();
							}
						});
						type.open();
					}
				});
			}
		}); 
	}
	
	public static void handleColumnCreation(Main main, Player player, Database db, String table, HarmonyGUI back) {
		String prefix = ConfigUtils.getMessage(main.configs, "prefix");
		player.sendMessage(prefix + " " + ConfigUtils.getMessage(main.configs, "messages.admin.create_column"));
		HarmonyInput col_name = new HarmonyInput(main, player);
		col_name.registerEvents(new HarmonyGUIListener() {
			public void onSend(GUITextEvent event) {
				String col_name = event.getText();
				HarmonyGUI type = TableEditor.createColumnSettings(main, player, new HarmonyGUIListener() {
					public void onClick(GUIClickEvent event) {
						String type = ChatColor.stripColor(event.getClickedItem().getItemMeta().getDisplayName());
						db.SetData("ALTER TABLE " + table + " ADD COLUMN (" + col_name + " " + type + ")");
						back.open();
					}
				});
				type.open();
			}
		});
	}
	
	public static HarmonyGUI showColumnList(Main main, Player player, Database db, String table, HarmonyGUIListener listener) {
		JSONArray rows = db.GetData("SHOW COLUMNS FROM " + table);
		ArrayList<HarmonyGUI> pages = new ArrayList<HarmonyGUI>();
		int pageno = rows.size()/36+1;
		for (int i=0;i<pageno;i++) {
			HarmonyGUI page = new HarmonyGUI(main, 54, "Columns (Page " + (i+1) + "/" + pageno + ")",player);
			int rws = (rows.size()-36*i) > 36 ? 36 : rows.size()-36*i;
			for (int j=0;j<rws;j++) {
				JSONObject row = (JSONObject) rows.get(i*36+j);
				page.createGuiItem(Material.REDSTONE_BLOCK, 9+j, "§3" + row.get("COLUMN_NAME"), "§bType: " + row.get("COLUMN_TYPE"));
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

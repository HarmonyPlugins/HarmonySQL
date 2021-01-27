package me.oreoezi.gui;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import me.oreoezi.Utils.ConfigUtils;
import me.oreoezi.Utils.GUI.HarmonyGUI;
import me.oreoezi.Utils.GUI.HarmonyGUIListener;
import me.oreoezi.Utils.GUI.HarmonyInput;
import me.oreoezi.Utils.GUI.Events.GUIBooleanOffEvent;
import me.oreoezi.Utils.GUI.Events.GUIBooleanOnEvent;
import me.oreoezi.Utils.GUI.Events.GUIClickEvent;
import me.oreoezi.Utils.GUI.Events.GUITextEvent;
import me.oreoezi.database.Database;
import me.oreoezi.main.Main;

public class RowSelector {
	public static HarmonyGUI rowSelectorMain(Main main, Player player, Database db, String table) {
		HarmonyGUI mainscreen = new HarmonyGUI(main, 27, "Select Rows", player);
		mainscreen.createGuiItem(Material.GLOWSTONE, 10, "§3Selection Columns");
		mainscreen.createGuiItem(Material.REDSTONE_COMPARATOR, 13, "§3Selection Conditions");
		mainscreen.createGuiItem(Material.EMERALD_BLOCK, 16, "§3Send Query");
		ArrayList<String> cols = new ArrayList<String>();
		ArrayList<String> conds = new ArrayList<String>();
		mainscreen.registerEvents(new HarmonyGUIListener() {
			public void onClick(GUIClickEvent event) {
				switch(event.getSlot()) {
				case 10: {
					HarmonyGUI colssc = colSelector(main, player, db, table, new HarmonyGUIListener() {
						public void onBooleanOff(GUIBooleanOffEvent event) {
							cols.remove(event.getName());
						}
						public void onBooleanOn(GUIBooleanOnEvent event) {
							cols.add(event.getName());
						}
					});
					for (int i=0;i<cols.size();i++) {
						colssc.setBooleanValue(cols.get(i), true);
					}
					colssc.setPrevious(mainscreen);
					mainscreen.close();
					colssc.open();
					break;
				}
				case 13: {
					mainscreen.close();
					conds.clear();
					handleConditions(main, player, db, table, cols, conds, mainscreen);
					break;
				}
				case 16: {
					HarmonyGUI outsc = DBScreens.createOutscreen(main, player, formatQuery(table, cols, conds), db);
					outsc.setPrevious(mainscreen);
					mainscreen.close();
					outsc.open();
					break;
				}
				}
			}
		});
		return mainscreen;
	}
	
	public static HarmonyGUI colSelector(Main main, Player player, Database db, String table, HarmonyGUIListener listener) {
		JSONArray rows = db.GetData("SHOW COLUMNS FROM " + table);
		ArrayList<HarmonyGUI> pages = new ArrayList<HarmonyGUI>();
		int pageno = rows.size()/36+1;
		for (int i=0;i<pageno;i++) {
			HarmonyGUI page = new HarmonyGUI(main, 54, "Columns (Page " + (i+1) + "/" + pageno + ")",player);
			int rws = (rows.size()-36*i) > 36 ? 36 : rows.size()-36*i;
			for (int j=0;j<rws;j++) {
				JSONObject row = (JSONObject) rows.get(i*36+j);
				page.createBooleanItem(Material.REDSTONE_BLOCK, 9+j,(String) row.get("COLUMN_NAME"), "§3" + row.get("COLUMN_NAME"), "§bType: " + row.get("COLUMN_TYPE"));
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
	public static void handleConditions(Main main, Player player, Database db, String table, ArrayList<String> cols, ArrayList<String> conds, HarmonyGUI back) {
		HandleInput(main, player, new HarmonyGUIListener() {
			@Override
			public void onSend(GUITextEvent event) {
				if (event.getText().equals("none")) {
					back.open();
				}
				else {
					conds.add(event.getText());
					String msg = ConfigUtils.getMessage(main.configs, "messages.admin.enter_condition");
					player.sendMessage(msg.replace("%query%", formatQuery(table, cols, conds)));
				}
			}
		});
	}
	
	public static void HandleInput(Main main, Player player, HarmonyGUIListener listener) {
		HarmonyInput input = new HarmonyInput(main, player);
		input.registerEvents(new HarmonyGUIListener() {
			@Override
			public void onSend(GUITextEvent event) {
				if (!event.getText().equals("none")) HandleInput(main, player, listener);
			}
		});
		input.registerEvents(listener);
	}
	
	public static String formatQuery(String table, ArrayList<String> cols, ArrayList<String> conds) {
		String query = "SELECT ";
		query += String.join(", ", cols);
		query += " FROM " + table;
		if (conds.size() > 0) {
			query += " WHERE ";
			query += String.join(" AND ", conds);
		}
		return query;
		
	}
}

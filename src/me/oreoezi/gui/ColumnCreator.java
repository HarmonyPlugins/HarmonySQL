package me.oreoezi.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import me.oreoezi.Utils.ConfigUtils;
import me.oreoezi.Utils.GUI.HarmonyGUI;
import me.oreoezi.Utils.GUI.HarmonyGUIListener;
import me.oreoezi.Utils.GUI.HarmonyInput;
import me.oreoezi.Utils.GUI.Events.GUIClickEvent;
import me.oreoezi.Utils.GUI.Events.GUITextEvent;
import me.oreoezi.database.Database;
import me.oreoezi.main.Main;

public class ColumnCreator {
	public static HarmonyGUI columnRemovalScreen(Main main, Player player, Database db, String table, HarmonyGUI back) {
		HarmonyGUI cl_list = TableCreator.showColumnList(main, player, db, table, new HarmonyGUIListener() {
			public void onClick(GUIClickEvent event) {
				db.SetData("ALTER TABLE " + table + " DROP COLUMN " + ChatColor.stripColor(event.getClickedItem().getItemMeta().getDisplayName()));
				HarmonyGUI newlist = columnRemovalScreen(main,player,db,table,back);
				newlist.setPrevious(back);
				back.setNext(newlist);
				newlist.open();
			}
		});
		cl_list.setPrevious(back);
		back.setNext(cl_list);
		back.close();
		return cl_list;
	}
	
	public static HarmonyGUI columnAlterScreen(Main main, Player player, Database db, String table, HarmonyGUI back) {
		HarmonyGUI cl_list = TableCreator.showColumnList(main, player, db, table, new HarmonyGUIListener() {
			public void onClick(GUIClickEvent event) {
				String colname = ChatColor.stripColor(event.getClickedItem().getItemMeta().getDisplayName());
				HarmonyGUI options = new HarmonyGUI(main, 54, "Column Options", player);
				options.createGuiItem(Material.BOOK, 10, "§3Change Name", "§bChange the column's name");
				options.createGuiItem(Material.REDSTONE_BLOCK, 16, "§3Change Type", "§bChange the column's type.");
				options.setPrevious(back);
				options.registerEvents(new HarmonyGUIListener() {
					public void onClick(GUIClickEvent event) {
						if (event.getSlot() == 10) {
							options.close();
							player.sendMessage(ConfigUtils.getMessage(main.configs, "prefix") + " " + ConfigUtils.getMessage(main.configs, "messages.admin.create_column"));
							HarmonyInput name = new HarmonyInput(main, player);
							name.registerEvents(new HarmonyGUIListener() {
								public void onSend(GUITextEvent event) {
									JSONObject row = (JSONObject) db.GetData("SHOW COLUMNS FROM " + table + " WHERE Field='" + colname + "'").get(0);
									String type = (String) row.get("COLUMN_TYPE");
									db.SetData("ALTER TABLE " + table + " CHANGE " + colname + " " + event.getText() + " " + type);
									back.open();
								}
							});
						}
						if (event.getSlot() == 16) {
							HarmonyGUI newType = TableEditor.createColumnSettings(main, player, new HarmonyGUIListener() {
								public void onClick(GUIClickEvent event) {
									String coltype = ChatColor.stripColor(event.getClickedItem().getItemMeta().getDisplayName());
									db.SetData("ALTER TABLE " + table + " MODIFY " + colname + " " + coltype);
									event.getGUI().close();
									back.open();
								}
							});
							newType.open();
						}
					}
				});
				event.getGUI().close();
				options.open();
			}
		});
		cl_list.setPrevious(back);
		back.setNext(cl_list);
		return cl_list;
	}
}

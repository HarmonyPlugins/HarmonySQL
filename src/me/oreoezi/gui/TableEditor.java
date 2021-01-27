package me.oreoezi.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.oreoezi.Utils.ConfigUtils;
import me.oreoezi.Utils.GUI.HarmonyGUI;
import me.oreoezi.Utils.GUI.HarmonyGUIListener;
import me.oreoezi.Utils.GUI.HarmonyInput;
import me.oreoezi.Utils.GUI.Events.GUIClickEvent;
import me.oreoezi.Utils.GUI.Events.GUITextEvent;
import me.oreoezi.database.Database;
import me.oreoezi.main.Main;

public class TableEditor {
	public static HarmonyGUI createTableSettings(Main main, Player player, Database db, String table) {
		HarmonyGUI tablescreen = new HarmonyGUI(main, 27, table + " settings", player);
		tablescreen.createGuiItem(Material.WATER_BUCKET, 10, "§3Rows", "§bShow, alter, add or remove rows.");
		tablescreen.createGuiItem(Material.LAVA_BUCKET, 16, "§3Columns", "§bShow, alter, add or remove columns.");
		tablescreen.registerEvents(new HarmonyGUIListener() {
			public void onClick(GUIClickEvent event) {
				if (event.getSlot() == 10) {
					HarmonyGUI rowsel = RowSelector.rowSelectorMain(main, player, db, table);
					rowsel.setPrevious(tablescreen);
					tablescreen.close();
					rowsel.open();
				}
				else if (event.getSlot() == 16) {
					HarmonyGUI columnscreen = new HarmonyGUI(main, 27, table + " settings", player);
					columnscreen.createGuiItem(Material.EMERALD_BLOCK, 10, "§3Create Columns", "§bCreate a new column");
					columnscreen.createGuiItem(Material.REDSTONE_BLOCK, 13, "§3Alter Columns", "§bChange the name or type of a column.");
					columnscreen.createGuiItem(Material.BARRIER, 16, "§3Remove Columns", "§bRemove columns.");
					columnscreen.setPrevious(tablescreen);
					tablescreen.setNext(columnscreen);
					columnscreen.registerEvents(new HarmonyGUIListener() {
						public void onClick(GUIClickEvent event) {
							switch (event.getSlot()) {
							case 10: {
								columnscreen.close();
								TableCreator.handleColumnCreation(main, player, db, table, columnscreen);
								break;
							}
							case 13: {
								HarmonyGUI cas = ColumnCreator.columnAlterScreen(main, player, db, table, columnscreen);
								columnscreen.close();
								cas.open();
								break;
							}
							case 16: {
								HarmonyGUI rms = ColumnCreator.columnRemovalScreen(main, player, db, table, columnscreen);
								columnscreen.close();
								rms.open();
								break;
							}
							}
						}
					});
					tablescreen.close();
					columnscreen.open();
				}
			}
		});
		return tablescreen;
	}
	
	public static HarmonyGUI createColumnSettings(Main main, Player player, HarmonyGUIListener listener) {
		HarmonyGUI columnType = DBScreens.columnType(main, player);
		columnType.registerEvents(new HarmonyGUIListener() {
			public void onClick(GUIClickEvent event) {
				columnType.close();
				switch(event.getSlot()) {
				case 10: {
					HarmonyGUI nom = DBScreens.columnNumber(main, player);
					nom.registerEvents(listener);
					nom.setPrevious(columnType);
					columnType.setNext(nom);
					nom.open();
					break;
				}
				case 12: {
					HarmonyGUI date = DBScreens.columnDate(main, player);
					date.registerEvents(listener);
					date.setPrevious(columnType);
					columnType.setNext(date);
					date.open();
					break;
				}
				case 14: {
					HarmonyGUI string = DBScreens.columnString(main, player);
					string.registerEvents(new HarmonyGUIListener() {
						public void onClick(GUIClickEvent event) {
							if(event.getSlot() == 10 || event.getSlot() == 16) {
								player.sendMessage(ConfigUtils.getMessage(main.configs, "prefix") + " " + ConfigUtils.getMessage(main.configs, "messages.admin.column_size"));
								String vartype = ChatColor.stripColor(event.getClickedItem().getItemMeta().getDisplayName());
								HarmonyInput input = new HarmonyInput(main, player);
								input.registerEvents(new HarmonyGUIListener() {
									public void onSend(GUITextEvent event) {
										String size = event.getText();
										String fin = vartype + "(" + size + ")";
										HarmonyGUI confirm = new HarmonyGUI(main, 54, "Confirm Column", player);
										confirm.createGuiItem(Material.EMERALD_BLOCK, 13, "§3" + fin);
										confirm.registerEvents(listener);
										confirm.setPrevious(columnType);
										confirm.open();
									}
								});
								string.close();
							}
						}
					});
					string.setPrevious(columnType);
					columnType.setNext(string);
					string.open();
					break;
				}
				case 16: {
					HarmonyGUI string = DBScreens.columnBinary(main, player);
					string.registerEvents(new HarmonyGUIListener() {
						public void onClick(GUIClickEvent event) {
							if(event.getSlot() == 10 || event.getSlot() == 16) {
								player.sendMessage(ConfigUtils.getMessage(main.configs, "prefix") + " " + ConfigUtils.getMessage(main.configs, "messages.admin.column_size"));
								String vartype = ChatColor.stripColor(event.getClickedItem().getItemMeta().getDisplayName());
								HarmonyInput input = new HarmonyInput(main, player);
								input.registerEvents(new HarmonyGUIListener() {
									public void onSend(GUITextEvent event) {
										String size = event.getText();
										String fin = vartype + "(" + size + ")";
										HarmonyGUI confirm = new HarmonyGUI(main, 54, "Confirm Column", player);
										confirm.createGuiItem(Material.EMERALD_BLOCK, 13, "§3" + fin);
										confirm.registerEvents(listener);
										confirm.setPrevious(columnType);
										confirm.open();
									}
								});
								string.close();
							}
						}
					});
					string.setPrevious(columnType);
					columnType.setNext(string);
					string.open();
					break;
				}
				}
			}
		});
		return columnType;
	}
}

package me.oreoezi.gui;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import me.oreoezi.Utils.GUI.HarmonyGUI;
import me.oreoezi.database.Database;
import me.oreoezi.main.Main;

public class DBScreens {
	public static HarmonyGUI createOutscreen(Main main, Player player, String query, Database db) {
		JSONArray rows = db.GetData(query);
		ArrayList<HarmonyGUI> pages = new ArrayList<HarmonyGUI>();
		int pageno = rows.size()/36+1;
		for (int i=0;i<pageno;i++) {
			HarmonyGUI page = new HarmonyGUI(main, 54, "Columns (Page " + (i+1) + "/" + pageno + ")",player);
			int rws = (rows.size()-36*i) > 36 ? 36 : rows.size()-36*i;
			for (int j=0;j<rws;j++) {
				JSONObject row = (JSONObject) rows.get(i*36+j);
				List<String> lore = new ArrayList<String>();
				for (Object key : row.keySet()) {
					lore.add("§f[§b" + key + "§f] §b" + row.get(key));
				}
				page.createGuiItem(Material.REDSTONE_BLOCK, 9+j, "§3Row #" + (pageno*i + j), lore);
			}
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
	
	public static HarmonyGUI columnType(Main main, Player player) {
		HarmonyGUI outscreen = new HarmonyGUI(main, 54, "Select Column Type", player);
		outscreen.createGuiItem(Material.DISPENSER, 10, "§3Number");
		outscreen.createGuiItem(Material.DAYLIGHT_DETECTOR, 12, "§3Date");
		outscreen.createGuiItem(Material.ANVIL, 14, "§3String");
		outscreen.createGuiItem(Material.REDSTONE_COMPARATOR, 16, "§3Binary");
		return outscreen;
	}
	
	public static HarmonyGUI columnDate(Main main, Player player) {
		HarmonyGUI outscreen = new HarmonyGUI(main, 54, "Select Column Type", player);
		outscreen.createGuiItem(Material.REDSTONE_BLOCK, 10, "§3DATETIME","§bDisplay Format: YYYY-MM-DD HH:MM:SS");
		outscreen.createGuiItem(Material.REDSTONE_BLOCK, 13, "§3DATE", "§bDisplay Format: YYYY-MM-DD");
		outscreen.createGuiItem(Material.REDSTONE_BLOCK, 16, "§3TIMESTAMP", "§bDisplay Format: YYYY-MM-DD HH:MM:SS");
		return outscreen;
	}
	
	public static HarmonyGUI columnString(Main main, Player player) {
		HarmonyGUI outscreen = new HarmonyGUI(main, 27, "Select Column Type", player);
		outscreen.createGuiItem(Material.REDSTONE_BLOCK, 10, "§3CHAR");
		outscreen.createGuiItem(Material.REDSTONE_BLOCK, 16, "§3VARCHAR");
		return outscreen;
	}
	public static HarmonyGUI columnBinary(Main main, Player player) {
		HarmonyGUI outscreen = new HarmonyGUI(main, 27, "Select Column Type", player);
		outscreen.createGuiItem(Material.REDSTONE_BLOCK, 10, "§3BINARY");
		outscreen.createGuiItem(Material.REDSTONE_BLOCK, 16, "§3VARBINARY");
		return outscreen;
	}
	public static HarmonyGUI columnNumber(Main main, Player player) {
		HarmonyGUI outscreen = new HarmonyGUI(main, 54, "Select Column Type", player);
		outscreen.createGuiItem(Material.REDSTONE_BLOCK, 10, "§3TINYINT", "§bByte Length: 1", "§bMin Value: -128", "§bMax Value: 127");
		outscreen.createGuiItem(Material.REDSTONE_BLOCK, 11, "§3SMALLINT", "§bByte Length: 2", "§bMin Value: -32768", "§bMax Value: 32767");
		outscreen.createGuiItem(Material.REDSTONE_BLOCK, 12, "§3MEDIUMINT", "§bByte Length: 3", "§bMin Value: -8388608", "§bMax Value: 2147483647");
		outscreen.createGuiItem(Material.REDSTONE_BLOCK, 13, "§3INT", "§bByte Length: 4", "§bMin Value: -2147483648", "§bMax Value: 2147483647");
		outscreen.createGuiItem(Material.REDSTONE_BLOCK, 14, "§3BIGINT", "§bByte Length: 8", "§bMin Value: -9223372036854775808", "§bMax Value: 9223372036854775807");
		outscreen.createGuiItem(Material.REDSTONE_BLOCK, 15, "§3FLOAT", "§bByte Length: 4", "§bMin Value: -3.402823466E+38", "§bMax Value: -1.175494351E-38");
		outscreen.createGuiItem(Material.REDSTONE_BLOCK, 16, "§3DOUBLE", "§bByte Length: 8", "§bMin Value: -1.7976931348623157E+ 308", "§bMax Value: -2.2250738585072014E-308");
		return outscreen;
	}
	

}

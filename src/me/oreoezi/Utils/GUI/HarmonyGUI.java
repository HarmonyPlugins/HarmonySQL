package me.oreoezi.Utils.GUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.oreoezi.Utils.Glow;
import me.oreoezi.Utils.GUI.Events.GUIBackEvent;
import me.oreoezi.Utils.GUI.Events.GUIBooleanOffEvent;
import me.oreoezi.Utils.GUI.Events.GUIBooleanOnEvent;
import me.oreoezi.Utils.GUI.Events.GUIBooleanUpdateEvent;
import me.oreoezi.Utils.GUI.Events.GUIClickEvent;
import me.oreoezi.main.Main;

public class HarmonyGUI implements Listener {
	private Inventory inv;
	private Player player;
	private String title;
	private Main main;
	private ArrayList<HarmonyGUIListener> listeners = new ArrayList<HarmonyGUIListener>();
	private HashMap<ItemStack, String> bools = new HashMap<ItemStack, String>();
	private HashMap<String, Boolean> bvars = new HashMap<String, Boolean>();
	private int slot = 0;
	private HarmonyGUI back;
	private HarmonyGUI page;
	public HarmonyGUI(Main main, int size, String name, Player player) {
		this.main = main;
		this.player = player;
		this.title = name;
		this.inv = Bukkit.createInventory(null, size, title);
	}
	public HarmonyGUI(Main main, int size, String name, Player player, HarmonyGUI back) {
		this.main = main;
		this.player = player;
		this.title = name;
		this.inv = Bukkit.createInventory(null, size, title);
		if (size == 27) this.slot = 18;
		else this.slot = 45;
		createGuiItem(Material.ARROW, slot, "§bBack", "§3Go back to the previous page");
		this.back = back;
	}
	public HarmonyGUI(Main main, int size, String name, Player player, HarmonyGUI back, HarmonyGUI next) {
		this.main = main;
		this.player = player;
		this.title = name;
		this.inv = Bukkit.createInventory(null, size, title);
		if (size == 27) this.slot = 18;
		else this.slot = 45;
		createGuiItem(Material.ARROW, slot, "§bBack", "§3Go back to the previous page.");
		createGuiItem(Material.ARROW, slot+8, "§bForward", "§3Go to the next page.");
		this.back = back;
		this.page = next;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPrevious(HarmonyGUI previous) {
		this.back = previous;
		if (inv.getSize() == 27) this.slot = 18;
		else this.slot = 45;
		createGuiItem(Material.ARROW, slot, "§bBack", "§3Go back to the previous page.");
	}
	
	public void setNext(HarmonyGUI upcoming) {
		this.page = upcoming;
		if (inv.getSize() == 27) this.slot = 18;
		else this.slot = 45;
		createGuiItem(Material.ARROW, slot, "§bBack", "§3Go back to the previous page.");
		createGuiItem(Material.ARROW, slot+8, "§bForward", "§3Go to the next page.");
	}
	public void registerEvents(HarmonyGUIListener listener) {
		listeners.add(listener);
	}
	
	public void unregisterEvents(HarmonyGUIListener listener) {
		listeners.remove(listener);
	}
	
	public void open() {	
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		player.openInventory(inv);
	}
	public void close() {
		player.closeInventory();
	}
	
	public void createGuiSkull(String username, int slot, String name, String... lore) {
        final ItemStack item = new ItemStack(Material.SKULL, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        meta.setOwner(username);
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }
	
	public void createGuiItem(Material material, int slot, String name, String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }
	
	public void createGuiItem(Material material, int slot, String name, List<String> lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }
	
	public void createBooleanItem(Material material, int slot, String boolname, String name, String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        bools.put(item, boolname);
        bvars.put(boolname, false);
        inv.setItem(slot, item);
	}
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getPlayer().getName() == player.getName()) {
			if (event.getView().getTitle().equals(title)) {
				HandlerList.unregisterAll(this);
			}	 
		}
	}
	public boolean getBoolean(String name) {
		return bvars.getOrDefault(name, false);
	}
	public HashMap<String, Boolean> getBooleans() {
		return bvars;
	}
	
	public void setBooleanValue(String name, boolean value) {
		for (Object key : bools.keySet()) {
			if (bools.get(key).equals(name)) {
				for (int i=0;i<inv.getContents().length;i++) {
					if (inv.getItem(i) == null) continue;
					ItemStack its = inv.getItem(i);
					if (its.equals(key)) {
						if (value) toggleOn(key, i);
						else toggleOff(key, i);
						break;
					}
				}
				break;
			}
		}
	}
	
	private void toggleOn(Object key, int slot) {
		bvars.put(bools.get(key), true);
		String bname = bools.get(key);
		bools.remove(key);
		ItemStack its = Glow.addGlow((ItemStack) key);
		bools.put(its, bname);
		inv.setItem(slot, its);
	}
	private void toggleOff(Object key, int slot) {
		bvars.put(bools.get(key), false);
		String bname = bools.get(key);
		bools.remove(key);
		ItemStack its = Glow.removeGlow((ItemStack) key);
		bools.put(its, bname);
		inv.setItem(slot, its);
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player whoclicked = (Player) event.getWhoClicked();
		if (player.getName() == whoclicked.getName() && event.getView().getTitle().equals(title)) {
			if (event.getSlot() == -999 || event.getInventory().getItem(event.getSlot()) == null) return;
			event.setCancelled(true);
			if (slot != 0 && event.getSlot() == slot) {
				for (int i=0;i<listeners.size();i++) {
					listeners.get(i).onBack(new GUIBackEvent());
				}
				close();
				back.open();
			}
			else if (slot != 0 && event.getSlot() == slot + 8 && page != null) {
				close();
				page.open();
			}
			else {
				boolean isBool = false;
				for (Object key : bools.keySet()) {
					if (event.getInventory().getItem(event.getSlot()).equals(key)) {
						isBool = true;
						boolean stg = bvars.get(bools.get(key));
						GUIBooleanUpdateEvent bupdate = new GUIBooleanUpdateEvent(bools.get(key), !stg, this);
						GUIBooleanOnEvent bon = new GUIBooleanOnEvent(bools.get(key), this);
						GUIBooleanOffEvent boff = new GUIBooleanOffEvent(bools.get(key), this);
						for (int i=0;i<listeners.size();i++) {
							if (bupdate.getCancelled()) continue;
							listeners.get(i).onBooleanUpdate(bupdate);
							if (!stg) {
								listeners.get(i).onBooleanOn(bon);
								if (bon.getCancelled()) continue;
								toggleOn(key, event.getSlot());
							}
							else {
								listeners.get(i).onBooleanOff(boff);
								if (boff.getCancelled()) continue;
								toggleOff(key, event.getSlot());
							}		
						}
						break;
					}
				}
				if (!isBool) {
					for (int i=0;i<listeners.size();i++) {
						listeners.get(i).onClick(new GUIClickEvent(event.getSlot(), event.getInventory().getItem(event.getSlot()), this));
					}
				}
			}
		}
	}	
}
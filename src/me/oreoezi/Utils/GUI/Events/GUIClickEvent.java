package me.oreoezi.Utils.GUI.Events;

import org.bukkit.inventory.ItemStack;

import me.oreoezi.Utils.GUI.HarmonyGUI;

public class GUIClickEvent {
	private int slot;
	private ItemStack itemstack;
	private HarmonyGUI gui;
	public GUIClickEvent(int slot, ItemStack itemstack, HarmonyGUI gui) {
		this.slot = slot;
		this.itemstack = itemstack;
		this.gui = gui;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public ItemStack getClickedItem() {
		return itemstack;
	}
	
	public HarmonyGUI getGUI() {
		return gui;
	}
}

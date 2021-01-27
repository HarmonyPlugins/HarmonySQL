package me.oreoezi.Utils.GUI.Events;

import me.oreoezi.Utils.GUI.HarmonyGUI;

public class GUIBooleanUpdateEvent {
	private String name;
	private boolean cancelled = false;
	private boolean value;
	private HarmonyGUI gui;
	public GUIBooleanUpdateEvent(String name, boolean value, HarmonyGUI gui) {
		this.name = name;
		this.value = value;
		this.gui = gui;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getValue() {
		return value;
	}
	
	public void setCancelled(boolean can) {
		cancelled = can;
	}
	public boolean getCancelled() {
		return cancelled;
	}
	public HarmonyGUI getGUI() {
		return gui;
	}
}

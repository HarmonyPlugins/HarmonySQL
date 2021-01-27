package me.oreoezi.Utils.GUI.Events;

import me.oreoezi.Utils.GUI.HarmonyGUI;

public class GUIBooleanOffEvent {
	private String name;
	private boolean cancelled = false;
	private HarmonyGUI gui;
	public GUIBooleanOffEvent(String name, HarmonyGUI gui) {
		this.name = name;
		this.gui = gui;
	}
	public void setCancelled(boolean can) {
		cancelled = can;
	}
	public boolean getCancelled() {
		return cancelled;
	}
	public String getName() {
		return name;
	}
	public HarmonyGUI getGUI() {
		return gui;
	}
}

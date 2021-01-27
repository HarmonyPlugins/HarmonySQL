package me.oreoezi.Utils.GUI;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.oreoezi.Utils.GUI.Events.GUITextEvent;
import me.oreoezi.main.Main;

public class HarmonyInput implements Listener {
	private Player player;
	private Main main;
	private ArrayList<HarmonyGUIListener> listeners = new ArrayList<HarmonyGUIListener>();
	public HarmonyInput(Main main, Player player) {
		this.player = player;
		this.main = main;
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}
	
	public void registerEvents(HarmonyGUIListener listener) {
		listeners.add(listener);
	}
	
	public void unregisterEvents(HarmonyGUIListener listener) {
		listeners.remove(listener);
	}
	
	
	@EventHandler
	public void onPlayerMessage(AsyncPlayerChatEvent event) {
		if (event.getPlayer().getName() == player.getName()) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			    public void run(){
					for (int i=0;i<listeners.size();i++) {
						listeners.get(i).onSend(new GUITextEvent(event.getMessage()));
					}     
			    }
			}, 0);
			event.setCancelled(true);
			HandlerList.unregisterAll(this);
		}
	}

}

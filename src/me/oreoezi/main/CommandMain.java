package me.oreoezi.main;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.oreoezi.Utils.ConfigUtils;
import me.oreoezi.Utils.GUI.HarmonyGUIListener;
import me.oreoezi.Utils.GUI.HarmonyInput;
import me.oreoezi.Utils.GUI.Events.GUITextEvent;
import me.oreoezi.database.Configs;
import me.oreoezi.gui.SQLGUI;

public class CommandMain implements CommandExecutor{
	private Main main;
	private String prefix;
	public CommandMain(Main main) {
		this.main = main;
		this.prefix = ConfigUtils.getMessage(main.configs, "prefix");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length != 0 && args[0].equals("reload")) {
				if (player.hasPermission("harmonysql.reload")) {
					main.configs = new Configs(main);
					sender.sendMessage(prefix + " " +ConfigUtils.getMessage(main.configs, "messages.admin.config_reloaded"));
				}
			}
			else if (player.hasPermission("harmonysql.admin")) {
				if (main.configs.getConfig("config").getBoolean("security.password")) {
					if (args.length != 0) {
						if (args[0].equals(main.configs.getConfig("config").getString("security.passphrase"))) {
							new SQLGUI(main, player);
						}
						else sender.sendMessage(prefix + " " + ConfigUtils.getMessage(main.configs, "messages.player.invalid_password"));
					}
					else sender.sendMessage(prefix + " " + ConfigUtils.getMessage(main.configs, "messages.player.no_password"));
				}
				else if (main.configs.getConfig("config").getBoolean("security.console")) {
					String code = ConfigUtils.randomString(10);
					Bukkit.getServer().getConsoleSender().sendMessage(prefix + " " + ConfigUtils.getMessage(main.configs, "messages.admin.console_code").replace("%player%", player.getName()).replace("%code%", code));
					sender.sendMessage(prefix + " " +ConfigUtils.getMessage(main.configs, "messages.player.code_sent"));
					HarmonyInput input = new HarmonyInput(main, player);
					input.registerEvents(new HarmonyGUIListener() {
						@Override
						public void onSend(GUITextEvent event) {
							if (event.getText().equals(code)) {
								new SQLGUI(main, player);   
							}
							else sender.sendMessage(prefix + " " +ConfigUtils.getMessage(main.configs, "messages.player.invalid_password"));
						}
					});
				} 
				else {
					new SQLGUI(main, player);
				}
			}
			else sender.sendMessage(prefix + " " + ConfigUtils.getMessage(main.configs, "messages.player.no_access"));
		}
		else sender.sendMessage(prefix + " " + ConfigUtils.getMessage(main.configs, "messages.admin.console_execute"));
		return true;
	}
	

} 

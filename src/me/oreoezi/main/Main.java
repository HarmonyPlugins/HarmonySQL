package me.oreoezi.main;

import org.bukkit.plugin.java.JavaPlugin;

import me.oreoezi.database.Configs;
import me.oreoezi.database.Database;

public class Main extends JavaPlugin {
	public Configs configs;
	public Database database;
	@Override
	public void onEnable() {
		configs = new Configs(this);
		String host = configs.getConfig("config").getString("mysql.host");
		String port = configs.getConfig("config").getString("mysql.port");
		String user = configs.getConfig("config").getString("mysql.username");
		String pass = configs.getConfig("config").getString("mysql.password");
		database = new Database(host,port,user,pass);
		this.getCommand("harmonysql").setExecutor(new CommandMain(this));
	}
}

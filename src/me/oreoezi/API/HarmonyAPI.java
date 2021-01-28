package me.oreoezi.API;

import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;

import me.oreoezi.database.Database;
import me.oreoezi.main.Main;

public class HarmonyAPI {
	private Main main;
	private String db_name;
	private Database db;
	public HarmonyAPI(Plugin main) {
		this.main = (Main) main;
	}
	public void setDatabase(String database_name) throws HarmonySQLException {
		if (main.configs.getConfig("config").getBoolean("security.api_access")) {
			db_name = database_name;
			String host = main.configs.getConfig("config").getString("mysql.host");
			String port = main.configs.getConfig("config").getString("mysql.port");
			String user = main.configs.getConfig("config").getString("mysql.username");
			String pass = main.configs.getConfig("config").getString("mysql.password");
			db = new Database(host,port,user,pass, db_name);
		}
		else throw new HarmonySQLException("API Access Denied");
	}
	public String getDatabase() {
		return db_name;
	}
	public JSONArray executeQuery(String query) {
		return db.GetData(query);
	}
	public void executeSetQuery(String query) {
		db.SetData(query);
	}
}

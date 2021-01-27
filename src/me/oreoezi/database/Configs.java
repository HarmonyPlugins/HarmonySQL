package me.oreoezi.database;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.oreoezi.main.Main;

public class Configs {
	public HashMap<String, FileConfiguration> configs = new HashMap<String, FileConfiguration>();
	private String[] confignames = {"config", "language"};
	public Configs(Main main) {
		for (int i=0;i<confignames.length;i++) {
			File config_file = new File(main.getDataFolder() + "/" + confignames[i] + ".yml");
				FileConfiguration file_config = YamlConfiguration.loadConfiguration(config_file);
				if (!config_file.exists())  {	
					try {
						Reader config_defaults = new InputStreamReader(main.getResource(confignames[i] + ".yml"), "UTF-8");
						file_config.setDefaults(YamlConfiguration.loadConfiguration(config_defaults));
						file_config.options().copyDefaults(true);
						file_config.save(config_file);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				configs.put(confignames[i], file_config);
		}
	}
	
	public FileConfiguration getConfig(String config_name) {
		return configs.get(config_name);
	}
}
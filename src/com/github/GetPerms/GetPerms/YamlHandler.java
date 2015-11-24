package com.github.GetPerms.GetPerms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class YamlHandler {
	private final String YAML_EXTENSION = ".yml";

	@SuppressWarnings("unused")
	private final Main plugin;
	private static FileConfiguration configuration;
	private File configurationFile;

	public YamlHandler(Main plugin) {
		this(plugin, "config");
	}

	public YamlHandler(Main plugin, String fileName) {
		this.plugin = plugin;
		File dataFolder = plugin.getDataFolder();
		configurationFile = new File(dataFolder, fileName + YAML_EXTENSION);
		try {
			// Check and create the plugin data folder
			if (!dataFolder.exists()) {
				dataFolder.mkdirs();
			}

			// Check and create the configuration file
			if (!configurationFile.exists()) {
				configurationFile.createNewFile();
			}

			// Write the default configuration file, if available
			load();
		} catch (Exception e) {
			// TODO
		}
	}

	public final void load() throws FileNotFoundException, IOException, InvalidConfigurationException {
		// Load the configuration
		configuration = YamlConfiguration.loadConfiguration(configurationFile);
	}

	public final void save() {
		try {
			configuration.save(configurationFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Configuration getConfig() {
		return configuration;
	}
}

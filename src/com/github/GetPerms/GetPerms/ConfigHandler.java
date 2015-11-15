package com.github.GetPerms.GetPerms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.bukkit.configuration.Configuration;

public class ConfigHandler {

	private final Main plugin;
	private static Configuration cfg;
	private final File cfgf = new File("plugins/GetPerms/config.yml");
	public String cfgV;
	public boolean firstRun;
	public boolean sendStats;
	public boolean silentMode;
	public boolean autoGen;
	public boolean regenerateOnPluginChange;
	public boolean autoUpdate;
	public boolean autoDownload;
	public boolean disableOnFinish;
	public boolean devBuilds;
	public boolean debugMode;

	public ConfigHandler(Main plugin) {
		this.plugin = plugin;
		cfg = plugin.getConfig();
	}

	public final void load() {
		cfgV = cfg.getString("cfgV", plugin.pluginVersion);
		firstRun = cfg.getBoolean("firstRun", true);
		sendStats = cfg.getBoolean("sendStats", true);
		silentMode = cfg.getBoolean("silentMode", false);
		autoGen = cfg.getBoolean("autoGen", true);
		regenerateOnPluginChange = cfg.getBoolean("regenerateOnPluginChange", true);
		autoUpdate = cfg.getBoolean("autoUpdate", true);
		autoDownload = cfg.getBoolean("autoDownload", true);
		disableOnFinish = cfg.getBoolean("disableOnFinish", false);
		devBuilds = cfg.getBoolean("devBuilds", false);
		debugMode = cfg.getBoolean("debugMode", false);
		save();
	}

	public final void addComments() {
		plugin.reloadConfig();
		Main.configuration = plugin.getConfig();
		cfg = plugin.getConfig();

		PrintWriter printWriter;

		try {
			printWriter = new PrintWriter(new FileWriter(cfgf));
		} catch (IOException e) {
			plugin.PST(e);
			plugin.warn("Error adding comments to config.yml!");
			return;
		}

		cfgV = cfg.getString("cfgV", plugin.pluginVersion);
		firstRun = cfg.getBoolean("firstRun", true);
		sendStats = cfg.getBoolean("sendStats", true);
		silentMode = cfg.getBoolean("silentMode", false);
		autoGen = cfg.getBoolean("autoGen", true);
		regenerateOnPluginChange = cfg.getBoolean("regenerateOnPluginChange", true);
		autoUpdate = cfg.getBoolean("autoUpdate", true);
		autoDownload = cfg.getBoolean("autoDownload", true);
		disableOnFinish = cfg.getBoolean("disableOnFinish", false);
		devBuilds = cfg.getBoolean("devBuilds", false);
		debugMode = cfg.getBoolean("debugMode", false);

		printWriter.println("#GetPerms config file.");
		printWriter.println("");
		printWriter.println("#Config version. Don't change it! Changing may cause undesirable results!");
		printWriter.println("cfgV: \"" + cfgV + "\"");
		printWriter.println("");
		printWriter.println("#Is it the first run?");
		printWriter
				.println("#Don't change it! Sometimes, it stays true even though it's not the first startup. This is");
		printWriter.println(
				"#normal. However, if you want to see one-time messages, set it to true. This manages certain");
		printWriter.println("#messages and actions at startup.");
		printWriter.println("firstRun: " + firstRun);
		printWriter.println("");
		printWriter.println("#Should the plugin send usage stats to metrics.griefcraft.com?");
		printWriter.println("sendStats: " + sendStats);
		printWriter.println("");
		printWriter.println("#Display output on startup/shutdown/generation? Does not display error messages or");
		printWriter.println("#debug messages even if debug is enabled. Will still display 'GetPerms vX.X.X enabled!'");
		printWriter.println("silentMode: " + silentMode);
		printWriter.println("");
		printWriter.println(
				"#Weather or not to automatically generate the permissions files on startup. Please note that the");
		printWriter.println("#files will only generate 20 seconds after this plugin loads.");
		printWriter.println("autoGen: " + autoGen);
		printWriter.println("");
		printWriter.println(
				"#When true, if any plugin is removed, added, or updated, the permission files will be regenerated.");
		printWriter.println("regenerateOnPluginChange: " + regenerateOnPluginChange);
		printWriter.println("");
		printWriter.println(
				"#Weather or not to check for updates. If autoDownload is disabled, you will still get a message");
		printWriter.println("#about available updates in the server console.");
		printWriter.println("autoUpdate: " + autoUpdate);
		printWriter.println("");
		printWriter.println("#Should GetPerms updates be downloaded? Has no effect unless autoUpdate is enabled.");
		printWriter.println("autoDownload: " + autoDownload);
		printWriter.println("");
		printWriter.println("#Disable the plugin once it finishes?");
		printWriter.println("#When true, the commands to regenerate the permissions files will not work!");
		printWriter.println("disableOnFinish: " + disableOnFinish);
		printWriter.println("");
		printWriter.println("#Download the latest dev build? May have bugs and errors.");
		printWriter.println("#If set to false, will only check for the latest recommended build.");
		printWriter.println("devBuilds: " + devBuilds);
		printWriter.println("");
		printWriter.println(
				"#Debug if needed for errors/bugs/info. Please enable it if anything seems to go wrong, or if");
		printWriter.println("#you suspect something is happening that shouldn't be.");
		printWriter.println("debugMode: " + debugMode);

		printWriter.close();
	}

	@SuppressWarnings("unused")
	private final void restore() {
		PrintWriter printWriter;

		try {
			printWriter = new PrintWriter(new FileWriter(cfgf));
		} catch (IOException e) {
			plugin.PST(e);
			plugin.warn("Error adding comments to config.yml!");
			return;
		}

		cfgV = plugin.pluginVersion;
		firstRun = cfg.getBoolean("firstRun", false);
		sendStats = cfg.getBoolean("sendStats", true);
		silentMode = cfg.getBoolean("silentMode", false);
		autoGen = cfg.getBoolean("autoGen", true);
		regenerateOnPluginChange = cfg.getBoolean("regenerateOnPluginChange", true);
		autoUpdate = cfg.getBoolean("autoUpdate", true);
		autoDownload = cfg.getBoolean("autoDownload", true);
		disableOnFinish = cfg.getBoolean("disableOnFinish", false);
		devBuilds = cfg.getBoolean("devBuilds", false);
		debugMode = cfg.getBoolean("debugMode", false);

		printWriter.println("cfgV: \"" + cfgV + "\"");
		printWriter.println("firstRun: " + firstRun);
		printWriter.println("sendStats: " + sendStats);
		printWriter.println("silentMode: " + silentMode);
		printWriter.println("autoGen: " + autoGen);
		printWriter.println("regenerateOnPluginChange: " + regenerateOnPluginChange);
		printWriter.println("autoUpdate: " + autoUpdate);
		printWriter.println("autoDownload: " + autoDownload);
		printWriter.println("disableOnFinish: " + disableOnFinish);
		printWriter.println("devBuilds: " + devBuilds);
		printWriter.println("debugMode: " + debugMode);

		printWriter.close();
	}

	public void save() {
		plugin.saveConfig();
		Main.configuration = plugin.getConfig();
		cfg = plugin.getConfig();
		addComments();
	}

	public void reload() {
		plugin.reloadConfig();
		Main.configuration = plugin.getConfig();
		cfg = plugin.getConfig();
	}

}
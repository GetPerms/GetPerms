package com.github.GetPerms.GetPerms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.bukkit.configuration.Configuration;

public class ConfigHandler{

	private final GetPerms gp;
	private static Configuration cfg;
	private final File cfgf = new File("plugins/GetPerms/config.yml");
	private PrintWriter pw;
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

	public ConfigHandler(GetPerms gp){
		this.gp = gp;
		cfg = gp.getConfig();
	}

	public final void load(){
		cfgV = cfg.getString("cfgV", gp.version);
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

	public final void addComments(){
		gp.reloadConfig();
		GetPerms.cfg = gp.getConfig();
		cfg = gp.getConfig();

		try{
			pw = new PrintWriter(new FileWriter(cfgf));
		}catch (IOException e){
			gp.PST(e);
			gp.warn("Error adding comments to config.yml!");
		}

		cfgV = cfg.getString("cfgV", gp.version);
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

		pw.println("#GetPerms config file.");
		pw.println("");
		pw.println("#Config version. Don't change it! Changing may cause undesirable results!");
		pw.println("cfgV: \"" + cfgV + "\"");
		pw.println("");
		pw.println("#Is it the first run?");
		pw.println("#Don't change it! Sometimes, it stays true even though it's not the first startup. This is");
		pw.println("#normal. However, if you want to see one-time messages, set it to true. This manages certain");
		pw.println("#messages and actions at startup.");
		pw.println("firstRun: " + firstRun);
		pw.println("");
		pw.println("#Should the plugin send usage stats to metrics.griefcraft.com?");
		pw.println("sendStats: " + sendStats);
		pw.println("");
		pw.println("#Display output on startup/shutdown/generation? Does not display error messages or");
		pw.println("#debug messages even if debug is enabled. Will still display 'GetPerms vX.X.X enabled!'");
		pw.println("silentMode: " + silentMode);
		pw.println("");
		pw.println("#Weather or not to automatically generate the permissions files on startup. Please note that the");
		pw.println("#files will only generate 20 seconds after this plugin loads.");
		pw.println("autoGen: " + autoGen);
		pw.println("");
		pw.println("#When true, if any plugin is removed, added, or updated, the permission files will be regenerated.");
		pw.println("regenerateOnPluginChange: " + regenerateOnPluginChange);
		pw.println("");
		pw.println("#Weather or not to check for updates. If autoDownload is disabled, you will still get a message");
		pw.println("#about available updates in the server console.");
		pw.println("autoUpdate: " + autoUpdate);
		pw.println("");
		pw.println("#Should GetPerms updates be downloaded? Has no effect unless autoUpdate is enabled.");
		pw.println("autoDownload: " + autoDownload);
		pw.println("");
		pw.println("#Disable the plugin once it finishes?");
		pw.println("#When true, the commands to regenerate the permissions files will not work!");
		pw.println("disableOnFinish: " + disableOnFinish);
		pw.println("");
		pw.println("#Download the latest dev build? May have bugs and errors.");
		pw.println("#If set to false, will only check for the latest recommended build.");
		pw.println("devBuilds: " + devBuilds);
		pw.println("");
		pw.println("#Debug if needed for errors/bugs/info. Please enable it if anything seems to go wrong, or if");
		pw.println("#you suspect something is happening that shouldn't be.");
		pw.println("debugMode: " + debugMode);

		pw.close();
	}

	@SuppressWarnings("unused")
	private final void restore(){
		try{
			pw = new PrintWriter(new FileWriter(cfgf));
		}catch (IOException e){
			gp.PST(e);
			gp.warn("Error adding comments to config.yml!");
		}

		cfgV = gp.version;
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

		pw.println("cfgV: \"" + cfgV + "\"");
		pw.println("firstRun: " + firstRun);
		pw.println("sendStats: " + sendStats);
		pw.println("silentMode: " + silentMode);
		pw.println("autoGen: " + autoGen);
		pw.println("regenerateOnPluginChange: " + regenerateOnPluginChange);
		pw.println("autoUpdate: " + autoUpdate);
		pw.println("autoDownload: " + autoDownload);
		pw.println("disableOnFinish: " + disableOnFinish);
		pw.println("devBuilds: " + devBuilds);
		pw.println("debugMode: " + debugMode);

		pw.close();
	}

	public void save(){
		gp.saveConfig();
		GetPerms.cfg = gp.getConfig();
		cfg = gp.getConfig();
		addComments();
	}

	public void reload(){
		gp.reloadConfig();
		GetPerms.cfg = gp.getConfig();
		cfg = gp.getConfig();
	}

}
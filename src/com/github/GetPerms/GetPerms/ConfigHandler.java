package com.github.GetPerms.GetPerms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import org.bukkit.configuration.Configuration;

public class ConfigHandler{

	private final GetPerms gp;
	private static Configuration cfg;
	private final File cfgf = new File("plugins/GetPerms/config.yml");
	private PrintWriter pw;
	private boolean firstRun;
	private boolean sendStats;
	private boolean silentMode;
	private boolean autoGen;
	private boolean regenerateOnPluginChange;
	private boolean autoUpdate;
	private boolean autoDownload;
	private boolean debugMode;
	private boolean disableOnFinish;
	private String cfgV;
	private boolean devBuilds;

	public ConfigHandler(GetPerms gp){
		this.gp = gp;
	}

	public final void addComments(){
		Logger log = gp.getLogger();
		gp.reloadConfig();
		cfg = gp.getConfig();

		try{
			pw = new PrintWriter(new FileWriter(cfgf));
		}catch (IOException e){
			gp.PST(e);
			log.warning("Error adding comments to config.yml!");
		}

		cfgV = cfg.getString("cfgV", gp.gpversion);
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
		pw.println("#Config version. DO NOT CHANGE! Changing may cause undesirable results!");
		pw.println("cfgV: \"" + cfgV + "\"");
		pw.println("");
		pw.println("#Is it the first run?");
		pw.println("#DO NOT CHANGE! Even if it's still true after you run it, don't change it.");
		pw.println("#Used to manage certain actions at startup.");
		pw.println("firstRun: " + firstRun);
		pw.println("");
		pw.println("#Should the plugin send usage stats to metrics.griefcraft.com?");
		pw.println("sendStats: " + sendStats);
		pw.println("");
		pw.println("#Display output on startup/shutdown/generation? Does not display error messages or");
		pw.println("#debug messages even if debug is enabled. Will still display 'GetPerms vX.X.X enabled!'");
		pw.println("silentMode: " + silentMode);
		pw.println("");
		pw.println("#Weather or not to automatically generate the permissions files on startup.");
		pw.println("autoGen: " + autoGen);
		pw.println("");
		pw.println("#When true, if any plugin is removed, added, or updated, the permission files will be regenerated.");
		pw.println("regenerateOnPluginChange: " + regenerateOnPluginChange);
		pw.println("");
		pw.println("#Weather or not to check for updates.");
		pw.println("autoUpdate: " + autoUpdate);
		pw.println("");
		pw.println("#Should the newest GetPerms.jar be downloaded?");
		pw.println("autoDownload: " + autoDownload);
		pw.println("");
		pw.println("#Disable the plugin once it finishes?");
		pw.println("#When true, the commands to regenerate the permissions files will not work!");
		pw.println("disableOnFinish: " + disableOnFinish);
		pw.println("");
		pw.println("#Download the latest dev build? May have bugs and errors.");
		pw.println("#If set to false, will only download the latest recommended build.");
		pw.println("devBuilds: " + devBuilds);
		pw.println("");
		pw.println("#Debug if needed for errors/bugs/info.");
		pw.println("debugMode: " + debugMode);

		pw.close();
	}

	public final void restore(){
		Logger log = gp.getLogger();
		cfg = gp.getConfig();
		try{
			pw = new PrintWriter(new FileWriter(cfgf));
		}catch (IOException e){
			gp.PST(e);
			log.warning("Error adding comments to config.yml!");
		}

		cfgV = gp.gpversion;
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

}

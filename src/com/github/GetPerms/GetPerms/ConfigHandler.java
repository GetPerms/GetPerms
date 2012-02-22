package com.github.GetPerms.GetPerms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.bukkit.configuration.Configuration;

public class ConfigHandler {

	private GetPerms gp;
	private static Configuration cfg;
	private File cfgf = new File("plugins/GetPerms/config.yml");
	private PrintWriter pw;
	private boolean firstRun;
	private boolean autoGen;
	private boolean autoUpdate;
	private boolean autoDownload;
	private boolean debugMode;
	private boolean disableOnFinish;

	public ConfigHandler(GetPerms gp)
	{
		this.gp = gp;
	}

	public final void change() {
		cfg = gp.getConfig();

		try {
			pw = new PrintWriter(new FileWriter(cfgf));
		} catch (IOException e) {
			gp.PST(e);
			gp.getLogger().warning("Error adding comments to config.yml!");
		}

		firstRun = cfg.getBoolean("firstRun");
		autoGen = cfg.getBoolean("autoGen");
		autoUpdate = cfg.getBoolean("autoUpdate");
		autoDownload = cfg.getBoolean("autoDownload");
		debugMode = cfg.getBoolean("debugMode");
		disableOnFinish = cfg.getBoolean("disableOnFinish");

		pw.println("#GetPerms config file");
		pw.println("");
		pw.println("#Is it the first run?");
		pw.println("#Do not change. It may still be true after you run it,");
		pw.println("# but just leave it. It is supposed to do that Leave it.");
		pw.println("#Leave it. Don't change it. Let it be.");
		pw.println("firstRun: "+firstRun);
		pw.println("");
		pw.println("#Weather or not to automatically generate the permissions files on startup");
		pw.println("autoGen: "+autoGen);
		pw.println("");
		pw.println("#Weather or not to check for updates");
		pw.println("autoUpdate: "+autoUpdate);
		pw.println("");
		pw.println("#Should it automatically download the newest GetPerms.jar?");
		pw.println("autoDownload: "+autoDownload);
		pw.println("");
		pw.println("#Disable the plugin once it finishes?");
		pw.println("disableOnFinish: "+disableOnFinish);
		pw.println("");
		pw.println("#Debug if needed");
		pw.println("debugMode: "+debugMode);

		pw.close();
	}

}

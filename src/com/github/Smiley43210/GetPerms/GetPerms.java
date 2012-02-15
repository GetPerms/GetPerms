package com.github.Smiley43210.GetPerms;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GetPerms extends JavaPlugin {
	
	Logger log = Logger.getLogger("Minecraft");
	PluginManager pm = Bukkit.getServer().getPluginManager();
	private int length;
	private Plugin[] pluginlist;
	private int i;
	private int j = 0;
	
	public void onEnable(){ 
		log.info("[GetPerms] Loading GetPerms 0.0.1");
		log.info("[GetPerms] GetPerms 0.0.1 loaded successfully");
		pluginlist = pm.getPlugins();
		i = pm.getPlugins().length;
		for (int j = 0; j < i; j++) {  // i indexes each element successively.
			pluginlist.;
		}
		log.info("[GetPerms]");
		log.info("[GetPerms] Loaded plugins");
		log.info("[GetPerms] Gathering permission nodes...");
		log.info("[GetPerms] Compiled permission nodes into permission.txt in plugins/GetPerms");
		
	}
	 
	public void onDisable(){ 
		log.info("[GetPerms] GetPerms 0.0.1 unloaded");
	}
	
}

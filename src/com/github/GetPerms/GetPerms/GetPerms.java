package com.github.GetPerms.GetPerms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.StringBuilder;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class GetPerms extends JavaPlugin {

	Logger log = Logger.getLogger("Minecraft");
	PluginManager pm = Bukkit.getServer().getPluginManager();
	private Plugin[] pluginlist;
	private List<Permission> plist;
	private File file1 = new File("pnodes.txt");
	private File file2 = new File("pnodesfull.txt");
	private PrintWriter pw1;
	private PrintWriter pw2;
	
	public void onEnable(){ 
		try {
			pw1 = new PrintWriter(new FileWriter(file1));
			pw2 = new PrintWriter(new FileWriter(file2));
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("[GetPerms] GetPerms 0.0.1 loaded successfully");
		pluginlist = pm.getPlugins();
		for (Plugin p : pluginlist) {
			log.info(p.getDescription().getName());
			plist = p.getDescription().getPermissions();
			for (Permission pr : plist) {
		    	//Write each permission to file
				pw1.println((new StringBuilder()).append(pr.getName()).toString());
				pw2.println((new StringBuilder()).append(pr.getName()).append(" - ").append(pr.getDescription()).toString());
		    }
		}
		pw1.close();
		pw2.close();
		log.info("[GetPerms] Retrieved plugin list!");
		log.info("[GetPerms] Gathering permission nodes...");
		log.info("[GetPerms] Arranging permission nodes...");
		//log.info("[GetPerms] Compiled permission nodes into 'pnodes.txt' and");
		//log.info("[GetPerms] 'pnodesfull.txt' in '/plugins/GetPerms'.");
		log.info("[GetPerms] Finished! Stopping...");
	}
	 
	public void onDisable(){ 
		log.info("[GetPerms] GetPerms 0.0.1 unloaded");
	}
	
}
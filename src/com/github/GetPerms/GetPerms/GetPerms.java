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

	private String version = "0.0.3";
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
		pluginlist = pm.getPlugins();
		log.info(new StringBuilder().append("[GetPerms] GetPerms ").append(version).append(" loaded").toString());
		log.info("[GetPerms] Retrieved plugin list!");
		log.info("[GetPerms] Gathering permission nodes...");
		for (Plugin p : pluginlist) {
			//DEBUG log.info(p.getDescription().getName());
			plist = p.getDescription().getPermissions();
			if (!plist.isEmpty()) {
				pw2.println((new StringBuilder()).append("----").append(p.getDescription().getName()).append("----").toString());
			}
			for (Permission pr : plist) {
				pw1.println((new StringBuilder()).append(pr.getName()).toString());
				if (pr.getDescription() == "") {
					pw2.println((new StringBuilder()).append(pr.getName()).append(" - ").append("No description given").toString());
				}
				else {
					pw2.println((new StringBuilder()).append(pr.getName()).append(" - ").append(pr.getDescription()).toString());
				}
		    }
			pw2.println("");
		}
		pw1.close();
		pw2.close();
		log.info("[GetPerms] Arranging permission nodes...");
		log.info("[GetPerms] Compiled permission nodes into 'pnodes.txt' and");
		log.info("[GetPerms] 'pnodesfull.txt' in the server root folder.");
		
	}

	public void onDisable(){ 
		log.info(new StringBuilder().append("[GetPerms] GetPerms ").append(version).append(" unloaded").toString());
	}

}
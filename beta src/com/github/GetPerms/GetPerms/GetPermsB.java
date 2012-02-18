package com.github.GetPerms.GetPerms;

import java.util.List;
import java.util.logging.Logger;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GetPermsB extends JavaPlugin {

	private final l l = null;
	private final WriteToFile WTF = null;
	public String version = "0.0.3";
	Logger mlog = Logger.getLogger("Minecraft");
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
		l.log(new StringBuilder().append("GetPerms ").append(version).append(" loaded").toString());
		for (Plugin p : pluginlist) {
		    System.out.println(p.getDescription().getName());
		    plist = p.getDescription().getPermissions();
		    for (Permission pr : plist) {
		    	//Write each permission to file
		    	try {
					WTF.Write(pr.getName(), file1);
					WTF.Write(pr.getName(), pr.getDescription(), file2);
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		}
		l.log("Retrieved plugin list!");
		l.log("Gathering permission nodes...");
		l.log("Arranging permission nodes...");
		l.log("Compiled permission nodes into 'pnodes.txt' and");
		l.log("'pnodesfull.txt' in '/plugins/GetPerms'.");
	}
	 
	public void onDisable(){ 
		l.log(new StringBuilder().append("GetPerms ").append(version).append(" unloaded").toString());
	}
	
}
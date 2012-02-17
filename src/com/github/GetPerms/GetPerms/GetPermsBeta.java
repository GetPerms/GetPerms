package com.github.GetPerms.GetPerms;

import java.util.List;
import java.util.logging.Logger;
import java.io.IOException;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GetPermsBeta extends JavaPlugin {

	Logger mlog = Logger.getLogger("Minecraft");
	PluginManager pm = Bukkit.getServer().getPluginManager();
	private Plugin[] pluginlist;
	private List<Permission> plist;
	private WriteToFile WTF;
	public l l;
	private File file1 = new File("pnodes.txt");
	private File file2 = new File("pnodesfull.txt");

	public void onEnable(){ 
		l.log("GetPerms 0.0.0 loaded successfully");
		pluginlist = pm.getPlugins();
		for (Plugin p : pluginlist) {
		    System.out.println(p.getDescription().getName());
		    plist = p.getDescription().getPermissions();
		    for (Permission pr : plist) {
		    	//Write each permission to file
		    	try {
					WTF.Write(pr.getName(), file1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
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
		l.log("Finished! Stopping...");
		onDisable();
	}
	 
	public void onDisable(){ 
		l.log("GetPerms 0.0.0 unloaded");
	}
	
}
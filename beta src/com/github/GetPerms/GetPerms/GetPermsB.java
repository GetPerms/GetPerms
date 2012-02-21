package com.github.GetPerms.GetPerms;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.lang.StringBuilder;
import java.util.List;
import java.util.logging.Logger;

import com.github.GetPerms.GetPerms.*;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GetPermsB extends JavaPlugin {

	public final String version = "0.0.3";
	l l;
	WriteToFile WTF;
	PluginManager pm = Bukkit.getServer().getPluginManager();
	public Plugin[] pluginlist;
	private File file1 = new File("pnodes.txt");
	private File file2 = new File("pnodesfull.txt");
	public PrintWriter pw1;
	public PrintWriter pw2;
	private FileConfiguration cfg;

	@Override
	public void onEnable() { 
		//cfgDef();
		//createCfg();
		try {
			pw1 = new PrintWriter(new FileWriter(file1));
			pw2 = new PrintWriter(new FileWriter(file2));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pluginlist = pm.getPlugins();
		l.log(new StringBuilder().append("GetPerms ").append(version).append(" enabled!").toString());
		checkForUpdates();
		l.log("Retrieved plugin list!");
		l.log("Gathering permission nodes...");
		for (Plugin p : pluginlist) {
		    try {
				WTF.Write(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		    if (!WTF.plist.isEmpty()) {
		    	pw2.println("");
		    }
		}
		l.log("Compiled permission nodes into 'pnodes.txt' and");
		l.log("'pnodesfull.txt' in the server root folder.");
	}
	@Override
	public void onDisable() { 
		l.log(new StringBuilder().append("GetPerms ").append(version).append(" unloaded").toString());
	}

	private final void checkForUpdates() {
		String check = "https://raw.github.com/GetPerms/GetPerms/master/Changelog.txt";
		try {
			URL client = new URL(check);
			BufferedReader buf = new BufferedReader(new InputStreamReader(client.openStream()));
			String line = buf.readLine();
			if(newer(version, line))
				l.log("Newer GetPerms version" + line + " is available for download, you can get it at https://raw.github.com/GetPerms/GetPerms/master/GetPerms.jar or http://dev.bukkit.org/server-mods/getperms/files");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final boolean newer(String current, String check) {
		boolean result = false;
		String[] currentVersion = current.split("\\.");
		String[] checkVersion = check.split("\\.");
		int i = Integer.parseInt(currentVersion[0]);
		int j = Integer.parseInt(checkVersion[0]);
		if(i>j)
			result = false;
		else if(i==j){
			i = Integer.parseInt(currentVersion[1]);
			j = Integer.parseInt(checkVersion[1]);
			if(i>j)
				result = false;
			else if(i == j){
				i = Integer.parseInt(currentVersion[2]);
				j = Integer.parseInt(checkVersion[2]);
				if(i >= j)
					result = false;
				else
					result = true;
			}else
				result = true;
		}else
			result = true;
		return result;
	}
	
	private final void createCfg() {
		cfg = this.getConfig();
		cfg.options().copyDefaults(true);
		this.saveConfig();
		//just getting ready for when the bleeding edge stuff comes out
	}

	private final void cfgDef() {
		cfg.addDefault("autoGenerate", true);
		cfg.addDefault("debugOutput", false);
	}

}
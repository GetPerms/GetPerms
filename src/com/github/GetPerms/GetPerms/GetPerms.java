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

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GetPerms extends JavaPlugin {

	public String gpversion;
	WriteToFile WTF;
	PluginManager pm = Bukkit.getServer().getPluginManager();
	public Plugin[] pluginlist;
	private File file1 = new File("pnodes.txt");
	private File file2 = new File("pnodesfull.txt");
	public PrintWriter pw1;
	public PrintWriter pw2;
	public static PluginDescriptionFile pdf;
	public static Configuration cfg;

	@Override
	public void onEnable() { 
		WTF = new WriteToFile(this);
		pdf = this.getDescription();
		gpversion = pdf.getVersion();
		gpCreateCfg();
		//gpCreateMisc();
		try {
			pw1 = new PrintWriter(new FileWriter(file1));
			pw2 = new PrintWriter(new FileWriter(file2));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pluginlist = pm.getPlugins();
		getLogger().info(new StringBuilder().append("GetPerms ").append(gpversion).append(" enabled!").toString());
		getLogger().info("GetPerms is the work of Smiley43210, with the help of Tahkeh, wwsean08, and desmin88.");
		gpCheckForUpdates();
		getLogger().info("Retrieved plugin list!");
		getLogger().info("Retrieving permission nodes...");
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
		pw1.close();
		pw2.close();
		getLogger().info("Compiled permission nodes into 'pnodes.txt' and");
		getLogger().info("'pnodesfull.txt' in the server root folder.");
	}
	@Override
	public void onDisable() { 
		getLogger().info(new StringBuilder().append("GetPerms ").append(gpversion).append(" unloaded").toString());
	}
	private final void gpCheckForUpdates() {
		String check = "https://raw.github.com/GetPerms/GetPerms/master/ver";
		try {
			URL client = new URL(check);
			BufferedReader buf = new BufferedReader(new InputStreamReader(client.openStream()));
			String line = buf.readLine();
			if(gpnewer(gpversion, line))
				getLogger().info("Newer GetPerms version" + line + " is available for download, you can get it at https://raw.github.com/GetPerms/GetPerms/master/GetPerms.jar or http://dev.bukkit.org/server-mods/getperms/files");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final boolean gpnewer(String current, String check) {
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
	
	private final void gpCreateCfg() {
		cfg = this.getConfig();
		cfg.options().copyDefaults(true);
		this.saveConfig();
		//just getting ready for when the bleeding edge stuff comes out
	}
	
	@SuppressWarnings("unused")
	private final void gpCreateMisc() {
		if (!new File(getDataFolder(), "plugins.yml").exists()) {
			try {
			getDataFolder().mkdir();
			new File(getDataFolder(), "plugins.yml").createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("[GetPerms] Error occurred while creating plugins.yml (plugin list)!");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}
	}

}
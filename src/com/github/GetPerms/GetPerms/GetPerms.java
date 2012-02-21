package com.github.GetPerms.GetPerms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.StringBuilder;
import java.util.List;
import java.util.logging.Logger;

import com.github.GetPerms.GetPerms.*;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

@SuppressWarnings("unused")
public class GetPerms extends JavaPlugin {

	private String version;
	private String PN = "[GetPerms] ";
	Logger log = Logger.getLogger("Minecraft");
	PluginManager pm = Bukkit.getServer().getPluginManager();
	private Plugin[] pluginlist;
	private List<Permission> plist;
	private File file1 = new File("pnodes.txt");
	private File file2 = new File("pnodesfull.txt");
	private PrintWriter pw1;
	private PrintWriter pw2;
	public static PluginDescriptionFile pdf;
	public static Configuration cfg;

	public void onEnable(){

		pdf = this.getDescription();
		version = pdf.getVersion();
		if (!new File(getDataFolder(), "config.yml").exists()) {
			try {
			getDataFolder().mkdir();
			new File(getDataFolder(), "config.yml").createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(PN+"Error occurred while creating config.yml!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}
		try {
			pw1 = new PrintWriter(new FileWriter(file1));
			pw2 = new PrintWriter(new FileWriter(file2));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pluginlist = pm.getPlugins();
		log.info(new StringBuilder().append(PN+"GetPerms ").append(version).append(" loaded").toString());
		log.info(PN+"GetPerms is the work of Smiley43210, with the help of Tahkeh, wwsean08, and desmin88.");
		log.info(PN+"Retrieved plugin list!");
		log.info(PN+"Gathering permission nodes...");
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
		log.info(PN+"Arranging permission nodes...");
		log.info(PN+"Compiled permission nodes into 'pnodes.txt' and");
		log.info(PN+"'pnodesfull.txt' in the server root folder.");

	}

	public void onDisable(){ 
		log.info(new StringBuilder().append(PN+"GetPerms ").append(version).append(" unloaded").toString());
	}

}
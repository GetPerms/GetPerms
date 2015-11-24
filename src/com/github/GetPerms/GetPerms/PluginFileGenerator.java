package com.github.GetPerms.GetPerms;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.List;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PluginFileGenerator extends BukkitRunnable {

	private Main plugin;

	public PluginFileGenerator(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		plugin.info("Generating files...");
		TempRetrieval tr = new TempRetrieval(plugin);
		Plugin[] pluginList = plugin.getServer().getPluginManager().getPlugins();
		plugin.debug("Retrieved plugin list!");
		plugin.debug("Retrieving permission nodes...");
		try {
			PrintWriter printWriter1 = new PrintWriter(new FileWriter(plugin.permissionNodes));
			PrintWriter printWriter2 = new PrintWriter(new FileWriter(plugin.permissionNodesDesc));

			for (Plugin loadedPlugin : pluginList) {
				List<Permission> permissionList = loadedPlugin.getDescription().getPermissions();

				if (permissionList.isEmpty()) {
					printWriter2.println("");
				} else {
					printWriter2.println("----" + loadedPlugin.getDescription().getName() + "----");

					for (Permission permission : permissionList) {
						printWriter1.println(permission.getName());

						printWriter2.print(permission.getName() + " - ");
						if (permission.getDescription() == "") {
							printWriter2.println("No description given");
						} else {
							printWriter2.println(permission.getDescription());
						}
					}
				}
			}
			printWriter1.close();
			printWriter2.close();
		} catch (IOException e) {
			plugin.printStackTrace(e);
		}
		plugin.info("Compiled permission nodes into 'permission_nodes.txt' and");
		plugin.info("'permission_nodes_desc' in plugins/" + plugin.getName() + "/.");
		try {
			tr.Get();
		} catch (MalformedURLException e) {
			plugin.printStackTrace(e);
		} catch (IOException e) {
			plugin.printStackTrace(e);
		}
		plugin.info("Permission lists generated!");
	}

}

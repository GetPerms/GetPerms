package com.github.GetPerms.GetPerms;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

public class PermissionTask implements Runnable {

	private Main plugin;

	public PermissionTask(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		plugin.info("Generating files...");
		Plugin[] pluginList = plugin.getServer().getPluginManager().getPlugins();

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
		plugin.info("Compiled permission nodes into '" + Main.PERMISSIONS_FILENAME + "' and");
		plugin.info(
				"'" + Main.PERMISSIONS_DESCRIPTION_FILENAME + "' in plugins/" + plugin.getName() + "/permissions/.");

		DatabaseDownloader downloader = new DatabaseDownloader(plugin, plugin.permissionFolder);
		downloader.getPluginPermissions();
		plugin.info("Permission lists generated!");
	}

}

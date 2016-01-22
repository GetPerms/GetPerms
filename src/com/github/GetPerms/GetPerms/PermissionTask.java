package com.github.getperms.getperms;

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

			for (int i = 0; i < pluginList.length; i++) {
				List<Permission> permissionList = pluginList[i].getDescription().getPermissions();

				if (!permissionList.isEmpty()) {
					printWriter2.println("----" + pluginList[i].getDescription().getName() + "----");

					for (int j = 0; j < permissionList.size(); j++) {
						printWriter1.print(permissionList.get(j).getName());

						printWriter2.print(permissionList.get(j).getName() + " - ");
						if (permissionList.get(j).getDescription() == "") {
							printWriter2.print("No description given");
						} else {
							printWriter2.print(permissionList.get(j).getDescription());
						}

						if (j + 1 < permissionList.size()) {
							printWriter1.println();

							printWriter2.println();
						}
					}

					if (i + 1 < pluginList.length) {
						printWriter1.println();

						printWriter2.println();
						printWriter2.println();
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

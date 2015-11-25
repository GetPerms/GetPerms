package com.github.GetPerms.GetPerms;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import org.bukkit.Bukkit;

public class TempRetrieval {

	private Main plugin;
	private File permissionsFile = new File("Essentials_permission_nodes.txt");
	private File permissionsDescriptionsFile = new File("Essentials_permission_nodes_desc.txt");

	public TempRetrieval(Main plugin) {
		this.plugin = plugin;
	}

	public void getPermissionLists() throws MalformedURLException, IOException {
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("Essentials")) {
			plugin.debug("Downloading Essentials node lists...");

			try {
				plugin.downloadFile("https://raw.github.com/GetPerms/GetPerms/master/db/Essentials/pnodes.txt",
						permissionsFile);
			} catch (NoSuchAlgorithmException e) {
				plugin.printStackTrace(e);
			}

			try {
				plugin.downloadFile("https://raw.github.com/GetPerms/GetPerms/master/db/Essentials/pnodesfull.txt",
						permissionsDescriptionsFile);
			} catch (NoSuchAlgorithmException e) {
				plugin.printStackTrace(e);
			}

			plugin.info("Downloaded Essentials plugin node lists to");
			plugin.info("Essentials_permission_nodes.txt and Essentials_permission_nodes_desc.txt");
		}
	}
}

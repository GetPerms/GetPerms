package com.github.GetPerms.GetPerms;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import org.bukkit.Bukkit;

public class TempRetrieval {

	private Main plugin;
	private File permissionsFile = new File("Essentials_permission_nodes_desc.txt");
	private File permissionsDescriptionsFile = new File("Essentials_permission_nodes.txt");

	public TempRetrieval(Main plugin) {
		this.plugin = plugin;
	}

	public void Get() throws MalformedURLException, IOException {
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("Essentials")) {
			plugin.debug("Downloading Essentials node lists...");
			Main.downloadFile("https://raw.github.com/GetPerms/GetPerms/master/db/Essentials/pnodesfull.txt", permissionsFile);
			Main.downloadFile("https://raw.github.com/GetPerms/GetPerms/master/db/Essentials/pnodes.txt", permissionsDescriptionsFile);
			plugin.info("Downloaded Essentials plugin node lists to");
			plugin.info("EssentialsPnodes.txt and EssentialsPnodesfull.txt");
		}
	}
}

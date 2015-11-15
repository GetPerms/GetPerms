package com.github.GetPerms.GetPerms;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import org.bukkit.Bukkit;

public class TempRetrieval {

	private Main gp;
	private File epnf = new File("EssentialsPnodesfull.txt");
	private File epn = new File("EssentialsPnodes.txt");

	public TempRetrieval(Main gp) {
		this.gp = gp;
	}

	public void Get() throws MalformedURLException, IOException {
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("Essentials")) {
			gp.debug("Downloading Essentials node lists...");
			Main.dlFile("https://raw.github.com/GetPerms/GetPerms/master/db/Essentials/pnodesfull.txt", epnf);
			Main.dlFile("https://raw.github.com/GetPerms/GetPerms/master/db/Essentials/pnodes.txt", epn);
			gp.info("Downloaded Essentials plugin node lists to");
			gp.info("EssentialsPnodes.txt and EssentialsPnodesfull.txt");
		}
	}
}

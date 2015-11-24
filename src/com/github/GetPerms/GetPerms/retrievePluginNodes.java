package com.github.GetPerms.GetPerms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class RetrievePluginNodes {

	Main plugin;
	String databaseEntryCheck = "https://raw.github.com/GetPerms/GetPerms/master/db/entries";
	URL databaseEntryChecks;

	public RetrievePluginNodes(Main plugin) {
		this.plugin = plugin;
	}

	public final void getDatabasePlugins() {
		@SuppressWarnings("unused")
		String list = "";
		try {
			databaseEntryChecks = new URL(databaseEntryCheck);
			BufferedReader reader = new BufferedReader(new InputStreamReader(databaseEntryChecks.openStream()));
			list = reader.readLine();
		} catch (MalformedURLException e) {
			plugin.printStackTrace(e);
		} catch (IOException e) {
			plugin.printStackTrace(e);
		}
	}

}

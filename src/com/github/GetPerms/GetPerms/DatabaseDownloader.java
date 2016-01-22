package com.github.getperms.getperms;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class DatabaseDownloader {

	private static final String DATABASE_URL = "https://raw.github.com/GetPerms/GetPerms/master/db/";
	private static final String ENTRIES_FILE = "entries";

	private Main plugin;

	public DatabaseDownloader(Main plugin, File destinationFolder) {
		this.plugin = plugin;
	}

	public final void getPluginPermissions() {
		try {
			URL databaseEntryListURL = new URL(DATABASE_URL + ENTRIES_FILE);
			BufferedReader reader = new BufferedReader(new InputStreamReader(databaseEntryListURL.openStream()));

			while (true) {
				String databaseEntry = reader.readLine();

				if (databaseEntry == null) {
					reader.close();
					return;
				}

				Plugin[] pluginList = Bukkit.getPluginManager().getPlugins();

				for (Plugin loadedPlugin : pluginList) {
					String pluginName = loadedPlugin.getDescription().getName();

					if (pluginName.equalsIgnoreCase(databaseEntry)) {
						try {
							plugin.downloadFile(DATABASE_URL + databaseEntry + "/" + Main.PERMISSIONS_FILENAME,
									new File(plugin.permissionFolder, pluginName + "_" + Main.PERMISSIONS_FILENAME));
						} catch (NoSuchAlgorithmException e) {
							plugin.printStackTrace(e);
						}

						try {
							plugin.downloadFile(
									DATABASE_URL + databaseEntry + "/" + Main.PERMISSIONS_DESCRIPTION_FILENAME,
									new File(plugin.permissionFolder,
											pluginName + "_" + Main.PERMISSIONS_DESCRIPTION_FILENAME));
						} catch (NoSuchAlgorithmException e) {
							plugin.printStackTrace(e);
						}
					}
				}
			}
		} catch (MalformedURLException e) {
			plugin.printStackTrace(e);
		} catch (IOException e) {
			plugin.printStackTrace(e);
		}
	}

}

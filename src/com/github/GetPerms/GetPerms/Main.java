package com.github.GetPerms.GetPerms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlException;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public ConfigHandler configHandler;
	private PluginManager pluginManager;
	protected String pluginVersion;
	private final File dataFolder = getDataFolder();
	private YamlHandler pluginListHandler = new YamlHandler(this, "plugins");
	protected final File permissionNodes = new File(dataFolder, "permission_nodes.txt");
	protected final File permissionNodesDesc = new File(dataFolder, "permission_nodes_desc.txt");
	private final File readMe = new File(dataFolder, "ReadMe.txt");
	private final File changeLog = new File(dataFolder, "Changelog.txt");
	private final File updateFolder = new File(dataFolder, "update");
	private final File temporaryDownloadFile = new File(dataFolder, "temporaryDownload");
	private final File updateDownloadFile = new File(updateFolder, "GetPerms.jar");
	public Configuration configuration;
	public Logger logger;
	private boolean generationFinished = false;

	@Override
	public void onEnable() {
		// Check and create the plugin data folder
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}

		configuration = getConfig();
		configuration.options().copyDefaults(true);
		saveConfig();

		configHandler = new ConfigHandler(this);
		configuration = getConfig();
		PluginDescriptionFile pluginDescriptionFile = getDescription();
		pluginVersion = pluginDescriptionFile.getVersion();
		logger = getLogger();
		configHandler.load();

		pluginManager = Bukkit.getServer().getPluginManager();
		debugValues();

		if (configuration.getBoolean("sendStats")) {
			info("Sending usage stats to mcstats.org every 15 minutes.");
			try {
				Metrics metrics = new Metrics(this);
				metrics.start();
			} catch (IOException e) {
				printStackTrace(e);
			}
		} else {
			info("The option to enable sending usage stats is in the configuration.");
		}
		debug("Configuration version: " + configuration.getString("cfgV") + " Plugin version: " + pluginVersion);
		if (!configuration.getString("cfgV").equalsIgnoreCase(pluginVersion)) {
			debug("Config version does not match jar version.");
			getStartFiles();
			cleanOldFiles();
		} else if (configuration.getBoolean("firstRun", true)) {
			debug("firstRun is set to true. Setting to false...");
			configuration.set("firstRun", false);
			getStartFiles();
		}
		configuration.set("cfgV", pluginVersion);

		configHandler.save();

		CommandHandler commandHandler = new CommandHandler(this);
		getCommand("getperms").setExecutor(commandHandler);
		getCommand("gp").setExecutor(commandHandler);

		info("GetPerms " + pluginVersion + " enabled!");

		if (configuration.getBoolean("autoUpdate", true)) {
			checkForUpdates();
		}

		if (configuration.getBoolean("regenerateOnPluginChange", true)) {
			debug("Checking for plugin changes...");

			if (!comparePlugins()) {
				debug("Changes found! Regenerating files...");
				generateFiles(true);
				generationFinished = true;
			}
		}

		writePluginList();

		if (configuration.getBoolean("autoGenerate", true)) {
			debug("autoGenerate enabled");
			generateFiles(true);
			generationFinished = true;
		}

		if (configuration.getBoolean("disableOnFinish", false)) {
			debug("DisableOnFinish enabled");
			info("Finished! Disabling...");
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	@Override
	public void onDisable() {
		configHandler.load();
		configHandler.addComments();
		info("GetPerms disabled");
	}

	private final void debugValues() {
		String cfgV = pluginVersion;
		boolean firstRun = configuration.getBoolean("firstRun", false);
		boolean sendStats = configuration.getBoolean("sendStats", true);
		boolean silentMode = configuration.getBoolean("silentMode", false);
		boolean autoGenerate = configuration.getBoolean("autoGenerate", true);
		boolean regenerateOnPluginChange = configuration.getBoolean("regenerateOnPluginChange", true);
		boolean autoUpdate = configuration.getBoolean("autoUpdate", true);
		boolean autoDownload = configuration.getBoolean("autoDownload", true);
		boolean disableOnFinish = configuration.getBoolean("disableOnFinish", false);
		boolean devBuilds = configuration.getBoolean("devBuilds", false);
		boolean debugMode = configuration.getBoolean("debugMode", false);

		config("cfgV: \"" + cfgV + "\"");
		config("firstRun: " + firstRun);
		config("sendStats: " + sendStats);
		config("silentMode: " + silentMode);
		config("autoGenerate: " + autoGenerate);
		config("regenerateOnPluginChange: " + regenerateOnPluginChange);
		config("autoUpdate: " + autoUpdate);
		config("autoDownload: " + autoDownload);
		config("disableOnFinish: " + disableOnFinish);
		config("devBuilds: " + devBuilds);
		config("debugMode: " + debugMode);
	}

	private final void checkForUpdates() {
		boolean devb = configuration.getBoolean("devBuilds");
		String dlurl = "https://raw.github.com/GetPerms/GetPerms/master/checks/dlurl";
		String newestVersion = "https://raw.github.com/GetPerms/GetPerms/master/checks/ver";
		String checkdev = "https://raw.github.com/GetPerms/GetPerms/master/checks/dev";
		String force = "https://raw.github.com/GetPerms/GetPerms/master/checks/force";
		String u = "https://raw.github.com/GetPerms/GetPerms/master/GetPerms.jar";
		String line;
		boolean dv;
		try {
			URL fa = new URL(force);
			BufferedReader fb = new BufferedReader(new InputStreamReader(fa.openStream()));
			String fc = fb.readLine();
			if (!devb) {
				URL dlcheck = new URL(dlurl);
				BufferedReader a = new BufferedReader(new InputStreamReader(dlcheck.openStream()));
				u = a.readLine();
				URL client = new URL(newestVersion);
				BufferedReader buf = new BufferedReader(new InputStreamReader(client.openStream()));
				line = buf.readLine();
				dv = false;
			} else {
				URL client = new URL(checkdev);
				BufferedReader buf = new BufferedReader(new InputStreamReader(client.openStream()));
				line = buf.readLine();
				dv = true;
			}
			if (fc == "true") {
				dv = true;
			}
			if (newer(pluginVersion, line, dv)) {
				if (configuration.getBoolean("autoDownload", true)) {
					info("Newest GetPerms version" + line + " is available.");

					if (!devb) {
						info("Downloading latest recommended build...");
					} else {
						info("Downloading latest developmental build...");
					}

					updateDownloadFile.mkdirs();

					if (downloadUpdate(u, updateDownloadFile)) {
						info("Newest version of GetPerms is located in");
						info("'plugins/" + getName() + "/update/GetPerms.jar'.");
					} else {
						warn("Update file is corrupt! (Contains HTML elements)");
						warn("The update will most likely be available later.");
					}

				} else {
					info("Newest GetPerms version" + line + " is available for download, you can");
					info("get it at " + u);
					info("or the latest dev build at");
					info("https://raw.github.com/GetPerms/GetPerms/master/GetPerms.jar");
				}
			} else {
				info("You have the latest version!");
			}
		} catch (MalformedURLException e) {
			printStackTrace(e);
			warn("Unable to check for updates.");
		} catch (IOException e) {
			printStackTrace(e);
			warn("Unable to check for updates.");
		} catch (AccessControlException e) {
			printStackTrace(e);
			warn("Unable to check for updates.");
		}
	}

	public final void printStackTrace(Exception e) {
		if (!configuration.getBoolean("silentMode", false)) {
			severe("An error has occurred! Please file an issue at");
			severe("https://github.com/GetPerms/GetPerms/issues with the");
			severe("below stack trace. Also, please include details about");
			severe("what caused this error to occur.");
			e.printStackTrace();
		}
	}

	public final void generateFiles(boolean internal) {
		// internal is true if called by onEnable and False if called by command
		if (internal && !generationFinished || !internal) {
			// 5*20 = 5 seconds (20 ticks per second)
			new PluginFileGenerator(this).runTaskLater(this, 5 * 20);
		} else if (!internal) {
			new PluginFileGenerator(this).run();
		}
	}

	private final boolean newer(String current, String check, boolean dev) {
		if (!dev) {
			boolean result = false;
			String[] currentVersion = current.split("\\.");
			String[] checkVersion = check.split("\\.");
			int i = Integer.parseInt(currentVersion[0]);
			int j = Integer.parseInt(checkVersion[0]);
			if (i > j) {
				result = false;
			} else if (i == j) {
				i = Integer.parseInt(currentVersion[1]);
				j = Integer.parseInt(checkVersion[1]);
				if (i > j) {
					result = false;
				} else if (i == j) {
					i = Integer.parseInt(currentVersion[2]);
					j = Integer.parseInt(checkVersion[2]);
					if (i >= j) {
						result = false;
					} else {
						result = true;
					}
				} else {
					result = true;
				}
			} else {
				result = true;
			}
			return result;
		} else {
			return true;
		}
	}

	// Gets the readme and changelog
	private void getStartFiles() {
		try {
			getDataFolder().mkdir();
			info("Downloading changelog and readme...");
			downloadFile("https://raw.github.com/GetPerms/GetPerms/master/Changelog.txt", changeLog);
			downloadFile("https://raw.github.com/GetPerms/GetPerms/master/ReadMe.txt", readMe);
			info("The changelog and readme can be found in 'plugins/GetPerms/'");
			debug("Downloads succeded. firstRun being set to false...");
			configuration.set("firstRun", false);
		} catch (MalformedURLException e) {
			debug("MalformedURLException thrown, setting firstRun to true...");
			configuration.set("firstRun", true);
			printStackTrace(e);
			warn("Error downloading readme and changelog!");
		} catch (FileNotFoundException e) {
			debug("FileNotFoundException thrown, setting firstRun to true...");
			configuration.set("firstRun", true);
			printStackTrace(e);
			warn("Error downloading readme and changelog!");
			info("The readme is available at");
			info("https://raw.github.com/GetPerms/GetPerms/master/ReadMe.txt");
			info("and the changelog is available at");
			info("https://raw.github.com/GetPerms/GetPerms/master/Changelog.txt");
		} catch (IOException e) {
			debug("IOException thrown, setting firstRun to true...");
			configuration.set("firstRun", true);
			printStackTrace(e);
			warn("Error downloading readme and changelog!");
		}
	}

	public static void downloadFile(String url, File file) throws MalformedURLException, IOException {
		BufferedInputStream in = new BufferedInputStream(new java.net.URL(url).openStream());
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
		byte[] data = new byte[1024];
		int x = 0;
		while ((x = in.read(data, 0, 1024)) >= 0) {
			bout.write(data, 0, x);
		}
		bout.close();
		in.close();
	}

	private boolean downloadUpdate(String url, File file) throws MalformedURLException, IOException {
		BufferedInputStream in = new BufferedInputStream(new java.net.URL(url).openStream());
		FileOutputStream fos = new FileOutputStream(temporaryDownloadFile);
		BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
		byte[] data = new byte[1024];
		int x = 0;
		while ((x = in.read(data, 0, 1024)) >= 0) {
			bout.write(data, 0, x);
		}
		bout.close();
		in.close();
		if (verifyDownload(temporaryDownloadFile)) {
			temporaryDownloadFile.renameTo(file);
			return true;
		}
		temporaryDownloadFile.delete();
		return false;
	}

	private boolean verifyDownload(File file) {
		BufferedReader reader = null;
		boolean valid = true;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("content=\"text/html;charset=utf-8\"")) {
					valid = false;
				} else if (line.contains("html lang=\"en\"")) {
					valid = false;
				} else if (line.contains("<html>")) {
					valid = false;
				}
			}
		} catch (EOFException ignored) {
		} catch (FileNotFoundException e) {
			printStackTrace(e);
		} catch (IOException e) {
			printStackTrace(e);
		}

		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				printStackTrace(e);
			}
		}
		return valid;
	}

	private boolean comparePlugins() {
		boolean ok = true;
		Plugin[] pluginList = pluginManager.getPlugins();
		Configuration pluginListConfiguration = pluginListHandler.getConfig();

		for (Plugin plugin : pluginList) {
			if (pluginListConfiguration.contains(plugin.getDescription().getName())) {
				if (!pluginListConfiguration.getString(plugin.getDescription().getName())
						.equals(plugin.getDescription().getVersion())) {
					ok = false;
					break;
				}
			} else {
				ok = false;
				break;
			}
		}

		return ok;
	}

	private void writePluginList() {
		Plugin[] pluginList = pluginManager.getPlugins();
		Configuration pluginListConfiguration = pluginListHandler.getConfig();
		Set<String> keys = pluginListConfiguration.getKeys(false);

		// Clear the configuration
		for (String key : keys) {
			pluginListConfiguration.set(key, null);
		}

		for (Plugin plugin : pluginList) {
			pluginListConfiguration.set(plugin.getDescription().getName(), plugin.getDescription().getVersion());
		}
	}

	public void debug(String i) {
		if (configuration.getBoolean("debugMode", false)) {
			if (!configuration.getBoolean("silentMode", false)) {
				logger.info("[Debug] " + i);
			}
		}
	}

	public void config(String i) {
		if (configuration.getBoolean("debugMode", false)) {
			if (!configuration.getBoolean("silentMode", false)) {
				logger.config("[Debug] " + i);
			}
		}
	}

	public void info(String i) {
		if (!configuration.getBoolean("silentMode", false)) {
			logger.info(i);
		}
	}

	public void warn(String i) {
		if (!configuration.getBoolean("silentMode", false)) {
			logger.warning(i);
		}
	}

	public void severe(String i) {
		if (!configuration.getBoolean("silentMode", false)) {
			logger.severe(i);
		}
	}

	private final void cleanOldFiles() {
		info("Cleaning up old files from previous versions...");

		File[] oldFileList = {new File(dataFolder, "pluginlist.txt"), new File("pnodes.txt"),
				new File("pnodesfull.txt"), new File("EssentialsPnodes.txt"), new File("EssentialsPnodesfull.txt"),
				new File("update/GetPerms.jar")};
		File[] oldSoftDirectoryList = {new File("update")};

		for (File file : oldFileList) {
			if (file.exists()) {
				file.delete();
			}
		}

		for (File file : oldSoftDirectoryList) {
			if (file.exists() && file.isDirectory()) {
				if (file.listFiles().length == 0) {
					file.delete();
				}
			}
		}
	}
}
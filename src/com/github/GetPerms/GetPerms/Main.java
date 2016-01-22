package com.github.getperms.getperms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Main extends JavaPlugin {

	public static final String PERMISSIONS_FILENAME = "permission_nodes.txt";
	public static final String PERMISSIONS_DESCRIPTION_FILENAME = "permission_nodes_desc.txt";

	public Configuration configuration;
	public ConfigHandler configHandler;
	private PluginManager pluginManager;
	private YamlHandler pluginListHandler;
	private BukkitScheduler scheduler;
	protected String pluginVersion;
	private File dataFolder;
	public File permissionFolder;
	protected File permissionNodes;
	protected File permissionNodesDesc;
	private File readMe;
	private File changeLog;
	public Logger logger;
	private boolean generationFinished = false;
	private int updateTaskId = -1;

	@Override
	public void onEnable() {
		configuration = getConfig();
		configuration.options().copyDefaults(true);
		saveConfig();

		pluginManager = Bukkit.getServer().getPluginManager();
		scheduler = Bukkit.getScheduler();

		configHandler = new ConfigHandler(this);
		configHandler.load();
		configuration = getConfig();
		logger = getLogger();
		debugValues();

		pluginListHandler = new YamlHandler(this, "plugins");

		PluginDescriptionFile pluginDescriptionFile = getDescription();
		pluginVersion = pluginDescriptionFile.getVersion();

		dataFolder = getDataFolder();
		permissionFolder = new File(dataFolder, "permissions");
		permissionNodes = new File(permissionFolder, PERMISSIONS_FILENAME);
		permissionNodesDesc = new File(permissionFolder, PERMISSIONS_DESCRIPTION_FILENAME);
		readMe = new File(dataFolder, "ReadMe.txt");
		changeLog = new File(dataFolder, "Changelog.txt");

		// Check and create the plugin data folder
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}

		if (!permissionFolder.exists()) {
			permissionFolder.mkdirs();
		}

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

		if (configuration.getBoolean("autoUpdate", true)) {
			scheduler.scheduleSyncRepeatingTask(this, new UpdateTask(this), 10 * 20L, 60 * 60 * 24 * 20L);
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

		info("GetPerms " + pluginVersion + " enabled!");

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

	public void runUpdateTask() {
		scheduler.scheduleSyncDelayedTask(this, new UpdateTask(this), 10L);
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
		if (internal && !generationFinished) {
			// 5 * 20 = 5 seconds (20 ticks per second)
			scheduler.scheduleSyncDelayedTask(this, new PermissionTask(this), 5 * 20L);
		} else if (!internal) {
			scheduler.scheduleSyncDelayedTask(this, new PermissionTask(this));
		}
	}

	// Gets the readme and changelog
	private void getStartFiles() {
		try {
			dataFolder.mkdir();
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
			info("The readme is available at");
			info("https://raw.github.com/GetPerms/GetPerms/master/ReadMe.txt");
			info("and the changelog is available at");
			info("https://raw.github.com/GetPerms/GetPerms/master/Changelog.txt");
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
			info("The readme is available at");
			info("https://raw.github.com/GetPerms/GetPerms/master/ReadMe.txt");
			info("and the changelog is available at");
			info("https://raw.github.com/GetPerms/GetPerms/master/Changelog.txt");
		} catch (NoSuchAlgorithmException e) {
			debug("NoSuchAlgorithmException thrown, setting firstRun to true...");
			configuration.set("firstRun", true);
			printStackTrace(e);
			warn("Error downloading readme and changelog!");
			info("The readme is available at");
			info("https://raw.github.com/GetPerms/GetPerms/master/ReadMe.txt");
			info("and the changelog is available at");
			info("https://raw.github.com/GetPerms/GetPerms/master/Changelog.txt");
		}
	}

	public String downloadFile(String url, File file)
			throws MalformedURLException, IOException, NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");

		DigestInputStream inputStream = new DigestInputStream(
				new BufferedInputStream(new java.net.URL(url).openStream()), messageDigest);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 1024);
		byte[] data = new byte[1024];
		int i = 0;
		while ((i = inputStream.read(data, 0, 1024)) >= 0) {
			bufferedOutputStream.write(data, 0, i);
		}
		bufferedOutputStream.close();
		inputStream.close();

		return convertByteArrayToHexString(messageDigest.digest());
	}

	private String convertByteArrayToHexString(byte[] arrayBytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (byte arrayByte : arrayBytes) {
			stringBuffer.append(Integer.toString((arrayByte & 0xff) + 0x100, 16).substring(1));
		}
		return stringBuffer.toString();
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

		pluginListHandler.save();
	}

	public void registerUpdateTask(int taskId) {
		if (updateTaskId != -1) {
			Bukkit.getScheduler().cancelTask(updateTaskId);
		}

		updateTaskId = taskId;
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
				new File("update/GetPerms.jar"), new File("update/GPtemp.jar"),
				new File("Essentials_permission_nodes.txt"), new File("Essentials_permission_nodes_desc.txt"),
				new File(dataFolder, "permission_nodes.txt"), new File(dataFolder, "permission_nodes_desc.txt"),
				new File(dataFolder, "EssentialsPnodes.txt"), new File(dataFolder, "EssentialsPnodesfull.txt")};
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
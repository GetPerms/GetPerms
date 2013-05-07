package com.github.GetPerms.GetPerms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlException;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GetPerms extends JavaPlugin{

	WriteToFile WTF;
	ConfigHandler ConfHandler;
	PluginManager pm = Bukkit.getServer().getPluginManager();
	public Plugin[] pluginlist;
	public String version;
	final File file1 = new File("pnodes.txt");
	final File file2 = new File("pnodesfull.txt");
	private final File rm = new File("plugins/GetPerms/ReadMe.txt");
	private final File cl = new File("plugins/GetPerms/Changelog.txt");
	private final File uf = new File("update");
	private final File gpdf = new File("plugins/GetPerms");
	private final File updt = new File("update/GetPerms.jar");
	public static PluginDescriptionFile pdf;
	public static Configuration cfg;
	public Logger logger;
	private boolean dlstate = true;
	boolean genDone = false;

	@Override
	public void onEnable(){
		this.saveDefaultConfig();
		ConfHandler = new ConfigHandler(this);
		GetPerms.cfg = getConfig();
		GetPerms.pdf = getDescription();
		this.logger = this.getLogger();
		createCfg();
		ConfHandler.reload();
		version = pdf.getVersion();
		debugValues();
		WTF = new WriteToFile(this);
		if (cfg.getBoolean("sendStats")){
			info("Sending usage stats to metrics.griefcraft.com every 10 minutes.");
			if (cfg.getBoolean("firstRun", true))
				info("Option to disable is in the config.");
			try{
				Metrics metrics = new Metrics(this);
				metrics.start();
			}catch (IOException e){
				PST(e);
			}
		}else{
			info("This plugin is not sending usage stats.");
			if (cfg.getBoolean("firstRun", true))
				info("Option to enable is in the config.");
		}
		if (cfg.getBoolean("firstRun", true))
			info("This plugin supports PermissionsEx, which will be used if detected.");
		if (!usePEX())
			info("PEx was not detected; Permissions defaulting to op's.");
		else
			info("PEx detected! Using as permissions plugin!");
		debug("CFG version: " + cfg.getString("cfgV") + " Plugin version: " + version);
		if (!cfg.getString("cfgV").equalsIgnoreCase(version)){
			debug("Config version does not match jar version.");
			getStartFilesFromVer();
		}
		if (dlstate)
			if (cfg.getBoolean("firstRun", true)){
				debug("firstRun is set to true. Setting to false...");
				cfg.set("firstRun", false);
				getStartFiles();
			}
		ConfHandler.save();
		ConfHandler.reload();
		info("GetPerms " + version + " enabled!");
		info("GetPerms is the work of Smiley43210, with the help of Tahkeh,");
		info("wwsean08, desmin88, and many others. Thanks!");
		if (cfg.getBoolean("autoUpdate", true)){
			info("Checking for updates...");
			checkForUpdates();
		}
		if (cfg.getBoolean("regenerateOnPluginChange", true)){
			debug("Checking for plugin changes...");
			if (!compareV())
				debug("Changes found! Regenerating files...");
			genFiles(true);
			genDone = true;
		}
		if (cfg.getBoolean("autoGen", true)){
			debug("AutoGen enabled");
			genFiles(true);
			genDone = true;
		}
		if (cfg.getBoolean("disableOnFinish", false)){
			debug("DisableOnFinish enabled");
			info("Finished! Disabling...");
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	@Override
	public void onDisable(){
		ConfHandler.reload();
		ConfHandler.save();
		ConfHandler.addComments();
		info("GetPerms disabled");
	}

	public final boolean usePEX(){
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx"))
			return true;
		return false;
	}

	private final void debugValues(){
		String cfgV = version;
		boolean firstRun = cfg.getBoolean("firstRun", false);
		boolean sendStats = cfg.getBoolean("sendStats", true);
		boolean silentMode = cfg.getBoolean("silentMode", false);
		boolean autoGen = cfg.getBoolean("autoGen", true);
		boolean regenerateOnPluginChange = cfg.getBoolean("regenerateOnPluginChange", true);
		boolean autoUpdate = cfg.getBoolean("autoUpdate", true);
		boolean autoDownload = cfg.getBoolean("autoDownload", true);
		boolean disableOnFinish = cfg.getBoolean("disableOnFinish", false);
		boolean devBuilds = cfg.getBoolean("devBuilds", false);
		boolean debugMode = cfg.getBoolean("debugMode", false);

		config("cfgV: \"" + cfgV + "\"");
		config("firstRun: " + firstRun);
		config("sendStats: " + sendStats);
		config("silentMode: " + silentMode);
		config("autoGen: " + autoGen);
		config("regenerateOnPluginChange: " + regenerateOnPluginChange);
		config("autoUpdate: " + autoUpdate);
		config("autoDownload: " + autoDownload);
		config("disableOnFinish: " + disableOnFinish);
		config("devBuilds: " + devBuilds);
		config("debugMode: " + debugMode);
	}

	private final void checkForUpdates(){
		boolean devb = cfg.getBoolean("devBuilds");
		String dlurl = "https://raw.github.com/GetPerms/GetPerms/master/checks/dlurl";
		String check = "https://raw.github.com/GetPerms/GetPerms/master/checks/ver";
		String checkdev = "https://raw.github.com/GetPerms/GetPerms/master/checks/dev";
		String force = "https://raw.github.com/GetPerms/GetPerms/master/checks/force";
		String u = "https://raw.github.com/GetPerms/GetPerms/master/GetPerms.jar";
		String line;
		boolean dv;
		try{
			URL fa = new URL(force);
			BufferedReader fb = new BufferedReader(new InputStreamReader(fa.openStream()));
			String fc = fb.readLine();
			if (!devb){
				URL dlcheck = new URL(dlurl);
				BufferedReader a = new BufferedReader(new InputStreamReader(dlcheck.openStream()));
				u = a.readLine();
				URL client = new URL(check);
				BufferedReader buf = new BufferedReader(new InputStreamReader(client.openStream()));
				line = buf.readLine();
				dv = false;
			}else{
				URL client = new URL(checkdev);
				BufferedReader buf = new BufferedReader(new InputStreamReader(client.openStream()));
				line = buf.readLine();
				dv = true;
			}
			if (fc == "true")
				dv = true;
			if (newer(version, line, dv)){
				if (cfg.getBoolean("autoDownload", true)){
					info("Newest GetPerms version" + line + " is available.");
					if (!uf.exists())
						uf.mkdir();
					if (!devb)
						info("Downloading latest recommended build...");
					else
						info("Downloading latest developmental build...");
					if (dlUpdate(u, updt)){
						info("Newest version of GetPerms is located in");
						info("'server_root_dir/update/GetPerms.jar'.");
					}else{
						warn("Update file is corrupt! (Contains HTML elements)");
						warn("The update will most likely be available a little later.");
					}
				}else{
					info("Newest GetPerms version" + line + " is available for download, you can");
					info("get it at " + u);
					info("or the latest dev build at");
					info("https://raw.github.com/GetPerms/GetPerms/master/GetPerms.jar");
				}
			}else
				info("You have the latest version!");
		}catch (MalformedURLException e){
			PST(e);
			warn("Unable to check for updates.");
		}catch (IOException e){
			PST(e);
			warn("Unable to check for updates.");
		}catch (AccessControlException e){
			PST(e);
			warn("Unable to check for updates.");
		}
	}

	public final void PST(IOException e){
		if (cfg.getBoolean("debugMode", false))
			if (!cfg.getBoolean("silentMode", false))
				e.printStackTrace();
		if (!cfg.getBoolean("silentMode", false)){
			severe("An error has occurred. If this problem persists, enable debugMode in the config, restart the server, and");
			severe("post a ticket with the stack trace, all GetPerms messages, and what you did before the error occured.");
		}
	}

	public final void PST(MalformedURLException e){
		if (cfg.getBoolean("debugMode", false))
			if (!cfg.getBoolean("silentMode", false))
				e.printStackTrace();
	}

	public final void PST(Exception e){
		if (cfg.getBoolean("debugMode", false))
			if (!cfg.getBoolean("silentMode", false))
				e.printStackTrace();
		if (!cfg.getBoolean("silentMode", false)){
			severe("An error has occurred. If this problem persists, enable debugMode in the config, restart the server, and");
			severe("post a ticket with the stack trace, all GetPerms messages, and what you did before the error occured.");
		}
	}

	public final void PST(FileNotFoundException e){
		if (cfg.getBoolean("debugMode", false))
			if (!cfg.getBoolean("silentMode", false))
				e.printStackTrace();
		if (!cfg.getBoolean("silentMode", false)){
			severe("An error has occurred. If this problem persists, enable debugMode in the config, restart the server, and");
			severe("post a ticket with the stack trace, all GetPerms messages, and what you did before the error occured.");
		}
	}

	public final void PST(AccessControlException e){
		if (cfg.getBoolean("debugMode", false))
			if (!cfg.getBoolean("silentMode", false))
				e.printStackTrace();
		if (!cfg.getBoolean("silentMode", false)){
			severe("An error has occurred. If this problem persists, enable debugMode in the config, restart the server, and");
			severe("post a ticket with the stack trace, all GetPerms messages, and what you did before the error occured.");
		}
	}

	public final void genFiles(boolean a){
		//a is True if called by onEnable and False if called by command
		if ((a && !genDone) || !a){
			// 20*20 = 20 seconds (20 ticks per second)
			new PermissionFileGenerator(this).runTaskLater(this, 20 * 20);
		}else if (!a){
			new PermissionFileGenerator(this).run();
		}
	}

	private final boolean newer(String current, String check, boolean dev){
		if (!dev){
			boolean result = false;
			String[] currentVersion = current.split("\\.");
			String[] checkVersion = check.split("\\.");
			int i = Integer.parseInt(currentVersion[0]);
			int j = Integer.parseInt(checkVersion[0]);
			if (i > j)
				result = false;
			else if (i == j){
				i = Integer.parseInt(currentVersion[1]);
				j = Integer.parseInt(checkVersion[1]);
				if (i > j)
					result = false;
				else if (i == j){
					i = Integer.parseInt(currentVersion[2]);
					j = Integer.parseInt(checkVersion[2]);
					if (i >= j)
						result = false;
					else
						result = true;
				}else
					result = true;
			}else
				result = true;
			return result;
		}else
			return true;
	}

	private final void createCfg(){
		/*if (!new File(getDataFolder(), "plugins.yml").exists())
			cfg.options().copyDefaults(true);
		ConfHandler.save();
		*/
		ConfHandler.load();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		GPCommandPlayer cp = new GPCommandPlayer(this);
		GPCommandConsole cc = new GPCommandConsole(this);
		if (!(sender instanceof Player))
			// Command sent by console/plugin
			cc.cmdHandler(sender, cmd, commandLabel, args);
		else
			// Command sent by player
			cp.cmdHandler(sender, cmd, commandLabel, args);
		return true;
	}

	/*
	public void logFile(String x){

	}
	 */

	// Gets the readme and changelog
	private void getStartFiles(){
		try{
			getDataFolder().mkdir();
			info("Downloading changelog and readme...");
			dlFile("https://raw.github.com/GetPerms/GetPerms/master/Changelog.txt", cl);
			dlFile("https://raw.github.com/GetPerms/GetPerms/master/ReadMe.txt", rm);
			info("The changelog and readme can be found in 'plugins/GetPerms/'");
			debug("Downloads succeded; M2.");
			dlstate = false;
		}catch (MalformedURLException e){
			debug("MalformedURLException thrown, setting firstRun to true...");
			cfg.set("firstRun", true);
			PST(e);
			warn("Error downloading readme and changelog!");
		}catch (FileNotFoundException e){
			debug("FileNotFoundException thrown, setting firstRun to true...");
			cfg.set("firstRun", true);
			PST(e);
			warn("Error downloading readme and changelog!");
			info("The readme is available at");
			info("https://raw.github.com/GetPerms/GetPerms/master/ReadMe.txt");
			info("and the changelog is available at");
			info("https://raw.github.com/GetPerms/GetPerms/master/ReadMe.txt");
		}catch (IOException e){
			debug("IOException thrown, setting firstRun to true...");
			cfg.set("firstRun", true);
			PST(e);
			warn("Error downloading readme and changelog!");
		}
	}

	// Gets readme and changelog, but code differs since this is called when the config verison does not match plugin version
	private void getStartFilesFromVer(){
		try{
			cfg.set("cfgV", version);
			debug("Config version changed to match jar version.");
			getDataFolder().mkdir();
			if (!gpdf.exists())
				gpdf.mkdir();
			info("Downloading changelog and readme...");
			dlFile("https://raw.github.com/GetPerms/GetPerms/master/Changelog.txt", cl);
			dlFile("https://raw.github.com/GetPerms/GetPerms/master/ReadMe.txt", rm);
			info("The changelog and readme can be found in 'plugins/GetPerms/'");
			dlstate = false;
			debug("Downloads succeded; M1. firstRun being set to false...");
			cfg.set("firstRun", false);
		}catch (MalformedURLException e){
			debug("MalformedURLException thrown, setting firstRun to true...");
			cfg.set("firstRun", true);
			PST(e);
			warn("Error downloading readme and changelog!");
			dlstate = false;
		}catch (FileNotFoundException e){
			debug("FileNotFoundException thrown, setting firstRun to true...");
			cfg.set("firstRun", true);
			PST(e);
			warn("Error downloading readme and changelog!");
			info("The readme is available at");
			info("https://raw.github.com/GetPerms/GetPerms/master/ReadMe.txt");
			info("and the changelog is available at");
			info("https://raw.github.com/GetPerms/GetPerms/master/Changelog.txt");
			dlstate = false;
		}catch (IOException e){
			debug("IOException thrown, setting firstRun to true...");
			cfg.set("firstRun", true);
			PST(e);
			warn("Error downloading readme and changelog!");
			dlstate = false;
		}
	}

	public static void dlFile(String url, File file) throws MalformedURLException, IOException{
		BufferedInputStream in = new BufferedInputStream(new java.net.URL(url).openStream());
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
		byte[] data = new byte[1024];
		int x = 0;
		while ((x = in.read(data, 0, 1024)) >= 0)
			bout.write(data, 0, x);
		bout.close();
		in.close();
	}

	private boolean dlUpdate(String url, File file) throws MalformedURLException, IOException{
		BufferedInputStream in = new BufferedInputStream(new java.net.URL(url).openStream());
		File fileTemp = new File("update/GPtemp.jar");
		FileOutputStream fos = new FileOutputStream(fileTemp);
		BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
		byte[] data = new byte[1024];
		int x = 0;
		while ((x = in.read(data, 0, 1024)) >= 0)
			bout.write(data, 0, x);
		bout.close();
		in.close();
		if (verifyDownload(fileTemp)){
			fileTemp.renameTo(file);
			return true;
		}
		fileTemp.delete();
		File dirTemp = new File("update/");
		dirTemp.delete();
		return false;
	}

	private boolean verifyDownload(File file){
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null){
				if (line.contains("content=\"text/html;charset=utf-8\""))
					return false;
				else if (line.contains("html lang=\"en\""))
					return false;
				else if (line.contains("<html>"))
					return false;
			}
		}catch (EOFException ignored){
		}catch (FileNotFoundException e){
			PST(e);
		}catch (IOException e){
			PST(e);
		}
		return true;
	}

	public boolean compareV(){
		boolean ok = true;
		if (WTF.file2.exists()){
			pluginlist = pm.getPlugins();
			try{
				WTF.WriteTempPluginList();
				FileInputStream fstream = new FileInputStream(WTF.file);
				FileInputStream fstream2 = new FileInputStream(WTF.file2);
				DataInputStream in = new DataInputStream(fstream);
				DataInputStream in2 = new DataInputStream(fstream2);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
				String line;
				String line2;
				//Read file line by line
				while ((line = br.readLine()) != null && ok == true){
					while ((line2 = br2.readLine()) != null && ok == true){
						if (line != line2)
							ok = false;
					}
				}
				while ((line2 = br.readLine()) != null && ok == true){
					while ((line = br2.readLine()) != null && ok == true){
						if (line != line2)
							ok = false;
					}
				}
				//Close the input stream
				in.close();
				in2.close();
				debug("Attempting to delete temporary list");
				if (!WTF.file2.delete())
					warn("Temporary file deletion failed!");
			}catch (IOException e){
				PST(e);
			}catch (Exception e){
				PST(e);
			}
		}
		return ok;
	}

	public void debug(String i){
		if (GetPerms.cfg.getBoolean("debugMode", false))
			if (!GetPerms.cfg.getBoolean("silentMode", false))
				logger.info("[Debug] " + i);
	}

	public void config(String i){
		if (GetPerms.cfg.getBoolean("debugMode", false))
			if (!GetPerms.cfg.getBoolean("silentMode", false))
				logger.config("[Debug] " + i);
	}

	public void info(String i){
		if (!GetPerms.cfg.getBoolean("silentMode", false))
			logger.info(i);
	}

	public void warn(String i){
		if (!GetPerms.cfg.getBoolean("silentMode", false))
			logger.warning(i);
	}

	public void severe(String i){
		if (!GetPerms.cfg.getBoolean("silentMode", false))
			logger.severe(i);
	}

	/*
	public final int count() {
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new
	Runnable() {

	public void run() {
		System.out.println("This message is printed by an async thread"); } },
	60L, 200L); }
	 */
	@SuppressWarnings("unused")
	private final void createMisc(){
		if (!new File(getDataFolder(), "plugins.yml").exists())
			try{
				getDataFolder().mkdir();
				new File(getDataFolder(), "plugins.yml").createNewFile();
			}catch (Exception e){
				PST(e);
				info("Error occurred while creating plugins.yml (plugin list)!");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
	}
}
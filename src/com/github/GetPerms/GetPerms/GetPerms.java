package com.github.GetPerms.GetPerms;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GetPerms extends JavaPlugin {

	public String gpversion;
	WriteToFile WTF;
	ConfigHandler ConfHandler = new ConfigHandler(this);
	PluginManager pm = Bukkit.getServer().getPluginManager();
	public Plugin[] pluginlist;
	private File file1 = new File("pnodes.txt");
	private File file2 = new File("pnodesfull.txt");
	private File rm = new File("plugins/GetPerms/ReadMe.txt");
	private File cl = new File("plugins/GetPerms/Changelog.txt");
	private File uf = new File("update");
	private File gpdf = new File("plugins/GetPerms");
	private File updt = new File("update/GetPerms.jar");
	public PrintWriter pw1;
	public PrintWriter pw2;
	public static PluginDescriptionFile pdf;
	public static Configuration cfg;
	private boolean dlstate = true;
	private GPCommandPlayer cp = new GPCommandPlayer(this);
	private GPCommandConsole cc = new GPCommandConsole(this);

	@Override
	public void onEnable() { 
		WTF = new WriteToFile(this);
		cfg = this.getConfig();
		pdf = this.getDescription();
		gpCreateCfg();
		cfg = this.getConfig();
		ConfHandler.restore();
		gpversion = pdf.getVersion();
		debugValues();
		getLogger().info("This plugin supports PermissionsEx. This plugin");
		getLogger().info("will use it if detected.");
		if (usePEX()){
			getLogger().info("PermissionsEx was not detected. Permissions will");
			getLogger().info("default to ops.");
		}
		else {
			getLogger().info("PermissionsEx detected! Using as permissions plugin!");
		}
		debug("CFG version: "+cfg.getString("cfgV")+" Plugin version: "+gpversion);
		if (!cfg.getString("cfgV").equalsIgnoreCase(gpversion)) {
			debug("Config version does not match jar version.");
			try {
				cfg.set("cfgV", gpversion);
				debug("Config version changed to match jar version.");
				getDataFolder().mkdir();
				if (!gpdf.exists()) {
					gpdf.mkdir();
				}
				getLogger().info("Downloading changelog and readme...");
				dlFile("https://raw.github.com/GetPerms/GetPerms/master/Changelog.txt", cl);
				getLogger().info("Downloaded changelog to");
				getLogger().info("'plugins/GetPerms/Changelog.txt'");
				dlFile("https://raw.github.com/GetPerms/GetPerms/master/ReadMe.txt", rm);
				getLogger().info("Downloaded readme to 'plugins/GetPerms/ReadMe.txt'");
				dlstate = false;
				debug("Downloads succeded, firstRun being set to false...");
				cfg.set("firstRun", false);
			} catch (MalformedURLException e) {
				debug("MalformedURLException thrown, setting firstRun to true...");
				cfg.set("firstRun", true);
				PST(e);
				getLogger().warning("Error downloading readme and changelog!");
				dlstate = false;
			} catch (FileNotFoundException e) {
				debug("FileNotFoundException thrown, setting firstRun to true...");
				cfg.set("firstRun", true);
				PST(e);
				getLogger().warning("Error downloading readme and changelog!");
				getLogger().info("The readme is available at");
				getLogger().info("https://raw.github.com/GetPerms/GetPerms/master/ReadMe.txt");
				getLogger().info("and the changelog is available at");
				getLogger().info("https://raw.github.com/GetPerms/GetPerms/master/Changelog.txt");
				dlstate = false;
			} catch (IOException e) {
				debug("IOException thrown, setting firstRun to true...");
				cfg.set("firstRun", true);
				PST(e);
				getLogger().warning("Error downloading readme and changelog!");
				dlstate = false;
			}
		}

		if (dlstate){
			if (cfg.getBoolean("firstRun", true)) {
				debug("firstRun is set to true. Setting to false...");
				cfg.set("firstRun", false);
				try {
					getDataFolder().mkdir();
					getLogger().info("Downloading changelog and readme...");
					dlFile("https://raw.github.com/GetPerms/GetPerms/master/Changelog.txt", cl);
					getLogger().info("Downloaded Changelog.txt to 'plugins/GetPerms/Changelog.txt'");
					dlFile("https://raw.github.com/GetPerms/GetPerms/master/ReadMe.txt", rm);
					getLogger().info("Downloaded ReadMe.txt to 'plugins/GetPerms/ReadMe.txt'");
					debug("Downloads succeded; Second method.");
					dlstate = false;
				} catch (MalformedURLException e) {
					debug("MalformedURLException thrown, setting firstRun to true...");
					cfg.set("firstRun", true);
					PST(e);
					getLogger().warning("Error downloading readme and changelog!");
				} catch (FileNotFoundException e) {
					debug("FileNotFoundException thrown, setting firstRun to true...");
					cfg.set("firstRun", true);
					PST(e);
					getLogger().warning("Error downloading readme and changelog!");
					getLogger().info("The readme is available at");
					getLogger().info("https://raw.github.com/GetPerms/GetPerms/master/ReadMe.txt");
					getLogger().info("and the changelog is available at");
					getLogger().info("https://raw.github.com/GetPerms/GetPerms/master/ReadMe.txt");
				} catch (IOException e) {
					debug("IOException thrown, setting firstRun to true...");
					cfg.set("firstRun", true);
					PST(e);
					getLogger().warning("Error downloading readme and changelog!");
				}
			}
		}
		//gpCreateMisc();
		try {
			pw1 = new PrintWriter(new FileWriter(file1));
			pw2 = new PrintWriter(new FileWriter(file2));
		} catch (IOException e) {
			PST(e);
		}
		getLogger().info("GetPerms "+gpversion+" enabled!");
		getLogger().info("GetPerms is the work of Smiley43210, with the help of");
		getLogger().info("Tahkeh, wwsean08, desmin88, and many others. Thanks!");
		if (cfg.getBoolean("autoUpdate", true)) {
			getLogger().info("Checking for updates...");
			gpCheckForUpdates();
		}
		if (cfg.getBoolean("autoGen", true))
			genFiles(true);
		this.saveConfig();
		if (cfg.getBoolean("disableOnFinish", true))
			getServer().getPluginManager().disablePlugin(this);
	}
	@Override
	public void onDisable() { 
		ConfHandler.addComments();
		getLogger().info("GetPerms "+gpversion+" unloaded");
	}

	public final boolean usePEX() {
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")){
			return true;
		}
		return false;
	}

	private final void debugValues(){
		cfg = this.getConfig();
		boolean firstRun = cfg.getBoolean("firstRun");
		boolean autoGen = cfg.getBoolean("autoGen");
		boolean autoUpdate = cfg.getBoolean("autoUpdate");
		boolean autoDownload = cfg.getBoolean("autoDownload");
		boolean debugMode = cfg.getBoolean("debugMode");
		boolean disableOnFinish = cfg.getBoolean("disableOnFinish");
		boolean devBuilds = cfg.getBoolean("devBuilds");
		String cfgV = cfg.getString("cfgV");
		debug("cfgV: \""+cfgV+"\"");
		debug("firstRun: "+firstRun);
		debug("autoGen: "+autoGen);
		debug("autoUpdate: "+autoUpdate);
		debug("autoDownload: "+autoDownload);
		debug("disableOnFinish: "+disableOnFinish);
		debug("devBuilds: "+devBuilds);
		debug("debugMode: "+debugMode);
	}

	private final void gpCheckForUpdates() {
		cfg = this.getConfig();
		boolean devb = cfg.getBoolean("devBuilds");
		String dlurl = "https://raw.github.com/GetPerms/GetPerms/master/checks/dlurl";
		String check = "https://raw.github.com/GetPerms/GetPerms/master/checks/ver";
		String checkdev = "https://raw.github.com/GetPerms/GetPerms/master/checks/dev";
		String u = "https://raw.github.com/GetPerms/GetPerms/master/GetPerms.jar";
		String line;
		boolean dv;
		try {
			if(!devb){
				URL dlcheck = new URL(dlurl);
				BufferedReader a = new BufferedReader(new InputStreamReader(dlcheck.openStream()));
				u = a.readLine();
				URL client = new URL(check);
				BufferedReader buf = new BufferedReader(new InputStreamReader(client.openStream()));
				line = buf.readLine();
				dv = false;
			}
			else{
				URL client = new URL(checkdev);
				BufferedReader buf = new BufferedReader(new InputStreamReader(client.openStream()));
				line = buf.readLine();
				dv = true;
			}
			if (gpnewer(gpversion, line, dv)) {
				getLogger().info("Newest GetPerms version" + line + " is available.");
				if (cfg.getBoolean("autoDownload", true)) {
					if (!uf.exists()) {
						uf.mkdir();
					}
					if(!devb){
						getLogger().info("Downloading latest recommended release...");
					}
					else{
						getLogger().info("Downloading latest developmental build...");
					}
					dlFile(u, updt);
					getLogger().info("Newest version of GetPerms is located in");
					getLogger().info("'server_root_dir/update/GetPerms.jar'.");
				}
				else {
					getLogger().info("Newest GetPerms version" + line + " is available for download, you can");
					getLogger().info("get it at "+u);
					getLogger().info("or the latest dev build at");
					getLogger().info("https://raw.github.com/GetPerms/GetPerms/master/GetPerms.jar");
				}
			}
			else
				getLogger().info("You have the latest version!");
		} catch (MalformedURLException e) {
			PST(e);
			getLogger().warning("Unable to check for updates.");
		} catch (IOException e) {
			PST(e);
			getLogger().warning("Unable to check for updates.");
		}
	}

	public final void PST(IOException e) {
		if (cfg.getBoolean("debugMode", true)) { 
			e.printStackTrace();
		}
	}

	public final void PST(MalformedURLException e) {
		if (cfg.getBoolean("debugMode", true)) { 
			e.printStackTrace();
		}
	}

	public final void PST(FileNotFoundException e) {
		if (cfg.getBoolean("debugMode", true)) { 
			e.printStackTrace();
		}
	}

	public final void genFiles(boolean a){
		pluginlist = pm.getPlugins();
		if (a) {
			getLogger().info("Retrieved plugin list!");
			getLogger().info("Retrieving permission nodes...");
		}
		for (Plugin p : pluginlist) {
			try {
				WTF.WritePNodes(p);
			} catch (IOException e) {
				PST(e);
				getLogger().warning("Error retrieving plugin list!");
			}
			if (!WTF.plist.isEmpty()) {
				pw2.println("");
			}
		}
		pw1.close();
		pw2.close();
		if(a){
			getLogger().info("Compiled permission nodes into 'pnodes.txt' and");
			getLogger().info("'pnodesfull.txt' in the server root folder.");
		}
		try {
			WTF.WritePluginList();
		} catch (IOException e) {
			PST(e);
			getLogger().warning("Error generating plugin list!");
		}
	}

	private final boolean gpnewer(String current, String check, boolean dev) {
		if (!dev){
			boolean result = false;
			String[] currentVersion = current.split("\\.");
			String[] checkVersion = check.split("\\.");
			int i = Integer.parseInt(currentVersion[0]);
			int j = Integer.parseInt(checkVersion[0]);
			if(i>j)
				result = false;
			else if(i==j){
				i = Integer.parseInt(currentVersion[1]);
				j = Integer.parseInt(checkVersion[1]);
				if(i>j)
					result = false;
				else if(i == j){
					i = Integer.parseInt(currentVersion[2]);
					j = Integer.parseInt(checkVersion[2]);
					if(i >= j)
						result = false;
					else
						result = true;
				}else
					result = true;
			}else
				result = true;
			return result;
		}
		else{
			return true;
		}
	}

	private final void gpCreateCfg() {
		cfg = this.getConfig();
		cfg.options().copyDefaults(true);
		this.saveConfig();
		//Getting ready for option changes
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			// Command sent by console/plugin
			cc.cmdHandler(sender, cmd, commandLabel, args);
		} else {
			// Command sent by player
			cp.cmdHandler((Player) sender, cmd, commandLabel, args);
		}
		return true;
	}

	public static void dlFile(String url, File file) throws MalformedURLException, IOException {		 
		BufferedInputStream in = new BufferedInputStream(new 
		java.net.URL(url).openStream());
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
		byte[] data = new byte[1024];
		int x=0;
		while((x=in.read(data,0,1024))>=0)
		{
			bout.write(data,0,x);
		}
		bout.close();
		in.close();
	}

	private final void debug(String i) {
		if (cfg.getBoolean("debugMode", true)) { 
			getLogger().info("[Debug] "+i);
		}
	}

	@SuppressWarnings("unused")
	private final void gpCreateMisc() {
		if (!new File(getDataFolder(), "plugins.yml").exists()) {
			try {
			getDataFolder().mkdir();
			new File(getDataFolder(), "plugins.yml").createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().info("[GetPerms] Error occurred while creating plugins.yml (plugin list)!");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}
	}

}
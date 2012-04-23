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

	public String gpversion;
	WriteToFile WTF;
	ConfigHandler ConfHandler = new ConfigHandler(this);
	PluginManager pm = Bukkit.getServer().getPluginManager();
	public Plugin[] pluginlist;
	private final File file1 = new File("pnodes.txt");
	private final File file2 = new File("pnodesfull.txt");
	private final File rm = new File("plugins/GetPerms/ReadMe.txt");
	private final File cl = new File("plugins/GetPerms/Changelog.txt");
	private final File uf = new File("update");
	private final File gpdf = new File("plugins/GetPerms");
	private final File updt = new File("update/GetPerms.jar");
	public PrintWriter pw1;
	public PrintWriter pw2;
	public static PluginDescriptionFile pdf;
	public static Configuration cfg;
	private boolean dlstate = true;

	@Override
	public void onEnable(){
		WTF = new WriteToFile(this);
		cfg = getConfig();
		pdf = getDescription();
		gpCreateCfg();
		cfg = getConfig();
		ConfHandler.restore();
		gpversion = pdf.getVersion();
		debugValues();
		if (cfg.getBoolean("sendStats")){
			info("Sending usage stats to metrics.griefcraft.com every 10 minutes.");
			if (cfg.getBoolean("firstRun", true))
				info("Option to disable is in the config.");
			try{
				Metrics metrics = new Metrics();
				metrics.beginMeasuringPlugin(this);
			}catch (IOException e){
				PST(e);
			}
		}else{
			info("This plugin is not sending usage stats.");
			if (cfg.getBoolean("firstRun", true))
				info("Option to enable is in the config.");
		}
		info("This plugin supports PermissionsEx, which will be used if detected.");
		if (!usePEX())
			info("PEx was not detected; Permissions defaulting to op's.");
		else
			info("PEx detected! Using as permissions plugin!");
		debug("CFG version: " + cfg.getString("cfgV") + " Plugin version: " + gpversion);
		if (!cfg.getString("cfgV").equalsIgnoreCase(gpversion)){
			debug("Config version does not match jar version.");
			try{
				cfg.set("cfgV", gpversion);
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
		if (dlstate)
			if (cfg.getBoolean("firstRun", true)){
				debug("firstRun is set to true. Setting to false...");
				cfg.set("firstRun", false);
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
		// gpCreateMisc();
		try{
			pw1 = new PrintWriter(new FileWriter(file1));
			pw2 = new PrintWriter(new FileWriter(file2));
		}catch (IOException e){
			PST(e);
		}
		info("GetPerms " + gpversion + " enabled!");
		info("GetPerms is the work of Smiley43210, with the help of");
		info("Tahkeh, wwsean08, desmin88, and many others. Thanks!");
		if (cfg.getBoolean("autoUpdate", true)){
			info("Checking for updates...");
			gpCheckForUpdates();
		}
		if (cfg.getBoolean("autoGen", true))
			genFiles(true);
		saveConfig();
		if (cfg.getBoolean("disableOnFinish", true))
			getServer().getPluginManager().disablePlugin(this);
	}

	@Override
	public void onDisable(){
		ConfHandler.addComments();
		info("GetPerms " + gpversion + " unloaded");
	}

	public final boolean usePEX(){
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx"))
			return true;
		return false;
	}

	private final void debugValues(){
		cfg = getConfig();
		boolean firstRun = cfg.getBoolean("firstRun");
		boolean autoGen = cfg.getBoolean("autoGen");
		boolean autoUpdate = cfg.getBoolean("autoUpdate");
		boolean autoDownload = cfg.getBoolean("autoDownload");
		boolean debugMode = cfg.getBoolean("debugMode");
		boolean disableOnFinish = cfg.getBoolean("disableOnFinish");
		boolean devBuilds = cfg.getBoolean("devBuilds");
		String cfgV = cfg.getString("cfgV");
		debug("cfgV: \"" + cfgV + "\"");
		debug("firstRun: " + firstRun);
		debug("autoGen: " + autoGen);
		debug("autoUpdate: " + autoUpdate);
		debug("autoDownload: " + autoDownload);
		debug("disableOnFinish: " + disableOnFinish);
		debug("devBuilds: " + devBuilds);
		debug("debugMode: " + debugMode);
	}

	private final void gpCheckForUpdates(){
		cfg = getConfig();
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
			if (gpnewer(gpversion, line, dv)){
				info("Newest GetPerms version" + line + " is available.");
				if (cfg.getBoolean("autoDownload", true)){
					if (!uf.exists())
						uf.mkdir();
					if (!devb)
						info("Downloading latest recommended release...");
					else
						info("Downloading latest developmental build...");
					dlFile(u, updt);
					info("Newest version of GetPerms is located in");
					info("'server_root_dir/update/GetPerms.jar'.");
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
		}
	}

	public final void PST(IOException e){
		if (cfg.getBoolean("debugMode", true))
			if (cfg.getBoolean("silentMode", false))
				e.printStackTrace();
	}

	public final void PST(MalformedURLException e){
		if (cfg.getBoolean("debugMode", true))
			if (cfg.getBoolean("silentMode", false))
				e.printStackTrace();
	}

	public final void PST(FileNotFoundException e){
		if (cfg.getBoolean("debugMode", true))
			if (cfg.getBoolean("silentMode", false))
				e.printStackTrace();
	}

	public final void genFiles(boolean a){
		TempRetrieval tr = new TempRetrieval(this);
		pluginlist = pm.getPlugins();
		if (a){
			debug("Retrieved plugin list!");
			debug("Retrieving permission nodes...");
		}
		for (Plugin p : pluginlist){
			try{
				WTF.WritePNodes(p);
			}catch (IOException e){
				PST(e);
				warn("Error retrieving plugin list!");
			}
			if (!WTF.plist.isEmpty())
				pw2.println("");
		}
		pw1.close();
		pw2.close();
		if (a){
			info("Compiled permission nodes into 'pnodes.txt' and");
			info("'pnodesfull.txt' in the server root folder.");
		}
		try{
			WTF.WritePluginList();
		}catch (IOException e){
			PST(e);
			warn("Error generating plugin list!");
		}
		try{
			tr.Get();
		}catch (MalformedURLException e){
			PST(e);
		}catch (IOException e){
			PST(e);
		}
	}

	private final boolean gpnewer(String current, String check, boolean dev){
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

	private final void gpCreateCfg(){
		cfg = getConfig();
		cfg.options().copyDefaults(true);
		saveConfig();
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

	private void debug(String i){
		Logger log = this.getLogger();
		if (cfg.getBoolean("debugMode", true))
			if (cfg.getBoolean("silentMode", false))
				log.info("[Debug] " + i);
	}

	public void info(String i){
		Logger log = this.getLogger();
		if (cfg.getBoolean("silentMode", false))
			log.info(i);
	}

	private void warn(String i){
		Logger log = this.getLogger();
		if (cfg.getBoolean("silentMode", false))
			log.warning(i);
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
	private final void gpCreateMisc(){
		Logger log = this.getLogger();
		if (!new File(getDataFolder(), "plugins.yml").exists())
			try{
				getDataFolder().mkdir();
				new File(getDataFolder(), "plugins.yml").createNewFile();
			}catch (Exception e){
				e.printStackTrace();
				log.info("[GetPerms] Error occurred while creating plugins.yml (plugin list)!");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
	}
}
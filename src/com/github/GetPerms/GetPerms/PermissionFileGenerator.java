package com.github.GetPerms.GetPerms;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PermissionFileGenerator extends BukkitRunnable{

	private GetPerms gp;
	WriteToFile WTF;
	private PrintWriter pw1;
	private PrintWriter pw2;

	public PermissionFileGenerator(GetPerms gp){
		this.gp = gp;
		WTF = gp.WTF;
	}

	@Override
	public void run(){
		try{
			pw1 = new PrintWriter(new FileWriter(gp.file1));
			pw2 = new PrintWriter(new FileWriter(gp.file2));
		}catch (IOException e){
			gp.PST(e);
		}

		gp.info("Generating files...");
		TempRetrieval tr = new TempRetrieval(gp);
		gp.pluginlist = gp.pm.getPlugins();
		gp.debug("Retrieved plugin list!");
		gp.debug("Retrieving permission nodes...");
		for (Plugin p : gp.pluginlist){
			try{
				WTF.WritePNodes(p, pw1, pw2);
			}catch (IOException e){
				gp.PST(e);
				gp.warn("Error retrieving plugin list!");
			}
			if (!WTF.plist.isEmpty())
				pw2.println("");
		}
		pw1.close();
		pw2.close();
		gp.info("Compiled permission nodes into 'pnodes.txt' and");
		gp.info("'pnodesfull.txt' in the server root folder.");
		try{
			WTF.WritePluginList();
		}catch (IOException e){
			gp.PST(e);
			gp.warn("Error generating plugin list!");
		}
		try{
			tr.Get();
		}catch (MalformedURLException e){
			gp.PST(e);
		}catch (IOException e){
			gp.PST(e);
		}
		gp.info("Permission lists generated!");
	}

}

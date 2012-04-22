package com.github.GetPerms.GetPerms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

public final class WriteToFile{

	private GetPerms gp;
	public List<Permission> plist;
	private File file = new File("pluginlist.txt");
	private PrintWriter pw;

	public WriteToFile(GetPerms gp){
		this.gp = gp;
	}

	public final void WritePNodes(Plugin p) throws IOException{

		plist = p.getDescription().getPermissions();
		if (!plist.isEmpty())
			gp.pw2.println("----" + p.getDescription().getName() + "----");

		for (Permission pr : plist){
			gp.pw1.println(pr.getName().toString());
			if (pr.getDescription() == "")
				gp.pw2.println(pr.getName() + " - " + "No description given");
			else
				gp.pw2.println(pr.getName() + " - " + pr.getDescription());
		}
	}

	public final void WritePluginList() throws IOException{
		pw = new PrintWriter(new FileWriter(file));
		for (Plugin p : gp.pluginlist)
			pw.println(p.getDescription().getName());
		pw.close();
	}

}
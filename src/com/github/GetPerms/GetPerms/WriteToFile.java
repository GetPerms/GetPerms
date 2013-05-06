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
	public File file = new File("plugins/GetPerms/pluginlist.txt");
	public File file2 = new File("plugins/GetPerms/pluginlist2.txt");
	private PrintWriter pw;
	private PrintWriter pw2;

	public WriteToFile(GetPerms gp){
		this.gp = gp;
	}

	public final void WritePNodes(Plugin p, PrintWriter p1, PrintWriter p2) throws IOException{

		plist = p.getDescription().getPermissions();
		if (!plist.isEmpty())
			p2.println("----" + p.getDescription().getName() + "----");

		for (Permission pr : plist){
			p1.println(pr.getName().toString());
			if (pr.getDescription() == "")
				p2.println(pr.getName() + " - " + "No description given");
			else
				p2.println(pr.getName() + " - " + pr.getDescription());
		}
	}

	public final void WritePluginList() throws IOException{
		if (file.exists())
			file.delete();
		pw = new PrintWriter(new FileWriter(file));
		for (Plugin p : gp.pluginlist)
			pw.println(p.getDescription().getName() + " - " + p.getDescription().getVersion());
		pw.close();
	}

	public final void WriteTempPluginList() throws IOException{
		pw2 = new PrintWriter(new FileWriter(file2));
		for (Plugin p : gp.pluginlist)
			pw2.println(p.getDescription().getName() + " - " + p.getDescription().getVersion());
		pw2.close();
	}

}
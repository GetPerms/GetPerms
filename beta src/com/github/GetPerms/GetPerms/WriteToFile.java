package com.github.GetPerms.GetPerms;

import java.io.IOException;
import java.lang.StringBuilder;
import java.util.List;

import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

public final class WriteToFile {

	private GetPermsB gp;
	public List<Permission> plist;

	public final void Write(Plugin p) throws IOException {
		
		plist = p.getDescription().getPermissions();
		if (!plist.isEmpty()) {
			gp.pw2.println((new StringBuilder()).append("----").append(p.getDescription().getName()).append("----").toString());
		}
		
		for (Permission pr : plist) {
			gp.pw1.println((new StringBuilder()).append(pr.getName()).toString());
			if (pr.getDescription() == "") {
				gp.pw2.println((new StringBuilder()).append(pr.getName()).append(" - ").append("No description given").toString());
			}
			else {
				gp.pw2.println((new StringBuilder()).append(pr.getName()).append(" - ").append(pr.getDescription()).toString());
			}
	    }
	}
}
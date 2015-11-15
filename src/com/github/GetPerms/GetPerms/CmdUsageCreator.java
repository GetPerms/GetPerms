package com.github.GetPerms.GetPerms;

import org.bukkit.ChatColor;

public class CmdUsageCreator {

	private static String[] aliases = { "getperms", "gp" };

	public static String[] createUsage(String cmd, String usage) {
		String line1 = "";
		for (int i = 0; i < aliases.length; i++) {
			line1 = line1 + ChatColor.YELLOW + "/" + aliases[i] + " " + cmd;
			if (i + 1 != aliases.length) {
				line1 = line1 + ChatColor.WHITE + ", ";
			}
		}
		String line2 = ChatColor.WHITE + "    " + usage;
		String[] lines = { line1, line2 };
		return lines;
	}
}

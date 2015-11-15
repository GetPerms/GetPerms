package com.github.GetPerms.GetPerms;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class GPCommandConsole {

	Main gp;

	public GPCommandConsole(Main gp) {
		this.gp = gp;
	}

	public final boolean cmdHandler(CommandSender i, Command j, String k, String[] l) {
		if (j.getName().equalsIgnoreCase("getperms") || j.getName().equalsIgnoreCase("gp")) {

			if (l.length > 1) {
				i.sendMessage(ChatColor.RED + "Too many arguments!");
				help(i);
				return false;
			} else if (l.length < 1) {
				info(i);
				return true;
			}

			if (l[0].equalsIgnoreCase("regen")) {
				i.sendMessage(ChatColor.AQUA + "[GetPerms] Regenerating permission files...");
				gp.generateFiles(false);
				i.sendMessage(ChatColor.AQUA + "[GetPerms] Files regenerated!");
				return true;
			} else if (l[0].equalsIgnoreCase("reload")) {
				i.sendMessage(ChatColor.AQUA + "[GetPerms] Reloading configuration file...");
				gp.configHandler.reload();
				i.sendMessage(ChatColor.AQUA + "[GetPerms] Configuration reloaded!");
				return true;
			} else if (l[0].equalsIgnoreCase("help")) {
				help(i);
				return true;
			} else {
				i.sendMessage(ChatColor.RED + "Unknown command.");
				help(i);
				return false;
			}
		}
		return false;
	}

	private final void info(CommandSender i) {
		i.sendMessage(ChatColor.GREEN + "GetPerms version " + ChatColor.AQUA + gp.pluginVersion + ChatColor.GREEN
				+ ", created by Smiley43210.");
		i.sendMessage(ChatColor.GOLD + "Use " + ChatColor.YELLOW + "/getperms help" + ChatColor.GOLD
				+ " for the command list");
	}

	private final void help(CommandSender i) {
		i.sendMessage(ChatColor.GREEN + "GetPerms commands:");
		i.sendMessage(CmdUsageCreator.createUsage("regen", "(Re)generate the permission node list files."));
		i.sendMessage(CmdUsageCreator.createUsage("reload", "Reload the configuration file for GetPerms."));
		i.sendMessage(CmdUsageCreator.createUsage("help", "Displays this help message."));
	}

}

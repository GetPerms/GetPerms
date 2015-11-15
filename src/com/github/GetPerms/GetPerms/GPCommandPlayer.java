package com.github.GetPerms.GetPerms;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class GPCommandPlayer {

	Main gp;
	PermissionManager permissions;

	public GPCommandPlayer(Main gp) {
		this.gp = gp;
	}

	public final boolean cmdHandler(CommandSender i, Command j, String k, String[] l) {
		String a = "";
		if (l.length > 0) {
			for (String st : l) {
				a = a + " " + st;
			}
		}
		if (gp.usePEX()) {
			permissions = PermissionsEx.getPermissionManager();
		}
		if (j.getName().equalsIgnoreCase("getperms") || j.getName().equalsIgnoreCase("gp")) {
			gp.info(i.getName() + ": /" + j.getName() + a);

			if (l.length > 1) {
				i.sendMessage(ChatColor.RED + "Too many arguments!");
				help(i);
				return false;
			} else if (l.length < 1) {
				info(i);
				return true;
			}

			if (gp.usePEX()) {

				if (l[0].equalsIgnoreCase("regen")) {
					// Permission check
					if (!permissions.has((Player) i, "getperms.regen")) {
						i.sendMessage(ChatColor.RED + "You don't have permission!");
						return true;
					}

					i.sendMessage(ChatColor.AQUA + "[GetPerms] Regenerating permission files...");
					gp.generateFiles(false);
					i.sendMessage(ChatColor.AQUA + "[GetPerms] Files regenerated!");
					return true;
				} else if (l[0].equalsIgnoreCase("reload")) {
					// Permission check
					if (!permissions.has((Player) i, "getperms.reload")) {
						i.sendMessage(ChatColor.RED + "You don't have permission!");
						return true;
					}

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

			} else if (i.isOp()) {

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

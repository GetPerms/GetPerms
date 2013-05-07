package com.github.GetPerms.GetPerms;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class GPCommandConsole{

	GetPerms gp;

	public GPCommandConsole(GetPerms gp){
		this.gp = gp;
	}

	public final boolean cmdHandler(CommandSender i, Command j, String k, String[] l){
		if (j.getName().equalsIgnoreCase("getperms") || j.getName().equalsIgnoreCase("gp")){

			if (l.length > 1){
				i.sendMessage(ChatColor.RED + "Too many arguments!");
				help(i);
				return false;
			}else if (l.length < 1){
				info(i);
				return true;
			}

			if (l[0].equalsIgnoreCase("regen")){
				i.sendMessage(ChatColor.AQUA + "[GetPerms] Regenerating permission files...");
				gp.genFiles(false);
				i.sendMessage(ChatColor.AQUA + "[GetPerms] Files regenerated!");
				return true;
			}else if (l[0].equalsIgnoreCase("reload")){
				i.sendMessage(ChatColor.AQUA + "[GetPerms] Reloading configuration file...");
				gp.ConfHandler.reload();
				i.sendMessage(ChatColor.AQUA + "[GetPerms] Configuration reloaded!");
				return true;
			}else if (l[0].equalsIgnoreCase("help")){
				help(i);
				return true;
			}else{
				i.sendMessage(ChatColor.RED + "Unknown command.");
				help(i);
				return false;
			}
		}
		return false;
	}

	private final void info(CommandSender i){
		i.sendMessage(ChatColor.GREEN + "GetPerms version " + ChatColor.AQUA + gp.version + ChatColor.GREEN + ", created by Smiley43210.");
		i.sendMessage(ChatColor.GOLD + "Use " + ChatColor.YELLOW + "/getperms help" + ChatColor.GOLD + " for the command list");
	}

	private final void help(CommandSender i){
		CmdUsageCreator cmd = new CmdUsageCreator();
		i.sendMessage(ChatColor.GREEN + "GetPerms Commands:");
		i.sendMessage(cmd.createUsage("regen", "(Re)generate the permission node list files."));
		i.sendMessage(cmd.createUsage("reload", "Reload the configuration file for GetPerms."));
		i.sendMessage(cmd.createUsage("help", "Displays this help message."));
	}

}

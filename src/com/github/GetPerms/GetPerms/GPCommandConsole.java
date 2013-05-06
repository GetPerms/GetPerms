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
				i.sendMessage(ChatColor.RED + "Too few arguments!");
				help(i);
				return false;
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

	public final void help(CommandSender i){
		i.sendMessage(ChatColor.GOLD + "GetPerms version " + gp.version + ", created by Smiley43210.");
		i.sendMessage(ChatColor.YELLOW + "Usage: /getperms regen");
		i.sendMessage(ChatColor.YELLOW + "Alias: /gp regen");
		i.sendMessage(ChatColor.WHITE + "(Re)generate the files containing the permission nodes lists.");
		i.sendMessage(ChatColor.YELLOW + "Usage: /getperms reload");
		i.sendMessage(ChatColor.YELLOW + "Alias: /gp reload");
		i.sendMessage(ChatColor.WHITE + "Reload the configuration file for GetPerms.");
		i.sendMessage(ChatColor.YELLOW + "Usage: /getperms help");
		i.sendMessage(ChatColor.YELLOW + "Alias: /gp help");
		i.sendMessage(ChatColor.WHITE + "Displays this help message.");
	}

}

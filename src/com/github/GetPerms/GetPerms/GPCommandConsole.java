package com.github.GetPerms.GetPerms;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class GPCommandConsole {

	GetPerms gp;

	public GPCommandConsole(GetPerms gp){
		this.gp = gp;
	}

	public final boolean cmdHandler(CommandSender i, Command j, String k, String[] l){
		if(j.getName().equalsIgnoreCase("getperms") || j.getName().equalsIgnoreCase("gp")){
			if (l.length > 1) {
				i.sendMessage(ChatColor.RED+"Too many arguments!");
				help(i);
				return false;
			}
			else if (l.length < 1) {
				i.sendMessage(ChatColor.RED+"Too few arguments!");
				help(i);
				return false;
			}
			if (l[0].equalsIgnoreCase("regen")) {
				i.sendMessage(ChatColor.AQUA+"[GetPerms] Regenerating permission files...");
				gp.genFiles(false);
				i.sendMessage(ChatColor.AQUA+"[GetPerms] Files regenerated!");
				return true;
			}
			else {
				i.sendMessage(ChatColor.RED+"Unknown command.");
				help(i);
				return false;
			}
		}
		else if(j.getName().equalsIgnoreCase("getpermsregen") || j.getName().equalsIgnoreCase("gpregen")){
			if (l.length > 0) {
				i.sendMessage(ChatColor.RED+"Unknown command.");
				help(i);
				return false;
			}
			i.sendMessage(ChatColor.AQUA+"[GetPerms] Regenerating permission files...");
			gp.genFiles(false);
			i.sendMessage(ChatColor.AQUA+"[GetPerms] Files regenerated!");
			return true;
		}
		return false;
	}

	public final void help(CommandSender i){
		i.sendMessage(ChatColor.DARK_RED+"Usage: /getperms <regen>");
		i.sendMessage(ChatColor.DARK_RED+"Aliases: /getpermsregen, /gp <regen>, /gpregen");
		i.sendMessage(ChatColor.DARK_RED+"Used to manually (re)generate the files");
		i.sendMessage(ChatColor.DARK_RED+"containing the permission nodes list.");
	}

}

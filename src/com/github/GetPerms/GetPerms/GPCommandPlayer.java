package com.github.GetPerms.GetPerms;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class GPCommandPlayer {

	GetPerms gp;
	PermissionManager permissions;

	public GPCommandPlayer(GetPerms gp){
		this.gp = gp;
	}

	public final boolean cmdHandler(CommandSender i, Command j, String k, String[] l){
		if(gp.usePEX())
			permissions = PermissionsEx.getPermissionManager();
		if(j.getName().equalsIgnoreCase("getperms") || j.getName().equalsIgnoreCase("gp")){
			if(gp.usePEX()){
				if (!permissions.has((Player) i, "getperms.regen")){
					i.sendMessage(ChatColor.RED+"You don't have permission!");
					return true;
				}
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
			else {
				if (i.isOp()) {
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
			}
		}
		else if(j.getName().equalsIgnoreCase("getpermsregen") || j.getName().equalsIgnoreCase("gpregen")){
			if(gp.usePEX()){
				if (!permissions.has((Player) i, "getperms.regen")){
					i.sendMessage(ChatColor.RED+"You don't have permission!");
					return true;
				}
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
			else {
				if (i.isOp()) {
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
			}
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

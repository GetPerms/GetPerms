package com.github.GetPerms.GetPerms;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class GPCommandPlayer{

	GetPerms gp;
	PermissionManager permissions;

	public GPCommandPlayer(GetPerms gp){
		this.gp = gp;
	}

	public final boolean cmdHandler(CommandSender i, Command j, String k, String[] l){
		String a = "";
		if (l.length > 0)
			for (String st : l)
				a = a + " " + st;
		if (gp.usePEX())
			permissions = PermissionsEx.getPermissionManager();
		if (j.getName().equalsIgnoreCase("getperms") || j.getName().equalsIgnoreCase("gp")){
			gp.info(i.getName() + ": /" + j.getName() + a);

			if (l.length > 1){
				i.sendMessage(ChatColor.RED + "Too many arguments!");
				help(i);
				return false;
			}else if (l.length < 1){
				i.sendMessage(ChatColor.RED + "Too few arguments!");
				help(i);
				return false;
			}

			if (gp.usePEX()){

				if (l[0].equalsIgnoreCase("regen")){
					// Permission check
					if (!permissions.has((Player) i, "getperms.regen")){
						i.sendMessage(ChatColor.RED + "You don't have permission!");
						return true;
					}

					i.sendMessage(ChatColor.AQUA + "[GetPerms] Regenerating permission files...");
					gp.genFiles(false);
					i.sendMessage(ChatColor.AQUA + "[GetPerms] Files regenerated!");
					return true;
				}else if (l[0].equalsIgnoreCase("reload")){
					// Permission check
					if (!permissions.has((Player) i, "getperms.reload")){
						i.sendMessage(ChatColor.RED + "You don't have permission!");
						return true;
					}

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

			}else if (i.isOp()){

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

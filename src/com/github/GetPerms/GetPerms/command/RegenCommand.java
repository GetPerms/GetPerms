package com.github.getperms.getperms.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.getperms.getperms.Main;

public class RegenCommand extends AbstractCommand {

	public RegenCommand(Main plugin) {
		super(plugin);
	}

	public RegenCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (args.length != 0) {
			return false;
		} else {
			if (!hasPermission(sender)) {
				sender.sendMessage(ChatColor.RED + "You do not have sufficient permissions to do that!");
				return true;
			}

			sender.sendMessage(ChatColor.AQUA + "[GetPerms] " + ChatColor.RESET + "Regenerating permission files...");
			getPlugin().generateFiles(false);
			sender.sendMessage(ChatColor.AQUA + "[GetPerms] " + ChatColor.GREEN + "Files regenerated!");
		}
		return true;
	}

}

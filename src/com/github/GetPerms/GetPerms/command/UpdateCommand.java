package com.github.getperms.getperms.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.getperms.getperms.Main;
import com.github.getperms.getperms.UpdateTask;

public class UpdateCommand extends AbstractCommand {

	public UpdateCommand(Main plugin) {
		super(plugin);
	}

	public UpdateCommand(Main plugin, String permissionNode) {
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

			sender.sendMessage(ChatColor.AQUA + "[GetPerms] " + ChatColor.RESET + "Running update task...");
			Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new UpdateTask(getPlugin(), sender), 10L);
		}
		return true;
	}

}

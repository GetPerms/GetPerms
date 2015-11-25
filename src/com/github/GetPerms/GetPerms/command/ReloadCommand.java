package com.github.GetPerms.GetPerms.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.GetPerms.GetPerms.Main;
import com.github.GetPerms.GetPerms.UpdateTask;

public class ReloadCommand extends AbstractCommand {

	public ReloadCommand(Main plugin) {
		super(plugin);
	}

	public ReloadCommand(Main plugin, String permissionNode) {
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

			sender.sendMessage(ChatColor.AQUA + "[GetPerms] " + ChatColor.RESET + "Reloading configuration file...");
			getPlugin().configHandler.load();
			Bukkit.getScheduler().cancelTasks(getPlugin());
			if (getPlugin().configuration.getBoolean("autoUpdate", true)) {
				Bukkit.getScheduler().scheduleSyncRepeatingTask(getPlugin(), new UpdateTask(getPlugin()), 10 * 20L,
						60 * 60 * 24 * 20L);
			}
			sender.sendMessage(ChatColor.AQUA + "[GetPerms] " + ChatColor.GREEN + "Configuration reloaded!");
		}
		return true;
	}

}
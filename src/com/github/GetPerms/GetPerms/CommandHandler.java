package com.github.GetPerms.GetPerms;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.github.GetPerms.GetPerms.command.AbstractCommand;
import com.github.GetPerms.GetPerms.command.RegenCommand;
import com.github.GetPerms.GetPerms.command.ReloadCommand;
import com.github.GetPerms.GetPerms.command.UpdateCommand;

public class CommandHandler implements CommandExecutor {

	private Main plugin;

	public CommandHandler(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			showHelp(sender);
			return true;
		}

		String[] newArgs = new String[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			newArgs[i - 1] = args[i];
		}

		AbstractCommand commandClass;
		switch (args[0].toLowerCase()) {
			case "reload":
				commandClass = new ReloadCommand(plugin, "reload");
				break;
			case "regen":
				commandClass = new RegenCommand(plugin, "regen");
				break;
			case "update":
				commandClass = new UpdateCommand(plugin, "update");
				break;
			default:
				showHelp(sender);
				return true;
		}

		if (!commandClass.execute(sender, newArgs)) {
			showHelp(sender);
		}
		return true;
	}

	private void showHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.GREEN + "--------------------GetPerms Help--------------------");
		sender.sendMessage(ChatColor.GOLD + "/getperms reload" + ChatColor.RESET + " - Reloads the configuration file");
		sender.sendMessage(
				ChatColor.GOLD + "/getperms regen" + ChatColor.RESET + " - Regenerates the permission node lists");
		sender.sendMessage(ChatColor.GOLD + "/getperms update" + ChatColor.RESET + " - Runs the update task");
	}

}

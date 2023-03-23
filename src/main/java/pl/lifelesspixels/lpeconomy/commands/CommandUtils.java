package pl.lifelesspixels.lpeconomy.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandUtils {

    public static void sendNoPermissionsMessage(CommandSender sender, String commandName) {
        sender.sendMessage(ChatColor.RED + "You have no permissions to run /" + commandName + " command");
    }

}

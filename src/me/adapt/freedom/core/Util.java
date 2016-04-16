package me.adapt.freedom.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Util {

    public static void action(String username, String message, boolean Red) {
        Bukkit.broadcastMessage((Red ? ChatColor.RED : ChatColor.WHITE) + username + ": " + message);
    }

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void message(CommandSender sender, String message, ChatColor color) {
        sender.sendMessage(color + message);
    }

    public static void message(CommandSender sender, String message) {
        message(sender, message, ChatColor.RED);
    }

}

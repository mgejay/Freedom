package me.adapt.freedom.core;

import me.adapt.freedom.ranking.Ranking.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminChat {

    public static void AChat(CommandSender sender, String message) {
        String name = sender.getName();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Rank.Admin(player)) {
                player.sendMessage(ChatColor.RED + "[" + ChatColor.WHITE + "Admin Chat" + ChatColor.RED + "] " + ChatColor.WHITE + name + " " + Rank.getRank(sender).getPrefix() + ChatColor.RESET + ": " + ChatColor.RED + message);
            }
        }
    }
}

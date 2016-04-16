package me.adapt.freedom.cmds;

import com.earth2me.essentials.Essentials;
import me.adapt.freedom.banning.Ban;
import me.adapt.freedom.core.Freedom;
import me.adapt.freedom.core.Util;
import static me.adapt.freedom.core.Util.message;
import me.adapt.freedom.ranking.Ranking.Rank;
import net.pravian.aero.command.SimpleCommand;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_ban extends SimpleCommand<Freedom> {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        final String reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");

        if (!Rank.getRank(sender).isAtLeast(Rank.SA)) {
            message(sender, "No permission!", ChatColor.YELLOW);
            return true;
        }

        if (args.length == 0) {
            message(sender, "Please provide arguments", ChatColor.RED);
            return true;
        }
        

        final Player player = getPlayer(args[0]);
        final Ban ban = new Ban();

        Essentials pl = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");

        if (player == null) {
            if (args.length > 1) {
                Util.action(sender.getName(), "Banning " + args[0] + "\nReason:" + ChatColor.YELLOW + reason, true);
                ban.initiateOfflineUserBan(pl.getUser(args[0]).getLastLoginAddress().replace(".", "-"), sender, reason, args[0]);
                plugin.bans.save();
                return true;
            }

            if (plugin.bans.contains(args[0])) {
                message(sender, "That IP is already banned!");
                return true;
            }
            
            if (!Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
                message(sender, "Cannot find that player's name! If they're offline, make sure you type it exactly how it is!");
                return true;
                }

            if (args.length == 1) {
                Util.action(sender.getName(), "Banning " + args[0], true);
                ban.initiateOfflineUserBan(pl.getUser(args[0]).getLastLoginAddress().replace(".", "-"), sender, args[0]);
                plugin.bans.save();
                return true;
            }

            if (args[0].contains(".")) {
                if (args.length > 1) {
                    Util.action(sender.getName(), "Banning " + args[0] + "\nReason: " + ChatColor.YELLOW + reason, true);
                    ban.initiateBanIP(args[0].replace(".", "-"), sender, pl.getUser(args[0]).getLastAccountName(), reason);
                    plugin.bans.save();
                    return true;
                }
                

                if (plugin.bans.contains(args[0].replace(".", "-"))) {
                    message(sender, "That IP is already banned!");
                    return true;
                }

                if (args.length == 1) {
                    Util.action(sender.getName(), "Banning " + args[0], true);
                    ban.initiateBanIP(args[0].replace(".", "-"), sender, pl.getUser(args[0]).getLastAccountName());
                    plugin.bans.save();
                    return true;
                }
            }
        }

        if (player != null) {
            if (args.length == 1) {
                Util.action(sender.getName(), "Banning " + player.getName(), true);
                player.kickPlayer(ChatColor.RED + "You have been banned!" + "\nBanned by: " + sender.getName() + "\n" + (plugin.config.getBoolean("canappeal") ? "You can appeal at " + plugin.config.getString("banurl") : ""));
                ban.initiateBan(player, sender);
                plugin.bans.save();
                return true;
            }

            if (args.length > 1) {
                Util.action(sender.getName(), "Banning " + player.getName() + "\nReason:" + ChatColor.YELLOW + reason, true);
                player.kickPlayer(ChatColor.RED + "You have been banned!" + "\nBanned by: " + sender.getName() + "\nReason: " + ChatColor.YELLOW + reason + ChatColor.RED + "\n" + (plugin.config.getBoolean("canappeal") ? "You can appeal at " + plugin.config.getString("banurl") : ""));
                ban.initiateBan(player, sender, reason);
                plugin.bans.save();
                return true;
            }
        }

        return true;
    }

}

package me.adapt.freedom.cmds;

import com.earth2me.essentials.Essentials;
import me.adapt.freedom.banning.Ban;
import me.adapt.freedom.core.Freedom;
import me.adapt.freedom.core.Util;
import me.adapt.freedom.ranking.Ranking.Rank;
import net.pravian.aero.command.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import static me.adapt.freedom.core.Util.message;
import org.bukkit.Bukkit;

public class CMD_unban extends SimpleCommand<Freedom> {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        final Ban ban = new Ban();
        Essentials pl = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
        

        if (!Rank.getRank(sender).isAtLeast(Rank.SA)) {
            message(sender, "No permission!", ChatColor.YELLOW);
            return true;
        }

        if (args.length < 1) {
            message(sender, "/unban <player/ip>");
            return true;
        }
        
        if (args.length > 1) {
            message(sender, "/unban <player/ip>");
            return true;
        }

        if (args[0].contains(".")) {
            if (plugin.bans.contains(args[0].replace(".", "-"))) {
            ban.initiateUnban(args[0].replace(".", "-"));
            ban.save();
            Util.action(sender.getName(), "Unbanning " + args[0], true);
            return true;
            }
            
            if (!plugin.bans.contains(args[0].replace(".", "-"))) {
                message(sender, "IP is not banned.");
                return true;
            }
            
            return true;
        }
        
        if (!args[0].contains(".")) {
            if (plugin.bans.contains(pl.getOfflineUser(args[0]).getLastLoginAddress().replace(".", "-"))) {
            ban.initiateUnban(pl.getOfflineUser(args[0]).getLastLoginAddress().replace(".", "-"));
            ban.save();
            Util.action(sender.getName(), "Unbanning " + args[0], true);
            return true;
        }
            if (!plugin.bans.contains(Bukkit.getOfflinePlayer(args[0]).getPlayer().getAddress().getAddress().getHostAddress().replace(".", "-"))) {
                message(sender, "User is not banned!");
                return true;
            }
      }


        return true;
    }
}

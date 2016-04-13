package me.adapt.freedom.cmds;

import me.adapt.freedom.banning.Ban;
import me.adapt.freedom.core.Freedom;
import me.adapt.freedom.core.Util;
import static me.adapt.freedom.core.Util.message;
import me.adapt.freedom.ranking.Ranking.Rank;
import net.pravian.aero.command.SimpleCommand;
import net.pravian.aero.util.Ips;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_unban extends SimpleCommand<Freedom> {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        final String reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");

        if (args.length == 0) {
            message(sender, "Please provide arguments", ChatColor.RED);
            return true;
        }

        if (!Rank.getRank(sender).isAtLeast(Rank.SA)) {
            message(sender, "No permission!", ChatColor.YELLOW);
            return true;
        }

        final Player player = getPlayer(args[0]);
        final Ban ban = new Ban();
        
            if (args.length > 1) {
                message(sender, "/unban <player>", ChatColor.RED);
                return true;
            }
            
            if (plugin.bans.contains(Ips.getIp(player))) {           
                Util.action(sender.getName(), "Unbanning " + player.getName(), true);
                ban.initiateUnban(player);
                plugin.bans.save();
                return true;
            }
            return true;
    }
}


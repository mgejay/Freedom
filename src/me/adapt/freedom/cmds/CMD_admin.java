package me.adapt.freedom.cmds;

import me.adapt.freedom.banning.Ban;
import me.adapt.freedom.core.Admin;
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

public class CMD_admin extends SimpleCommand<Freedom> {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            message(sender, "/admin <add/remove> <player>", ChatColor.GRAY);
            return true;
        }

        if (args[0].equalsIgnoreCase("add")) {
            Player player = getPlayer(args[1]);
            if (player == null) {
                message(sender, "Cannot find player!", ChatColor.RED);
                return true;
            }

            if (!Rank.getRank(sender).isAtLeast(Rank.SRA)) {
                message(sender, "That is for users ranked 'SENIOR' or higher!", ChatColor.RED);
                return true;
            }

            if (Rank.Admin(player)) {
                message(sender, "That player is already an admin!", ChatColor.RED);
                return true;
            }

            if (!Rank.Admin(player)) {
                Util.action(sender.getName(), "Giving " + player.getName() + " admin", true);
                final Admin admin = new Admin();
                admin.addAdmin(player.getUniqueId().toString(), player.getName(), Ips.getIp(player), "super admin");
                plugin.admins.save();
            }
        }
        return true;
    }
}

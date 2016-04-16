package me.adapt.freedom.cmds;

import java.util.ArrayList;
import me.adapt.freedom.core.Freedom;
import me.adapt.freedom.core.Util;
import static me.adapt.freedom.core.Util.message;
import me.adapt.freedom.ranking.Ranking.Rank;
import net.pravian.aero.command.SimpleCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_mute extends SimpleCommand<Freedom> {

    public static ArrayList<String> muted = new ArrayList<String>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!Rank.getRank(sender).isAtLeast(Rank.SA)) {
            message(sender, "No permission!", ChatColor.YELLOW);
            return true;
        }

        if (args.length == 0) {
            message(sender, "Please provide arguments", ChatColor.RED);
            return true;
        }

        Player player = getPlayer(args[0]);
        if (player == null) {
            message(sender, "Cannot find player!", ChatColor.RED);
            return true;
        }

        if (args[0].equalsIgnoreCase("all")) {
            if (args.length > 1) {
                message(sender, "/mute all", ChatColor.RED);
                return true;
            }

            for (Player allplayers : Bukkit.getOnlinePlayers()) {
                if (!Rank.Admin(allplayers)) {
                    muted.add(allplayers.getName());
                    Util.action(sender.getName(), "Muting all non-admins", true);
                }
            }
        }

        if (Rank.getRank(player).isAtLeast(Rank.SA)) {
            message(sender, "That player is an admin!", ChatColor.RED);
            return true;
        }

        if (!muted.contains(player.getName())) {

            Util.action(sender.getName(), "Muting " + player.getName(), true);
            muted.add(player.getName());
            return true;
        }

        if (muted.contains(player.getName())) {
            muted.remove(player.getName());
            Util.action(sender.getName(), "Unmuting " + player.getName(), true);
        }
        return true;
    }
}

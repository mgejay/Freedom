package me.adapt.freedom.cmds;

import me.adapt.freedom.core.Freedom;
import me.adapt.freedom.core.Util;
import static me.adapt.freedom.core.Util.message;
import me.adapt.freedom.ranking.Ranking.Rank;
import net.pravian.aero.command.SimpleCommand;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_smack extends SimpleCommand<Freedom> {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");

        if (!Rank.getRank(sender).isAtLeast(Rank.SA)) {
            message(sender, "No permission!", ChatColor.YELLOW);
            return true;
        }

        if (args.length == 0) {
            message(sender, "Please include a player and a reason(optional)", ChatColor.RED);
            return true;
        }

        Player player = getPlayer(args[0]);

        if (player == null) {
            message(sender, "That player cannot be found!", ChatColor.RED);
            return true;
        }

        if (args.length == 1) {
            Util.action(sender.getName(), "Smacking " + player.getName(), true);
        }
        if (args.length > 1) {
            Util.action(sender.getName(), "Smacking " + player.getName() + "\nReason: " + ChatColor.YELLOW + reason, true);
        }

        player.setVelocity(player.getEyeLocation().getDirection().multiply(10.0));
        player.setHealth(0.0);
        return true;
    }
}

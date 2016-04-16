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

public class CMD_lockdown extends SimpleCommand<Freedom> {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!Rank.getRank(sender).isAtLeast(Rank.SA)) {
            message(sender, "No permission!", ChatColor.YELLOW);
            return true;
        }

        if (args.length > 0) {
            message(sender, "/lockdown", ChatColor.RED);
            return true;
        }

        if (args.length == 0 && !plugin.config.getBoolean("lockdown-enabled")) {
            Util.action(sender.getName(), "Enabling lockdown", true);
            plugin.config.set("lockdown-enabled", true);
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (!Rank.getRank(players).isAtLeast(Rank.SA)) {
                    players.kickPlayer(ChatColor.RED + "Lockdown has been enabled!\nServer restricted to super admins and higher!");
                }
            }
            plugin.config.save();
            return true;
        }

        if (args.length == 0 && plugin.config.getBoolean("lockdown-enabled")) {
            Util.action(sender.getName(), "Disabling lockdown", true);
            plugin.config.set("lockdown-enabled", false);
            return true;
        }

        return true;
    }
}

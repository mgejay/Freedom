package me.adapt.freedom.cmds;

import me.adapt.freedom.ranking.Ranking.Rank;
import me.adapt.freedom.core.Freedom;
import java.util.ArrayList;
import java.util.List;
import static me.adapt.freedom.core.Util.message;
import net.pravian.aero.command.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_list extends SimpleCommand<Freedom> {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        final List<String> names = new ArrayList<String>();
        for (Player player : server.getOnlinePlayers()) {
            names.add(Rank.getRank(player).getPrefix() + player.getName());
        }
        message(sender, "Current players online: " + names.size() + " out of " + server.getMaxPlayers() + ":\n" + StringUtils.join(names, ChatColor.WHITE + ", "));
        return true;
    }
}

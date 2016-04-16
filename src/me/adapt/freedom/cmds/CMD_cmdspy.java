package me.adapt.freedom.cmds;

import me.adapt.freedom.core.UserData;
import me.adapt.freedom.core.Freedom;
import static me.adapt.freedom.core.Util.message;
import me.adapt.freedom.ranking.Ranking.Rank;
import net.pravian.aero.command.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_cmdspy extends SimpleCommand<Freedom> {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            message(sender, "Console cannot toggle cmdspy, please include message!");
        }
        
        
        if (!Rank.getRank(sender).isAtLeast(Rank.SA)) {
            message(sender, "No permission!", ChatColor.YELLOW);
            return true;
        }

        if (args.length > 1) {
            message(sender, "/cmdspy", ChatColor.RED);
        }

        if (args.length == 0) {
            UserData.getPlayerData((Player) sender).setCMDSPYon(!UserData.getPlayerData((Player) sender).CMDSPYon());
            message(sender, "Toggling commandspy " + (UserData.getPlayerData((Player) sender).CMDSPYon() ? "on" : "off") + ".", ChatColor.RED);
        }

        return true;
    }
}

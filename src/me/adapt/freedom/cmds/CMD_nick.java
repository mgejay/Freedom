package me.adapt.freedom.cmds;

import me.adapt.freedom.ranking.Ranking.Rank;
import me.adapt.freedom.core.Freedom;
import me.adapt.freedom.core.UserData;
import java.util.Arrays;
import java.util.List;
import static me.adapt.freedom.core.Util.colorize;
import static me.adapt.freedom.core.Util.message;
import net.pravian.aero.command.SimpleCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_nick extends SimpleCommand<Freedom> {

    public static final List<String> FORBIDDEN_WORDS = Arrays.asList(new String[]{
        "admin", "owner", "moderator", "server", "developer", "console", "mod"
    });

    public static final List<String> FORBIDDEN_CODES = Arrays.asList(new String[]{
        "&k", "&m", "&n", "&0", "&o"
    });

    // Thanks to Prozza for this
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            message(sender, "/nick <set/off/blacklist/realname> [nick/player]", ChatColor.RED);
            return true;
        }
        
        if (args[0].equalsIgnoreCase("realname")) {
            if (args.length > 2) {
                message(sender, "/nick realname <player>", ChatColor.RED);
                return true;
            }
            
            if (args.length < 2) {
                message(sender, "/nick realname <player>", ChatColor.RED);
                return true;
            }
            
            if (args.length == 2) {
                Player player = getPlayer(args[1]);
                
                if (player == null) {
                    message(sender, "Cannot find player!", ChatColor.RED);
                    return true;
                }
                
                final UserData data = UserData.getPlayerData(player);
                
                if (data.obtainNick() == null) {
                    message(sender, "That player does not have a nickname!", ChatColor.RED);
                    return true;
                }
                
                if (data.obtainNick() != null) {
                    message(sender, colorize(data.obtainNick()) + "'s realname is " + player.getName(), ChatColor.RED);
                    return true;
                }
                
                
            }
        }

        if (args[0].equalsIgnoreCase("blacklist")) {
            if (args.length > 1) {
                message(sender, "/nick blacklist", ChatColor.RED);
                return true;
            }

            message(sender, "Blacklisted words:", ChatColor.GOLD);
            message(sender, StringUtils.join(FORBIDDEN_WORDS, ChatColor.WHITE + ", "), ChatColor.GOLD);
            message(sender, "Blacklisted color codes:", ChatColor.GOLD);
            message(sender, StringUtils.join(FORBIDDEN_CODES, ChatColor.WHITE + ", "), ChatColor.GOLD);
        }

        if (args[0].equalsIgnoreCase("set")) {
            final String inputNick = StringUtils.join(args, " ", 1, args.length);
            final String outputNick = Freedom.colorize(StringUtils.replaceEachRepeatedly(StringUtils.strip(inputNick),
                    new String[]{
                        "" + ChatColor.COLOR_CHAR, "&k", "&m", "&n", "&0", "&o"
                    },
                    new String[]{
                        "", "", "", "", "", ""
                    })) + ChatColor.RESET;

            if (inputNick.equalsIgnoreCase("")) {
                message(sender, "Please provide a valid nickname!", ChatColor.RED);
                return true;
            }

            if (!Rank.getRank(sender).isAtLeast(Rank.SA)) {
                final String rawNick = ChatColor.stripColor(outputNick).toLowerCase();

                if (rawNick.length() > 15) {
                    message(sender, "Nick cannot be longer than 15 characters!", ChatColor.RED);
                    return true;
                }

                for (String word : FORBIDDEN_WORDS) {
                    if (rawNick.contains(word)) {
                        message(sender, "Nick contains a forbidden word!", ChatColor.RED);
                        return true;
                    }
                }

                for (String codes : FORBIDDEN_CODES) {
                    if (rawNick.contains(codes)) {
                        message(sender, "Nick contains a forbidden word!", ChatColor.RED);
                        return true;
                    }
                }
            }
            UserData.getPlayerData((Player) sender).setNick(outputNick);
            sender.sendMessage("Nick set to '" + outputNick + "'.");
            return true;

        }

        if (args[0].equalsIgnoreCase("off")) {

            UserData.getPlayerData((Player) sender).setNick(null);
            message(sender, "Nick has been removed.", ChatColor.RED);
            return true;
        }

        if (!args[0].equalsIgnoreCase("set") || !args[0].equalsIgnoreCase("off") || !args[0].equalsIgnoreCase("blacklist")) {
            message(sender, "/nick <set/off> [nick]", ChatColor.RED);
            return true;
        }

        return true;
    }
}

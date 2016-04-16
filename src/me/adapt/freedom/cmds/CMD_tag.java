package me.adapt.freedom.cmds;

import me.adapt.freedom.ranking.Ranking.Rank;
import me.adapt.freedom.core.Freedom;
import me.adapt.freedom.core.UserData;
import java.util.Arrays;
import java.util.List;
import static me.adapt.freedom.core.Util.message;
import net.pravian.aero.command.SimpleCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_tag extends SimpleCommand<Freedom> {

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
            message(sender, "/tag <set/off/blacklist> [nick]", ChatColor.RED);
            return true;
        }

        if (args[0].equalsIgnoreCase("blacklist")) {
            if (args.length > 1) {
                message(sender, "/tag blacklist", ChatColor.RED);
                return true;
            }

            message(sender, "Blacklisted words:", ChatColor.GOLD);
            message(sender, StringUtils.join(FORBIDDEN_WORDS, ", "));
            message(sender, "Blacklisted color codes:", ChatColor.GOLD);
            message(sender, StringUtils.join(FORBIDDEN_CODES, ChatColor.WHITE + ", "));
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            final String inputTag = StringUtils.join(args, " ", 1, args.length);
            final String outputTag = Freedom.colorize(StringUtils.replaceEachRepeatedly(StringUtils.strip(inputTag),
                    new String[]{
                        "" + ChatColor.COLOR_CHAR, "&k", "&m", "&n", "&0", "&o"
                    },
                    new String[]{
                        "", "", "", "", "", ""
                    })) + ChatColor.WHITE;

            if (inputTag.equalsIgnoreCase("")) {
                message(sender, "Please provide a valid tag!", ChatColor.RED);
                return true;
            }

            if (!Rank.getRank(sender).isAtLeast(Rank.SA)) {
                final String rawTag = ChatColor.stripColor(outputTag).toLowerCase();

                if (rawTag.length() > 20) {
                    message(sender, "Tag cannot be longer than 20 characters!", ChatColor.RED);
                    return true;
                }

                for (String word : FORBIDDEN_WORDS) {
                    if (rawTag.contains(word)) {
                        message(sender, "Tag contains a forbidden word!", ChatColor.RED);
                        return true;
                    }
                }
            }
            UserData.getPlayerData((Player) sender).setTag(outputTag);
            message(sender, "Tag set to '" + outputTag + "'.");
            return true;

        }

        if (args[0].equalsIgnoreCase("off")) {
            if (args.length > 1) {
                message(sender, "/tag off", ChatColor.RED);
                return true;
            }
            UserData.getPlayerData((Player) sender).setTag(null);
            message(sender, "Tag has been removed.", ChatColor.RED);
            return true;
        }

        if (!args[0].equalsIgnoreCase("set") || !args[0].equalsIgnoreCase("off") || !args[0].equalsIgnoreCase("blacklist")) {
            message(sender, "/tag <set/off> [tag]", ChatColor.RED);
            return true;
        }

        return true;
    }
}

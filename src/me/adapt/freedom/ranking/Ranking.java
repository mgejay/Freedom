package me.adapt.freedom.ranking;

import java.util.ArrayList;
import static me.adapt.freedom.core.Freedom.plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ranking {

    public static ArrayList<String> Imposter = new ArrayList<>();

    public enum Rank {

        NON(-2, "Non-Op", ChatColor.GREEN + ""), IMPOSTER(-1, "Imposter", ChatColor.YELLOW + "[Imposter]"), OP(0, "Op", ChatColor.RED + "[OP]"), SA(1, "Super Admin", ChatColor.GOLD + "[SA]"), STA(2, "Super Telnet Admin", ChatColor.DARK_GREEN + "[STA]"), SRA(3, "Senior Admin", ChatColor.LIGHT_PURPLE + "[SrA]"), CONSOLE(4, "Console", ChatColor.DARK_AQUA + "[Console]");

        private final String name;
        private final String prefix;
        private int level;

        private Rank(int level, String name, String prefix) {
            this.level = level;
            this.name = name;
            this.prefix = prefix;
        }

        public int getLevel() {
            return level;
        }

        public boolean isAtLeast(Rank rank) {
            return level >= rank.getLevel();
        }

        public String getName() {
            return name;
        }

        public String getPrefix() {
            return prefix;
        }

        public static boolean Admin(CommandSender sender) {
            Player player = (Player) sender;
            return plugin.admins.contains(player.getUniqueId().toString());
        }

        public static boolean SA(CommandSender sender) {
            Player player = (Player) sender;
            return plugin.admins.getString(player.getUniqueId() + ".rank").equalsIgnoreCase("super admin");
        }

        public static boolean STA(CommandSender sender) {
            Player player = (Player) sender;
            return plugin.admins.getString(player.getUniqueId() + ".rank").equalsIgnoreCase("telnet admin");
        }

        public static boolean SrA(CommandSender sender) {
            Player player = (Player) sender;
            return plugin.admins.getString(player.getUniqueId() + ".rank").equalsIgnoreCase("senior admin");
        }

        public static boolean Imposter(CommandSender sender) {
            Player player = (Player) sender;
            return Imposter.contains(player.getName());
        }

        public static Rank getRank(CommandSender sender) {
            if (!(sender instanceof Player)) {
                return Rank.CONSOLE;
            }

            final Player player = (Player) sender;

            if (SA(player)) {
                return Rank.SA;
            }

            if (STA(player)) {
                return Rank.STA;
            }

            if (SrA(player)) {
                return Rank.SRA;
            }

            if (!Admin(player) && player.isOp()) {
                return Rank.OP;
            }

            if (Imposter(player)) {
                return Rank.IMPOSTER;
            }
            return Rank.NON;
        }
    }
}

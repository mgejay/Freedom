package me.adapt.freedom.ranking;

import static me.adapt.freedom.core.Freedom.plugin;
import net.pravian.aero.util.Ips;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ranking {

    public enum Rank {

        NON(-2, ChatColor.GREEN + "Non-Op", ChatColor.GREEN + ""),
        IMPOSTER(-1, ChatColor.YELLOW + "Imposter", ChatColor.YELLOW + "(Imposter)"),
        OP(0, ChatColor.DARK_RED + "Op", ChatColor.RED + "(OP)"),
        SA(1, ChatColor.GOLD + "Super Admin", ChatColor.GOLD + "(SA)"),
        STA(2, ChatColor.DARK_GREEN + "Super Telnet Admin", ChatColor.DARK_GREEN + "(STA)"),
        SRA(3, ChatColor.LIGHT_PURPLE + "Senior Admin", ChatColor.LIGHT_PURPLE + "(SrA)"),
        EXECUTIVE(4, ChatColor.DARK_RED + "Executive", ChatColor.DARK_RED + "(Exec)"),
        DEVELOPER(5, ChatColor.DARK_PURPLE + "Developer", ChatColor.DARK_PURPLE + "(Dev)"),
        OWNER(6, ChatColor.AQUA + "Owner", ChatColor.AQUA + "(Owner)"),
        CONSOLE(7, ChatColor.DARK_AQUA + "Console", ChatColor.DARK_AQUA + "(Console)");

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
            return Admin(player) && plugin.admins.getString(player.getUniqueId().toString() + ".rank").equalsIgnoreCase("super admin");
        }

        public static boolean STA(CommandSender sender) {
            Player player = (Player) sender;
            return Admin(player) && plugin.admins.getString(player.getUniqueId().toString() + ".rank").equalsIgnoreCase("telnet admin");
        }

        public static boolean SrA(CommandSender sender) {
            Player player = (Player) sender;
            return Admin(player) && plugin.admins.getString(player.getUniqueId().toString() + ".rank").equalsIgnoreCase("senior admin");
        }

        public static boolean Dev(CommandSender sender) {
            Player player = (Player) sender;
            return Admin(player) && plugin.admins.getString(player.getUniqueId().toString() + ".rank").equalsIgnoreCase("developer");
        }

        public static boolean Executive(CommandSender sender) {
            Player player = (Player) sender;
            return Admin(player) && plugin.admins.getString(player.getUniqueId().toString() + ".rank").equalsIgnoreCase("executive");
        }

        public static boolean Owner(CommandSender sender) {
            Player player = (Player) sender;
            return Admin(player) && plugin.admins.getString(player.getUniqueId().toString() + ".rank").equalsIgnoreCase("owner");
        }

        public static boolean Imposter(CommandSender sender) {
            Player player = (Player) sender;
            return Admin(player) && !plugin.admins.getString(player.getUniqueId().toString() + ".ip").contains(Ips.getIp(player));
        }

        public static Rank getRank(CommandSender sender) {
            if (!(sender instanceof Player)) {
                return Rank.CONSOLE;
            }

            final Player player = (Player) sender;

            if (!Imposter(player) && SA(player)) {
                return Rank.SA;
            }

            if (!Imposter(player) && STA(player)) {
                return Rank.STA;
            }

            if (!Imposter(player) && SrA(player)) {
                return Rank.SRA;
            }

            if (player.isOp() && !Admin(player)) {
                return Rank.OP;
            }

            if (Imposter(player)) {
                return Rank.IMPOSTER;
            }

            if (!Imposter(player) && Dev(player)) {
                return Rank.DEVELOPER;
            }

            if (!Imposter(player) && Owner(player)) {
                return Rank.OWNER;
            }

            if (!Imposter(player) && Executive(player)) {
                return Rank.EXECUTIVE;
            }

            if (!player.isOp() && !Admin(player)) {
                return Rank.NON;
            }

            return Rank.NON;
        }
    }
}

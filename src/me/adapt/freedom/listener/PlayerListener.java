package me.adapt.freedom.listener;

import java.lang.reflect.Field;
import static me.adapt.freedom.core.Freedom.plugin;
import me.adapt.freedom.core.UserData;
import me.adapt.freedom.core.Util;
import static me.adapt.freedom.core.Util.message;
import me.adapt.freedom.ranking.Ranking;
import me.adapt.freedom.ranking.Ranking.Rank;
import net.pravian.aero.util.Ips;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class PlayerListener implements Listener {

    CommandMap cmap = getcmdMap();

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        final UserData data = UserData.getPlayerData(p);

        if (Rank.Admin(p)) {
            event.setJoinMessage(p.getName() + " is a " + Rank.getRank(p).getName() + ChatColor.YELLOW + "\n" + p.getName() + " has joined the game.");
            data.setTag(Rank.getRank(p).getPrefix());
        }

        if (Rank.Imposter(p)) {
            event.setJoinMessage(p.getName() + " is an " + ChatColor.UNDERLINE + Rank.getRank(p).getName() + ChatColor.RESET + ChatColor.YELLOW + "\n" + p.getName() + " has joined the game.");
            data.setTag(Rank.getRank(p).getPrefix());
        }

        if (p.isOp() && !Rank.Admin(p)) {
            data.setTag("&c[Op]");
        }

        if (Rank.Admin(p) && !Ips.getIp(p).equals(plugin.admins.getString(p.getUniqueId() + ".ip"))) {
            Ranking.Imposter.add(p.getName());
        }

        if (plugin.bans.contains(Ips.getIp(p)) && !plugin.bans.contains(Ips.getIp(p) + ".reason")) {
            p.kickPlayer(ChatColor.RED + "Your IP address is banned!" + "\nBanned by: " + plugin.bans.getString(Ips.getIp(p) + ".banned-by") + "\nYou can appeal at http://play.immafreedom.eu");
        }

        if (plugin.bans.contains(Ips.getIp(p)) && plugin.bans.contains(Ips.getIp(p) + ".reason")) {
            p.kickPlayer(ChatColor.RED + "Your IP address is banned!" + "\nBanned by: " + plugin.bans.getString(Ips.getIp(p) + ".banned-by") + "\nReason: " + ChatColor.YELLOW + plugin.bans.getString(Ips.getIp(p) + ".reason") + ChatColor.RED + "\nYou can appeal at http://play.immafreedom.eu");
        }
    }

    private CommandMap getcmdMap() {
        if (cmap == null) {
            try {
                final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Bukkit.getServer());
                return getcmdMap();
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (cmap != null) {
            return cmap;
        }
        return getcmdMap();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPreprocessCommand(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();

        if (Rank.Imposter(player)) {
            message(player, "You cannot send commands whilst impostered.", ChatColor.GREEN);
            event.setCancelled(true);
        }
        if (event.getMessage().split(" ")[0].contains(":")) {
            message(player, "You cannot send plugin specific commands.", ChatColor.GREEN);
            event.setCancelled(true);
        }

        for (String blocked : plugin.config.getStringList("Blocked-Cmds")) {
            if ((event.getMessage().equalsIgnoreCase("/" + blocked) || event.getMessage().split(" ")[0].equalsIgnoreCase("/" + blocked))) {
                event.setCancelled(true);
                message(player, "That command is disabled.", ChatColor.GREEN);
                return;
            }
            if (cmap.getCommand(blocked) == null) {
                continue;
            }
            if (cmap.getCommand(blocked).getAliases() == null) {
                continue;
            }
            for (String blocked2 : cmap.getCommand(blocked).getAliases()) {
                if ((event.getMessage().equalsIgnoreCase("/" + blocked2) || event.getMessage().split(" ")[0].equalsIgnoreCase("/" + blocked2))) {
                    event.setCancelled(true);
                    message(player, "That command is disabled.", ChatColor.GREEN);
                    return;
                }
            }
        }

        for (String blockedsa : plugin.config.getStringList("SA-Commands")) {
            if ((event.getMessage().equalsIgnoreCase("/" + blockedsa) || event.getMessage().split(" ")[0].equalsIgnoreCase("/" + blockedsa)) && !Rank.Admin(player)) {
                event.setCancelled(true);
                message(player, "That command is disabled for non-admins.", ChatColor.GREEN);
                return;
            }
            if (cmap.getCommand(blockedsa) == null) {
                continue;
            }
            if (cmap.getCommand(blockedsa).getAliases() == null) {
                continue;
            }
            for (String blockedsa2 : cmap.getCommand(blockedsa).getAliases()) {
                if ((event.getMessage().equalsIgnoreCase("/" + blockedsa2) || event.getMessage().split(" ")[0].equalsIgnoreCase("/" + blockedsa2)) && !Rank.Admin(player)) {
                    event.setCancelled(true);
                    message(player, "That command is disabled for non-admins.", ChatColor.GREEN);
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OnServerPing(ServerListPingEvent event) {
        String ip = event.getAddress().getHostAddress();

        if (plugin.bans.contains(ip)) {
            event.setMotd(Util.colorize(plugin.config.getString("motd") + ChatColor.RED + "\nYou are banned!"));
        }

        if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
            event.setMotd(Util.colorize(plugin.config.getString("motd") + ChatColor.RED + "\nServer is full!"));
        } else {
            event.setMotd(Util.colorize(plugin.config.getString("motd")));
        }

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(PlayerChatEvent event) {
        final Player player = event.getPlayer();
        final UserData data = UserData.getPlayerData(player);
        event.setFormat(Util.colorize("" + "%1$s&7-> %2$s"));
        if (data.obtainTag() != null) {
            event.setFormat(Util.colorize("" + data.obtainTag().replaceAll("%", "%%") + " %1$s&7-> %2$s"));
        }
        if (data.Muted()) {
            event.setCancelled(true);
            message(player, "You are muted, you cannot speak!", ChatColor.RED);
        }
    }
}

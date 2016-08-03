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
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class PlayerListener implements Listener {

    CommandMap cmap = getcmdMap();

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        switch (event.getBlockPlaced().getType()) {
            case TNT:
                if (!Rank.getRank(player).isAtLeast(Rank.SA)) {
                    player.sendMessage(ChatColor.RED + "I'm sorry, TNT is disabled for regular players");
                    event.setCancelled(true);
                } else {
                    event.setCancelled(false);
                }
            case STATIONARY_LAVA:
            case LAVA_BUCKET:
            case LAVA:
                if (!Rank.getRank(player).isAtLeast(Rank.SA)) {
                    player.sendMessage(ChatColor.RED + "I'm sorry, Lava is disabled for regular players!");
                    event.setCancelled(true);
                } else {
                    event.setCancelled(false);
                }
                break;

        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (Rank.Imposter(player)) {
            player.sendMessage(ChatColor.RED + "You may not move as an impostor.");
            player.teleport(player.getLocation());
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (plugin.bans.contains(Ips.getIp(event).replace(".", "-")) || plugin.bans.contains(player.getName().replace("username: ", ""))) {
            if (plugin.bans.contains(Ips.getIp(event).replace(".", "-") + ".reason")) {
                if (plugin.bans.getBoolean("canappeal")) {
                    event.disallow(null, ChatColor.RED + "You are banned!" + "\nBanned by: " + plugin.bans.getString(Ips.getIp(event).replace(".", "-") + ".banned-by") + "\nReason: " + ChatColor.YELLOW + plugin.bans.getString(Ips.getIp(event).replace(".", "-") + ".reason") + ChatColor.RED + "\nYou can appeal at " + plugin.config.getString("ban-url"));
                }
                if (!plugin.bans.getBoolean("canappeal")) {
                    event.disallow(null, ChatColor.RED + "You are banned!" + "\nBanned by: " + plugin.bans.getString(Ips.getIp(event).replace(".", "-") + ".banned-by") + "\nReason: " + ChatColor.YELLOW + plugin.bans.getString(Ips.getIp(event).replace(".", "-") + ".reason"));
                }
            }
            if (!plugin.bans.contains(Ips.getIp(event).replace(".", "-") + ".reason")) {
                if (plugin.bans.getBoolean("canappeal")) {
                    event.disallow(null, ChatColor.RED + "You are banned!" + "\nBanned by: " + plugin.bans.getString(Ips.getIp(event).replace(".", "-") + ".banned-by") + "\nYou can appeal at " + plugin.config.getString("ban-url"));
                }
                if (!plugin.bans.getBoolean("canappeal")) {
                    event.disallow(null, ChatColor.RED + "You are banned!" + "\nBanned by: " + plugin.bans.getString(Ips.getIp(event).replace(".", "-") + ".banned-by"));
                }
            }
        }

        if (plugin.config.getBoolean("lockdown-enabled") && !Rank.getRank(player).isAtLeast(Rank.SA)) {
            event.disallow(null, ChatColor.RED + "Lockdown is enabled!\nOnly admins can access the server during lockdown!\nIf you think that this is a mistake, please contact a server executive/administrator!");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        final UserData data = UserData.getPlayerData(player);

        if (Rank.Admin(player)) {
            if (!Rank.Imposter(player)) {
                plugin.admins.set(player.getUniqueId().toString() + ".last-login-name", player.getName());
            }

            event.setJoinMessage(player.getName() + " is " + (Rank.Owner(player) || Rank.Imposter(player) || Rank.Executive(player) ? "an " : "a ") + Rank.getRank(player).getName() + ChatColor.YELLOW + "\n" + player.getName() + " has joined the game.");
            data.setTag(Rank.getRank(player).getPrefix());
        }

        if (plugin.config.getBoolean("lockdown-enabled") && Rank.getRank(player).isAtLeast(Rank.SA)) {
            message(player, "Info: Lockdown is enabled! Only admins can access the server!", ChatColor.RED);
        }

        if (player.isOp() && !Rank.Admin(player)) {
            data.setTag("&c(Op)");
        }

    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        final UserData data = UserData.getPlayerData(player);
        final UserData udata = new UserData(player, player.getUniqueId(), data.toString());
        if (Rank.Imposter(player)) {
            udata.DATATable.remove(Ips.getIp(player), data);
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

        if (Rank.getRank(player).equals(Rank.NON)) {

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

        if (!Rank.Admin(player)) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (Rank.Admin(players) && UserData.getPlayerData(players).CMDSPYon()) {
                    if (event.getMessage().startsWith("//")) {
                        players.sendMessage(ChatColor.GRAY + player.getName() + ": " + ChatColor.RED + event.getMessage());
                    } else {
                        players.sendMessage(ChatColor.GRAY + player.getName() + ": " + event.getMessage().toLowerCase());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public static void onPreLogin(AsyncPlayerPreLoginEvent event) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.getName().equalsIgnoreCase(event.getName())) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Username is logged in!");
            }           
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OnServerPing(ServerListPingEvent event) {
        String ip = event.getAddress().toString().replace(".", "-");

        if (plugin.bans.contains(ip)) {
            event.setMotd(Util.colorize(plugin.config.getString("motd").replace("%servername%", plugin.config.getString("server-name")) + "\nYou are banned!"));
        }

        if (plugin.config.getBoolean("lockdown-enabled")) {
            event.setMotd(Util.colorize(plugin.config.getString("motd").replace("%servername%", plugin.config.getString("server-name")) + "\nLockdown is enabled!"));
        }

        if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
            event.setMotd(Util.colorize(plugin.config.getString("motd").replace("%servername%", plugin.config.getString("server-name")) + "\nServer is full!"));
        }

        event.setMotd(Util.colorize(plugin.config.getString("motd").replace("%servername%", plugin.config.getString("server-name"))));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(PlayerChatEvent event) {
        final Player player = event.getPlayer();
        final UserData data = UserData.getPlayerData(player);
        String nick = null;
        String tag = null;

        if (data.obtainNick() != null) {
            nick = Util.colorize(data.obtainNick());
        }

        if (data.obtainTag() != null) {
            tag = Util.colorize(data.obtainTag());
        }

        event.setFormat((tag != null ? tag + " " : "") + ChatColor.GRAY + "[" + (nick != null ? ChatColor.WHITE + "-" + nick : ChatColor.GRAY + player.getName()) + ChatColor.GRAY "] " + ChatColor.DARK_GRAY + "-> " + ChatColor.WHITE + event.getMessage());

        if (data.Muted()) {
            event.setCancelled(true);
            message(player, "You are muted, you cannot speak!", ChatColor.RED);
        }

        if (event.getMessage().length() >= 6) {
            int caps = 0;
            for (char c : event.getMessage().toCharArray()) {
                if (Character.isUpperCase(c)) {
                    caps++;
                }
            }
            if (caps / event.getMessage().length() > 0.65) {
                player.sendMessage(ChatColor.RED + "Too many caps!");
                String message = event.getMessage().toLowerCase();
                event.setMessage(message);
            }
        }
    }
}

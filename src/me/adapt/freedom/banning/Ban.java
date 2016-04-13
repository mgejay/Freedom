package me.adapt.freedom.banning;

import static me.adapt.freedom.core.Freedom.plugin;
import net.pravian.aero.util.Ips;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ban {

    public void initiateBan(Player player, CommandSender sender) {
        plugin.bans.set(Ips.getIp(player), Ips.getIp(player));
        plugin.bans.set(Ips.getIp(player) + ".last-login-name", player.getName());
        plugin.bans.set(Ips.getIp(player) + ".banned-by", sender.getName());
    }

    public void initiateBan(Player player, CommandSender sender, String reason) {
        plugin.bans.set(Ips.getIp(player), Ips.getIp(player));
        plugin.bans.set(Ips.getIp(player) + ".last-login-name", player.getName());
        plugin.bans.set(Ips.getIp(player) + ".banned-by", sender.getName());
        plugin.bans.set(Ips.getIp(player) + ".reason", reason);
    }

    public void initiateUnban(Player player) {
        plugin.bans.set(Ips.getIp(player), null);
    }

}

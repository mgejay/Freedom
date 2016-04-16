package me.adapt.freedom.banning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static me.adapt.freedom.core.Freedom.plugin;
import net.pravian.aero.util.Ips;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ban {

    public void initiateBan(Player player, CommandSender sender) {
        try {
            FileWriter stream = new FileWriter(plugin.bans.toString());
            BufferedWriter out = new BufferedWriter(stream);
            plugin.bans.set(Ips.getIp(player).replace(".", "-"), Ips.getIp(player).replace(".", "-"));
            plugin.bans.set(Ips.getIp(player).replace(".", "-") + ".username", player.getName());
            plugin.bans.set(Ips.getIp(player).replace(".", "-") + ".banned-by", sender.getName());
            out.newLine();
        } catch (IOException ex) {
            Logger.getLogger(Ban.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initiateBanIP(String ip, CommandSender sender, String user) {
        try {
            FileWriter stream = new FileWriter(plugin.bans.toString());
            BufferedWriter out = new BufferedWriter(stream);
            plugin.bans.set(ip, ip);
            plugin.bans.set(ip + ".username", user);
            plugin.bans.set(ip + ".banned-by", sender.getName());
            out.newLine();
        } catch (IOException ex) {
            Logger.getLogger(Ban.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initiateBanIP(String ip, CommandSender sender, String user, String reason) {
        try {
            FileWriter stream = new FileWriter(plugin.bans.toString());
            BufferedWriter out = new BufferedWriter(stream);
            plugin.bans.set(ip, ip);
            plugin.bans.set(ip + ".username", user);
            plugin.bans.set(ip + ".banned-by", sender.getName());
            plugin.bans.set(ip + ".reason", reason);
            out.newLine();
        } catch (IOException ex) {
            Logger.getLogger(Ban.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initiateOfflineUserBan(String ip, CommandSender sender, String args) {
        try {
            FileWriter stream = new FileWriter(plugin.bans.toString());
            BufferedWriter out = new BufferedWriter(stream);
            plugin.bans.set(ip, ip);
            plugin.bans.set(ip + ".username", args);
            plugin.bans.set(ip + ".banned-by", sender.getName());
            out.newLine();
        } catch (IOException ex) {
            Logger.getLogger(Ban.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initiateOfflineUserBan(String ip, CommandSender sender, String args, String reason) {
        try {
            FileWriter stream = new FileWriter(plugin.bans.toString());
            BufferedWriter out = new BufferedWriter(stream);
            plugin.bans.set(ip, ip);
            plugin.bans.set(ip + ".username", args);
            plugin.bans.set(ip + ".banned-by", sender.getName());
            plugin.bans.set(ip + ".reason", reason);
            out.newLine();
        } catch (IOException ex) {
            Logger.getLogger(Ban.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save() {
        plugin.bans.save();
    }

    public void initiateBan(Player player, CommandSender sender, String reason) {
        try {
            FileWriter stream = new FileWriter(plugin.bans.toString());
            BufferedWriter out = new BufferedWriter(stream);
            plugin.bans.set(Ips.getIp(player).replace(".", "-"), Ips.getIp(player).replace(".", "-"));
            plugin.bans.set(Ips.getIp(player).replace(".", "-") + ".username", player.getName());
            plugin.bans.set(Ips.getIp(player).replace(".", "-") + ".banned-by", sender.getName());
            plugin.bans.set(Ips.getIp(player).replace(".", "-") + ".reason", reason);
            out.newLine();
        } catch (IOException ex) {
            Logger.getLogger(Ban.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initiateUnban(String args) {
        plugin.bans.set(args, null);
    }

}

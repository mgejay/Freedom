package me.adapt.freedom.core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static me.adapt.freedom.core.Freedom.plugin;
import org.bukkit.entity.Player;

public class Admin {

    public void addAdmin(Player player, String IP, String lastloginname, String rank) {
        try {
            FileWriter stream = new FileWriter(plugin.admins.toString());
            BufferedWriter out = new BufferedWriter(stream);

            plugin.admins.set(player.getUniqueId().toString(), player.getUniqueId().toString());
            plugin.admins.set(player.getUniqueId() + ".ip", IP);
            plugin.admins.set(player.getUniqueId() + ".last-login-name", lastloginname);
            plugin.admins.set(player.getUniqueId() + ".rank", rank);
            out.write("\n");
        } catch (IOException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void removeAdmin(String IP) {
        plugin.admins.set(IP, null);
    }

    public void save() {
        plugin.admins.save();
    }
}

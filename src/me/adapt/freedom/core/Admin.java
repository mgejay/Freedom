package me.adapt.freedom.core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static me.adapt.freedom.core.Freedom.plugin;

public class Admin {

    public void addAdmin(String UUID, String lastloginname, String ip, String rank) {
        try {
            FileWriter stream = new FileWriter(plugin.admins.toString());
            BufferedWriter out = new BufferedWriter(stream);

            plugin.admins.set(UUID, UUID);
            plugin.admins.set(UUID + ".last-login-name", lastloginname);
            plugin.admins.set(UUID + ".ip", ip);
            plugin.admins.set(UUID + ".rank", rank);
            out.newLine();
        } catch (IOException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void removeAdmin(String UUID) {
        plugin.admins.set(UUID, null);
    }
}

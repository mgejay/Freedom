package me.adapt.freedom.core;

import me.adapt.freedom.cmds.CMD_ban;
import me.adapt.freedom.listener.PlayerListener;
import net.pravian.aero.command.handler.AeroCommandHandler;
import net.pravian.aero.command.handler.SimpleCommandHandler;
import net.pravian.aero.config.YamlConfig;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.plugin.PluginManager;

public class Freedom extends AeroPlugin<Freedom> {

    public static Freedom plugin;
    public static AeroCommandHandler handler;
    public YamlConfig config;
    public YamlConfig admins;
    public YamlConfig bans;

    //
    @Override
    public void load() {
        Freedom.plugin = this;
        this.config = new YamlConfig(plugin, "config.yml", true);
        this.bans = new YamlConfig(plugin, "bans.yml", true);
        this.admins = new YamlConfig(plugin, "admins.yml", true);
    }

    @Override
    public void enable() {
        Freedom.plugin = this;
        handler = new SimpleCommandHandler(plugin);
        handler.setCommandClassPrefix("CMD_");
        handler.loadFrom(CMD_ban.class.getPackage());
        handler.registerAll(handler.getCommandClassPrefix(), true);

        config.load();
        bans.load();
        admins.load();

        final PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(), plugin);

    }

    @Override
    public void disable() {
        Freedom.plugin = null;
    }

}

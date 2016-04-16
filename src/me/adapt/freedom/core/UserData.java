package me.adapt.freedom.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static me.adapt.freedom.core.Util.message;
import net.pravian.aero.util.Ips;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class UserData {

    // Thanks to Prozza for the idea here.
    public static final Map<String, UserData> DATATable = new HashMap<String, UserData>(); // ip,data
    public static final long PURGETable = 20L * 60L * 5L;

    public static boolean hasPlayerData(Player player) {
        return DATATable.containsKey(Ips.getIp(player));
    }

    public static UserData getSyncedUserData(Player player) {
        synchronized (DATATable) {
            return getPlayerData(player);
        }
    }

    public static UserData getPlayerData(Player player) {
        final String ip = Ips.getIp(player);

        UserData userdata = UserData.DATATable.get(ip);

        if (userdata != null) {
            return userdata;
        }

        if (Bukkit.getOnlineMode()) {
            for (UserData check : DATATable.values()) {
                if (check.player.getName().equalsIgnoreCase(player.getName())) {
                    userdata = check;
                    break;
                }
            }
        }

        if (userdata != null) {
            return userdata;
        }

        userdata = new UserData(player, player.getUniqueId(), ip);
        userdata.DATATable.put(ip, userdata);

        return userdata;
    }
    //
    private final Player player;
    private final String ip;
    private final UUID uuid;
    //
    private boolean inAC = false;
    private boolean CMDSPYon = true;
    private boolean halted = false;
    private String tag = null;
    private String nick = null;
    private Location frozenArea;
    private BukkitTask autounmute;
    private BukkitTask autounfreeze;

    public UserData(Player player, UUID uuid, String ip) {
        this.player = player;
        this.uuid = uuid;
        this.ip = ip;
    }

    public boolean Muted() {
        return autounmute != null;
    }

    public void setMuted(boolean muted) {
        autounmute.cancel();
        autounmute = null;

        if (!muted) {
            return;
        }

        autounmute = new BukkitRunnable() {
            @Override
            public void run() {
                Util.action("Freedom", "Unmuting " + player.getName(), false);
                setMuted(false);
            }
        }.runTaskLater(Freedom.plugin, PURGETable);
    }

    public boolean CMDSPYon() {
        return this.CMDSPYon;
    }

    public void setCMDSPYon(boolean CMDSPYon) {
        this.CMDSPYon = CMDSPYon;
    }

    public boolean inAC() {
        return this.CMDSPYon;
    }

    public void setInAC(boolean inAC) {
        this.inAC = inAC;
    }

    public void setTag(String tag) {
        if (tag == null) {
            this.tag = null;
        } else {
            this.tag = Util.colorize(tag) + ChatColor.RESET;
        }
    }

    public String obtainTag() {
        return this.tag;
    }

    public void setNick(String nick) {
        if (nick == null) {
            this.nick = null;
        } else {
            this.nick = Util.colorize(nick) + ChatColor.RESET;
        }
    }

    public String obtainNick() {
        return this.nick;
    }

    public boolean Halted() {
        return this.halted;
    }

    public void setHalted(boolean halted) {
        this.halted = halted;

        if (halted) {
            player.setOp(false);
            player.setGameMode(GameMode.SURVIVAL);
            player.setFlying(false);
            setFrozen(true);
            setMuted(true);

            message(player, "You are halted!", ChatColor.RED);
        } else {
            player.setOp(true);
            player.setGameMode(GameMode.CREATIVE);
            setFrozen(false);
            setMuted(false);

            message(player, "You have been released from the halt.", ChatColor.RED);
        }

    }

    public boolean Frozen() {
        return autounfreeze != null;
    }

    public void setFrozen(boolean freeze) {
        autounfreeze.cancel();
        autounfreeze = null;
        frozenArea = null;

        if (player.getGameMode() != GameMode.CREATIVE) {
            player.setFlying(true);
        }

        if (!freeze) {
            return;
        }

        frozenArea = player.getLocation();
        player.setFlying(true);

        autounfreeze = new BukkitRunnable() {
            @Override
            public void run() {
                Util.action("Freedom", "Unfreezing " + player.getName(), false);
                setFrozen(false);
            }

        }.runTaskLater(Freedom.plugin, PURGETable);
    }
}

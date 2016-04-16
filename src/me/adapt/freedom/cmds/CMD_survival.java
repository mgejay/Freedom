package me.adapt.freedom.cmds;

import me.adapt.freedom.core.Freedom;
import static me.adapt.freedom.core.Util.message;
import net.pravian.aero.command.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_survival extends SimpleCommand<Freedom> {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player idk = (Player) sender;

        if (args.length == 0) {
            if (!idk.getGameMode().equals(GameMode.SURVIVAL)) {
                message(sender, "Gamemode set to survival");
                idk.setGameMode(GameMode.SURVIVAL);
                return true;
            }

            if (idk.getGameMode().equals(GameMode.SURVIVAL)) {
                message(sender, "You are already in SURVIVAL!");
                return true;
            }
            return true;
        }

        if (args.length > 0) {
            message(sender, "/survival");
            return true;
        }

        return true;
    }
}

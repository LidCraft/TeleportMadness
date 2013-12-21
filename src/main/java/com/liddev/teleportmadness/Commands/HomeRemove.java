package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.Managers.Data;
import com.liddev.teleportmadness.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeRemove implements MadCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        String player;
        String home;
        PlayerData data;
        if (args.length == 2) {
            player = args[0];
            home = args[1];
            data = Data.get().getPlayerData(((Player) sender).getName());
            if (data == null) {
                sender.sendMessage(player + " is not online, attempting to look up in database.");
                data = Data.get().getPlayer(player);
            }
            if (data == null) {
                sender.sendMessage(player + " was not found in the database, name must be exact for database lookup to succeed.");
                return false;
            }
        } else {
            home = args[0];
            data = Data.get().getPlayerData(((Player) sender).getName());
        }
        if (!data.removeHome(args[1])) {
            sender.sendMessage("Home, " + home + " was not found.");
            return false;
        }
        data.removeHome(home);
        return true;
    }

}
//TODO: continue from here.

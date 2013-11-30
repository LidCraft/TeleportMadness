package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.JumpPoint;
import com.liddev.teleportmadness.LocationHandler;
import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.Managers.Data;
import com.liddev.teleportmadness.PlayerData;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class Home implements MadCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        Location l = p.getLocation(); //TODO: test if player can teleport from here.
        PlayerData data = Data.get().getPlayerData(p);
        JumpPoint jump = null;
        if (args.length == 0) {
            jump = data.getDefaultHome();
        } else {
            jump = data.getHome(args[0]);
        }
        return LocationHandler.teleport(p, jump);
    }
}

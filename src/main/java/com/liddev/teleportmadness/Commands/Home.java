package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.JumpPoint;
import com.liddev.teleportmadness.LocationHandler;
import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.DataManager;
import com.liddev.teleportmadness.PlayerData;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class Home extends MadCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        Location l = p.getLocation(); //TODO: test if player can teleport from here.
        PlayerData data = DataManager.get().getPlayerData(p);
        JumpPoint jump = null;
        if (args.length == 0) {
            jump = data.getDefaultHome();
        } else {
            jump = data.getHome(args[0]);
        }
        if (DataManager.get().getClaimData(jump.getLocation()).hasPermission(p)) {
        return LocationHandler.teleport(p, jump);
        }
        return false;
    }
}

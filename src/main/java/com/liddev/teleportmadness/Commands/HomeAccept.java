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
public class HomeAccept implements MadCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        Location l = p.getLocation();  //TODO: check if player can teleport from here.
        PlayerData data = Data.get().getPlayerData(p);
        JumpPoint jump = null;
        if (args.length == 0) {
            jump = data.getInvite().getJumpPoint();
            if (jump == null) {
                p.sendMessage("Unable to teleport: You have no active invitations.");
                return false;
            }
            data.removeInvite();
        } else {
            data.getInvite(args[0]).getJumpPoint();
            if (jump == null) {
                p.sendMessage("Unable to teleport:" + args[0] + "has not invited you recently or does not exist.  Make sure you typed the name correctly and check if " + args[0] + " has invited you with /home invite.");
                return false;
            }
            data.removeInvite(args[0]);
        }
        return LocationHandler.teleport(p, jump);
    }

}

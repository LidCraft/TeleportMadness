package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.Invite;
import com.liddev.teleportmadness.JumpPoint;
import com.liddev.teleportmadness.LocationHandler;
import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.Managers.Data;
import com.liddev.teleportmadness.PlayerData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeInvite implements MadCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerData invitee = Data.get().getPlayerData(args[0]);
        PlayerData inviter = Data.get().getPlayerData(player);
        JumpPoint jump = null;
        if (args.length == 1) {
            jump = LocationHandler.findNearestHome(player);
        }else{
            jump = inviter.getHome(args[1]);
        }
        if(jump != null){
            Invite invite = new Invite(invitee, inviter, jump);
            invitee.addInvite(invite); //TODO: tell invitee about invite.
            return true;
        }
        return false;
    }
}

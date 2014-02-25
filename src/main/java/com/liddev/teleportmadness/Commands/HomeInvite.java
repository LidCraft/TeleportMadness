package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.Invite;
import com.liddev.teleportmadness.JumpPoint;
import com.liddev.teleportmadness.LocationHandler;
import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.PlayerData;
import com.liddev.teleportmadness.TeleportMadness;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeInvite extends MadCommand{

    public HomeInvite(ConfigurationSection config) {
        super(config);
    }

    @Override
    public boolean run(CommandSender sender, String alias, String[] args) {
        Player player = (Player) sender;
        PlayerData invitee = TeleportMadness.getDataManager().getPlayerData(args[0]);
        PlayerData inviter = TeleportMadness.getDataManager().getPlayerData(player);
        JumpPoint jump = null;
        if (args.length == 1) {
            jump = LocationHandler.findNearestHome(player);
            if (jump == null) {
                sender.sendMessage("Unable to send invite.  You have no homes in this world.");
            }
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

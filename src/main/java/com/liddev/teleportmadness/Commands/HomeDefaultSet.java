package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.JumpPoint;
import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.PlayerData;
import com.liddev.teleportmadness.TeleportMadness;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeDefaultSet extends MadCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerData data = TeleportMadness.getDataManager().getPlayerData(player);
        JumpPoint home = data.getHome(args[0]);
        if(home.equals(data.getDefaultHome())){
            player.sendMessage("Home is already set to" + args[0]);
        }else if(home == null){
            player.sendMessage("You do not have a home called " + args[0]);
        }else{
            data.setDefaultHome(home);
        }
        return true;
    }

}

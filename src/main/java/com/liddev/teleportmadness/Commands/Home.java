package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.Commands.MadCommand;
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
        Player p = (Player)sender;
        Location l = p.getLocation();
        if(args.length == 0){
            
        }
        return false;
    }
    
}

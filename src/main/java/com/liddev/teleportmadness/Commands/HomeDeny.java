package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.MadCommand;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeDeny implements MadCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        sender.sendMessage("This is not the command you are looking for.");  //TODO: decide what to do with home deny command;
        return true;
    }
    
}

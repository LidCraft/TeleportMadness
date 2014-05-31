package com.liddev.mad.teleport.home.player.commands;

import com.liddev.mad.core.MadCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeDeny extends MadCommand {

    public HomeDeny(ConfigurationSection config) {
        super(config);
    }

    @Override
    public boolean run(CommandSender sender, String alias, String[] args) {
        sender.sendMessage("This is not the command you are looking for.");  //TODO: decide what to do with home deny command;
        return true;
    }
    
}

package com.liddev.mad.teleport.home.server.commands;

import com.liddev.mad.core.MadCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Renlar <liddev.com>
 */
public class Limit extends MadCommand {

    public Limit(ConfigurationSection config) {
        super(config);
    }

    @Override
    public boolean run(CommandSender sender, String alias, String[] args) {
        return false;
    }
    
}

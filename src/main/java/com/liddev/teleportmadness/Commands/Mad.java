package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.MadCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Renlar <liddev.com>
 */
public class Mad extends MadCommand{

    public Mad(ConfigurationSection config) {
        super(config);
    }

    @Override
    public boolean run(CommandSender sender, String alias, String[] args) {
        return true;
    }
    
}

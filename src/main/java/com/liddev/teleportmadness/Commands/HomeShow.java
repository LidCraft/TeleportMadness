package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.MadCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeShow extends MadCommand {

    public HomeShow(ConfigurationSection config) {
        super(config);
    }

    @Override
    public boolean run(CommandSender sender, String alias, String[] args) {
        //TODO: implement home show.
        return false;
    }
    
}

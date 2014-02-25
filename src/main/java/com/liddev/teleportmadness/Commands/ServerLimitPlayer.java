package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.MadCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Renlar <liddev.com>
 */
public class ServerLimitPlayer extends MadCommand {

    public ServerLimitPlayer(ConfigurationSection config) {
        super(config);
    }

    @Override
    public boolean run(CommandSender sender, String alias, String[] args) {
        return false;
    }
    
}

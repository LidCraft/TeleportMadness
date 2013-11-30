package com.liddev.teleportmadness;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public interface MadCommand {
    
    public abstract boolean run(CommandSender sender, String[] args);
    
}

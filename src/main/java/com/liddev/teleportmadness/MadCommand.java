package com.liddev.teleportmadness;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

/**
 *
 * @author Renlar <liddev.com>
 */
public abstract class MadCommand {

    public abstract boolean run(CommandSender sender, String[] args);
}

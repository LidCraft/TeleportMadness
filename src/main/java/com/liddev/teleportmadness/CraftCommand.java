package com.liddev.teleportmadness;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

/**
 *
 * @author Renlar <liddev.com>
 */
public class CraftCommand extends PluginCommand {

    private CommandExecutor exe = null;

    protected CraftCommand(String name) {
        super(name, TeleportMadness.get());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (exe != null) {
            exe.onCommand(sender, this, commandLabel, args);
        }
        return false;
    }

    public void setExecutor(CommandExecutor exe) {
        this.exe = exe;
    }
}

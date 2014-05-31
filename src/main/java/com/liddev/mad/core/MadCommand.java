package com.liddev.mad.core;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Renlar <liddev.com>
 */

// TODO: deal with all parent fields as most of them are not currently set.
// This means some features of other plugins and bukkit will not work when accessing this plugin.
// Investigate org.bukkit.command.PluginCommand

//TODO: associate with a command map?  similar to how pluginCommands are handled.  
//make this a wrapper class aroud org.bukkit.command.pluginCommand or eliminate altogehter?
public class MadCommand extends Command{

    private boolean console = false;
    private CommandPattern pattern;
    private CommandExecutor exe;
    
    public MadCommand(ConfigurationSection config){
        super(config.getString("name"), config.getString("desc"), config.getString("use"), config.getStringList("alias"));
        console = config.getBoolean("console");
        super.setPermission(config.getString("permission"));
    }
    
    public MadCommand(String name, String description, String usageMessage, List<String> aliases){
        super(name, description, usageMessage, aliases);
    }
    
    @Override
    public boolean execute(CommandSender sender, String alias, String[] args){
        return exe.onCommand(sender, this, alias, args);
    }
    
    public boolean run(CommandSender sender, String alias, String[] args){
        sender.sendMessage(this.getUsage()); //TODO: determin what to do for filler commands.  Print a list of their sub commands?
        return true;
    }
    
    public boolean isConsole(){
        return console;
    }
    
    public void setConsole(boolean console){
        this.console = console;
    }
    
    public void setPattern(CommandPattern pattern){
        this.pattern = pattern;
    }
    
    public CommandPattern pattern(){
        return pattern;
    }
    
    public void setExecutor(CommandExecutor exe){
        this.exe = exe;
    }
    
    public boolean hasPermission(CommandSender sender) {
        return this.getPermission() == null || this.getPermission().isEmpty() || sender.hasPermission(this.getPermission());
    }

    public boolean validateArguemnts(String[] args) {
        return pattern.matches(args);
    }
}

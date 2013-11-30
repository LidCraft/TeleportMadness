package com.liddev.teleportmadness;

import java.util.List;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Renlar <liddev.com>
 */
public interface CommandEnum {

    public boolean isValid(String[] command);

    public String getDesc();

    public String getHelp();
    
    public MadCommand get();
    
    public List<String> getAliases();
}

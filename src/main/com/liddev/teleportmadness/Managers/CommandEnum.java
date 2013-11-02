/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liddev.teleportmadness.Managers;

import com.liddev.teleportmadness.Commands.MadCommand;
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

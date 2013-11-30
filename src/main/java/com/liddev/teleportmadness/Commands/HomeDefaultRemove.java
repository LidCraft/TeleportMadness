package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.Managers.Data;
import com.liddev.teleportmadness.PlayerData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeDefaultRemove implements MadCommand{

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerData data = Data.get().getPlayerData(player);
        data.setDefaultHome(null);
        return true;
    }
    
}

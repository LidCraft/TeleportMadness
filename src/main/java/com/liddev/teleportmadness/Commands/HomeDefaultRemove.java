package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.DataManager;
import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.PlayerData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeDefaultRemove extends MadCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerData data = DataManager.get().getPlayerData(player);
        data.setDefaultHome(null);
        return true;
    }
    
}

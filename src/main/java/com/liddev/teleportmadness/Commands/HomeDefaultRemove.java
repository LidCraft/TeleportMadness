package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.PlayerData;
import com.liddev.teleportmadness.TeleportMadness;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeDefaultRemove extends MadCommand {

    public HomeDefaultRemove(ConfigurationSection config) {
        super(config);
    }

    @Override
    public boolean run(CommandSender sender, String alias, String[] args) {
        Player player = (Player) sender;
        PlayerData data = TeleportMadness.getDataManager().getPlayerData(player);
        data.setDefaultHome(null);
        return true;
    }

}

package com.liddev.mad.teleport.home.player.commands;

import com.liddev.mad.core.MadCommand;
import com.liddev.mad.teleport.PlayerData;
import com.liddev.mad.teleport.TeleportMadness;
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

package com.liddev.mad.teleport.home.player.commands;

import com.liddev.mad.teleport.ClaimData;
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
public class HomeUntrust extends MadCommand {

    public HomeUntrust(ConfigurationSection config) {
        super(config);
    }

    @Override
    public boolean run(CommandSender sender, String alias, String[] args) {
        Player p = (Player) sender;
        PlayerData pData = TeleportMadness.getDataManager().getPlayer(args[0]);
        if (pData == null) {
            return false;
        }
        ClaimData data = TeleportMadness.getDataManager().loadClaimData(p.getLocation());
        if (data.isClaimManager(pData.getName())) {
            data.getPermissionGroup().blacklist(pData.getPlayerUUID());
            return true;
        }
        return false;
    }
}

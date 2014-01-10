package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.ClaimData;
import com.liddev.teleportmadness.DataManager;
import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.PlayerData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeUntrust extends MadCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        PlayerData pData = DataManager.get().getPlayer(args[0]);
        if (pData == null) {
            return false;
        }
        ClaimData data = DataManager.get().loadClaimData(p.getLocation());
        if (data.isClaimManager(pData.getName())) {
            data.getPermissionGroup().blacklist(pData.getPlayerUUID());
            return true;
        }
        return false;
    }
}

package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.ClaimData;
import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.PlayerData;
import com.liddev.teleportmadness.TeleportMadness;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeTrust extends MadCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        PlayerData pData = TeleportMadness.getDataManager().getPlayer(args[0]);
        if (pData == null) {
            return false;
        }
        ClaimData data = TeleportMadness.getDataManager().loadClaimData(p.getLocation());
        if (data.isClaimManager(pData.getName())) {
            data.getPermissionGroup().whitelist(pData.getPlayerUUID());
            return true;
        }
        return false;
    }

}

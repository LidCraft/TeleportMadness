package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.ClaimData;
import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.Managers.Data;
import com.liddev.teleportmadness.PlayerData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeUntrust implements MadCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        PlayerData pData = Data.get().getPlayer(args[0]);
        if (pData == null) {
            return false;
        }
        ClaimData data = Data.get().loadClaimData(p.getLocation());
        if (data.isClaimManager(pData.getName())) {
            data.getPermissionGroup().blacklist(pData.getPlayerUUID());
            return true;
        }
        return false;
    }
}

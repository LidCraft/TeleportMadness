package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.ClaimData;
import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.Managers.Data;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.liddev.teleportmadness.PermissionLevel;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeAllow implements MadCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ClaimData cd = Data.get().getClaimData(player.getLocation());
        if (Data.get().getClaim(cd.getId()).getOwnerName().equals(player.getName()) || player.hasPermission("teleportMadness.home.claim.allow.admin")) {
            PermissionLevel p = PermissionLevel.getPermission(args[0]);
            cd.setPermissionLevel(p);
            player.sendMessage("Permission level successfully set to " + p.name());
            return true;
        }
        player.sendMessage("You do not have permission to use that command in this claim.");
        return false;
    }

}

package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.ClaimData;
import com.liddev.teleportmadness.DataManager;
import com.liddev.teleportmadness.MadCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.liddev.teleportmadness.PermissionLevel;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeAllow extends MadCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ClaimData cd = DataManager.get().getClaimData(player.getLocation());
        //TODO: replace all getOwnerName() api calls to griefprevention with equivalent uuid calls.
        if (DataManager.get().getClaim(cd.getId()).getOwnerName().equals(player.getName()) || player.hasPermission("teleportMadness.home.claim.allow.admin")) {
            PermissionLevel p = PermissionLevel.getPermission(args[0]);
            cd.setPermissionLevel(p);
            player.sendMessage("Permission level successfully set to " + p.name());
            return true;
        }
        player.sendMessage("You do not have permission to use that command in this claim.");
        return false;
    }
}

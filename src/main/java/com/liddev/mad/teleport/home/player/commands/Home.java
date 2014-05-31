package com.liddev.mad.teleport.home.player.commands;

import com.liddev.mad.teleport.JumpPoint;
import com.liddev.mad.teleport.LocationHandler;
import com.liddev.mad.core.MadCommand;
import com.liddev.mad.teleport.PlayerData;
import com.liddev.mad.teleport.TeleportMadness;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class Home extends MadCommand {

    public Home(ConfigurationSection config) {
        super(config);
    }

    @Override
    public boolean run(CommandSender sender, String alias, String[] args) {
        Player p = (Player) sender;
        Location l = p.getLocation(); //TODO: test if player can teleport from here.
        PlayerData data = TeleportMadness.getDataManager().getPlayerData(p);
        JumpPoint jump = null;
        if (args.length == 0) {
            jump = data.getDefaultHome();
        } else {
            jump = data.getHome(args[0]);
        }
        if (TeleportMadness.getDataManager().getClaimData(jump.getLocation()).hasPermission(p)) {
        return LocationHandler.teleport(p, jump);
        }
        return false;
    }
}

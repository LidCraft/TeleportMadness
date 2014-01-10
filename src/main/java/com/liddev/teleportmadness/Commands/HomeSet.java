/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.JumpPoint;
import com.liddev.teleportmadness.JumpType;
import com.liddev.teleportmadness.DataManager;
import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeSet extends MadCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        if (args.length > 1) {
            p = Bukkit.getServer().getPlayer(args[0]);
            if (p == null) {
                sender.sendMessage("Player, " + args[0] + " was not found.");
                return false;
            }
        }
        JumpPoint j;
        PlayerData data = DataManager.get().getPlayerData(p);
        if (args.length == 1) {
            if (data.getHomeLimit() <= data.getHomeCount() || data.getWorldLimit(p.getLocation().getWorld()) <= data.getWorldCount(p.getLocation().getWorld())) {
                StringBuilder b = new StringBuilder();
                if (data.getHomeLimit() <= data.getHomeCount()) {
                    b.append("You have reached or exceeded your server home limit of ").append(data.getHomeLimit()).append(".  ");
                }
                if (data.getWorldLimit(p.getLocation().getWorld()) <= data.getWorldCount(p.getLocation().getWorld())) {
                    if (data.getWorldLimit(p.getLocation().getWorld()) <= 0) {
                        b.append("You are not allowed to set homes in this world.");
                    } else {
                        b.append("You have reached or exceeded your world home limit of ").append(data.getWorldLimit(p.getWorld())).append(".  ");
                    }
                    b.append("To add a new home, first delete an existing one with /home remove <homename>.");
                    p.sendMessage(b.toString());
                    return false;
                }
            }
            if (!DataManager.get().loadClaimData(p.getLocation()).hasPermission(p)) {
                sender.sendMessage("You do not have permission to set a home in this claim.");
                return false;
            }
            j = new JumpPoint(p.getLocation(), args[0]);
            if (data.hasHome(j.getName())) {
                sender.sendMessage("You already have a home with that name.");
                return false;
            }
        } else if (args.length == 2) {
            j = new JumpPoint(p.getLocation(), args[1]);
        } else {
            JumpType jt = JumpType.match(args[1]);
            if (jt == null || !jt.equals(JumpType.GIFT) || !jt.equals(JumpType.PERSONAL)) {
                sender.sendMessage("Invalid home type, " + args[1] + ", must be GIFT or PERSONAL. (default GIFT)");
                return false;
            }
            j = new JumpPoint(p.getLocation(), args[0], jt);
        }
        if (data.hasHome(j.getName())) {
            sender.sendMessage(p.getName() + ", already has a home with that name.");
            return false;
        }
        data.addHome(j);
        return true;
    }

}

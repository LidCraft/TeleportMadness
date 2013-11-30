package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.JumpPoint;
import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.Managers.Data;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeList implements MadCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        StringBuilder builder = new StringBuilder();
        if (sender.hasPermission("teleportMadness.home.server.list")) {
            builder.append("ServerHome: ").append(Data.get().getServerHome().toString()).append("\n");
        }
        if (sender.hasPermission("teleportMadness.home.world.list")) {
            builder.append("WorldHomes: \n");
            for (World world : Bukkit.getServer().getWorlds()) {
                builder.append(world.getName()).append(": ").append(Data.get().getWorldData(world).getHome().toString()).append("\n");
            }
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            for (JumpPoint j : Data.get().getPlayerData(player).getHomes()) {

            }
        }
        sender.sendMessage(builder.toString());
        return true;
    }

}

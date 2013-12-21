package com.liddev.teleportmadness.Commands;

import com.liddev.teleportmadness.JumpPoint;
import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.Managers.Data;
import java.util.List;
import java.util.UUID;
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
        Player player;
        StringBuilder builder = new StringBuilder();
        if (args.length == 0) {
            if (sender.hasPermission("teleportMadness.home.server.list")) {
                builder.append("Server Home: ").append(Data.get().getServerHome().toString()).append("\n");
            }
            if (sender.hasPermission("teleportMadness.home.world.list")) {
                builder.append("World Homes: \n");
                for (World world : Bukkit.getServer().getWorlds()) {
                    if (Data.get().getWorldData(world).getHome() != null) {
                        builder.append(world.getName()).append(": ").append(Data.get().getWorldData(world).getHome().toString()).append("\n");
                    }
                }
            }
            if (sender instanceof Player) {
                player = (Player) sender;
                addHomeList(player, builder);
            }
        } else if (args.length == 1) {
            player = Bukkit.getServer().getPlayer(args[0]);
            addHomeList(player, builder);
        }
        sender.sendMessage(builder.toString());
        return true;
    }

    private void addHomeList(Player player, StringBuilder builder) {
        List<World> list = Bukkit.getServer().getWorlds();
        builder.append("Your Homes: \n");
        for (World w : list) {
            UUID worldUUID = w.getUID();
            builder.append(w.getName()).append(": ");
            for (JumpPoint j : Data.get().getPlayerData(player).getHomes()) {
                if (j.getWorldUUID().equals(worldUUID)) {
                    builder.append(j.toString()).append(", ");
                }
            }
            builder.append("\n");
        }
    }
}

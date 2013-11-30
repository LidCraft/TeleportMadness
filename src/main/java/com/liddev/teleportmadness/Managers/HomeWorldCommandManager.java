package com.liddev.teleportmadness.Managers;

import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.CommandEnum;
import com.liddev.teleportmadness.Commands.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public enum HomeWorldCommandManager implements CommandEnum, MadCommand {

    HOMEWORLDDEFAULT(WorldHome.class, false, "teleportMadness.home.world", 0,0),
    HOMEWORLDSET(WorldSet.class, false, "teleportMadness.home.world.set", 1, 1, "+", "add", "set"),
    HOMEWORLDREMOVE(WorldRemove.class, false, "teleportMadness.home.world.remove", 1, 1, "rm", "-", "remove"),
    HOMEWORLDALLOW(WorldAllow.class, false, "teleportMadness.home.world.allow", 1, 1, "al", "allow"),
    HOMEWORLDDENY(WorldDeny.class, false, "teleportMadness.home.world.deny", 1, 1, "dn","deny"),
    HOMEWORLDTRUSTED(WorldTrusted.class, false, "teleportMadness.home.world.trusted", 0, 0, "t", "trusted"),
    HOMEWORLDTRUST(WorldTrust.class, false, "teleportMadness.home.world.trust", 1, 1, "tr", "trust"),
    HOEWORLDUNTRUST(WorldUntrust.class, false, "teleportMadness.home.world.untrust", 1, 1, "ut", "untrust"),
    HOMEWORLDLIMIT(WorldLimit.class, false, "teleportMadness.home.world.limit", 0, 0, "limit"),
    HOMEWORLDLIMITOTHER(WorldLimit.class, true, "teleportMadness.home.world.limit.other", 1, 1, "limit"),
    HOMEWORLDLIMITSET(WorldLimit.class, true, "teleportMadness.home.world.limit.set", 1, 2, "limit set"),
    HOMEWORLDLIMITPLAYER(WorldLimitPlayer.class, true, "teleportMadness.home.world.limit.player", 1, 2,"limit player"),
    HOMEWORLDLIMITSETPLAYER(WorldLimitPlayer.class, true, "teleportMadness.home.world.limit.player.set", 2, 3, "limit player set"),
    HOMEWORLDSHOW(WorldShow.class, false, "teleportMadness.home.world.show", 0, 0, "show", "view");
    
    
    private MadCommand command;
    private final int min, max;
    private final String permission;
    private final boolean console;
    private final ArrayList<String> aliases;

    HomeWorldCommandManager(Class<?> cClass, boolean console, String permission, int minArgs, int maxArgs, String... aliases) {
        try {
            this.command = (MadCommand) cClass.newInstance();
        } catch (InstantiationException e) {
            Bukkit.getServer().getLogger().log(Level.SEVERE, "InstantiationException: Error instancing command!");
        } catch (IllegalAccessException e) {
            Bukkit.getServer().getLogger().log(Level.SEVERE, "IllegalAccessException: Error instancing command!");
        }

        this.console = console;
        this.permission = permission;
        this.min = minArgs;
        this.max = maxArgs;
        this.aliases = new ArrayList<String>();
        this.aliases.addAll(Arrays.asList(aliases));

    }

    @Override
    public boolean run(CommandSender sender, String[] commands) {
        if (sender != null) {
            if (sender instanceof Player) {
                Bukkit.getServer().getLogger().log(Level.FINEST,
                        "{0} issued command: {1}", new Object[]{sender.getName(),
                            this.get().getClass()});
            } else if (sender instanceof ConsoleCommandSender) {
                Bukkit.getServer().getLogger().finest("Console Issued Command.");
            } else {
                Bukkit.getServer().getLogger().finest("Madness is here the sender "
                        + "could not be found");
            }
        } else {
            sender = Bukkit.getConsoleSender();
        }
        if (permission != null && (sender instanceof Player) && !sender.hasPermission(permission)) {
            Bukkit.getServer().getLogger().log(Level.WARNING,
                    "{0} tried to run command {1}, but does not have permission, {3}",
                    new Object[]{sender.getName(), command.getClass(), permission});
            noPermission(sender);
            return false;
        }
        try {
            return command.run(sender, commands);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isValid(String[] command) {
        String testString = "";
        boolean valid = false;
        if (this.command instanceof CommandEnum) {
            String[] args = new String[command.length - 1];
            for (int i = 1; i < command.length; i++) {
                args[i - 1] = command[i];
            }
            ((CommandEnum) this.command).isValid(command);
        }
        if (aliases.contains(testString)) {
            valid = true;
        }
        for (String s : command) {
            testString += s;
            for (String n : aliases) {
                if (testString.equalsIgnoreCase(n)) {
                    valid = true;
                }
            }
        }
        return valid;
    }

    @Override
    public String getDesc() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getHelp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MadCommand get() {
        return command;
    }

    @Override
    public List<String> getAliases() {
        return (List<String>) aliases.clone();
    }

    //TODO: load messages from config file.
    public void noPermission(CommandSender sender) {
        sender.sendMessage("Monkies with Banannas! You don't have a tail to do that.");
    }

    public void wrongLength(CommandSender sender) {
        sender.sendMessage("You have the wrong number of rocks for that potato.");
    }

    public void notPlayer(CommandSender sender) {
        sender.sendMessage("Oh great overlord, you can not access that command.");
    }

}

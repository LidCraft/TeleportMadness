package com.liddev.teleportmadness.Managers;

import com.liddev.teleportmadness.MadCommand;
import com.liddev.teleportmadness.CommandEnum;
import com.liddev.teleportmadness.Commands.*;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public enum HomeCommandManager implements CommandEnum, MadCommand {

    // name may be more than one word, class, console, min args, max args
    HOMESET(HomeSet.class, false, "teleportMadness.home.set", 1, 1, "set", "add", "+"),
    HOMESETOTHER(HomeSet.class, false, "teleportMadness.home.set.other", 2, 2, "set", "add", "+"),
    HOMEREMOVE(HomeRemove.class, false, "teleportMadness.home.set", 1, 1, "remove", "rm", "-"),
    HOMEREMOVEOTHER(HomeRemove.class, true, "teleportMadness.home.set", 2, 2, "remove", "rm", "-"),
    HOMEDEFAULTSET(HomeDefaultSet.class, false, "teleportMadness.home.default.set", 1, 1, "default set", "d s"),
    HOMEDEFAULTREMOVE(HomeDefaultRemove.class, false, "teleportMadness.home.default.remove", 0, 0, "default remove", "d r"),
    HOMELIST(HomeList.class, false, "teleportMadness.home.set", 0, 0, "list", "l", "ls"),
    HOMELISTOTHER(HomeList.class, true, "teleportMadness.home.set", 1, 1, "list", "l", "ls"),
    HOMEINVITE(HomeInvite.class, false, "teleportMadness.home.invite", 1, 2, "invite", "i", "inv"),
    HOMEACCEPT(HomeAccept.class, false, "teleportMadness.home.accept", 1, 1, "accept", "a", "acp"),
    HOMESHOW(HomeShow.class, false, "teleportMadness.home.show", 0, 0, "show", "view"),
    HOMESHOWOTHER(HomeShow.class, false, "teleportMadness.home.show.other", 1, 1, "show", "view"),
    //TODO: deal with bypass permissions.

    //TODO: deal with admin: trust, untrust, allow, deny, and trusted permissions.
    HOMETRUST(HomeTrust.class, false, "teleportMadness.home.claim.trust", 1, 1, "trust", "tr"),
    HOMEUNTRUST(HomeUntrust.class, false, "teleportMadness.home.claim.untrust", 1, 1, "untrust", "ut"),
    HOMEALLOW(HomeAllow.class, false, "teleportMadness.home.claim.allow", 1, 1, "allow", "al"),
    HOMEDENY(HomeDeny.class, false, "teleportMadness.home.claim.deny", 1, 1, "deny", "dn"),
    HOMETRUSTED(HomeTrusted.class, false, "teleportMadness.home.claim.deny", 0, 0, "trusted", "td"),
    HOMESERVER(HomeServerCommandManager.class, true, null, 0, -1, "server", "s"),
    HOMEWORlD(HomeWorldCommandManager.class, true, null, 0, -1, "world", "w"),
    HOME(Home.class, false, "teleportMadness.home", 0, 1),
    HOMEOTHER(Home.class, false, "teleportMadness.home.other", 2, 2),
    //HOMEGROUPLIMIT(),
    //HOMEWORLDINFO(),
    //HOMEINFOPLAYER(),
    //HOMEINFO()
    ;

    private MadCommand command;
    private final int min, max;
    private final String permission;
    private final boolean console;
    private final ArrayList<String> aliases;

    HomeCommandManager(Class<?> cClass, boolean console, String permission, int minArgs, int maxArgs, String... aliases) {
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

    public static String[] reduceArgs(String[] args) {
        String[] newArgs = new String[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            newArgs[i - 1] = args[i];
        }
        return newArgs;
    }
}

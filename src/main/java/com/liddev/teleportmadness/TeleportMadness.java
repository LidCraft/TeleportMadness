package com.liddev.teleportmadness;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Renlar < liddev.com >
 */
public class TeleportMadness extends JavaPlugin {

    private PlayerListener playerListener;
    private ClaimListener claimListener;
    private WorldListener worldListener;
    private DataManager dataManager;
    private FileManager fileManager;
    private CommandManager commandManager;
    private static TeleportMadness active;
    private static SimpleCommandMap cmap;

    public PluginDescriptionFile dsc;

    @Override
    public void onEnable() {
        active = this;
        dsc = getDescription();
        getLogger().log(Level.INFO, "Loading {0}.", new Object[]{dsc.getFullName()});
        if (!checkDepend()) {
            return;
        }
        getCommand("");

        setupCMap();
        setupManagers();
        setupListeners();
//TODO: divide plugin into a module for each dependency where possible and only enable the portions which have their dependencies.

        getLogger().log(Level.INFO, "{0}: Load Complete.", new Object[]{dsc.getFullName()});
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}: Saving data!", new Object[]{dsc.getFullName()});
        dataManager.saveAll();

        clearMemory();
    }

    public boolean checkDepend() {
        List<String> depends = dsc.getDepend();
        ArrayList<String> disabled = new ArrayList<String>();
        for (String s : depends) {
            if (getServer().getPluginManager().getPlugin(s) == null) {
                disabled.add(s);
            }
        }
        if (!disabled.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("These dependencies are required and missing: ");
            for (String s : disabled) {
                builder.append(s).append(", ");
            }
            builder.append("\n Exiting plugin as can not continue.");
            getLogger().log(Level.SEVERE, builder.toString());
            return false;
        }
        return true;
    }

    public boolean isInGroup(Player p) {
        getDatabase().find(PlayerData.class).where().ieq("playerName", p.getName());
        return true;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public static TeleportMadness get() {
        return active;
    }

    public void register(List<PluginCommand> commands) {
        for (PluginCommand command : commands) {
            register(command);
        }
    }

    public void register(PluginCommand command) {
        cmap.register(dsc.getPrefix(), (Command) command);
    }

    private void clearMemory() {
        getLogger().log(Level.FINE, "{0}: Clearing Memory.", new Object[]{dsc.getFullName()});
        playerListener = null;
        dataManager.clearMemory();
        dataManager.closeDatabase();
        dataManager = null;
        claimListener = null;
        playerListener = null;
        worldListener = null;
        active = null;

        getLogger().log(Level.INFO, "{0}: Shutdown Complete.", new Object[]{dsc.getFullName()});
        dsc = null;
    }

    //Allows access to protected command hash in bukkit.  Uses refection similar to:
    //https://forums.bukkit.org/threads/register-command-without-plugin-yml.112932/#post-1430463
    //https://forums.bukkit.org/threads/dynamic-registration-of-commands.65786/#post-1033134
    private void setupCMap() {
        cmap = ((CraftServer) Bukkit.getServer()).getCommandMap();
    }

    private void setupManagers() {
        dataManager = new DataManager(this);
        dataManager.openDatabase();
        dataManager.loadData();

        fileManager = new FileManager(this);
        fileManager.loadCommands();
        //fileManager.loadConfig();

        commandManager = new CommandManager(this);
    }

    public void setupListeners() {
        claimListener = new ClaimListener(this);
        playerListener = new PlayerListener(this);
        worldListener = new WorldListener(this);

        getServer().getPluginManager().registerEvents(playerListener, this);
        getServer().getPluginManager().registerEvents(claimListener, this);
        getServer().getPluginManager().registerEvents(worldListener, this);
    }

    public PluginCommand createPluginCommand(String name) {
        Constructor<PluginCommand> cons;
        try {
            cons = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            cons.setAccessible(true);
            return cons.newInstance(name, this);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(TeleportMadness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(TeleportMadness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(TeleportMadness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TeleportMadness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TeleportMadness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(TeleportMadness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

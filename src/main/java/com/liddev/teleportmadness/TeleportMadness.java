package com.liddev.teleportmadness;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Renlar < liddev.com >
 */
public class TeleportMadness extends JavaPlugin {

    private static PlayerListener playerListener;
    private static ClaimListener claimListener;
    private static WorldListener worldListener;
    private static DataManager dataManager;
    private static FileManager fileManager;
    private static CommandManager commandManager;
    private static TeleportMadness instance;
    private static SimpleCommandMap cmap;

    private static PluginDescriptionFile dsc;

    @Override
    public void onEnable() {
        instance = this;
        dsc = getDescription();
        getLogger().log(Level.INFO, "Loading {0}.", new Object[]{dsc.getFullName()});
        if (!checkDepend()) {
            return;
        }
        getCommand("");

        setupManagers();
        setupListeners();
//TODO: divide plugin into a module for each dependency where possible and only enable the portions which have their dependencies.

        getLogger().log(Level.INFO, "{0}: Load Complete.", new Object[]{dsc.getName()});
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}: Saving data!", new Object[]{dsc.getName()});
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

    public static DataManager getDataManager() {
        return dataManager;
    }

    public static FileManager getFileManager() {
        return fileManager;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static TeleportMadness getInstance() {
        return instance;
    }

    public static PluginDescriptionFile getProp() {
        return dsc;
    }

    private void clearMemory() {
        getLogger().log(Level.FINE, "{0}: Clearing Memory.", new Object[]{dsc.getName()});
        playerListener = null;
        dataManager.clearMemory();
        dataManager.closeDatabase();
        dataManager = null;
        claimListener = null;
        playerListener = null;
        worldListener = null;
        instance = null;

        getLogger().log(Level.INFO, "{0}: Shutdown Complete.", new Object[]{dsc.getName()});
        dsc = null;
    }

    private void setupManagers() {
        dataManager = new DataManager(this);
        dataManager.openDatabase();
        dataManager.loadData();

        fileManager = new FileManager(this);
        fileManager.loadCommands();
        fileManager.loadConfig();

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
}

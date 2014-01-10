package com.liddev.teleportmadness;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


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

    public PluginDescriptionFile dsc;

    @Override
    public void onEnable() {
        active = this;
        dsc = getDescription();
        getLogger().log(Level.INFO, "Loading {0}.", new Object[]{dsc.getFullName()});
        if (!checkDepend()) {
            return;
        }

        dataManager = new DataManager(this);
        dataManager.openDatabase();
        dataManager.loadData();

        fileManager = new FileManager(this);
        fileManager.loadCommand();
        fileManager.loadConfig();

        commandManager = new CommandManager(this);

//TODO: divide plugin into a module for each dependency where possible and only enable the portions which have their dependencies.
        claimListener = new ClaimListener(this);
        playerListener = new PlayerListener(this);
        worldListener = new WorldListener(this);

        getServer().getPluginManager().registerEvents(playerListener, this);
        getServer().getPluginManager().registerEvents(claimListener, this);

        getLogger().log(Level.INFO, "{0}: Load Complete.", new Object[]{dsc.getFullName()});
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}: Saving data!", new Object[]{dsc.getFullName()});
        dataManager.saveAll();
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

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        return commandManager.run(cs, cmnd, args);
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
}

package com.liddev.teleportmadness;

import com.liddev.teleportmadness.Managers.Data;
import com.liddev.teleportmadness.Managers.HomeCommandManager;
import com.liddev.teleportmadness.Managers.MadCommandManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.PersistenceException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Renlar < liddev.com >
 */
public class TeleportMadness extends JavaPlugin {

    private PlayerListener homeListener;
    private HomeCommandManager m;
    private Data dataManager;

    public PluginDescriptionFile dsc;

    @Override
    public void onEnable() {
        dsc = getDescription();
        getLogger().log(Level.INFO, "Loading {0}.", new Object[]{dsc.getFullName()});
        if (!checkDepend()) {
            return;
        }

        setupDatabase();
        dataManager = new Data(this);
        dataManager.loadData();

        getServer().getPluginManager().registerEvents(homeListener, this);

        getLogger().log(Level.INFO, "{0}: Load Complete.", new Object[]{dsc.getFullName()});
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}: Shuting Down, Saving data!", new Object[]{dsc.getFullName()});
        dataManager.saveAll();
        getLogger().log(Level.FINE, "{0}: Data Save Complete, Clearing Memory.", new Object[]{dsc.getFullName()});
        homeListener = null;
        dataManager.clearMemory();
        dsc = null;
        getLogger().log(Level.INFO, "{0}: Shutdown Complete.", new Object[]{dsc.getFullName()});
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (cmnd.getName().equalsIgnoreCase("home")) {
            for (HomeCommandManager cm : HomeCommandManager.values()) {
                if (cm.isValid(args)) {
                    return cm.run(cs, args);
                }
            }
            return false;
        } else if (cmnd.getName().equalsIgnoreCase("mad")) {
            for (MadCommandManager cm : MadCommandManager.values()) {
                if (cm.isValid(args)) {
                    return cm.run(cs, args);
                }
            }
            return false;
        }
        return false;
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

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(Home.class);
        list.add(PlayerData.class);
        list.add(ClaimData.class);
        list.add(PermissionGroup.class);
        return list;
    }

    private boolean setupDatabase() {
        try {
            getDatabase().find(Home.class).findRowCount();
        } catch (PersistenceException ex) {
            System.out.println("Installing Database.");
            installDDL();
            return true;
        }
        return false;
    }

    public boolean isInGroup(Player p) {
        getDatabase().find(PlayerData.class).where().ieq("playerName", p.getName());
        return true;
    }
    
    public Data getDataManager(){
        return dataManager;
    }
}

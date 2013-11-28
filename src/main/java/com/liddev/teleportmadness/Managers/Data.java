package com.liddev.teleportmadness.Managers;

import com.liddev.teleportmadness.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

/**
 *
 * @author Renlar <liddev.com>
 */
//TODO: Consider adding sqlibrary to support other databases.
public class Data {

    private TeleportMadness mad;
    private HashMap<String, PlayerData> playerDataMap;
    private HashMap<String, WorldData> worldDataMap;
    private ArrayList<ClaimData> claimDataList;
    private HashMap<String, JumpPoint> serverHomeMap;
    private final String DB_NAME = "TeleportMadness.odb";
    private ODB db = null;

    public Data(TeleportMadness mad) {
        this.mad = mad;
    }

    public void openDatabase() {
        try {
            // Open the database
            db = ODBFactory.open(DB_NAME);
        } catch (Exception e) {

        }
    }

    public void closeDatabase() {
        if (db != null) {
            // Close the database
            db.close();
        }
    }

    public void loadData() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayer(player);
        }
        /*for (World world : Bukkit.getWorlds()) {
         loadWorld(world);
         }*/
    }

    public void savePlayer(PlayerData data) {
        //TODO: add loging and playerdata checks.
        db.store(data);
    }

    public void savePlayers(HashMap<String, PlayerData> players) {
        for (Player p : mad.getServer().getOnlinePlayers()) {
            savePlayer(players.get(p.getName()));
        }
    }

    public void loadPlayer(Player player) {
        if (playerDataMap.get(player.getName()) != null) {
            mad.getLogger().log(Level.FINEST, "Data for player {0} is alread loaded, reloading.", player);
            playerDataMap.remove(player.getName());
        } else {
            mad.getLogger().log(Level.FINEST, "Loading data for player {0}", player);
        }
        IQuery query = new CriteriaQuery(PlayerData.class, Where.equal("name", player.getName()));
        Objects<PlayerData> players = db.getObjects(query);
        PlayerData data = (PlayerData) players.getFirst();
        if (data == null) {
            data = new PlayerData();
            data.setPlayer(player);
        }
        db.store(data);
        playerDataMap.put(player.getName(), data);
    }

    public void unloadPlayer(Player player) {
        playerDataMap.remove(player.getName());
    }

    public void saveWorld(WorldData data) {
        db.store(data);
    }

    public void saveWorlds(HashMap<String, WorldData> worlds) {
        for (World w : mad.getServer().getWorlds()) {
            saveWorld(worlds.get(w.getName()));
        }
    }

    public void loadWorld(World world) {
        WorldData data = mad.getDatabase().find(WorldData.class).where().ieq("worldName", world.getName()).findUnique();
        if (data == null) {
            data = new WorldData();
            data.setName(world.getName());
        }
        worldDataMap.put(world.getName(), data);
    }

    public void unloadWorld(World world) {
        worldDataMap.remove(world.getName());
    }

    public void saveClaim(ClaimData data) {
        db.store(data);
    }

    public void saveClaims(ArrayList<ClaimData> claims) {
        for (ClaimData claim : claims) {
            saveClaim(claim);
        }
    }

    public ClaimData loadClaim(Location location) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
        ClaimData data = null;
        if (claim != null) {
            IQuery query = new CriteriaQuery(ClaimData.class, Where.equal("name", claim.getID()));
            Objects<ClaimData> claims = db.getObjects(query);
            data = claims.getFirst();
            if (data == null) {
                PermissionGroup group = new PermissionGroup();
                data = new ClaimData();
                data.setId(claim.getID());
                data.setPermissionLevel(ClaimData.defaultPermissionLevel);
                data.setWorld(claim.getGreaterBoundaryCorner().getWorld());
                data.setPermissionGroup(group);
                db.store(data);
            }
            claimDataList.add(data);
        }
        return data;
    }

    public HashMap<String, PlayerData> getPlayerDataMap() {
        return playerDataMap;
    }

    public PlayerData getPlayerData(String playerName) {
        return playerDataMap.get(playerName);
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.get(player.getName());
    }

    public HashMap<String, WorldData> getWorldDataMap() {
        return worldDataMap;
    }

    public WorldData getWorldData(String worldName) {
        return worldDataMap.get(worldName);
    }

    public WorldData getWorldData(World world) {
        return worldDataMap.get(world.getName());
    }

    public void saveAll() {
        savePlayers(playerDataMap);
        //saveWorlds(worldDataMap);
        saveClaims(claimDataList);
    }

    public void clearMemory() {
        mad = null;
        playerDataMap = null;
        worldDataMap = null;
        claimDataList = null;
        serverHomeMap = null;
    }

    public void createClaim(Claim claim) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteClaim(Claim claim) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void updateClaim(Claim newClaim) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

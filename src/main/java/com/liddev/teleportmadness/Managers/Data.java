package com.liddev.teleportmadness.Managers;

import com.liddev.teleportmadness.ClaimData;
import com.liddev.teleportmadness.Home;
import com.liddev.teleportmadness.PermissionGroup;
import com.liddev.teleportmadness.PlayerData;
import com.liddev.teleportmadness.TeleportMadness;
import com.liddev.teleportmadness.WorldData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.PersistenceException;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class Data {
    
    private TeleportMadness mad;
    private HashMap<String, PlayerData> playerDataMap;
    private HashMap<String, WorldData> worldDataMap;
    private ArrayList<ClaimData> claimDataList;
    private HashMap<ClaimData, ArrayList<Player>> claimDataPlayerMap;
    private HashMap<String, Home> serverHomeMap;
    
    public Data(TeleportMadness mad){
        this.mad = mad;
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
        mad.getDatabase().save(data);
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
        PlayerData data = mad.getDatabase().find(PlayerData.class).where().ieq("playerName", player.getName()).findUnique();
        if (data == null) {
            data = new PlayerData();
        }
        mad.getDatabase().save(data);
        playerDataMap.put(player.getName(), data);
        for (Home home : playerDataMap.get(player.getName()).getHomes()) {
            ClaimData key = loadClaim(home.getLocation());
            if (claimDataPlayerMap.containsKey(key)) {
                claimDataPlayerMap.get(key).add(player);
            } else {
                claimDataPlayerMap.put(key, new ArrayList<Player>());
            }
        }
    }

    public void unloadPlayer(Player player) {
        playerDataMap.remove(player.getName());
    }

    public void saveWorld(WorldData data) {
        mad.getDatabase().save(data);
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
        mad.getDatabase().save(data);
    }

    public void saveClaims(ArrayList<ClaimData> claims) {
        mad.getDatabase().save(claims);
    }

    public ClaimData loadClaim(Location location) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
        ClaimData data = null;
        if (claim != null) {
            data = mad.getDatabase().find(ClaimData.class).where().idEq(claim.getID().longValue()).findUnique();
            if (data == null) {
                PermissionGroup group = new PermissionGroup();
                data = new ClaimData();
                data.setId(claim.getID().longValue());
                data.setPermissionLevel(ClaimData.defaultPermissionLevel);
                data.setWorld(claim.getGreaterBoundaryCorner().getWorld());
                data.setPermissionGroup(group);
                mad.getDatabase().save(data);
            }
            claimDataList.add(data);
        }
        return data;
    }
    
    public HashMap<String, PlayerData> getPlayerDataMap(){
        return playerDataMap;
    }
    
    public PlayerData getPlayerData(String playerName){
        return playerDataMap.get(playerName);
    }
    
    public PlayerData getPlayerData(Player player){
        return playerDataMap.get(player.getName());
    }
    
    public HashMap<String, WorldData> getWorldDataMap(){
        return worldDataMap;
    }
    
    public WorldData getWorldData(String worldName){
        return worldDataMap.get(worldName);
    }
    
    public WorldData getWorldData(World world){
        return worldDataMap.get(world.getName());
    }

    public void cleanMemory() {
        for (ClaimData d : claimDataList) {
            if (claimDataPlayerMap.get(d).isEmpty()) {
                claimDataList.remove(d);
                claimDataPlayerMap.remove(d);
            }
        }
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
        claimDataPlayerMap = null;
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

package com.liddev.teleportmadness.Managers;

import com.liddev.teleportmadness.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private List<ClaimData> claimDataList;
    private JumpPoint serverHome;
    private final String DB_LOC;
    private ODB db = null;
    private static Data d;

    public Data(TeleportMadness mad) {
        this.mad = mad;
        d = this;
        DB_LOC = mad.getDataFolder().toString() + File.separator + "TeleportMadness.odb";
        playerDataMap = new HashMap<String, PlayerData>();
        worldDataMap = new HashMap<String, WorldData>();
        claimDataList = new ArrayList<ClaimData>();
    }

    public void openDatabase() {
        try {
            // Open the database
            db = ODBFactory.open(DB_LOC);
        } catch (Exception e) {
            mad.getLogger().log(Level.SEVERE, "{0} Error opening Database: {1}", new Object[]{mad.dsc.getFullName(), e});
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
        loadServerJumps();
        for(World world : Bukkit.getWorlds()){
            loadWorld(world);
        }
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
        IQuery query = new CriteriaQuery(WorldData.class, Where.equal("name", world.getName()));
        Objects<WorldData> worlds = db.getObjects(query);
        WorldData data = worlds.getFirst();
        if (data == null) {
            data = new WorldData();
            data.setName(world.getName());
        }
        worldDataMap.put(world.getName(), data);
    }

    public void unloadWorld(World world) {
        worldDataMap.remove(world.getName());
    }

    public void loadServerJumps() {
        IQuery query = new CriteriaQuery(JumpPoint.class, Where.equal("type", JumpType.GLOBAL));
        Objects<JumpPoint> points = db.getObjects(query);
        serverHome = points.getFirst();
    }
    
    public JumpPoint getServerHome(){
        return serverHome;
    }
    
    public void setServerHome(JumpPoint home){
        serverHome = home;
    }

    public void saveClaim(ClaimData data) {
        db.store(data);
    }

    public void saveClaims(List<ClaimData> claims) {
        for (ClaimData claim : claims) {
            saveClaim(claim);
        }
    }

    public ClaimData loadClaim(Location location) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
        if (claim != null) {
            return loadClaimData(claim);
        }
        return null;
    }

    public ClaimData loadClaimData(Claim claim) {
        for(ClaimData cd : claimDataList){
            if(cd.getId() == claim.getID()){
                return cd;
            }
        }
        ClaimData data = null;
        IQuery query = new CriteriaQuery(ClaimData.class, Where.equal("id", claim.getID()));
        Objects<ClaimData> claims = db.getObjects(query);
        data = claims.getFirst();
        if (data == null) {
            PermissionGroup group = new PermissionGroup();
            data = new ClaimData();
            data.setId(claim.getID());
            data.setPermissionLevel(PermissionLevel.defaultLevel);
            data.setWorld(claim.getGreaterBoundaryCorner().getWorld());
            data.setPermissionGroup(group);
            db.store(data);
        }
        claimDataList.add(data);
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
    
    public ClaimData getClaimData(long id){
        Claim claim = getClaim(id);
        for(ClaimData data : claimDataList){
            if(data.getId() == claim.getID()){
                return data;
            }
        }
        return null;
    }
    
    public ClaimData getClaimData(Location l){
        Claim claim = getClaim(l);
        for(ClaimData data : claimDataList){
            if(data.getId() == claim.getID()){
                return data;
            }
        }
        return null;
    }
    
    public Claim getClaim(Location l){
        return GriefPrevention.instance.dataStore.getClaimAt(l, true, null);
    }
    
    public Claim getClaim(long id){
        return GriefPrevention.instance.dataStore.getClaim(id);
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
        saveWorlds(worldDataMap);
        saveClaims(claimDataList);
    }

    public void clearMemory() {
        mad = null;
        playerDataMap = null;
        worldDataMap = null;
        claimDataList = null;
        serverHome = null;
    }

    public void createClaim(Claim claim) {
        for (Player p : mad.getServer().getOnlinePlayers()) {
            for (JumpPoint j : playerDataMap.get(p.getName()).getHomes()) {
                if (claim.contains(j.getLocation(), true, true)) {
                    loadClaimData(claim);
                    return;
                }
            }
        }
    }

    public void deleteClaim(Claim claim) {
        for (ClaimData d : claimDataList) {
            if (d.getId() == claim.getID()) {
                db.delete(d);
                claimDataList.remove(d);
                break;
            }
        }
    }

    public void updateClaim(Claim claim) {
        //TODO: deal with events on claim update
    }

    public void modifyClaim(Claim claim) {
        //TODO: deal with events on claim modification
    }
    
    public static Data get(){
        return d;
    }
}

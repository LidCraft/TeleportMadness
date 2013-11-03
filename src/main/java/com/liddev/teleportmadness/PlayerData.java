package com.liddev.teleportmadness;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static javax.persistence.CascadeType.PERSIST;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar < liddev.com >
 */
@Entity()
@Table(name = "mad_player_data")
public class PlayerData implements Serializable {
    
    @Id
    private long id;

    @Length(max = 30)
    @NotEmpty
    private String playerName;

    private HashMap<String, Integer> worldLimits;

    @OneToMany(cascade = PERSIST)
    @JoinColumn(name="mad_player_data_home")
    private List<Home> homes;

    private int homeLimit;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Player getPlayer() {
        return Bukkit.getServer().getPlayer(playerName);
    }

    public void setPlayer(Player player) {
        this.playerName = player.getName();
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public HashMap<String, Integer> getWorldLimits() {
        return worldLimits;
    }

    public void setWorldLimits(HashMap<String, Integer> worldLimits) {
        this.worldLimits = worldLimits;
    }

    public void setWorldLimit(String world, Integer limit) {
        worldLimits.put(world, limit);
    }

    public int getWorldLimit(String world) {
        return worldLimits.get(world);
    }

    public int getWorldLimit(World world) {
        return worldLimits.get(world.getName());
    }

    public boolean isPrivateAllowed() {
        return homeLimit != 0;
    }

    public int getHomeLimit() {
        return homeLimit;
    }

    public void setHomeLimit(int homeLimit) {
        this.homeLimit = homeLimit;
    }

    public void setHomes(List<Home> homes) {
        this.homes = homes;
    }

    public List<Home> getHomes() {
        return homes;
    }

    public void addHome(Home home) {
        homes.add(home);
    }

    public Home getHome(String name) {
        Home home = null;
        for (Home h : homes) {
            if (h.getName().equalsIgnoreCase(name)) {
                home = h;
            }
        }
        return home;
    }

    public void removeHome(String name) {
        for (Home h : homes) {
            if (h.getName().equalsIgnoreCase(name)) {
                homes.remove(h);
            }
        }
    }
}

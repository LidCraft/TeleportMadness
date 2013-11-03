package com.liddev.teleportmadness;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar < liddev.com >
 */
@Entity
@Table(name = "mad_permission_group")
public class PermissionGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty
    private String name;

    @ManyToMany
    @JoinTable(name="mad_permission_group_player_data_w")
    private List<PlayerData> whiteList;

    @ManyToMany
    @JoinTable(name="mad_permission_group_player_data_b")
    private List<PlayerData> blackList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<PlayerData> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(List<PlayerData> whiteList) {
        this.whiteList = whiteList;
    }

    public List<PlayerData> getBlackList() {
        return blackList;
    }

    public void setBlackList(List<PlayerData> blackList) {
        this.blackList = blackList;
    }

    public boolean isTrustedPlayer(Player player) {
        boolean trusted = false;
        for (int i = 0; i < whiteList.size(); i++) {
            if (player.getName().equals(whiteList.get(i))) {
                trusted = true;
            }
        }
        return trusted;
    }

    public boolean isUntrustedPlayer(Player player) {
        boolean untrusted = false;
        for (int i = 0; i < blackList.size(); i++) {
            if (player.getName().equals(blackList.get(i))) {
                untrusted = true;
            }
        }
        return untrusted;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /*public PermissionGroup() {
     //groups = new ArrayList<>();
     }

     public void addGroup() {

     }*/
    /*public ArrayList<String> getGroups() {
     return groups;
     }

     public void setGroups(ArrayList<String> groups) {
     this.groups = groups;
     }*/

    /*public boolean isTrustedGroup(String group) {
     char level = group.charAt(0);
     if (level == (char) this.level) {
     return true;
     }
     if (groups.contains(group)) {
     return true;
     }
     return false;
     }*/
}

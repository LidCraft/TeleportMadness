package com.liddev.teleportmadness;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar < liddev.com >
 */
public class PermissionGroup implements Serializable {

    private long id;

    private String name;

    private List<UUID> whiteList;

    private List<UUID> blackList;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<UUID> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(List<UUID> whiteList) {
        this.whiteList = whiteList;
    }

    public List<UUID> getBlackList() {
        return blackList;
    }

    public void setBlackList(List<UUID> blackList) {
        this.blackList = blackList;
    }

    public boolean isTrustedPlayer(Player player) {
        boolean trusted = false;
        for (int i = 0; i < whiteList.size(); i++) {
            if (player.getUniqueId().equals(whiteList.get(i))) {
                trusted = true;
            }
        }
        return trusted;
    }

    public boolean isUntrustedPlayer(Player player) {
        boolean untrusted = false;
        for (int i = 0; i < blackList.size(); i++) {
            if (player.getUniqueId().equals(blackList.get(i))) {
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

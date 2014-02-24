package com.liddev.teleportmadness;

/**
 *
 * @author Renlar <liddev.com>
 */
public enum PermissionLevel {

    OWNER(0), PERMISSIONTRUST(1), TRUST(2), CONTAINERTRUST(3), ACCESSTRUST(4), EVERYONE(5);

    /**
     *
     */
    public static final PermissionLevel defaultLevel = EVERYONE;
    public final int level;

    PermissionLevel(int n) {
        level = n;
    }

    public static PermissionLevel getPermission(String arg) {
        Integer test = -1;
        try {
            test = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            for (PermissionLevel l : PermissionLevel.values()) {
                if (arg.equalsIgnoreCase(l.name())) {
                    return l;
                }
            }
        }
        for (PermissionLevel l : PermissionLevel.values()) {
            if (l.level == test) {
                return l;
            }
        }
        return null;
    }
}

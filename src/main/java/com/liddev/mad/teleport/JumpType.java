package com.liddev.mad.teleport;

/**
 *
 * @author Renlar <liddev.com>
 */
public enum JumpType {

  GLOBAL, WORLD, GLOBALHOME, WORLDHOME, PERSONAL, GIFT;

  public static JumpType match(String s) {
    if (s.toLowerCase().equals("personal") || "personal".startsWith(s.toLowerCase())) {
      return PERSONAL;
    }

    if (s.toLowerCase().equals("gift") || "gift".startsWith(s.toLowerCase())) {
      return GIFT;
    }

    if (s.toLowerCase().equals("global") || "global".startsWith(s.toLowerCase())) {
      return GLOBAL;
    }

    if (s.toLowerCase().equals("world") || "world".startsWith(s.toLowerCase())) {
      return WORLD;
    }

    if (s.toLowerCase().equals("globalhome") || "globalhome".startsWith(s.toLowerCase()) || s.toLowerCase().equals("gh")) {
      return GLOBALHOME;
    }

    if (s.toLowerCase().equals("worldhome") || "worldhome".startsWith(s.toLowerCase()) || s.toLowerCase().equals("wh")) {
      return WORLDHOME;
    }
    return null;
  }
}

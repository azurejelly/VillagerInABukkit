package com.imjustdoom.villagerinabucket;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public static boolean ZOMBIE_VILLAGER;

    public static void init() {
        Main.get().saveDefaultConfig();
        FileConfiguration fileConfiguration = Main.get().getConfig();
        ZOMBIE_VILLAGER = fileConfiguration.getBoolean("zombie-villager", true);
    }
}

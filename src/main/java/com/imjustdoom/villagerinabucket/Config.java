package com.imjustdoom.villagerinabucket;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public static boolean VILLAGER;
    public static boolean ZOMBIE_VILLAGER;
    public static boolean WANDERING_TRADER;
    public static boolean DISABLE_PLACING_OF_DISABLED;

    public static void init() {
        Main.get().saveDefaultConfig();
        FileConfiguration fileConfiguration = Main.get().getConfig();
        VILLAGER = fileConfiguration.getBoolean("villager", true);
        ZOMBIE_VILLAGER = fileConfiguration.getBoolean("zombie-villager", true);
        WANDERING_TRADER = fileConfiguration.getBoolean("wandering-trader", true);
        DISABLE_PLACING_OF_DISABLED = fileConfiguration.getBoolean("disable-bucket-use-on-disable", false);
    }
}

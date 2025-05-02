package com.imjustdoom.villagerinabucket;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public static boolean VILLAGER = true;
    public static boolean ZOMBIE_VILLAGER = true;
    public static boolean WANDERING_TRADER = true;
    public static boolean DISABLE_PLACING_OF_DISABLED = false;
    public static boolean HARM_REPUTATION = false;

    public static void init() {
        Main.get().saveDefaultConfig();
        FileConfiguration fileConfiguration = Main.get().getConfig();
        VILLAGER = fileConfiguration.getBoolean("villager", VILLAGER);
        ZOMBIE_VILLAGER = fileConfiguration.getBoolean("zombie-villager", ZOMBIE_VILLAGER);
        WANDERING_TRADER = fileConfiguration.getBoolean("wandering-trader", WANDERING_TRADER);
        DISABLE_PLACING_OF_DISABLED = fileConfiguration.getBoolean("disable-bucket-use-on-disable", DISABLE_PLACING_OF_DISABLED);
        HARM_REPUTATION = fileConfiguration.getBoolean("harm-reputation", HARM_REPUTATION);
    }
}

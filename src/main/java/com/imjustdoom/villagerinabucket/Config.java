package com.imjustdoom.villagerinabucket;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public static boolean VILLAGER = true;
    public static boolean ZOMBIE_VILLAGER = true;
    public static boolean WANDERING_TRADER = true;
    public static boolean DISABLE_PLACING_OF_DISABLED = false;
    public static boolean HARM_REPUTATION = false;

    public static boolean RESOURCE_PACK = true;
    public static String RESOURCE_PACK_URL = "https://test-cdn.imjustdoom.com/VillagerInABukkitPack.zip";
    public static String RESOURCE_PACK_HASH = "f2d4dd5bf8ee221234b738236099b2592c58b8e8";
    public static String RESOURCE_PACK_ID = "68a4b411-e409-4d89-b563-66049ba4914b";

    public static void init() {
        Main.get().saveDefaultConfig();
        FileConfiguration fileConfiguration = Main.get().getConfig();
        VILLAGER = fileConfiguration.getBoolean("villager", VILLAGER);
        ZOMBIE_VILLAGER = fileConfiguration.getBoolean("zombie-villager", ZOMBIE_VILLAGER);
        WANDERING_TRADER = fileConfiguration.getBoolean("wandering-trader", WANDERING_TRADER);
        DISABLE_PLACING_OF_DISABLED = fileConfiguration.getBoolean("disable-bucket-use-on-disable", DISABLE_PLACING_OF_DISABLED);
        HARM_REPUTATION = fileConfiguration.getBoolean("harm-reputation", HARM_REPUTATION);

        RESOURCE_PACK = fileConfiguration.getBoolean("resource-pack", RESOURCE_PACK);
        RESOURCE_PACK_URL = fileConfiguration.getString("resource-pack-url", RESOURCE_PACK_URL);
        RESOURCE_PACK_HASH = fileConfiguration.getString("resource-pack-hash", RESOURCE_PACK_HASH);
        RESOURCE_PACK_ID = fileConfiguration.getString("resource-pack-id", RESOURCE_PACK_ID);
    }
}

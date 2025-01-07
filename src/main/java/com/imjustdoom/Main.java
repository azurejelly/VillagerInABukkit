package com.imjustdoom;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main INSTANCE;

    public Main() {
        INSTANCE = this;
    }

    public VillagerItem VILLAGER_IN_A_BUCKET;

    @Override
    public void onEnable() {
        // The item needs to be initialised somewhere. Add as variable to this class so it can be referenced to add to inventories
        VILLAGER_IN_A_BUCKET = new VillagerItem();

        // Normal event registering
        Bukkit.getPluginManager().registerEvents(new VillagerListener(), this);
    }

    public static Main get() {
        return INSTANCE;
    }
}
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
        VILLAGER_IN_A_BUCKET = new VillagerItem();

        Bukkit.getPluginManager().registerEvents(new VillagerListener(), this);
    }

    public static Main get() {
        return INSTANCE;
    }
}
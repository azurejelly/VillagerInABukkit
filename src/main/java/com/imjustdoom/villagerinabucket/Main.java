package com.imjustdoom.villagerinabucket;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main INSTANCE;

    public Main() {
        INSTANCE = this;
    }

    public NamespacedKey key = new NamespacedKey(this, "villager_data");

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new VillagerListener(), this);
    }

    public static Main get() {
        return INSTANCE;
    }
}
package com.imjustdoom.villagerinabucket;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {
    private static Main INSTANCE;

    public NamespacedKey key = new NamespacedKey(this, "villager_data");

    public Main() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        Config.init();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public boolean isVillagerBucket(ItemStack itemStack) {
        if (itemStack.getType() != Material.BUCKET || itemStack.getItemMeta() == null) {
            return false;
        }

        PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        return dataContainer.has(this.key) && dataContainer.get(this.key, PersistentDataType.BYTE_ARRAY) != null;
    }

    public void createVillagerBucket(ItemStack itemStack, Entity entity) {
        itemStack.editMeta(meta -> {
            meta.getPersistentDataContainer().set(this.key, PersistentDataType.BYTE_ARRAY, Bukkit.getUnsafe().serializeEntity(entity));
            meta.setMaxStackSize(1);

            switch (entity) {
                case Villager villager -> {
                    meta.customName(Component.text("Villager In A Bucket"));
                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.text("Level: " + villager.getVillagerLevel(), TextColor.color(Color.GRAY.asRGB()), TextDecoration.ITALIC));
                    lore.add(Component.text("Region: " + villager.getVillagerType().getKey().getKey().toUpperCase(), TextColor.color(Color.GRAY.asRGB()), TextDecoration.ITALIC));
                    lore.add(Component.text("Profession: " + villager.getProfession(), TextColor.color(Color.GRAY.asRGB()), TextDecoration.ITALIC));
                    if (!villager.isAdult()) {
                        lore.add(Component.text("Baby", TextColor.color(Color.GRAY.asRGB()), TextDecoration.ITALIC));
                    }
                    meta.lore(lore);
                }
                case ZombieVillager zombieVillager -> {
                    meta.customName(Component.text("Zombie Villager In A Bucket"));
                    List<Component> lore = new ArrayList<>();
                    if (!zombieVillager.isAdult()) {
                        lore.add(Component.text("Baby", TextColor.color(Color.GRAY.asRGB()), TextDecoration.ITALIC));
                    }
                    meta.lore(lore);
                }
                case WanderingTrader wanderingTrader -> {
                    meta.customName(Component.text("Wandering Trader In A Bucket"));
                    List<Component> lore = new ArrayList<>();
                    if (!wanderingTrader.isAdult()) {
                        lore.add(Component.text("Baby", TextColor.color(Color.GRAY.asRGB()), TextDecoration.ITALIC));
                    }
                    meta.lore(lore);
                }
                default -> {
                }
            }
        });
    }

    @EventHandler
    public void villagerInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Entity clicked = event.getRightClicked();
        if (((clicked.getType() != EntityType.VILLAGER || !Config.VILLAGER)
                && (clicked.getType() != EntityType.WANDERING_TRADER || !Config.WANDERING_TRADER)
                && (clicked.getType() != EntityType.ZOMBIE_VILLAGER || !Config.ZOMBIE_VILLAGER))
                || itemStack.getType() != Material.BUCKET || isVillagerBucket(itemStack)) {
            return;
        }

        // Handle single or multiple bucket stacks
        if (itemStack.getAmount() > 1) {
            ItemStack newStack = new ItemStack(Material.BUCKET);
            createVillagerBucket(newStack, clicked);
            itemStack.setAmount(itemStack.getAmount() - 1);
            player.getInventory().addItem(newStack);
        } else {
            createVillagerBucket(itemStack, clicked);
        }
        clicked.remove();
        event.setCancelled(true);
    }

    @EventHandler
    public void bucketInteract(PlayerInteractEvent event) {
        if (event.getHand() == null) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(event.getHand());

        if (!event.getAction().isRightClick() || event.getClickedBlock() == null || !isVillagerBucket(itemStack)) {
            return;
        }
        event.setCancelled(true);

        PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        Entity entity = Bukkit.getUnsafe().deserializeEntity(dataContainer.get(this.key, PersistentDataType.BYTE_ARRAY), player.getWorld());

        if (((!Config.VILLAGER && entity.getType() == EntityType.VILLAGER)
            || (!Config.ZOMBIE_VILLAGER && entity.getType() == EntityType.ZOMBIE_VILLAGER)
            || (!Config.WANDERING_TRADER && entity.getType() == EntityType.WANDERING_TRADER))
                && Config.DISABLE_PLACING_OF_DISABLED) {
            player.sendMessage(Component.text("You are not allowed to place this villager"));
            return;
        }

        entity.spawnAt(event.getInteractionPoint());
        itemStack.editMeta(meta -> {
            meta.customName(null);
            meta.getPersistentDataContainer().remove(this.key);
            meta.setMaxStackSize(null);
            if (meta.hasLore()) {
                meta.lore(null);
            }
        });
    }

    public static Main get() {
        return INSTANCE;
    }
}
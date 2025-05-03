package com.imjustdoom.villagerinabucket;

import com.destroystokyo.paper.entity.villager.Reputation;
import com.destroystokyo.paper.entity.villager.ReputationType;
import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
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
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
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

        Metrics metrics = new Metrics(this, 25722);
    }

    public boolean isVillagerBucket(ItemStack itemStack) {
        if (itemStack.getType() != Material.BUCKET || itemStack.getItemMeta() == null) {
            return false;
        }

        PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        return dataContainer.has(this.key) && dataContainer.get(this.key, PersistentDataType.BYTE_ARRAY) != null;
    }

    public void createVillagerBucket(ItemStack itemStack, Entity entity, Player player) {
        switch (entity) {
            case Villager villager -> {
                itemStack.setData(DataComponentTypes.ITEM_MODEL, Key.key("villagerinabucket", "villager_in_a_bucket"));
                itemStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addString(villager.getVillagerType().key().value()).build());
            }
            case ZombieVillager zombieVillager -> {
                itemStack.setData(DataComponentTypes.ITEM_MODEL, Key.key("villagerinabucket", "zombie_villager_in_a_bucket"));
            }
            case WanderingTrader trader -> {
                itemStack.setData(DataComponentTypes.ITEM_MODEL, Key.key("villagerinabucket", "wandering_trader_in_a_bucket"));
            }
            default -> throw new IllegalStateException("Unexpected value: " + entity);
        }
        itemStack.editMeta(meta -> {
            switch (entity) {
                case Villager villager -> {
                    if (Config.HARM_REPUTATION) {
                        Reputation reputation = villager.getReputation(player.getUniqueId());
                        int minorRep = reputation.getReputation(ReputationType.MINOR_NEGATIVE);
                        reputation.setReputation(ReputationType.MINOR_NEGATIVE, minorRep >= 175 ? 200 : minorRep + 25);
                        villager.setReputation(player.getUniqueId(), reputation);
                    }

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
                default -> throw new IllegalStateException("Unexpected value: " + entity);
            }
            meta.getPersistentDataContainer().set(this.key, PersistentDataType.BYTE_ARRAY, Bukkit.getUnsafe().serializeEntity(entity));
            meta.setMaxStackSize(1);
        });
    }

    @EventHandler
    public void villagerInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(event.getHand());
        Entity clicked = event.getRightClicked();
        // Make sure it could possibly be a villager bucket item
        if (itemStack.getType() != Material.BUCKET) {
            return;
        }

        // If it is a villager bucket item cancel the event - stops milking and picking up multiple villagers in a single bucket (overrides old one)
        if (isVillagerBucket(itemStack)) {
            event.setCancelled(true);
            return;
        }

        // Check if the clicked entity is able to be picked up
        if (((clicked.getType() != EntityType.VILLAGER || !Config.VILLAGER)
                && (clicked.getType() != EntityType.WANDERING_TRADER || !Config.WANDERING_TRADER)
                && (clicked.getType() != EntityType.ZOMBIE_VILLAGER || !Config.ZOMBIE_VILLAGER))) {
            return;
        }

        // Handle single or multiple bucket stacks
        if (itemStack.getAmount() > 1) {
            ItemStack newStack = new ItemStack(Material.BUCKET);
            createVillagerBucket(newStack, clicked, player);
            itemStack.setAmount(itemStack.getAmount() - 1);
            player.getInventory().addItem(newStack);
        } else {
            createVillagerBucket(itemStack, clicked, player);
        }
        clicked.remove();
        event.setCancelled(true);
    }

    @EventHandler
    public void bucketInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        // Check if the action is related to a villager in a bucket item
        if (!event.getAction().isRightClick() || itemStack == null || !isVillagerBucket(itemStack)) {
            return;
        }
        event.setCancelled(true);

        // Return after cancelling event if a block is null because it is either air or water which could override the villager
        if (event.getClickedBlock() == null) {
            return;
        }

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
        itemStack.setData(DataComponentTypes.ITEM_MODEL, Key.key("minecraft", "bucket"));
        itemStack.unsetData(DataComponentTypes.CUSTOM_MODEL_DATA);
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
package com.imjustdoom.villagerinabucket.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event that occurs after a Villager has been placed down
 */
public class VillagerPlaceEvent extends EntityEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final ItemStack itemStack;

    public VillagerPlaceEvent(@NotNull Entity entity, @NotNull Player player, ItemStack itemStack) {
        super(entity);
        this.player = player;
        this.itemStack = itemStack;
    }

    /**
     * Returns the involved player
     * @return the player who placed down the Villager
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the ItemStack of the empty bucket
     * @return the bucket item
     */
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}

package com.imjustdoom.villagerinabucket.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event that occurs after a Villager has been picked up by a bucket
 */
public class VillagerPickupEvent extends EntityEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final ItemStack itemStack;

    public VillagerPickupEvent(@NotNull Entity entity, @NotNull Player player, ItemStack itemStack) {
        super(entity);
        this.player = player;
        this.itemStack = itemStack;
    }

    /**
     * Returns the involved player
     * @return the player who picked up the Villager
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the ItemStack of the Villager in a bucket item
     * @return the Villager in a bucket item
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

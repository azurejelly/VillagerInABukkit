package com.imjustdoom.villagerinabucket.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event that occurs before a Villager is placed
 */
public class PreVillagerPlaceEvent extends EntityEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final ItemStack itemStack;
    private boolean cancelled;

    public PreVillagerPlaceEvent(@NotNull Entity entity, @NotNull Player player, ItemStack itemStack) {
        super(entity);
        this.player = player;
        this.itemStack = itemStack;
    }

    /**
     * Returns the involved player
     * @return the player who is attempting to place the bucket
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the Villager in a bucket item that is getting placed
     * @return the ItemStack
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

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

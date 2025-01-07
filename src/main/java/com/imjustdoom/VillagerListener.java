package com.imjustdoom;

import net.minecraft.server.v1_6_R3.MerchantRecipe;
import net.minecraft.server.v1_6_R3.NBTBase;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftVillager;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.getspout.spoutapi.inventory.SpoutItemStack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class VillagerListener implements Listener {

    @EventHandler
    public void villagerInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();
        Entity clicked = event.getRightClicked();

        if (clicked.getType() != EntityType.VILLAGER || itemStack.getType() != Material.BUCKET) {
            return;
        }

        CraftVillager villager = (CraftVillager) clicked;

        List<String> recipes = new ArrayList<>();
        recipes.add(villager.getProfession().toString());
        recipes.add(String.valueOf(villager.getAge()));

        for (MerchantRecipe recipe : (Iterable<MerchantRecipe>) villager.getHandle().getOffers(((CraftPlayer) player).getHandle())) {
            ByteArrayOutputStream inMemoryOutput = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(inMemoryOutput);
            NBTBase.a(recipe.i(), dataOutputStream);
            recipes.add(Base64.getEncoder().encodeToString(inMemoryOutput.toByteArray()));
        }

        // This is how you can add the item to the inventory through code. Use a SpoutItemStack
        SpoutItemStack item = new SpoutItemStack(Main.get().VILLAGER_IN_A_BUCKET);

        // Set lore
        ItemMeta meta = item.getItemMeta();
        CraftItemStack craftItemStack = (CraftItemStack) itemStack;
        craftItemStack.getItemMeta();
        meta.setLore(recipes);
        item.setItemMeta(meta);

        clicked.remove();
        event.setCancelled(true);

        if (player.getGameMode() != GameMode.CREATIVE) {
            itemStack.setAmount(itemStack.getAmount() - 1);
            player.setItemInHand(itemStack);
        }

        if (itemStack.getAmount() < 1) {
            player.setItemInHand(item);
        } else {
            player.getInventory().addItem(item);
        }
    }
}

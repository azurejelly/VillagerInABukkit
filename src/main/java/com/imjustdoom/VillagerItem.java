package com.imjustdoom;

import net.minecraft.server.v1_6_R3.MerchantRecipe;
import net.minecraft.server.v1_6_R3.NBTBase;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftVillager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.material.item.GenericCustomItem;
import org.getspout.spoutapi.player.SpoutPlayer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Base64;
import java.util.List;

public class VillagerItem extends GenericCustomItem {

    public VillagerItem() {
        super(Main.get(), "villagerinabucket", "https://i.ibb.co/sCFvQBD/q6wBozz.png");
        setName("Villager In A Bucket");
        setStackable(false);
    }

    @Override
    public boolean onItemInteract(SpoutPlayer player, SpoutBlock block, BlockFace face) {
        if (player == null || block == null || face == null) return true;

        World world = player.getWorld();
        ItemStack item = player.getItemInHand();
        CraftVillager villager = (CraftVillager) world.spawnEntity(new Location(world, block.getX() + face.getModX(), block.getY() + face.getModY(), block.getZ() + face.getModZ()), EntityType.VILLAGER);
        if (!item.getItemMeta().getLore().isEmpty()) {
            List<String> lore = item.getItemMeta().getLore();
            villager.setProfession(Villager.Profession.valueOf(lore.get(0)));
            villager.setAge(Integer.parseInt(lore.get(1)));

            lore.remove(0);
            lore.remove(0);

            villager.getHandle().getOffers(((CraftPlayer) player).getHandle()).clear();
            for (String recipe : lore) {
                byte[] bytes = Base64.getDecoder().decode(recipe);
                NBTTagCompound nbt = (NBTTagCompound) NBTBase.b(new DataInputStream(new ByteArrayInputStream(bytes)), bytes.length);
                villager.getHandle().getOffers(((CraftPlayer) player).getHandle()).a(new MerchantRecipe(nbt));
            }
        }

        if (player.getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount());
        }

        return true;
    }
}

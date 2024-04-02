package me.faln.skyblockcore.utils;

import org.bukkit.ChatColor;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;


public final class SpawnerUtils {

    public static EntityType getSpawnerType(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        EntityType spawnType = null;

        try {
            BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
            spawnType = ((CreatureSpawner) blockStateMeta.getBlockState()).getSpawnedType();
        } catch (Exception ignored) {}

        if (spawnType != null) {
            return spawnType;
        }

        String name = ChatColor.stripColor(itemMeta.getDisplayName());

        if (!name.equals(itemMeta.getDisplayName())) {
            spawnType = getEntityTypeFromName(name);
        }

        return spawnType;
    }

    private static EntityType getEntityTypeFromName(String name) {
        try {
            name = name.toUpperCase();
            int spawnerIndex = name.indexOf("SPAWNER");
            if (spawnerIndex != -1)
                name = name.substring(0, spawnerIndex).trim();
            return EntityType.valueOf(name.replace(" ", "_"));
        } catch (Exception ignored) {
            return null;
        }
    }
}

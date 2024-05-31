package me.faln.skyblockcore.progression.impl.woodcutting.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.set.ImmutableSet;

import java.util.Set;

@UtilityClass
public class WoodUtils {

    private static final ImmutableSet<Material> WOOD_MATERIALS;

    static {
        WOOD_MATERIALS = Sets.immutable.withAll(Set.of(
                Material.ACACIA_WOOD,
                Material.BIRCH_WOOD,
                Material.CHERRY_WOOD,
                Material.DARK_OAK_WOOD,
                Material.JUNGLE_WOOD,
                Material.MANGROVE_WOOD,
                Material.OAK_WOOD,
                Material.SPRUCE_WOOD,
                Material.STRIPPED_ACACIA_WOOD,
                Material.STRIPPED_BIRCH_WOOD,
                Material.STRIPPED_CHERRY_WOOD,
                Material.STRIPPED_JUNGLE_WOOD,
                Material.STRIPPED_OAK_WOOD,
                Material.STRIPPED_MANGROVE_WOOD,
                Material.STRIPPED_SPRUCE_WOOD,
                Material.STRIPPED_DARK_OAK_WOOD
        ));
    }

    public boolean isWood(final Block block) {
        return WOOD_MATERIALS.contains(block.getType());
    }

}

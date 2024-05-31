package me.faln.skyblockcore.profilecompass.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.stormdev.utils.NBTEditor;

@UtilityClass
public class CompassUtil {

    private static final String COMPASS_NBT = "COMPASS_NBT";

    public boolean isCompass(final ItemStack item) {
        return NBTEditor.contains(item, COMPASS_NBT);
    }

    public ItemStack applyCompassNBT(ItemStack item) {
        return NBTEditor.set(item, COMPASS_NBT);
    }
}

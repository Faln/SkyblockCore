package me.faln.skyblockcore.progression.impl.farming;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.progression.base.AbstractProgression;
import org.bukkit.Material;

public final class FarmingProgression extends AbstractProgression<Material> {

    public FarmingProgression(final SkyblockCore plugin) {
        super(plugin, plugin.config("farming"), Material::valueOf);
    }

}

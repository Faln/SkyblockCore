package me.faln.skyblockcore.progression.impl.mining;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.progression.base.AbstractProgression;
import org.bukkit.Material;

public final class MiningProgression extends AbstractProgression<Material> {

    public MiningProgression(final SkyblockCore plugin) {
        super(plugin, plugin.config("mining"), Material::valueOf);
    }

}

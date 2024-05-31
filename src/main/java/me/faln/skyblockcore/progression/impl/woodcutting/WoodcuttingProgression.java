package me.faln.skyblockcore.progression.impl.woodcutting;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.progression.base.AbstractProgression;
import org.bukkit.Material;

public final class WoodcuttingProgression extends AbstractProgression<Material> {

    public WoodcuttingProgression(final SkyblockCore plugin) {
        super(plugin, plugin.config("woodcutting"), Material::valueOf);
    }

}

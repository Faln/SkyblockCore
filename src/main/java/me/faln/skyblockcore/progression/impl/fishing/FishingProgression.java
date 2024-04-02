package me.faln.skyblockcore.progression.impl.fishing;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.progression.base.AbstractProgression;
import org.bukkit.entity.EntityType;

public final class FishingProgression extends AbstractProgression<EntityType> {

    public FishingProgression(final SkyblockCore plugin) {
        super(plugin, plugin.config("fishing"), EntityType::valueOf);
    }

}

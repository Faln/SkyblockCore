package me.faln.skyblockcore.progression.impl.slaying;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.progression.base.AbstractProgression;
import org.bukkit.entity.EntityType;

public final class SlayingProgression extends AbstractProgression<EntityType> {

    public SlayingProgression(final SkyblockCore plugin) {
        super(plugin, plugin.config("slaying"), EntityType::valueOf);
    }

}

package me.faln.skyblockcore.progression.registry;

import lombok.AllArgsConstructor;
import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.progression.base.AbstractProgression;
import me.faln.skyblockcore.progression.impl.farming.FarmingProgression;
import me.faln.skyblockcore.progression.impl.farming.listeners.FarmingListeners;
import me.faln.skyblockcore.progression.impl.fishing.FishingProgression;
import me.faln.skyblockcore.progression.impl.fishing.listeners.FishingListeners;
import me.faln.skyblockcore.progression.impl.mining.MiningProgression;
import me.faln.skyblockcore.progression.impl.mining.listeners.MiningListeners;
import me.faln.skyblockcore.progression.impl.slaying.SlayingProgression;
import me.faln.skyblockcore.progression.impl.slaying.listeners.SlayingListeners;
import me.faln.skyblockcore.progression.types.ProgressionType;
import me.faln.skyblockcore.utils.EclipseRegistry;

@AllArgsConstructor
public final class ProgressionRegistry extends EclipseRegistry<ProgressionType, AbstractProgression<?>> {

    private final SkyblockCore plugin;

    public void load() {
        this.register(ProgressionType.FARMING, new FarmingProgression(plugin));
        this.register(ProgressionType.SLAYING, new SlayingProgression(plugin));
        this.register(ProgressionType.MINING, new MiningProgression(plugin));
        this.register(ProgressionType.FISHING, new FishingProgression(plugin));

        new FarmingListeners(this.plugin, (FarmingProgression) this.getRegistry().get(ProgressionType.FARMING));
        new SlayingListeners(this.plugin, (SlayingProgression) this.getRegistry().get(ProgressionType.SLAYING));
        new MiningListeners(this.plugin, (MiningProgression) this.getRegistry().get(ProgressionType.MINING));
        new FishingListeners(this.plugin, (FishingProgression) this.getRegistry().get(ProgressionType.FISHING));
    }

}

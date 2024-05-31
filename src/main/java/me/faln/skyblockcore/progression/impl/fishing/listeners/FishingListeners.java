package me.faln.skyblockcore.progression.impl.fishing.listeners;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.events.ProfessionExperienceGainEvent;
import me.faln.skyblockcore.player.PlayerData;
import me.faln.skyblockcore.progression.impl.fishing.FishingProgression;
import me.faln.skyblockcore.progression.types.ProgressionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.stormdev.abstracts.CommonListener;

public final class FishingListeners extends CommonListener<SkyblockCore> {

    private final FishingProgression progression;

    public FishingListeners(final SkyblockCore plugin, final FishingProgression progression) {
        super(plugin);
        this.progression = progression;
    }

    @EventHandler
    public void onFish(final PlayerFishEvent event) {
        if (event.getCaught() == null) {
            return;
        }

        if (!this.progression.isEnabled()) {
            return;
        }

        final EntityType type = event.getCaught().getType();

        if (!this.progression.getLevelRequirement().containsKey(type)) {
            return;
        }

        final Player player = event.getPlayer();
        final PlayerData data = this.plugin.getPlayerStorage().get(player.getUniqueId());

        if (!this.progression.getExperienceValues().containsKey(type)) {
            return;
        }

        final double exp = this.progression.getExperienceValues().get(type).get();
        final ProfessionExperienceGainEvent experienceGainEvent = new ProfessionExperienceGainEvent(player, ProgressionType.FISHING, exp, false);

        Bukkit.getServer().getPluginManager().callEvent(experienceGainEvent);

        if (experienceGainEvent.isCancelled()) {
            return;
        }

        data.addExp(ProgressionType.FISHING, experienceGainEvent.getExp());
    }
}

package me.faln.skyblockcore.progression.impl.fishing.listeners;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.events.ExperienceGainEvent;
import me.faln.skyblockcore.player.PlayerData;
import me.faln.skyblockcore.progression.impl.fishing.FishingProgression;
import me.faln.skyblockcore.progression.types.ProgressionType;
import me.faln.skyblockcore.utils.CropUtils;
import net.abyssdev.abysslib.listener.AbyssListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

public final class FishingListeners extends AbyssListener<SkyblockCore> {

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

        if (this.progression.getLevelRequirement().get(type).getLevelRequirement() > data.getLevels().get(ProgressionType.FARMING).getLevel()) {
            event.setCancelled(true);
            this.plugin.getMessageCache().sendMessage(player, "messages.fish-locked");
            return;
        }

        if (!this.progression.getExperienceValues().containsKey(type)) {
            return;
        }

        final double exp = this.progression.getExperienceValues().get(type).get();
        final ExperienceGainEvent experienceGainEvent = new ExperienceGainEvent(player, ProgressionType.FARMING, exp, false);

        Bukkit.getServer().getPluginManager().callEvent(experienceGainEvent);

        if (experienceGainEvent.isCancelled()) {
            return;
        }

        data.addExp(ProgressionType.FARMING, experienceGainEvent.getExp());
    }
}

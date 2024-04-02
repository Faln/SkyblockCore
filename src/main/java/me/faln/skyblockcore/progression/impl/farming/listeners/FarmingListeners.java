package me.faln.skyblockcore.progression.impl.farming.listeners;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.events.ExperienceGainEvent;
import me.faln.skyblockcore.player.PlayerData;
import me.faln.skyblockcore.progression.impl.farming.FarmingProgression;
import me.faln.skyblockcore.progression.types.ProgressionType;
import me.faln.skyblockcore.utils.CropUtils;
import net.abyssdev.abysslib.listener.AbyssListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public final class FarmingListeners extends AbyssListener<SkyblockCore> {

    private final FarmingProgression progression;

    public FarmingListeners(final SkyblockCore plugin, final FarmingProgression progression) {
        super(plugin);
        this.progression = progression;
    }

    @EventHandler
    public void onBreak(final BlockBreakEvent event) {
        final Material material = event.getBlock().getType();

        if (!CropUtils.ALL_CROPS.contains(material)) {
            return;
        }

        if (!this.progression.getLevelRequirement().containsKey(material)) {
            return;
        }

        final Player player = event.getPlayer();
        final PlayerData data = this.plugin.getPlayerStorage().get(player.getUniqueId());

        if (this.progression.getLevelRequirement().get(material).getLevelRequirement() > data.getLevels().get(ProgressionType.FARMING).getLevel()) {
            event.setCancelled(true);
            this.plugin.getMessageCache().sendMessage(player, "messages.crop-locked");
            return;
        }

        if (!CropUtils.isMaxAge(event.getBlock())) {
            event.setCancelled(true);
            return;
        }

        if (!this.progression.getExperienceValues().containsKey(material)) {
            return;
        }

        final double exp = this.progression.getExperienceValues().get(material).get();
        final ExperienceGainEvent experienceGainEvent = new ExperienceGainEvent(player, ProgressionType.FARMING, exp, false);

        Bukkit.getServer().getPluginManager().callEvent(experienceGainEvent);

        if (experienceGainEvent.isCancelled()) {
            return;
        }

        data.addExp(ProgressionType.FARMING, experienceGainEvent.getExp());
    }
}

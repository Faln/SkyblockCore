package me.faln.skyblockcore.progression.impl.farming.listeners;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.events.ProfessionExperienceGainEvent;
import me.faln.skyblockcore.player.PlayerData;
import me.faln.skyblockcore.progression.impl.farming.FarmingProgression;
import me.faln.skyblockcore.progression.types.ProgressionType;
import me.faln.skyblockcore.utils.CropUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.stormdev.abstracts.CommonListener;
import org.stormdev.chat.PlaceholderReplacer;

public final class FarmingListeners extends CommonListener<SkyblockCore> {

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
        final int levelRequirement = this.progression.getLevelRequirement().get(material).getLevelRequirement();

        if (levelRequirement > data.getLevels().get(ProgressionType.FARMING).getLevel()) {
            event.setCancelled(true);
            this.plugin.getMessageCache().sendMessage(player, "messages.crop-locked", new PlaceholderReplacer()
                    .addPlaceholder("%level%", String.valueOf(levelRequirement)));
            return;
        }

        if (!CropUtils.isMaxAge(event.getBlock())) {
            //event.setCancelled(true);
            return;
        }

        if (!this.progression.getExperienceValues().containsKey(material)) {
            return;
        }

        final double exp = this.progression.getExperienceValues().get(material).get();
        final ProfessionExperienceGainEvent experienceGainEvent = new ProfessionExperienceGainEvent(player, ProgressionType.FARMING, exp, false);

        Bukkit.getServer().getPluginManager().callEvent(experienceGainEvent);

        if (experienceGainEvent.isCancelled()) {
            return;
        }

        data.addExp(ProgressionType.FARMING, experienceGainEvent.getExp());
    }
}

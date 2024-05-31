package me.faln.skyblockcore.progression.impl.mining.listeners;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.events.ProfessionExperienceGainEvent;
import me.faln.skyblockcore.player.PlayerData;
import me.faln.skyblockcore.progression.impl.mining.MiningProgression;
import me.faln.skyblockcore.progression.types.ProgressionType;
import me.faln.skyblockcore.utils.CropUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.stormdev.abstracts.CommonListener;
import org.stormdev.chat.PlaceholderReplacer;

public final class MiningListeners extends CommonListener<SkyblockCore> {

    private final MiningProgression progression;

    public MiningListeners(final SkyblockCore plugin, final MiningProgression progression) {
        super(plugin);
        this.progression = progression;
    }

    @EventHandler
    public void onBreak(final BlockBreakEvent event) {
        final Block block = event.getBlock();
        final Material type = block.getType();

        if (!type.name().endsWith("_ORE")) {
            return;
        }

        if (!this.progression.getLevelRequirement().containsKey(type)) {
            return;
        }

        final Player player = event.getPlayer();
        final PlayerData data = this.plugin.getPlayerStorage().get(player.getUniqueId());
        final int levelRequirement = this.progression.getLevelRequirement().get(type).getLevelRequirement();

        if (levelRequirement > data.getLevels().get(ProgressionType.MINING).getLevel()) {
            event.setCancelled(true);
            this.plugin.getMessageCache().sendMessage(player, "messages.ore-locked", new PlaceholderReplacer()
                    .addPlaceholder("%level%", String.valueOf(levelRequirement)));
            return;
        }

        if (!CropUtils.isMaxAge(event.getBlock())) {
            event.setCancelled(true);
            return;
        }

        if (!this.progression.getExperienceValues().containsKey(type)) {
            return;
        }

        final double exp = this.progression.getExperienceValues().get(type).get();
        final ProfessionExperienceGainEvent experienceGainEvent = new ProfessionExperienceGainEvent(player, ProgressionType.MINING, exp, false);

        Bukkit.getServer().getPluginManager().callEvent(experienceGainEvent);

        if (experienceGainEvent.isCancelled()) {
            return;
        }

        data.addExp(ProgressionType.MINING, experienceGainEvent.getExp());
    }
}

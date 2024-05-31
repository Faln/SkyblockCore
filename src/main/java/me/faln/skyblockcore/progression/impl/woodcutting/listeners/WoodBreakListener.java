package me.faln.skyblockcore.progression.impl.woodcutting.listeners;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.events.ProfessionExperienceGainEvent;
import me.faln.skyblockcore.player.PlayerData;
import me.faln.skyblockcore.progression.impl.woodcutting.WoodcuttingProgression;
import me.faln.skyblockcore.progression.impl.woodcutting.utils.WoodUtils;
import me.faln.skyblockcore.progression.types.ProgressionType;
import me.faln.skyblockcore.utils.CropUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.stormdev.abstracts.CommonListener;

public final class WoodBreakListener extends CommonListener<SkyblockCore> {

    private final WoodcuttingProgression progression;

    public WoodBreakListener(final SkyblockCore plugin, final WoodcuttingProgression progression) {
        super(plugin);
        this.progression = progression;
    }

    @EventHandler
    public void onBreak(final BlockBreakEvent event) {
        final Block block = event.getBlock();
        final Material type = block.getType();

        if (!WoodUtils.isWood(block)) {
            return;
        }

        if (!this.progression.getLevelRequirement().containsKey(type)) {
            return;
        }

        final Player player = event.getPlayer();
        final PlayerData data = this.plugin.getPlayerStorage().get(player.getUniqueId());

        if (this.progression.getLevelRequirement().get(type).getLevelRequirement() > data.getLevels().get(ProgressionType.WOODCUTTING).getLevel()) {
            event.setCancelled(true);
            this.plugin.getMessageCache().sendMessage(player, "messages.wood-locked");
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
        final ProfessionExperienceGainEvent experienceGainEvent = new ProfessionExperienceGainEvent(player, ProgressionType.WOODCUTTING, exp, false);

        Bukkit.getServer().getPluginManager().callEvent(experienceGainEvent);

        if (experienceGainEvent.isCancelled()) {
            return;
        }

        data.addExp(ProgressionType.WOODCUTTING, experienceGainEvent.getExp());
    }
}

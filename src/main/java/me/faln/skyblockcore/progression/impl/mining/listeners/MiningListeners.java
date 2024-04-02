package me.faln.skyblockcore.progression.impl.mining.listeners;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.events.ExperienceGainEvent;
import me.faln.skyblockcore.player.PlayerData;
import me.faln.skyblockcore.progression.impl.mining.MiningProgression;
import me.faln.skyblockcore.progression.types.ProgressionType;
import me.faln.skyblockcore.utils.CropUtils;
import net.abyssdev.abysslib.listener.AbyssListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public final class MiningListeners extends AbyssListener<SkyblockCore> {

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

        if (this.progression.getLevelRequirement().get(type).getLevelRequirement() > data.getLevels().get(ProgressionType.MINING).getLevel()) {
            event.setCancelled(true);
            this.plugin.getMessageCache().sendMessage(player, "messages.ore-locked");
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
        final ExperienceGainEvent experienceGainEvent = new ExperienceGainEvent(player, ProgressionType.MINING, exp, false);

        Bukkit.getServer().getPluginManager().callEvent(experienceGainEvent);

        if (experienceGainEvent.isCancelled()) {
            return;
        }

        data.addExp(ProgressionType.MINING, experienceGainEvent.getExp());
    }
}

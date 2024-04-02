package me.faln.skyblockcore.progression.impl.slaying.listeners;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.events.ExperienceGainEvent;
import me.faln.skyblockcore.player.PlayerData;
import me.faln.skyblockcore.progression.impl.slaying.SlayingProgression;
import me.faln.skyblockcore.progression.types.ProgressionType;
import me.faln.skyblockcore.utils.SpawnerUtils;
import net.abyssdev.abysslib.listener.AbyssListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public final class SlayingListeners extends AbyssListener<SkyblockCore> {

    private final SlayingProgression progression;

    public SlayingListeners(final SkyblockCore plugin, final SlayingProgression progression) {
        super(plugin);
        this.progression = progression;
    }

    @EventHandler
    public void onAttack(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        final EntityType type = event.getEntity().getType();

        if (!this.progression.getLevelRequirement().containsKey(type)) {
            return;
        }

        final Player player = (Player) event.getDamager();
        final PlayerData data = this.plugin.getPlayerStorage().get(player.getUniqueId());

        if (this.progression.getLevelRequirement().get(type).getLevelRequirement() > data.getLevels().get(ProgressionType.SLAYING).getLevel()) {
            event.setCancelled(true);
            this.plugin.getMessageCache().sendMessage(player, "messages.entity-locked");
        }
    }

    @EventHandler
    public void onDeath(final EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        final EntityType type = event.getEntity().getType();

        if (!this.progression.getLevelRequirement().containsKey(type)) {
            return;
        }

        final Player player = event.getEntity().getKiller();

        if (player == null) {
            return;
        }

        final PlayerData data = this.plugin.getPlayerStorage().get(player.getUniqueId());

        if (!this.progression.getExperienceValues().containsKey(type)) {
            return;
        }

        final double exp = this.progression.getExperienceValues().get(type).get();
        final ExperienceGainEvent experienceGainEvent = new ExperienceGainEvent(player, ProgressionType.SLAYING, exp, false);

        Bukkit.getServer().getPluginManager().callEvent(experienceGainEvent);

        if (experienceGainEvent.isCancelled()) {
            return;
        }

        data.addExp(ProgressionType.SLAYING, experienceGainEvent.getExp());
    }

    @EventHandler
    public void onSpawnerPlace(final BlockPlaceEvent event) {
        final Block block = event.getBlockPlaced();
        final Material type = block.getType();

        if (type != Material.SPAWNER || !(block.getState() instanceof CreatureSpawner)) {
            return;
        }

        final EntityType entityType = SpawnerUtils.getSpawnerType(event.getItemInHand());

        if (entityType == EntityType.UNKNOWN || entityType == null) {
            return;
        }

        if (!this.progression.getLevelRequirement().containsKey(entityType)) {
            return;
        }

        final Player player = event.getPlayer();
        final PlayerData data = this.plugin.getPlayerStorage().get(player.getUniqueId());

        if (this.progression.getLevelRequirement().get(entityType).getLevelRequirement() > data.getLevels().get(ProgressionType.SLAYING).getLevel()) {
            event.setCancelled(true);
            this.plugin.getMessageCache().sendMessage(player, "messages.spawner-locked");
        }
    }
}

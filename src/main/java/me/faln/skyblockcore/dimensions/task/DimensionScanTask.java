package me.faln.skyblockcore.dimensions.task;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.stormdev.abstracts.CommonTask;

public final class DimensionScanTask extends CommonTask<SkyblockCore> {

    private static final String DIMENSION_WORLD_NAME = "Dimensions";

    public DimensionScanTask(final SkyblockCore plugin) {
        super(plugin, 20);
        this.run();
    }

    @Override
    public void run() {
        final World world = Bukkit.getWorld(DIMENSION_WORLD_NAME);

        if (world == null) {
            return;
        }

        for (final Player player : world.getPlayers()) {
            this.plugin.getDimensionRegistry()
                    .getFromLocation(player.getLocation())
                    .ifPresent(dimension -> {
                        final PlayerData data = this.plugin.getPlayerStorage().get(player.getUniqueId());

                        if (data.getLevel() >= dimension.getLevelRequirement()) {
                            return;
                        }

                        player.teleport(this.plugin.getSpawnLocation());
                    });

        }
    }
}

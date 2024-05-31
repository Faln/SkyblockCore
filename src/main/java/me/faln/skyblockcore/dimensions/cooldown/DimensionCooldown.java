package me.faln.skyblockcore.dimensions.cooldown;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.player.PlayerData;
import me.faln.skyblockcore.utils.EclipseRegistry;
import me.faln.skyblockcore.yml.YMLConfig;
import org.bukkit.entity.Player;
import org.stormdev.abstracts.Registry;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public final class DimensionCooldown implements Registry<UUID, Long> {

    private final Registry<Integer, Long> cooldowns = new EclipseRegistry<>() {
        @Override
        public Map<Integer, Long> getRegistry() {
            return super.getRegistry();
        }
    };

    private final Cache<UUID, Long> nextAvailable = Caffeine.newBuilder()
            .expireAfterAccess(6, TimeUnit.HOURS)
            .build();
    private final SkyblockCore plugin;

    public void load() {
        final YMLConfig config = this.plugin.config("dimensions");

        for (final String s : config.list("cooldowns")) {
            final String[] split = s.split(";");

            this.cooldowns.register(Integer.parseInt(split[0]), Integer.parseInt(split[1]) * 1000L);
        }
    }

    public boolean isOnCooldown(final Player player) {
        if (!this.nextAvailable.asMap().containsKey(player.getUniqueId())) {
            return false;
        }

        return System.currentTimeMillis() > this.nextAvailable.asMap().get(player.getUniqueId());
    }

    public void register(final Player player) {
        final PlayerData data = this.plugin.getPlayerStorage().get(player.getUniqueId());
        final int min = this.cooldowns.keySet().stream()
                .mapToInt(Integer::intValue)
                .filter(i -> i <= data.getLevel())
                .max()
                .getAsInt();

        this.nextAvailable.put(player.getUniqueId(), this.cooldowns.getRegistry().get(min) * System.currentTimeMillis());
    }

    @Override
    public Map<UUID, Long> getRegistry() {
        return this.nextAvailable.asMap();
    }
}

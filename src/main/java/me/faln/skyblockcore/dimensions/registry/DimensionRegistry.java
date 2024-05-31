package me.faln.skyblockcore.dimensions.registry;

import lombok.AllArgsConstructor;
import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.dimensions.Dimension;
import me.faln.skyblockcore.utils.EclipseRegistry;
import me.faln.skyblockcore.yml.YMLConfig;
import org.bukkit.Location;

import java.util.Optional;

@AllArgsConstructor
public final class DimensionRegistry extends EclipseRegistry<String, Dimension> {

    private final SkyblockCore plugin;

    public void load() {
        final YMLConfig config = this.plugin.config("dimensions");

        for (final String id : config.getSectionKeys("dimensions")) {
            this.register(id, Dimension.of(config, "dimensions." + id));
        }
    }

    public Optional<Dimension> getFromLocation(final Location location) {
        for (final Dimension dimension : this.values()) {
            if (dimension.getRegion().isInside(location)) {
                return Optional.of(dimension);
            }
        }

        return Optional.empty();
    }
}

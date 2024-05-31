package me.faln.skyblockcore.dimensions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.faln.skyblockcore.player.PlayerData;
import me.faln.skyblockcore.yml.YMLConfig;
import org.bukkit.Location;
import org.stormdev.builder.ItemBuilder;
import org.stormdev.utils.Region;

@AllArgsConstructor @Getter @Builder
public final class Dimension {

    public static Dimension of(final YMLConfig config, final String path) {
        return Dimension.builder()
                .id(path.split("\\.")[1])
                .levelRequirement(config.getInt(path + ".level-requirement"))
                .name(config.coloredString(path + ".name"))
                .region(config.getRegion(path + ".region"))
                .lockedMenuItem(config.getItemBuilder(path + ".locked-menu-item"))
                .unlockedMenuItem(config.getItemBuilder(path + ".unlocked-menu-item"))
                .menuSlot(config.getInt(path + ".menu-slot"))
                .spawnLocation(config.getLocation(path + ".spawn-location"))
                .build();
    }

    private final String id;
    private final Region region;
    private final int levelRequirement;
    private final String name;
    private final ItemBuilder lockedMenuItem;
    private final ItemBuilder unlockedMenuItem;
    private final int menuSlot;
    private final Location spawnLocation;

    public boolean isUnlocked(final PlayerData data) {
        return data.getLevel() >= this.levelRequirement;
    }

}

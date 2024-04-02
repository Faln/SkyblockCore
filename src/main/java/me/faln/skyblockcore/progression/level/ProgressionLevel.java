package me.faln.skyblockcore.progression.level;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.faln.skyblockcore.yml.YMLConfig;
import net.abyssdev.abysslib.builders.ItemBuilder;

@AllArgsConstructor @Getter @Builder
public final class ProgressionLevel {

    public static ProgressionLevel of(final YMLConfig config, final String path) {
        return ProgressionLevel.builder()
                .levelRequirement(config.getInt(path + "level-requirement"))
                .menuSlot(config.getInt(path + "menu-slot"))
                .lockedItem(config.getItemBuilder(path + "locked-item"))
                .unlockedItem(config.getItemBuilder(path + "unlocked-item"))
                .ongoingItem(config.getItemBuilder(path + "ongoing-item"))
                .build();
    }

    private final int levelRequirement;
    private final int menuSlot;
    private final ItemBuilder lockedItem;
    private final ItemBuilder unlockedItem;
    private final ItemBuilder ongoingItem;

}

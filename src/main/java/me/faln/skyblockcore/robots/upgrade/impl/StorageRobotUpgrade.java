package me.faln.skyblockcore.robots.upgrade.impl;

import me.faln.skyblockcore.robots.upgrade.RobotUpgrade;
import me.faln.skyblockcore.robots.upgrade.RobotUpgradeType;
import me.faln.skyblockcore.yml.YMLConfig;
import org.eclipse.collections.api.factory.Maps;

import java.util.Map;

public final class StorageRobotUpgrade extends RobotUpgrade<Double> {

    public static StorageRobotUpgrade of(final YMLConfig config) {
        final Map<Integer, UpgradeDTO<Double>> levels = Maps.mutable.empty();

        for (final String level : config.getSectionKeys("upgrades.STORAGE")) {
            final int levelInt = Integer.parseInt(level);

            levels.put(levelInt, new UpgradeDTO<>(
                    levelInt,
                    config.getDouble("upgrades.STORAGE." + level + ".value"),
                    config.getDouble("upgrades.STORAGE." + level + ".cost")
            ));
        }

        return new StorageRobotUpgrade(levels, RobotUpgradeType.STORAGE);
    }

    private StorageRobotUpgrade(final Map<Integer, UpgradeDTO<Double>> levels, final RobotUpgradeType type) {
        super(levels, type);
    }

}

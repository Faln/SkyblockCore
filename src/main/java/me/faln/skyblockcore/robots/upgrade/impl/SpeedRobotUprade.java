package me.faln.skyblockcore.robots.upgrade.impl;

import me.faln.skyblockcore.robots.upgrade.RobotUpgrade;
import me.faln.skyblockcore.robots.upgrade.RobotUpgradeType;
import me.faln.skyblockcore.yml.YMLConfig;
import org.eclipse.collections.api.factory.Maps;

import java.util.Map;

public final class SpeedRobotUprade extends RobotUpgrade<Double> {

    public static SpeedRobotUprade of(final YMLConfig config) {
        final Map<Integer, UpgradeDTO<Double>> levels = Maps.mutable.empty();

        for (final String level : config.getSectionKeys("upgrades.SPEED")) {
            final int levelInt = Integer.parseInt(level);

            levels.put(levelInt, new UpgradeDTO<>(
                    levelInt,
                    config.getDouble("upgrades.SPEED." + level + ".value"),
                    config.getDouble("upgrades.SPEED." + level + ".cost"))
            );
        }

        return new SpeedRobotUprade(levels, RobotUpgradeType.SPEED);
    }

    private SpeedRobotUprade(final Map<Integer, UpgradeDTO<Double>> levels, final RobotUpgradeType type) {
        super(levels, type);
    }
}

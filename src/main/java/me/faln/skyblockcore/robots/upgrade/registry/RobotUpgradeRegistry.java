package me.faln.skyblockcore.robots.upgrade.registry;

import lombok.AllArgsConstructor;
import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.robots.upgrade.RobotUpgrade;
import me.faln.skyblockcore.robots.upgrade.RobotUpgradeType;
import me.faln.skyblockcore.robots.upgrade.impl.SpeedRobotUprade;
import me.faln.skyblockcore.robots.upgrade.impl.StorageRobotUpgrade;
import me.faln.skyblockcore.utils.EclipseRegistry;
import me.faln.skyblockcore.yml.YMLConfig;

@AllArgsConstructor
public final class RobotUpgradeRegistry extends EclipseRegistry<RobotUpgradeType, RobotUpgrade<?>> {

    private final SkyblockCore plugin;

    public void load() {
        this.getRegistry().clear();

        final YMLConfig config = this.plugin.config("robots");

        this.register(RobotUpgradeType.SPEED, SpeedRobotUprade.of(config));
        this.register(RobotUpgradeType.STORAGE, StorageRobotUpgrade.of(config));
    }
}

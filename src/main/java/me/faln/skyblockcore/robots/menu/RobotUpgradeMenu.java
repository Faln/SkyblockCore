package me.faln.skyblockcore.robots.menu;

import lombok.val;
import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.robots.impl.Robot;
import me.faln.skyblockcore.robots.upgrade.RobotUpgrade;
import me.faln.skyblockcore.robots.upgrade.RobotUpgradeType;
import me.faln.skyblockcore.robots.upgrade.impl.SpeedRobotUprade;
import me.faln.skyblockcore.robots.upgrade.impl.StorageRobotUpgrade;
import me.faln.skyblockcore.utils.FormatUtils;
import me.faln.skyblockcore.yml.YMLConfig;
import org.bukkit.entity.Player;
import org.stormdev.chat.PlaceholderReplacer;
import org.stormdev.hooks.economy.registry.impl.DefaultEconomyRegistry;
import org.stormdev.menus.v2.MenuBuilder;
import org.stormdev.menus.v2.item.MenuItemBuilder;
import org.stormdev.menus.v2.templates.GenericCommonMenu;

public final class RobotUpgradeMenu extends GenericCommonMenu<SkyblockCore> {

    private final YMLConfig config;

    public RobotUpgradeMenu(final SkyblockCore plugin) {
        super(plugin, plugin.config("menus").getConfig(), "robot-upgrades");
        this.config = plugin.config("menus");
    }

    public void open(final Player player, final Robot robot) {
        final MenuBuilder builder = this.createBase();
        final MenuItemBuilder speedItem = this.config.getMenuItemBuilder("robot-upgrades.speed");
        final MenuItemBuilder storageItem = this.config.getMenuItemBuilder("robot-upgrades.storage");

        val speedUpgrade = (SpeedRobotUprade) this.plugin.getRobotUpgradeRegistry().getRegistry().get(RobotUpgradeType.SPEED);
        val speed = speedUpgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.SPEED));
        val storageUpgrade = (StorageRobotUpgrade) this.plugin.getRobotUpgradeRegistry().getRegistry().get(RobotUpgradeType.STORAGE);
        val storage = storageUpgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.STORAGE));

        builder.setItem(speedItem.getSlot(), speedItem.getItem().parse(new PlaceholderReplacer()
                .addPlaceholder("%current%", String.valueOf(robot.getUpgrade(RobotUpgradeType.SPEED)))
                .addPlaceholder("%max%", String.valueOf(speedUpgrade.getLevels().values().stream()
                        .map(RobotUpgrade.UpgradeDTO::getValue)
                        .mapToDouble(Double::doubleValue)
                        .max()
                        .getAsDouble()))
                .addPlaceholder("%speed%", String.valueOf(speed.getValue()))
                .addPlaceholder("%cost%", speed.getCost() == 0.0 ? "Maxed" : FormatUtils.formatComma(speed.getCost()))
        ));

        builder.addClickEvent(speedItem.getSlot(), event -> {
            if (speed.getLevel() == speedUpgrade.getLevels().keySet().stream().mapToInt(Integer::intValue).max().getAsInt()) {
                this.plugin.getMessageCache().sendMessage(player, "messages.upgrade-maxed");
                return;
            }

            if (!DefaultEconomyRegistry.get().getEconomy("vault").hasBalance(player, speed.getCost())) {
                this.plugin.getMessageCache().sendMessage(player, "messages.not-enough-money");
                return;
            }

            DefaultEconomyRegistry.get().getEconomy("vault").withdrawBalance(player, speed.getCost());
            robot.getUpgrades().put(RobotUpgradeType.SPEED, robot.getUpgrade(RobotUpgradeType.SPEED));

            this.plugin.getMessageCache().sendMessage(player, "messages.speed-upgraded");
            this.open(player, robot);
        });

        builder.setItem(storageItem.getSlot(), storageItem.getItem().parse(new PlaceholderReplacer()
                .addPlaceholder("%current%", String.valueOf(robot.getUpgrade(RobotUpgradeType.STORAGE)))
                .addPlaceholder("%max%", String.valueOf(storageUpgrade.getLevels().values().stream()
                        .map(RobotUpgrade.UpgradeDTO::getValue)
                        .mapToDouble(Double::doubleValue)
                        .max()
                        .getAsDouble()))
                .addPlaceholder("%storage%", FormatUtils.formatComma(storage.getValue()))
                .addPlaceholder("%cost%", speed.getCost() == 0.0 ? "Maxed" : FormatUtils.formatComma(storage.getCost()))
        ));

        builder.addClickEvent(storageItem.getSlot(), event -> {
            if (storage.getLevel() == storageUpgrade.getLevels().keySet().stream().mapToInt(Integer::intValue).max().getAsInt()) {
                this.plugin.getMessageCache().sendMessage(player, "messages.upgrade-maxed");
                return;
            }

            if (!DefaultEconomyRegistry.get().getEconomy("vault").hasBalance(player, storage.getCost())) {
                this.plugin.getMessageCache().sendMessage(player, "messages.not-enough-money");
                return;
            }

            DefaultEconomyRegistry.get().getEconomy("vault").withdrawBalance(player, storage.getCost());
            robot.getUpgrades().put(RobotUpgradeType.STORAGE, robot.getUpgrade(RobotUpgradeType.STORAGE));

            this.plugin.getMessageCache().sendMessage(player, "messages.storage-upgraded");
            this.open(player, robot);
        });

        player.openInventory(builder.build());
    }
}

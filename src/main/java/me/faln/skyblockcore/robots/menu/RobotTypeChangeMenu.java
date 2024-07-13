package me.faln.skyblockcore.robots.menu;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.robots.impl.Robot;
import me.faln.skyblockcore.robots.type.RobotType;
import me.faln.skyblockcore.yml.YMLConfig;
import org.bukkit.entity.Player;
import org.stormdev.menus.v2.MenuBuilder;
import org.stormdev.menus.v2.item.MenuItemBuilder;
import org.stormdev.menus.v2.templates.GenericCommonMenu;

public final class RobotTypeChangeMenu extends GenericCommonMenu<SkyblockCore> {

    private final YMLConfig config;

    public RobotTypeChangeMenu(final SkyblockCore plugin) {
        super(plugin, plugin.config("menus").getConfig(), "robot-type-change.");
        this.config = plugin.config("menus");
    }

    public void open(final Player player, final Robot robot) {
        final MenuBuilder menuBuilder = this.createBase();
        final MenuItemBuilder farmerItem = this.config.getMenuItemBuilder("robot-type-change.farmer");
        final MenuItemBuilder mineritem = this.config.getMenuItemBuilder("robot-type-change.miner");
        final MenuItemBuilder slayerItem = this.config.getMenuItemBuilder("robot-type-change.slayer");
        final MenuItemBuilder noneItem = this.config.getMenuItemBuilder("robot-type-change.none");

        menuBuilder.setItem(farmerItem.getSlot(), farmerItem.getItem());
        menuBuilder.addClickEvent(farmerItem.getSlot(), event -> {
            robot.setType(RobotType.FARMER);
            player.closeInventory();
        });

        menuBuilder.setItem(mineritem.getSlot(), mineritem.getItem());
        menuBuilder.addClickEvent(mineritem.getSlot(), event -> {
            robot.setType(RobotType.MINER);
            player.closeInventory();
        });

        menuBuilder.setItem(slayerItem.getSlot(), slayerItem.getItem());
        menuBuilder.addClickEvent(slayerItem.getSlot(), event -> {
            robot.setType(RobotType.SLAYER);
            player.closeInventory();
        });

        menuBuilder.setItem(noneItem.getSlot(), noneItem.getItem());
        menuBuilder.addClickEvent(noneItem.getSlot(), event -> {
            robot.setType(RobotType.NONE);
            player.closeInventory();
        });

        player.openInventory(menuBuilder.build());
    }
}

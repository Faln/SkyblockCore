package me.faln.skyblockcore.robots.menu;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.robots.RobotManager;
import me.faln.skyblockcore.robots.impl.Robot;
import me.faln.skyblockcore.utils.FormatUtils;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.chat.PlaceholderReplacer;
import org.stormdev.hooks.economy.registry.impl.DefaultEconomyRegistry;
import org.stormdev.menus.v2.MenuBuilder;
import org.stormdev.menus.v2.templates.GenericCommonMenu;

public final class RobotMenu extends GenericCommonMenu<SkyblockCore> {

    private final RobotManager robotManager;

    public RobotMenu(final SkyblockCore plugin) {
        super(plugin, plugin.config("menus").getConfig(), "robot-menu.");
        this.robotManager = plugin.getRobotManager();
    }

    public void open(final Player player, final Robot robot) {
        final MenuBuilder builder = this.createBase();
        final double sellPrice = robot.getSellPrice();
        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .addPlaceholder("%sell-price%", FormatUtils.formatComma(sellPrice))
                .addPlaceholder("%item-count%", FormatUtils.formatComma(robot.getItemCount()));

        builder.setItem(this.robotManager.getTypeChangeMenuItem().getSlot(), this.robotManager.getTypeChangeMenuItem().getItem().parse(replacer));
        builder.addClickEvent(this.robotManager.getTypeChangeMenuItem().getSlot(), event -> {
            new RobotTypeChangeMenu(this.plugin).open(player, robot);
        });

        builder.setItem(this.robotManager.getPickupMenuItem().getSlot(), this.robotManager.getPickupMenuItem().getItem().parse(replacer));
        builder.addClickEvent(this.robotManager.getPickupMenuItem().getSlot(), event -> {
            this.robotManager.dispose(robot);

            player.closeInventory();
        });

        builder.setItem(this.robotManager.getUpgradesMenuItem().getSlot(), this.robotManager.getUpgradesMenuItem().getItem().parse(replacer));
        builder.addClickEvent(this.robotManager.getUpgradesMenuItem().getSlot(), event -> {
            new RobotUpgradeMenu(this.plugin).open(player, robot);
        });

        builder.setItem(this.robotManager.getSellMenuItem().getSlot(), this.robotManager.getSellMenuItem().getItem().parse(replacer));
        builder.addClickEvent(this.robotManager.getSellMenuItem().getSlot(), event -> {
            if (sellPrice <= 0) {
                return;
            }

            robot.clearContents();

            DefaultEconomyRegistry.get().getEconomy("vault").addBalance(player, sellPrice);

            this.plugin.getMessageCache().sendMessage(player, "messages.robot-sold", replacer);
            this.open(player, robot);
        });

        player.openInventory(builder.build());
    }

}

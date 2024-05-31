package me.faln.skyblockcore.profilecompass.listener;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.profilecompass.utils.CompassUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.stormdev.abstracts.CommonListener;

public final class CompassInteractListener extends CommonListener<SkyblockCore> {

    private final String compassCommand;

    public CompassInteractListener(final SkyblockCore plugin) {
        super(plugin);
        this.compassCommand = plugin.config("compass").string("compass-command");
    }

    @EventHandler
    public void onClick(final PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND || event.getItem() == null || event.getItem().getType() == Material.AIR) {
            return;
        }

        final ItemStack item = event.getItem();

        if (!CompassUtil.isCompass(item)) {
            return;
        }

        this.plugin.getServer().dispatchCommand(event.getPlayer(), this.compassCommand);
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        final ItemStack item = event.getCurrentItem();

        if (!CompassUtil.isCompass(item)) {
            return;
        }

        event.setCancelled(true);
    }
}

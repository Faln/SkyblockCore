package me.faln.skyblockcore.dimensions.menu;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.dimensions.Dimension;
import me.faln.skyblockcore.player.PlayerData;
import org.bukkit.entity.Player;
import org.stormdev.chat.PlaceholderReplacer;
import org.stormdev.menus.v2.MenuBuilder;
import org.stormdev.menus.v2.templates.GenericCommonMenu;

public final class DimensionMenu extends GenericCommonMenu<SkyblockCore> {

    public DimensionMenu(final SkyblockCore plugin) {
        super(plugin, plugin.config("menus").getConfig(), "dimension-menu.");
    }

    @Override
    public void open(final Player player) {
        final MenuBuilder builder = this.createBase();
        final PlayerData data = this.plugin.getPlayerStorage().get(player.getUniqueId());

        for (final Dimension dimension : this.plugin.getDimensionRegistry().values()) {
            builder.setItem(dimension.getMenuSlot(), dimension.isUnlocked(data) ? dimension.getUnlockedMenuItem() : dimension.getLockedMenuItem());
            builder.addClickEvent(dimension.getMenuSlot(), event -> {
                if (data.getLevel() < dimension.getLevelRequirement()) {
                    this.plugin.getMessageCache().sendMessage(player, "messages.dimension-locked", new PlaceholderReplacer()
                            .addPlaceholder("%level%", String.valueOf(dimension.getLevelRequirement())));
                    return;
                }

                if (this.plugin.getDimensionCooldown().isOnCooldown(player)) {
                    this.plugin.getMessageCache().sendMessage(player, "messages.dimension-on-cooldown", new PlaceholderReplacer()
                            .addPlaceholder("%time%", "1"));
                    return;
                }

                this.plugin.getDimensionCooldown().register(player);
                player.teleport(dimension.getSpawnLocation());
            });
        }

        player.openInventory(builder.build());
    }
}

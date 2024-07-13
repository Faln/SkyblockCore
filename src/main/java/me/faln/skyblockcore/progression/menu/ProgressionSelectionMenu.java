package me.faln.skyblockcore.progression.menu;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.player.PlayerData;
import me.faln.skyblockcore.progression.types.ProgressionType;
import me.faln.skyblockcore.yml.YMLConfig;
import org.bukkit.entity.Player;
import org.stormdev.builder.ItemBuilder;
import org.stormdev.chat.PlaceholderReplacer;
import org.stormdev.menus.v2.MenuBuilder;
import org.stormdev.menus.v2.templates.GenericCommonMenu;

public final class ProgressionSelectionMenu extends GenericCommonMenu<SkyblockCore> {

    public ProgressionSelectionMenu(final SkyblockCore plugin) {
        super(plugin, plugin.config("menus").getConfig(), "selection.");
    }

    @Override
    public void open(final Player player) {
        final MenuBuilder builder = this.createBase();
        final YMLConfig config = this.plugin.config("menus");
        final PlayerData data = this.plugin.getPlayerStorage().get(player.getUniqueId());

        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .addPlaceholder("%farming-level%", String.valueOf(data.getLevels().get(ProgressionType.FARMING).getLevel()))
                .addPlaceholder("%fishing-level%", String.valueOf(data.getLevels().get(ProgressionType.FISHING).getLevel()))
                .addPlaceholder("%slaying-level%", String.valueOf(data.getLevels().get(ProgressionType.SLAYING).getLevel()))
                .addPlaceholder("%woodcutting-level%", String.valueOf(data.getLevels().get(ProgressionType.WOODCUTTING).getLevel()));

        for (final String s : config.getSectionKeys("selection.items")) {
            final ProgressionType type;

            try {
                type = ProgressionType.valueOf(s.toUpperCase());
            } catch (final IllegalArgumentException ignored) {
                continue;
            }

            final int slot = config.getInt("selection.items." + s + ".slot");
            final ItemBuilder item = config.getItemBuilder("selections.items." + s + ".item");

            builder.setItem(slot, item.parse(replacer));
            builder.addClickEvent(slot, event -> new ProgressionMenu(this.plugin, type).open(player));
        }

        player.openInventory(builder.build());
    }
}

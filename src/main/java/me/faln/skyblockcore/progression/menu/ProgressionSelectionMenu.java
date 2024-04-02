package me.faln.skyblockcore.progression.menu;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.progression.types.ProgressionType;
import me.faln.skyblockcore.yml.YMLConfig;
import net.abyssdev.abysslib.builders.ItemBuilder;
import net.abyssdev.abysslib.menu.MenuBuilder;
import net.abyssdev.abysslib.menu.templates.GenericAbyssMenu;
import org.bukkit.entity.Player;

public final class ProgressionSelectionMenu extends GenericAbyssMenu<SkyblockCore> {

    public ProgressionSelectionMenu(final SkyblockCore plugin) {
        super(plugin, plugin.config("menus").getConfig(), "selection.");
    }

    @Override
    public void open(final Player player) {
        final MenuBuilder builder = this.createBase();
        final YMLConfig config = this.plugin.config("menus");

        for (final String s : config.getSectionKeys("selection.items")) {
            final ProgressionType type;

            try {
                type = ProgressionType.valueOf(s.toUpperCase());
            } catch (IllegalArgumentException ignored) {
                continue;
            }

            final int slot = config.getInt("selection.items." + s + ".slot");
            final ItemBuilder item = config.getItemBuilder("selections.items." + s + ".item");

            builder.setItem(slot, item);
            builder.addClickEvent(slot, event -> new ProgressionMenu(this.plugin, type).open(player));
        }

        player.openInventory(builder.build());
    }
}

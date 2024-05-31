package me.faln.skyblockcore.dimensions.commands;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.dimensions.menu.DimensionMenu;
import org.bukkit.entity.Player;
import org.stormdev.commands.CommonCommand;
import org.stormdev.commands.context.CommandContext;

public final class DimensionCommand extends CommonCommand<SkyblockCore, Player> {

    public DimensionCommand(final SkyblockCore plugin) {
        super(plugin, "dimension", Player.class);
    }

    @Override
    public void execute(final CommandContext<Player> context) {
        final Player player = context.getSender();

        if (!player.hasPermission("skyblockcore.dimension")) {
            return;
        }

        new DimensionMenu(this.plugin).open(player);
    }
}

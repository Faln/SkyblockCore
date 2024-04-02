package me.faln.skyblockcore.progression.commands;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.progression.menu.ProgressionSelectionMenu;
import net.abyssdev.abysslib.command.AbyssCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import org.bukkit.entity.Player;

public final class ProgressionCommands extends AbyssCommand<SkyblockCore, Player> {

    public ProgressionCommands(final SkyblockCore plugin) {
        super(
                plugin,
                plugin.config("settings").string("progression-command"),
                "Base command for progressions",
                plugin.config("settings").list("progression-aliases"),
                Player.class);
    }

    @Override
    public void execute(final CommandContext<Player> context) {
        final Player player = context.getSender();

        if (!player.hasPermission("skyblockcore.progression")) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission");
            return;
        }

        new ProgressionSelectionMenu(this.plugin).open(player);
    }
}

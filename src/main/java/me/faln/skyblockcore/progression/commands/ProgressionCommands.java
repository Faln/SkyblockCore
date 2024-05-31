package me.faln.skyblockcore.progression.commands;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.progression.menu.ProgressionSelectionMenu;
import org.bukkit.entity.Player;
import org.stormdev.commands.CommonCommand;
import org.stormdev.commands.context.CommandContext;

public final class ProgressionCommands extends CommonCommand<SkyblockCore, Player> {

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

        if (!player.hasPermission("skyblockcore.admin")) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission");
            return;
        }

        new ProgressionSelectionMenu(this.plugin).open(player);
    }
}

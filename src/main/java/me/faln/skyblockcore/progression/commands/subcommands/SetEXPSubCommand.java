package me.faln.skyblockcore.progression.commands.subcommands;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.player.PlayerData;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.eclipse.collections.api.factory.Sets;
import org.stormdev.commands.CommonSubCommand;
import org.stormdev.commands.context.CommandContext;

public final class SetEXPSubCommand extends CommonSubCommand<SkyblockCore> {

    public SetEXPSubCommand(final SkyblockCore plugin) {
        super(plugin, 2, Sets.immutable.of("setexp"));
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final CommandSender sender = context.getSender();

        if (!sender.hasPermission("skyblockcore.admin")) {
            return;
        }

        final OfflinePlayer player = context.asOfflinePlayer(0);
        final double level = context.asDouble(1);

        if (level < 0.0 || player == null) {
            return;
        }

        final PlayerData data = this.plugin.getPlayerStorage().get(player.getUniqueId());

        data.setExp(level);
    }
}

package me.faln.skyblockcore.progression.commands.subcommands;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.player.PlayerData;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.eclipse.collections.api.factory.Sets;
import org.stormdev.commands.CommonSubCommand;
import org.stormdev.commands.context.CommandContext;

public final class SetLevelSubCommand extends CommonSubCommand<SkyblockCore> {

    public SetLevelSubCommand(final SkyblockCore plugin) {
        super(plugin, 2, Sets.immutable.of("setlevel"));
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final CommandSender sender = context.getSender();

        if (!sender.hasPermission("skyblockcore.admin")) {
            return;
        }

        final OfflinePlayer player = context.asOfflinePlayer(0);
        final int level = context.asInt(1);

        if (level < 1 || player == null) {
            return;
        }

        final PlayerData data = this.plugin.getPlayerStorage().get(player.getUniqueId());

        data.setLevel(level);
    }
}

package me.faln.skyblockcore.progression.commands.subcommands;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.player.PlayerData;
import me.faln.skyblockcore.progression.types.ProgressionType;
import me.faln.skyblockcore.utils.FormatUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.eclipse.collections.api.factory.Sets;
import org.stormdev.chat.PlaceholderReplacer;
import org.stormdev.commands.CommonSubCommand;
import org.stormdev.commands.context.CommandContext;

public final class RemoveEXPSubCommand extends CommonSubCommand<SkyblockCore> {

    public RemoveEXPSubCommand(final SkyblockCore plugin) {
        super(plugin, 2, Sets.immutable.of("removeexp"));
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final CommandSender sender = context.getSender();

        if (!sender.hasPermission("skyblockcore.admin")) {
            return;
        }

        final OfflinePlayer player = context.asOfflinePlayer(0);
        final double expAmount = context.asDouble(1);

        if (player == null || expAmount <= 0.0) {
            return;
        }

        final PlayerData data = this.plugin.getPlayerStorage().get(player.getUniqueId());

        data.removeExp(expAmount);

        if (player.isOnline()) {
            this.plugin.getMessageCache().sendMessage(player.getPlayer(), "messages.exp-loss", new PlaceholderReplacer()
                    .addPlaceholder("%amount%", FormatUtils.formatComma(expAmount)));
        }
    }
}

package me.faln.skyblockcore.commands.subcommands;

import me.faln.skyblockcore.SkyblockCore;
import org.bukkit.command.CommandSender;
import org.eclipse.collections.api.factory.Sets;
import org.stormdev.commands.CommonSubCommand;
import org.stormdev.commands.context.CommandContext;

public final class ReloadSubCommand extends CommonSubCommand<SkyblockCore> {

    public ReloadSubCommand(final SkyblockCore plugin) {
        super(plugin, 0, Sets.immutable.of("reload"));
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final CommandSender sender = context.getSender();

        if (!sender.hasPermission("skyblockcore.admin")) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.no-permissison");
            return;
        }

        this.plugin.load();
    }
}

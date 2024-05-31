package me.faln.skyblockcore.commands;

import me.faln.skyblockcore.SkyblockCore;
import org.bukkit.command.CommandSender;
import org.stormdev.commands.CommonCommand;
import org.stormdev.commands.context.CommandContext;

public final class SkyblockCoreCommand extends CommonCommand<SkyblockCore, CommandSender> {

    public SkyblockCoreCommand(final SkyblockCore plugin) {
        super(plugin, "core", CommandSender.class);
    }

    @Override
    public void execute(final CommandContext<CommandSender> context) {
        final CommandSender sender = context.getSender();

        if (!sender.hasPermission("skyblockcore.admin")) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.no-permissison");
            return;
        }
    }
}

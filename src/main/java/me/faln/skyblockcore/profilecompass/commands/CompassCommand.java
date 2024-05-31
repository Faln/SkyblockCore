package me.faln.skyblockcore.profilecompass.commands;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.profilecompass.utils.CompassUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.chat.PlaceholderReplacer;
import org.stormdev.commands.CommonCommand;
import org.stormdev.commands.context.CommandContext;

public final class CompassCommand extends CommonCommand<SkyblockCore, Player> {

    public CompassCommand(SkyblockCore plugin) {
        super(plugin, "compass", Player.class);
    }

    @Override
    public void execute(final CommandContext<Player> context) {
        final Player player = context.getSender();

        if (!player.hasPermission("skyblockcore.compass")) {
            return;
        }

        final ItemStack item = player.getInventory().getItem(8);

        if (item == null || item.getType() == Material.AIR || !CompassUtil.isCompass(item)) {
            player.getInventory().setItem(0, CompassUtil.applyCompassNBT(this.plugin.getCompassItem().parse(new PlaceholderReplacer()
                    .addPlaceholder("%player%", player.getName())
            )));
        }
    }
}

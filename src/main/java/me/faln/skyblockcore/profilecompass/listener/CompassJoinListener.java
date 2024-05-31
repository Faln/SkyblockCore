package me.faln.skyblockcore.profilecompass.listener;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.profilecompass.utils.CompassUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.stormdev.abstracts.CommonListener;
import org.stormdev.chat.PlaceholderReplacer;

public final class CompassJoinListener extends CommonListener<SkyblockCore> {

    public CompassJoinListener(final SkyblockCore plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (player.hasPlayedBefore()) {
            return;
        }

        ItemStack compass = this.plugin.getCompassItem().parse(new PlaceholderReplacer()
                .addPlaceholder("%player%", player.getName()));

        player.getInventory().setItem(8, CompassUtil.applyCompassNBT(compass));
    }

}

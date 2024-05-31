package me.faln.skyblockcore.progression.listeners;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.events.ProfessionLevelUpEvent;
import me.faln.skyblockcore.progression.types.ProgressionType;
import me.faln.skyblockcore.utils.FormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.stormdev.abstracts.CommonListener;
import org.stormdev.chat.PlaceholderReplacer;

public final class ProgressionLevelUpListener extends CommonListener<SkyblockCore> {

    public ProgressionLevelUpListener(final SkyblockCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onLevelUp(final ProfessionLevelUpEvent event) {
        final Player player = event.getPlayer();
        final String normalized = ProgressionType.normalize(event.getType());
        final int level = event.getLevel();

        this.plugin.getMessageCache().sendMessage(player, "messages.profession-level-up", new PlaceholderReplacer()
                .addPlaceholder("%type%", normalized)
                .addPlaceholder("%level%", FormatUtils.formatComma(level))
        );

        if (!this.plugin.getMilestonesRegistry().containsKey(level)) {
            return;
        }

        final String messagePath = this.plugin.getMilestonesRegistry().getRegistry().get(level);
        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .addPlaceholder("%type%", normalized)
                .addPlaceholder("%player%", player.getName())
                .addPlaceholder("%level%", FormatUtils.formatComma(level));

        for (final Player p : Bukkit.getOnlinePlayers()) {
            this.plugin.getMessageCache().sendMessage(p, messagePath, replacer);
        }
    }
}

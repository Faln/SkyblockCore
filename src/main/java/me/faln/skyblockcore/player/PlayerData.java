package me.faln.skyblockcore.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.events.LevelUpEvent;
import me.faln.skyblockcore.progression.data.ProgressionData;
import me.faln.skyblockcore.progression.types.ProgressionType;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.storage.id.Id;
import net.abyssdev.abysslib.utils.AbyssMath;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Getter @RequiredArgsConstructor @Setter
public final class PlayerData {

    private final Map<ProgressionType, ProgressionData> levels = new EnumMap<>(ProgressionType.class);
    private final SkyblockCore plugin;

    @Id
    private final UUID id;

    public void addExp(final ProgressionType type, final double amount) {
        final ProgressionData data = this.levels.getOrDefault(type, new ProgressionData());
        final double max = AbyssMath.parseEquation(this.plugin.getProgressionRegistry().getRegistry().get(type)
                .getExpFormula().replace("%level%", String.valueOf(data.getLevel())));
        final double total = data.getExp() + amount;

        if (total < max) {
            data.setExp(total);
            return;
        }

        data.setLevel(data.getLevel() + 1);
        data.setExp(0.0);

        final Player player = Bukkit.getPlayer(this.id);
        final LevelUpEvent event = new LevelUpEvent(player, type, data.getLevel());

        Bukkit.getServer().getPluginManager().callEvent(event);

        if (player != null) {
            this.plugin.getMessageCache().sendMessage(player, "messages.leveled-up", new PlaceholderReplacer()
                    .addPlaceholder("%level%", String.valueOf(data.getLevel()))
            );
        }

        this.addExp(type, total - max);
    }
}

package me.faln.skyblockcore.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.events.LevelDownEvent;
import me.faln.skyblockcore.events.LevelUpEvent;
import me.faln.skyblockcore.events.ProfessionLevelUpEvent;
import me.faln.skyblockcore.progression.data.ProgressionData;
import me.faln.skyblockcore.progression.types.ProgressionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.stormdev.chat.PlaceholderReplacer;
import org.stormdev.storage.id.Id;
import org.stormdev.utils.CommonsMath;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

/**
 * The player profile class
 *
 * @author Faln
 */
@Getter @RequiredArgsConstructor @Setter
public final class PlayerData {

    private final Map<ProgressionType, ProgressionData> levels = new EnumMap<>(ProgressionType.class);
    private final transient SkyblockCore plugin;
    private int level = 1;
    private double exp = 0.0;

    @Id
    private final UUID id;

    /**
     * Removes provided amount of exp with a baseline
     * level of 1 and exp of 0.
     *
     * @param amount - amount of exp to remove.
     */
    public void removeExp(final double amount) {
        if (this.exp >= amount) {
            this.exp -= amount;
            return;
        }

        if (this.level == 1) {
            this.exp = 0.0;
            return;
        }

        this.level--;
        this.exp = CommonsMath.parseEquation(this.plugin.getExpFormula().replace("%level%", String.valueOf(this.level)));

        final Player player = Bukkit.getPlayer(this.id);
        final LevelDownEvent event = new LevelDownEvent(player, this.level);

        Bukkit.getServer().getPluginManager().callEvent(event);

        if (player != null) {
            this.plugin.getMessageCache().sendMessage(player, "messages.leveled-down", new PlaceholderReplacer()
                    .addPlaceholder("%level%", String.valueOf(this.level))
            );
        }

        this.removeExp(this.exp - amount);
    }

    /**
     * Adds provided amount of exp.
     *
     * @param amount - amount of exp to add
     */
    public void addExp(final double amount) {
        final double max = CommonsMath.parseEquation(this.plugin.getExpFormula().replace("%level%", String.valueOf(this.level)));
        final double total = this.exp + amount;

        if (total < max) {
            this.exp = total;
            return;
        }

        this.level++;
        this.exp = 0.0;

        final Player player = Bukkit.getPlayer(this.id);
        final LevelUpEvent event = new LevelUpEvent(player, this.level);

        Bukkit.getServer().getPluginManager().callEvent(event);

        if (player != null) {
            this.plugin.getMessageCache().sendMessage(player, "messages.leveled-up", new PlaceholderReplacer()
                    .addPlaceholder("%level%", String.valueOf(this.level))
            );
        }

        this.addExp(total - max);
    }

    /**
     * Adds provided exp to the provided progression.
     *
     * @param type - the type of progression
     * @param amount - the exp amount to add
     */
    public void addExp(final ProgressionType type, final double amount) {
        final ProgressionData data = this.levels.getOrDefault(type, new ProgressionData());
        final double max = CommonsMath.parseEquation(this.plugin.getProgressionRegistry().getRegistry().get(type)
                .getExpFormula().replace("%level%", String.valueOf(data.getLevel())));
        final double total = data.getExp() + amount;

        if (total < max) {
            data.setExp(total);
            return;
        }

        data.setLevel(data.getLevel() + 1);
        data.setExp(0.0);

        final Player player = Bukkit.getPlayer(this.id);
        final ProfessionLevelUpEvent event = new ProfessionLevelUpEvent(player, type, data.getLevel());

        Bukkit.getServer().getPluginManager().callEvent(event);

        if (player != null) {
            this.plugin.getMessageCache().sendMessage(player, "messages.profession-leveled-up", new PlaceholderReplacer()
                    .addPlaceholder("%level%", String.valueOf(data.getLevel()))
                    .addPlaceholder("%profession%", ProgressionType.normalize(type))
            );
        }

        this.addExp(type, total - max);
    }
}

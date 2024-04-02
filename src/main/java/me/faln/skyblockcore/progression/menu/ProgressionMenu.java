package me.faln.skyblockcore.progression.menu;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.player.PlayerData;
import me.faln.skyblockcore.progression.base.AbstractProgression;
import me.faln.skyblockcore.progression.data.ProgressionData;
import me.faln.skyblockcore.progression.level.ProgressionLevel;
import me.faln.skyblockcore.progression.types.ProgressionType;
import me.faln.skyblockcore.utils.FormatUtils;
import net.abyssdev.abysslib.menu.MenuBuilder;
import net.abyssdev.abysslib.menu.templates.GenericAbyssMenu;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.AbyssMath;
import org.bukkit.entity.Player;

public final class ProgressionMenu extends GenericAbyssMenu<SkyblockCore> {

    private final ProgressionType type;

    public ProgressionMenu(final SkyblockCore plugin, final ProgressionType type) {
        super(plugin, plugin.config("menus").getConfig(), type.name().toLowerCase() + ".");
        this.type = type;
    }

    @Override
    public void open(final Player player) {
        final MenuBuilder builder = this.createBase();
        final AbstractProgression<?> progression = this.plugin.getProgressionRegistry().getRegistry().get(this.type);
        final PlayerData data = this.plugin.getPlayerStorage().get(player.getUniqueId());

        for (final ProgressionLevel level : progression.getLevelRequirement().values()) {
            final ProgressionData progressionData = data.getLevels().get(this.type);
            final boolean unlocked = level.getLevelRequirement() <= progressionData.getLevel();
            final PlaceholderReplacer replacer = new PlaceholderReplacer()
                    .addPlaceholder("%current-exp%", FormatUtils.formatComma(progressionData.getExp()))
                    .addPlaceholder("%max-exp%", FormatUtils.formatComma(AbyssMath.parseEquation(progression.getExpFormula().replace("%level%", String.valueOf(progressionData.getLevel())))))
                    .addPlaceholder("%current-level%", FormatUtils.formatComma(progressionData.getLevel()));

            builder.setItem(level.getMenuSlot(), unlocked ? level.getUnlockedItem().parse(replacer) : level.getLockedItem().parse(replacer));
        }

        player.openInventory(builder.build());
    }
}

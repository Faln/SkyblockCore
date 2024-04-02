package me.faln.skyblockcore.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.faln.skyblockcore.progression.types.ProgressionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor @Getter @Setter
public final class LevelUpEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final ProgressionType type;
    private final int level;

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}

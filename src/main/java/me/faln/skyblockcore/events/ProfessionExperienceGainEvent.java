package me.faln.skyblockcore.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.faln.skyblockcore.progression.types.ProgressionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Simple event for player exp increments in professions
 *
 * @author Faln
 */
@AllArgsConstructor @Getter @Setter @SuppressWarnings("all")
public final class ProfessionExperienceGainEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final ProgressionType type;
    private double exp;
    private boolean cancelled;

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean b) {
        this.cancelled = b;
    }
}

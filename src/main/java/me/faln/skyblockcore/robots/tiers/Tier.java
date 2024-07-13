package me.faln.skyblockcore.robots.tiers;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import net.citizensnpcs.api.npc.NPC;
import org.stormdev.builder.ItemBuilder;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * POJO for rarities of {@code Robot}s
 */
@AllArgsConstructor @Getter
public final class Tier {

    public static Tier of(
            final String name,
            final String display,
            final double speed,
            final double storage,
            final ItemBuilder robotItem,
            final Consumer<NPC> npcConsumer
    ) {
        Objects.requireNonNull(name, "Tier must have a identifier");
        Objects.requireNonNull(display, "Tier must have a display");
        Preconditions.checkArgument(speed > 0.0, "Cannot have a interval <= 0");

        return new Tier(name, display, speed, storage, robotItem, npcConsumer);
    }

    @NonNull
    private final String name;
    @NonNull
    private final String display;
    private final double speed;
    private final double storage;
    private final ItemBuilder robotItem;
    private final Consumer<NPC> npcConsumer;

}

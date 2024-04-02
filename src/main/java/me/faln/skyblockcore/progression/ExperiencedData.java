package me.faln.skyblockcore.progression;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor @Getter
public final class ExperiencedData {

    private final double max;
    private final double min;

    public double get() {
        return ThreadLocalRandom.current().nextDouble(this.min, this.max + 1);
    }
}

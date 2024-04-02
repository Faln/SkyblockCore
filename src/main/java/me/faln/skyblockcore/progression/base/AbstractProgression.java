package me.faln.skyblockcore.progression.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.progression.ExperiencedData;
import me.faln.skyblockcore.progression.Parser;
import me.faln.skyblockcore.progression.level.ProgressionLevel;
import me.faln.skyblockcore.progression.types.ProgressionType;
import me.faln.skyblockcore.yml.YMLConfig;
import org.eclipse.collections.api.factory.Maps;

import java.util.Map;

@AllArgsConstructor @Getter
public abstract class AbstractProgression<T> {

    protected final Map<T, ExperiencedData> experienceValues = Maps.mutable.empty();
    protected final Map<T, ProgressionLevel> levelRequirement = Maps.mutable.empty();
    protected final ProgressionType type;
    protected final SkyblockCore plugin;
    protected final String expFormula;
    protected final boolean enabled;

    protected AbstractProgression(final SkyblockCore plugin, final YMLConfig config, final Parser<T> parser) {
        this.plugin = plugin;
        this.type = ProgressionType.valueOf(config.getFile().getName().toUpperCase());
        this.expFormula = config.string("exp-formula");
        this.enabled = config.getBoolean("enabled");

        for (final String key : config.getSectionKeys("levels")) {
            final T thing = parser.parse(key);
            final ProgressionLevel level = ProgressionLevel.of(config, "levels." + key + ".");

            this.levelRequirement.put(thing, level);
        }

        for (final String s : config.list("experience")) {
            final String[] split = s.split(";");

            if (split.length < 3) {
                continue;
            }

            this.experienceValues.put(parser.parse(split[0]), new ExperiencedData(Double.parseDouble(split[1]), Double.parseDouble(split[2])));
        }
    }
}
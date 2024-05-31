package me.faln.skyblockcore.progression.milestones;

import lombok.AllArgsConstructor;
import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.utils.EclipseRegistry;
import me.faln.skyblockcore.yml.YMLConfig;

@AllArgsConstructor
public final class MilestonesRegistry extends EclipseRegistry<Integer, String> {

    private final SkyblockCore plugin;

    public void load() {
        this.getRegistry().clear();

        final YMLConfig config = this.plugin.config("settings");

        for (final String s : config.list("milestones")) {
            final String[] split = s.split(";");

            this.register(Integer.parseInt(split[0]), split[1]);
        }

        this.plugin.getLogger().info("Loaded " + this.getRegistry().size() + " Milestones");
    }
}

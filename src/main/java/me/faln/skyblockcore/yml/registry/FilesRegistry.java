package me.faln.skyblockcore.yml.registry;

import me.faln.skyblockcore.yml.YMLConfig;
import me.faln.skyblockcore.yml.factory.YMLConfigFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.collections.impl.factory.Maps;
import org.stormdev.abstracts.Registry;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

public final class FilesRegistry<P extends JavaPlugin> implements Registry<String, YMLConfig> {

    private final Map<String, YMLConfig> configs = Maps.mutable.empty();

    private final P plugin;
    private final YMLConfigFactory configFactory;

    public FilesRegistry(final P plugin) {
        this.plugin = plugin;
        this.configFactory = new YMLConfigFactory(plugin);
    }

    public YMLConfig getConfig(final String fileName) {
        if (this.configs.containsKey(fileName)) {
            return this.configs.get(fileName);
        }

        this.createFile(this.plugin.getDataFolder(), fileName);
        return this.configs.get(fileName);
    }

    public void createFile(final File folder, final String... files) {
        Arrays.stream(files).forEach(fileName -> {
            this.configs.put(fileName, this.configFactory.createConfig(folder, fileName));
        });
    }
    @Override
    public Map<String, YMLConfig> getRegistry() {
        return this.configs;
    }
}

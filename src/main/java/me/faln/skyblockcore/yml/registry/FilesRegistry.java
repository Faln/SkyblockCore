package me.faln.skyblockcore.yml.registry;

import me.faln.skyblockcore.yml.YMLConfig;
import me.faln.skyblockcore.yml.factory.YMLConfigFactory;
import net.abyssdev.abysslib.patterns.registry.Registry;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.collections.api.factory.Maps;

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
        final String folderName = folder.getAbsolutePath().replaceAll(this.plugin.getDataFolder().getAbsolutePath() + File.separator, "");
        Arrays.stream(files)
                .forEach(fileName -> this.configs.put(fileName, this.configFactory.createConfig(folder, fileName, folderName)));
    }
    @Override
    public Map<String, YMLConfig> getRegistry() {
        return this.configs;
    }
}

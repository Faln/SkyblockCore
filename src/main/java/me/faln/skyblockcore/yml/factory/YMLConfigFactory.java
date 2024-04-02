package me.faln.skyblockcore.yml.factory;

import me.faln.skyblockcore.yml.YMLConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

@SuppressWarnings("all")
public final class YMLConfigFactory {

    private final JavaPlugin main;

    public YMLConfigFactory(final JavaPlugin main) {
        this.main = main;
    }

    public YMLConfig createConfig(final File folder, final String name) {

        if (!folder.exists()) {
            folder.mkdirs();
        }

        final File ymlFile = new File(folder, name + ".yml");

        if (!ymlFile.exists()) {
            try {
                this.main.saveResource(ymlFile.getName(), false);

                ymlFile.createNewFile();
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }

        final FileConfiguration config = YamlConfiguration.loadConfiguration(ymlFile);

        return new YMLConfig(ymlFile, config);
    }

    public YMLConfig createConfig(final File folder, final String name, final String path) {

        if (!folder.exists()) {
            folder.mkdirs();
        }

        final File ymlFile = new File(folder, name + ".yml");

        if (!ymlFile.exists()) {
            try {
                this.main.saveResource(path + File.separator + ymlFile.getName(), false);
                ymlFile.createNewFile();
            } catch (final IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }

        final FileConfiguration config = YamlConfiguration.loadConfiguration(ymlFile);

        return new YMLConfig(ymlFile, config);
    }

    public YMLConfig createOptionalConfig(final File folder, final String name) {

        if (!folder.exists()) {
            folder.mkdirs();
        }

        final File ymlFile = new File(folder, name + ".yml");
        return new YMLConfig(ymlFile, YamlConfiguration.loadConfiguration(ymlFile));
    }
}

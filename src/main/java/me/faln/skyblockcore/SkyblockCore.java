package me.faln.skyblockcore;

import lombok.Getter;
import me.faln.skyblockcore.player.PlayerData;
import me.faln.skyblockcore.player.storage.PlayerJsonStorage;
import me.faln.skyblockcore.progression.commands.ProgressionCommands;
import me.faln.skyblockcore.progression.registry.ProgressionRegistry;
import me.faln.skyblockcore.utils.MessageCache;
import me.faln.skyblockcore.yml.YMLConfig;
import me.faln.skyblockcore.yml.registry.FilesRegistry;
import net.abyssdev.abysslib.plugin.AbyssPlugin;
import net.abyssdev.abysslib.storage.common.CommonStorageImpl;

import java.util.UUID;

@Getter
public final class SkyblockCore extends AbyssPlugin {

    private final FilesRegistry<SkyblockCore> filesRegistry = new FilesRegistry<>(this);

    private final YMLConfig settingsConfig = this.config("settings");
    private final MessageCache messageCache = new MessageCache(this.config("lang"));

    private final ProgressionRegistry progressionRegistry = new ProgressionRegistry(this);

    private final CommonStorageImpl<UUID, PlayerData> playerStorage = new CommonStorageImpl<>(new PlayerJsonStorage(this));

    private final ProgressionCommands progressionCommands = new ProgressionCommands(this);

    @Override
    public void onEnable() {
        this.messageCache.load();
        this.progressionRegistry.register();
        this.progressionCommands.register();
    }

    @Override
    public void onDisable() {
        this.progressionCommands.unregister();
    }

    public YMLConfig config(final String fileName) {
        if (this.filesRegistry.containsKey(fileName)) {
            return this.filesRegistry.getRegistry().get(fileName);
        }

        this.filesRegistry.createFile(this.getDataFolder(), fileName);
        return this.filesRegistry.getRegistry().get(fileName);
    }
}

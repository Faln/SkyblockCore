package me.faln.skyblockcore.player.storage;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.player.PlayerData;
import net.abyssdev.abysslib.patterns.registry.impl.EclipseRegistry;
import net.abyssdev.abysslib.storage.json.JsonStorage;
import net.abyssdev.abysslib.utils.file.Files;

import java.util.UUID;

public final class PlayerJsonStorage extends JsonStorage<UUID, PlayerData> {

    private final SkyblockCore plugin;

    public PlayerJsonStorage(final SkyblockCore plugin) {
        super(Files.file("player-data.json", plugin), PlayerData.class, new EclipseRegistry<>());
        this.plugin = plugin;
    }

    @Override
    public PlayerData constructValue(final UUID id) {
        return new PlayerData(this.plugin, id);
    }
}

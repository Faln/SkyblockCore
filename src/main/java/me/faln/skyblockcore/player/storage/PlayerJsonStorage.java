package me.faln.skyblockcore.player.storage;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.player.PlayerData;
import org.stormdev.files.Files;
import org.stormdev.storage.json.JsonStorage;

import java.util.UUID;

/**
 * A JSON storage implementation of {@PlayerData} class
 *
 * @author Faln
 */
public final class PlayerJsonStorage extends JsonStorage<UUID, PlayerData> {

    private final SkyblockCore plugin;

    public PlayerJsonStorage(final SkyblockCore plugin) {
        super(Files.file("player-data.json", plugin), PlayerData.class);
        this.plugin = plugin;
    }

    @Override
    public PlayerData constructValue(final UUID id) {
        return new PlayerData(this.plugin, id);
    }
}

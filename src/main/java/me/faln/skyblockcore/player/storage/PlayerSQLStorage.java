package me.faln.skyblockcore.player.storage;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.player.PlayerData;
import net.abyssdev.abysslib.storage.sql.SQLStorage;

import java.util.UUID;

public final class PlayerSQLStorage extends SQLStorage<UUID, PlayerData> {

    private final SkyblockCore plugin;

    public PlayerSQLStorage(final SkyblockCore plugin) {
        super(UUID.class, PlayerData.class, "core-player", plugin.getSettingsConfig().getCredentials());
        this.plugin = plugin;
    }

    @Override
    public PlayerData constructValue(final UUID id) {
        return new PlayerData(this.plugin, id);
    }
}

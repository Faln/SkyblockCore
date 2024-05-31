package me.faln.skyblockcore.player.storage;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.player.PlayerData;
import org.stormdev.storage.sql.SQLStorage;

import java.util.UUID;

/**
 * A SQL storage implementation of {@PlayerData} class
 *
 * @author Faln
 */
public final class PlayerSQLStorage extends SQLStorage<UUID, PlayerData> {

    private final SkyblockCore plugin;

    public PlayerSQLStorage(final SkyblockCore plugin) {
        super(UUID.class, PlayerData.class, "skyblockcore_player", plugin.getSettingsConfig().getCredentials());
        this.plugin = plugin;
    }

    @Override
    public PlayerData constructValue(final UUID id) {
        return new PlayerData(this.plugin, id);
    }
}

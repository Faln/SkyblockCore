package me.faln.skyblockcore.tasks;

import me.faln.skyblockcore.SkyblockCore;
import org.stormdev.abstracts.CommonTask;

/**
 * Async auto-save task for {@code @PlayerData} storage.
 *
 * @author Faln
 */
public final class PlayerDataSaveTask extends CommonTask<SkyblockCore> {

    public PlayerDataSaveTask(final SkyblockCore plugin) {
        super(plugin, 20 * 60 * 5, true);
    }

    @Override
    public void run() {
        this.plugin.getPlayerStorage().write();
    }
}

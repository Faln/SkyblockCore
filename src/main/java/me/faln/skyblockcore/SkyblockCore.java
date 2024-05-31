package me.faln.skyblockcore;

import lombok.Getter;
import me.faln.skyblockcore.commands.SkyblockCoreCommand;
import me.faln.skyblockcore.commands.subcommands.ReloadSubCommand;
import me.faln.skyblockcore.dimensions.commands.DimensionCommand;
import me.faln.skyblockcore.dimensions.cooldown.DimensionCooldown;
import me.faln.skyblockcore.dimensions.registry.DimensionRegistry;
import me.faln.skyblockcore.dimensions.task.DimensionScanTask;
import me.faln.skyblockcore.player.PlayerData;
import me.faln.skyblockcore.player.storage.PlayerJsonStorage;
import me.faln.skyblockcore.profilecompass.commands.CompassCommand;
import me.faln.skyblockcore.profilecompass.listener.CompassInteractListener;
import me.faln.skyblockcore.profilecompass.listener.CompassJoinListener;
import me.faln.skyblockcore.progression.commands.ProgressionCommands;
import me.faln.skyblockcore.progression.commands.subcommands.GiveEXPSubCommand;
import me.faln.skyblockcore.progression.commands.subcommands.RemoveEXPSubCommand;
import me.faln.skyblockcore.progression.commands.subcommands.SetEXPSubCommand;
import me.faln.skyblockcore.progression.commands.subcommands.SetLevelSubCommand;
import me.faln.skyblockcore.progression.listeners.ProgressionLevelUpListener;
import me.faln.skyblockcore.progression.milestones.MilestonesRegistry;
import me.faln.skyblockcore.progression.registry.ProgressionRegistry;
import me.faln.skyblockcore.tasks.PlayerDataSaveTask;
import me.faln.skyblockcore.utils.MessageCache;
import me.faln.skyblockcore.yml.YMLConfig;
import me.faln.skyblockcore.yml.registry.FilesRegistry;
import org.bukkit.Location;
import org.stormdev.CommonPlugin;
import org.stormdev.builder.ItemBuilder;
import org.stormdev.storage.common.CommonStorageImpl;

import java.util.UUID;

@Getter
public final class SkyblockCore extends CommonPlugin<SkyblockCore> {

    private final FilesRegistry<SkyblockCore> filesRegistry = new FilesRegistry<>(this);

    private final YMLConfig settingsConfig = this.config("settings");
    private final MessageCache messageCache = new MessageCache(this.config("lang"));

    private final ProgressionRegistry progressionRegistry = new ProgressionRegistry(this);
    private final MilestonesRegistry milestonesRegistry = new MilestonesRegistry(this);
    private final DimensionRegistry dimensionRegistry = new DimensionRegistry(this);
    private final DimensionCooldown dimensionCooldown = new DimensionCooldown(this);

    private final CommonStorageImpl<UUID, PlayerData> playerStorage = new CommonStorageImpl<>(new PlayerJsonStorage(this));

    private final ProgressionCommands progressionCommands = new ProgressionCommands(this);
    private final SkyblockCoreCommand skyblockCoreCommand = new SkyblockCoreCommand(this);
    private final CompassCommand compassCommand = new CompassCommand(this);
    private final DimensionCommand dimensionCommand = new DimensionCommand(this);

    private PlayerDataSaveTask saveTask;
    private DimensionScanTask scanTask;

    private Location spawnLocation;
    private String expFormula;
    private ItemBuilder compassItem;

    @Override
    public void onEnable() {
        this.progressionCommands.register();
        this.progressionCommands.register(
                new GiveEXPSubCommand(this),
                new RemoveEXPSubCommand(this),
                new SetEXPSubCommand(this),
                new SetLevelSubCommand(this)
        );
        this.skyblockCoreCommand.register();
        this.skyblockCoreCommand.register(new ReloadSubCommand(this));
        this.compassCommand.register();
        this.dimensionCommand.register();

        this.load();

        this.saveTask = new PlayerDataSaveTask(this);
        this.scanTask = new DimensionScanTask(this);

        new ProgressionLevelUpListener(this);
        new CompassInteractListener(this);
        new CompassJoinListener(this);
    }

    public void load() {
        final YMLConfig config = this.config("settings");

        this.spawnLocation = config.getLocation("spawn-location");
        this.expFormula = config.string("exp-formula");
        this.compassItem = this.config("compass").getItemBuilder("compass-item");

        this.messageCache.load();
        this.milestonesRegistry.load();
        this.dimensionRegistry.load();
        this.progressionRegistry.load();
        this.dimensionCooldown.load();
    }

    @Override
    public void onDisable() {
        this.progressionCommands.unregister();
        this.skyblockCoreCommand.unregister();
        this.compassCommand.unregister();

        if (!this.saveTask.isCancelled()) {
            this.saveTask.cancel();
        }

        if (!this.scanTask.isCancelled()) {
            this.scanTask.cancel();
        }
    }

    public YMLConfig config(final String fileName) {
        return this.filesRegistry.getConfig(fileName);
    }
}

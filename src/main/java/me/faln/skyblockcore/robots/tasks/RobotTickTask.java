package me.faln.skyblockcore.robots.tasks;

import me.faln.skyblockcore.SkyblockCore;
import org.stormdev.abstracts.CommonTask;

public final class RobotTickTask extends CommonTask<SkyblockCore> {

    public RobotTickTask(final SkyblockCore plugin) {
        super(plugin, 20);
        this.run();
    }

    @Override
    public void run() {
        this.plugin.getRobotManager().tickRobots();
    }
}

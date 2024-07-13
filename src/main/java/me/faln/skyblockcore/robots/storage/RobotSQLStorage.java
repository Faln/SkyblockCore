package me.faln.skyblockcore.robots.storage;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.robots.impl.Robot;
import org.bukkit.Location;
import org.stormdev.storage.sql.SQLStorage;

public final class RobotSQLStorage extends SQLStorage<Location, Robot> {

    public RobotSQLStorage(final SkyblockCore plugin) {
        super(Location.class, Robot.class, "skyblockcore-robots", plugin.config("robots").getCredentials());
    }

}

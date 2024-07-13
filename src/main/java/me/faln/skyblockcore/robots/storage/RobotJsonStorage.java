package me.faln.skyblockcore.robots.storage;

import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.robots.impl.Robot;
import me.faln.skyblockcore.utils.JsonStorage;
import org.bukkit.Location;
import org.stormdev.files.Files;


public final class RobotJsonStorage extends JsonStorage<Location, Robot> {

    public RobotJsonStorage(final SkyblockCore plugin) {
        super(Files.file("robot-data.json", plugin), Robot.class);
    }

}

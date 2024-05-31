package me.faln.skyblockcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class LocationSerializer {

    public static Location deserialize(final String location) {
        final String[] split = location.split(";");
        return new Location(
                Bukkit.getWorld(split[0]),
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3])
        );
    }
}

package me.faln.skyblockcore.utils;

import net.abyssdev.abysslib.patterns.registry.Registry;
import org.eclipse.collections.api.factory.Maps;

import java.util.Map;

public abstract class EclipseRegistry<K, V> implements Registry<K, V> {

    private final Map<K, V> registry = Maps.mutable.empty();

    @Override
    public Map<K, V> getRegistry() {
        return this.registry;
    }
}

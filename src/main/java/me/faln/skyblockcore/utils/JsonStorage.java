package me.faln.skyblockcore.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import me.faln.skyblockcore.progression.types.ProgressionType;
import org.stormdev.abstracts.Registry;
import org.stormdev.storage.ConstructableValue;
import org.stormdev.storage.Storage;
import org.stormdev.storage.id.util.IdUtils;
import org.stormdev.storage.json.registry.JsonCacheRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Collection;

public abstract class JsonStorage<K, V> implements Storage<K, V>, ConstructableValue<K, V> {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ProgressionType.class, new ProgressionType.Serializer())
            .setPrettyPrinting()
            .create();

    private final Registry<K, V> cache;
    private final File file;
    private final Class<V> type;

    @SneakyThrows
    public JsonStorage(final File file, final Class<V> type, final Registry<K, V> registry) {
        this.file = file;
        this.type = type;
        this.cache = registry;

        final V[] values = (V[]) GSON.fromJson(new FileReader(this.file), Array.newInstance(type, 0).getClass());

        for (final V value : values) {
            this.cache.register((K) IdUtils.getId(type, value), value);
        }
    }

    public JsonStorage(final File file, final Class<V> type) {
        this(file, type, new JsonCacheRegistry<>());
    }


    @Override
    public Registry<K, V> cache() {
        return this.cache;
    }

    @Override
    public V get(final K key) {
        return this.cache.get(key).orElseGet(() -> {
            final V value = this.constructValue(key);

            if (value == null) {
                return null;
            }

            this.save(value);
            return value;
        });
    }

    @Override
    public void save(final V value) {
        this.cache.register((K) IdUtils.getId(this.type, value), value);
    }

    @Override
    public void saveAll(final Collection<V> values) {
        for (final V value : values) {
            this.cache.register((K) IdUtils.getId(this.type, value), value);
        }
    }

    @Override
    public void remove(final K key) {
        this.cache.unregister(key);
    }

    @SneakyThrows
    public void write() {
        //Clear the file
        final Writer writer = new FileWriter(this.file);

        GSON.toJson(this.cache.values(), writer);
        writer.close();
    }

    @Override
    public boolean contains(final K key) {
        return this.cache.containsKey(key);
    }

    @Override
    public Collection<K> allKeys() {
        return this.cache.keySet();
    }

    @Override
    public Collection<V> allValues() {
        return this.cache.values();
    }
}
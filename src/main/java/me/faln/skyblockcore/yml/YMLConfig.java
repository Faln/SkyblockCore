package me.faln.skyblockcore.yml;

import gg.optimalgames.hologrambridge.HologramAPI;
import gg.optimalgames.hologrambridge.hologram.Hologram;
import me.faln.skyblockcore.utils.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.list.mutable.primitive.DoubleArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;
import org.jetbrains.annotations.NotNull;
import org.stormdev.builder.ItemBuilder;
import org.stormdev.chat.PlaceholderReplacer;
import org.stormdev.files.random.RandomConfigDouble;
import org.stormdev.files.random.RandomConfigInt;
import org.stormdev.files.random.RandomConfigLong;
import org.stormdev.menus.v2.item.*;
import org.stormdev.storage.credentials.Credentials;
import org.stormdev.utils.Color;
import org.stormdev.utils.Region;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

public final class YMLConfig {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private final File file;
    private FileConfiguration config;

    public YMLConfig(final File file, final FileConfiguration config) {
        this.file = file;
        this.config = config;
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException ex) {
            throw new UncheckedIOException("Could not save YMLConfig", ex);
        }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
        this.save();
    }

    public Credentials getCredentials() {
        return Credentials.from(this.config);
    }
    public File getFile() {
        return file;
    }

    public ItemBuilder getItemBuilder(final String path) {
        return new ItemBuilder(this.config, path);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public String string(final String path) {
        return this.config.getString(path);
    }

    public String string(final String path, final String def) {
        return this.config.getString(path, def);
    }

    public int getInt(final String path) {
        return this.parseInt(path, 0);
    }

    public int parseInt(final String path, int def) {
        return this.config.getInt(path, def);
    }

    public long getLong(final String path, long def) {
        return this.config.getLong(path, def);
    }

    public double getDouble(final String path) {
        return this.config.getDouble(path);
    }

    public double getDouble(final String path, double def) {
        return this.config.getDouble(path, def);
    }

    public List<String> list(final String path) {
        return this.config.getStringList(path);
    }

    public char getChar(final String path) {
        return this.string(path, null).charAt(0);
    }

    public boolean getBoolean(final String path) {
        return this.config.getBoolean(path);
    }

    public boolean getBoolean(final String path, final boolean def) {
        return this.config.getBoolean(path, def);
    }

    public ConfigurationSection section(final String path) {
        return this.config.getConfigurationSection(path);
    }

    public List<Integer> intList(final String path) {
        return this.config.getIntegerList(path);
    }

    public Material material(final String path) {
        return Material.getMaterial(this.config.getString(path, "AIR"));
    }

    public String coloredString(final String path) {
        return Color.parse(this.string(path));
    }

    public List<String> coloredList(final String path) {
        return Color.parse(this.list(path));
    }

    public ItemStack getItemStack(@NotNull final String path) {
        return new ItemBuilder(this.config, path).parse();
    }

    public ItemStack getItemStack(@NotNull final String path, final PlaceholderReplacer replacer) {
        return new ItemBuilder(this.config, path).parse(replacer);
    }

    public MultiSlotMenuItemBuilder getMultiSlotMenuItemBuilder(@NotNull final String path) {
        return new MultiSlotMenuItemBuilder(this.getItemBuilder(path), this.getPrimitiveIntList(path + ".slot"));
    }

    public MultiSlotMenuItemStack getMultiSlotMenuItemStack(@NotNull final String path) {
        return new MultiSlotMenuItemStack(this.getItemStack(path), this.getPrimitiveIntList(path + ".slot"));
    }

    public MultiSlotMenuItemStack getMultiSlotMenuItemStack(@NotNull final String path, final PlaceholderReplacer replacer) {
        return new MultiSlotMenuItemStack(this.getItemStack(path, replacer), this.getPrimitiveIntList(path + ".slot"));
    }

    public MenuItemBuilder getMenuItemBuilder(@NotNull final String path) {
        return new MenuItemBuilder(this.getItemBuilder(path), this.getInt(path + ".slot"));
    }

    public MenuItemStack getMenuItemStack(@NotNull final String path) {
        return new MenuItemStack(this.getItemStack(path), this.getInt(path + ".slot"));
    }

    public MenuItemStack getMenuItemStack(@NotNull final String path, final PlaceholderReplacer replacer) {
        return new MenuItemStack(this.getItemStack(path, replacer), this.getInt(path + ".slot"));
    }

    public TempItemStack getTempItemStack(@NotNull final String path) {
        return new TempItemStack(this.getItemStack(path), this.getInt(path + ".duration"));
    }

    public TempItemStack getTempItemStack(@NotNull final String path, final PlaceholderReplacer replacer) {
        return new TempItemStack(this.getItemStack(path, replacer), this.getInt(path + ".duration"));
    }

    public TempItemBuilder getTempItemBuilder(@NotNull final String path) {
        return new TempItemBuilder(this.getItemBuilder(path), this.getInt(path + ".duration"));
    }

    public World getWorld(@NotNull final String path) {
        return Bukkit.getWorld(this.string(path));
    }

    public Region getRegion(@NotNull final String path) {
        return new Region(
                this.getWorld(path + ".world"),
                this.getInt(path + ".min-x"),
                this.getInt(path + ".min-y"),
                this.getInt(path + ".min-z"),
                this.getInt(path + ".max-x"),
                this.getInt(path + ".max-y"),
                this.getInt(path + ".max-z"));
    }

    public RandomConfigInt getRandomConfigInt(@NotNull final String path) {
        final String[] data = this.string(path).split(";");
        return new RandomConfigInt(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
    }

    public RandomConfigDouble getRandomConfigDouble(@NotNull final String path) {
        final String[] data = this.string(path).split(";");
        return new RandomConfigDouble(Double.parseDouble(data[0]), Double.parseDouble(data[1]));
    }

    public RandomConfigLong getRandomConfigLong(@NotNull final String path) {
        final String[] data = this.string(path).split(";");
        return new RandomConfigLong(Long.parseLong(data[0]), Long.parseLong(data[1]));
    }

    public Date getDate(@NotNull final String path) throws ParseException {
        return DATE_FORMAT.parse(this.string(path));
    }

    public Location getLocation(@NotNull final String path) {
        return LocationSerializer.deserialize(this.string(path));
    }

    public Hologram getHologram(@NotNull final Location location, @NotNull final String path) {
        final Hologram hologram = HologramAPI.createHologram(location);

        for (final String line : this.coloredList(path + ".lines")) {
            hologram.appendTextLine(line);
        }

        hologram.teleport(location.add(0, this.getDouble(path + ".y-increase"), 0));
        return hologram;
    }

    public Hologram getHologram(@NotNull final String path) {
        final Location location = this.getLocation(path + ".location");
        final Hologram hologram = HologramAPI.createHologram(location);

        for (final String line : this.coloredList(path + ".lines")) {
            hologram.appendTextLine(line);
        }

        hologram.teleport(location.add(0, this.getDouble(path + ".y-increase"), 0));
        return hologram;
    }

    public Material getMaterial(@NotNull final String path) {
        return Material.matchMaterial(this.string(path).toUpperCase());
    }

    public EntityType getEntityType(@NotNull final String path) {
        return EntityType.valueOf(this.string(path).toUpperCase());
    }

    public PotionEffectType getPotionEffectType(@NotNull final String path) {
        return PotionEffectType.getByName(this.string(path).toUpperCase());
    }

    public Enchantment getEnchantment(@NotNull final String path) {
        return Enchantment.getByName(this.string(path).toUpperCase());
    }

    public IntArrayList getPrimitiveIntList(@NotNull final String path) {
        final List<Integer> wrapped = this.intList(path);
        final IntArrayList primitive = new IntArrayList(wrapped.size());

        for (final int i : wrapped) {
            primitive.add(i);
        }

        return primitive;
    }

    public DoubleArrayList getPrimitiveDoubleList(@NotNull final String path) {
        final List<Double> wrapped = this.config.getDoubleList(path);
        final DoubleArrayList primitive = new DoubleArrayList(wrapped.size());

        for (final double d : wrapped) {
            primitive.add(d);
        }

        return primitive;
    }

    public LongArrayList getPrimitiveLongList(@NotNull final String path) {
        final List<Long> wrapped = this.config.getLongList(path);
        final LongArrayList primitive = new LongArrayList(wrapped.size());

        for (final long l : wrapped) {
            primitive.add(l);
        }

        return primitive;
    }

    public Set<String> getStringSet(@NotNull final String path) {
        return Sets.mutable.ofAll(this.list(path));
    }

    public Set<String> getSectionKeys(@NotNull final String path) {
        return this.section(path).getKeys(false);
    }
}

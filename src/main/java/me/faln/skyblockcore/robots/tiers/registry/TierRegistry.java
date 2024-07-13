package me.faln.skyblockcore.robots.tiers.registry;

import lombok.AllArgsConstructor;
import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.robots.tiers.Tier;
import me.faln.skyblockcore.utils.EclipseRegistry;
import me.faln.skyblockcore.yml.YMLConfig;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

@AllArgsConstructor
public final class TierRegistry extends EclipseRegistry<String, Tier> {

    private final SkyblockCore plugin;

    public void load() {
        this.getRegistry().clear();

        final YMLConfig config = this.plugin.config("robots");

        for (final String key : config.getSectionKeys("tiers")) {
            this.register(key, Tier.of(
                    key,
                    config.coloredString("tiers." + key + ".display"),
                    config.getDouble("tiers." + key + ".speed"),
                    config.getDouble("tiers." + key + ".storage"),
                    config.getItemBuilder("tiers." + key + ".item"),
                    this.readEquipment(config, key))
            );
        }
    }

    private Consumer<NPC> readEquipment(final YMLConfig config, final String path) {
        final Map<Equipment.EquipmentSlot, ItemStack> equipment = new EnumMap<>(Equipment.EquipmentSlot.class);

        for (final String s : config.getSectionKeys("tiers." + path + ".equipment")) {
            final ItemStack item = config.getItemStack("tiers." + path + ".equipment." + s);
            final Equipment.EquipmentSlot slot = Equipment.EquipmentSlot.valueOf(s.toUpperCase());

            equipment.put(slot, item);
        }

        return npc -> {
            final Equipment e = npc.getOrAddTrait(Equipment.class);

            e.set(Equipment.EquipmentSlot.HELMET, equipment.get(Equipment.EquipmentSlot.HELMET));
            e.set(Equipment.EquipmentSlot.CHESTPLATE, equipment.get(Equipment.EquipmentSlot.CHESTPLATE));
            e.set(Equipment.EquipmentSlot.LEGGINGS, equipment.get(Equipment.EquipmentSlot.LEGGINGS));
            e.set(Equipment.EquipmentSlot.BOOTS, equipment.get(Equipment.EquipmentSlot.BOOTS));
        };
    }
}

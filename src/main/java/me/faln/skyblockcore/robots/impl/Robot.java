package me.faln.skyblockcore.robots.impl;

import lombok.*;
import me.faln.skyblockcore.robots.type.RobotType;
import me.faln.skyblockcore.robots.upgrade.RobotUpgradeType;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public final class Robot {

    private final Map<RobotUpgradeType, Integer> upgrades = new EnumMap<>(RobotUpgradeType.class);
    private final Map<Material, Integer> storage = new EnumMap<>(Material.class);

    private final UUID npcId;
    private final UUID owner;
    private final Location location;
    private final String tier;

    private double itemCount = 0;
    private RobotType type = RobotType.NONE;
    private long nextTick = System.currentTimeMillis();

    public int getUpgrade(final RobotUpgradeType upgrade) {
        return this.upgrades.getOrDefault(upgrade, 1);
    }

    public void upgrade(final RobotUpgradeType upgrade) {
        this.upgrades.put(upgrade, this.getUpgrade(upgrade) + 1);
    }

    public Location getLocation() {
        return this.location.clone();
    }

    public double getSellPrice() {
        double sum = 0;

        for (var entry : this.storage.entrySet()) {
            sum += ShopGuiPlusApi.getItemStackPriceSell(new ItemStack(entry.getKey())) * entry.getValue();
        }

        return sum;
    }

    public void clearContents() {
        this.storage.clear();
        this.itemCount = 0;
    }

}

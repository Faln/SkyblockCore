package me.faln.skyblockcore.robots;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.faln.skyblockcore.SkyblockCore;
import me.faln.skyblockcore.robots.impl.Robot;
import me.faln.skyblockcore.robots.tiers.Tier;
import me.faln.skyblockcore.robots.type.RobotType;
import me.faln.skyblockcore.robots.upgrade.RobotUpgradeType;
import me.faln.skyblockcore.robots.upgrade.impl.SpeedRobotUprade;
import me.faln.skyblockcore.robots.upgrade.impl.StorageRobotUpgrade;
import me.faln.skyblockcore.robots.utils.RobotUtils;
import me.faln.skyblockcore.utils.FormatUtils;
import me.faln.skyblockcore.yml.YMLConfig;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.chat.PlaceholderReplacer;
import org.stormdev.menus.v2.item.MenuItemBuilder;
import org.stormdev.utils.NBTEditor;

@RequiredArgsConstructor
@Getter
public class RobotManager {

    private static final String TIER_PLACEHOLDER = "%tier%";
    private static final String SPEED_PLACEHOLDER = "%speed%";
    private static final String STORAGE_PLACEHOLDER = "%storage%";
    private static final String ROBOT_NAME = "%tier% %type% Robot";

    private final SkyblockCore plugin;

    private MenuItemBuilder typeChangeMenuItem, pickupMenuItem, upgradesMenuItem, sellMenuItem;

    public void load() {
        final YMLConfig config = this.plugin.config("menus.");

        this.typeChangeMenuItem = config.getMenuItemBuilder("robot.type-change");
        this.pickupMenuItem = config.getMenuItemBuilder("robot.pickup");
        this.upgradesMenuItem = config.getMenuItemBuilder("robot.upgrades");
        this.sellMenuItem = config.getMenuItemBuilder("robot.sell");
    }

    public ItemStack getDefaultItemstack(final Tier tier) {
        ItemStack item = tier.getRobotItem().parse(new PlaceholderReplacer()
                .addPlaceholder(TIER_PLACEHOLDER, tier.getDisplay())
                .addPlaceholder(SPEED_PLACEHOLDER, String.valueOf(tier.getSpeed()))
                .addPlaceholder(STORAGE_PLACEHOLDER, FormatUtils.formatComma(tier.getStorage()))
        );

        item = NBTEditor.set(item, true, RobotUtils.ROBOT_NBT);
        item = NBTEditor.set(item, tier.getName(), RobotUtils.ROBOT_NBT);
        item = NBTEditor.set(item, tier.getSpeed(), RobotUtils.ROBOT_SPEED);
        item = NBTEditor.set(item, tier.getStorage(), RobotUtils.ROBOT_STORAGE);

        return item;
    }

    public ItemStack toItem(final Robot robot) {
        val tier = this.plugin.getTierRegistry().getRegistry().get(robot.getTier());
        val speedUpgrade = (SpeedRobotUprade) this.plugin.getRobotUpgradeRegistry().getRegistry().get(RobotUpgradeType.SPEED);
        val storageUpgrade = (StorageRobotUpgrade) this.plugin.getRobotUpgradeRegistry().getRegistry().get(RobotUpgradeType.STORAGE);
        val speed = speedUpgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.SPEED));
        val storage = storageUpgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.STORAGE));

        ItemStack item = tier.getRobotItem().parse(new PlaceholderReplacer()
                .addPlaceholder(TIER_PLACEHOLDER, robot.getTier())
                .addPlaceholder(SPEED_PLACEHOLDER, String.valueOf(speed.getValue()))
                .addPlaceholder(STORAGE_PLACEHOLDER,FormatUtils.formatComma(storage.getValue()))
        );

        item = NBTEditor.set(item, true, RobotUtils.ROBOT_NBT);
        item = NBTEditor.set(item, tier, RobotUtils.ROBOT_TIER);
        item = NBTEditor.set(item, speed, RobotUtils.ROBOT_SPEED);
        item = NBTEditor.set(item, storage, RobotUtils.ROBOT_STORAGE);

        return item;
    }

    public void initRobot(final Player player, final ItemStack item, final Location location) {
        final Tier tier = this.plugin.getTierRegistry().getRegistry().get(RobotUtils.getTier(item));
        final RobotType type = RobotUtils.getType(item);

        final NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, ROBOT_NAME
                .replace(TIER_PLACEHOLDER, tier.getDisplay()).replace("%type%", type.getFormattedName()),
                location
        );

        npc.setFlyable(true);
        npc.setProtected(true);
        npc.setUseMinecraftAI(false);
        npc.spawn(location);

        final Robot robot = new Robot(npc.getUniqueId(), player.getUniqueId(), location, tier.getName());

        this.plugin.getRobotStorage().save(robot);
    }

    public void setType(final Robot robot, final RobotType type) {
        final NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(robot.getNpcId());
        final Tier tier = this.plugin.getTierRegistry().getRegistry().get(robot.getTier());

        npc.setName(ROBOT_NAME.replace(TIER_PLACEHOLDER, tier.getDisplay()).replace("%type%", type.getFormattedName()));
        robot.setType(type);
    }

    public void dispose(final Robot robot) {
        final NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(robot.getNpcId());

        npc.despawn();
        npc.destroy();

        robot.getStorage().clear();
    }

    public void tickRobots() {
        for (final Robot robot : this.plugin.getRobotStorage().cache().values()) {
            this.tickRobot(robot);
        }
    }

    public void tickRobot(final Robot robot) {
        final RobotType type = robot.getType();

        if (type == RobotType.NONE || System.currentTimeMillis() > robot.getNextTick()) {
            return;
        }

        if (this.isStorageFull(robot)) {
            return;
        }

        val drops = type.getFunction().apply(robot.getLocation());
        val storageUpgrade = (StorageRobotUpgrade) this.plugin.getRobotUpgradeRegistry()
                .getRegistry()
                .get(RobotUpgradeType.STORAGE);
        val max = storageUpgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.STORAGE)).getValue();

        for (val drop : drops.entrySet()) {
            if (robot.getItemCount() >= max) {
                break;
            }

            if (drop.getValue() + robot.getItemCount() > max) {
                break;
            }
        }

        robot.getStorage().putAll(drops);
        robot.setItemCount(robot.getStorage().values().stream().mapToInt(Integer::intValue).sum());

        val upgrade = (SpeedRobotUprade) this.plugin.getRobotUpgradeRegistry()
                .getRegistry()
                .get(RobotUpgradeType.SPEED);
        val speed = upgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.SPEED)).getValue();

        robot.setNextTick((long) (System.currentTimeMillis() + speed * 1000));
    }

    public boolean isStorageFull(final Robot robot) {
        val upgrade = (StorageRobotUpgrade) this.plugin.getRobotUpgradeRegistry()
                .getRegistry()
                .get(RobotUpgradeType.STORAGE);

        return robot.getItemCount() >= upgrade.getLevels().get(robot.getUpgrades().get(RobotUpgradeType.STORAGE)).getValue();
    }
}

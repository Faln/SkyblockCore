package me.faln.skyblockcore.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.faln.skyblockcore.yml.YMLConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Maps;
import org.stormdev.chat.Message;
import org.stormdev.chat.PlaceholderReplacer;

import java.util.Map;

@AllArgsConstructor @Getter
public final class MessageCache {

    private final Map<String, Message> messages = Maps.mutable.empty();
    private final YMLConfig config;

    public void load() {
        this.messages.clear();

        for (final String key : this.config.section("messages").getKeys(false)) {
            this.loadMessage("messages." + key);
        }
    }

    public MessageCache sendMessage(Player player, String path) {
        if (hasMessage(path))
            getMessage(path).send(player);

        return this;
    }

    public MessageCache sendMessage(CommandSender sender, String path) {
        if (hasMessage(path))
            getMessage(path).send(sender);

        return this;
    }

    public MessageCache sendMessage(Player player, PlaceholderReplacer placeholders, String path) {
        if (hasMessage(path))
            getMessage(path).send(player, placeholders);

        return this;
    }

    public MessageCache sendMessage(CommandSender sender, PlaceholderReplacer placeholders, String path) {
        if (hasMessage(path))
            getMessage(path).send(sender, placeholders);

        return this;
    }

    public MessageCache sendMessage(Player player, String path, PlaceholderReplacer placeholders) {
        if (hasMessage(path))
            getMessage(path).send(player, placeholders);

        return this;
    }

    public MessageCache sendMessage(CommandSender sender, String path, PlaceholderReplacer placeholders) {
        if (hasMessage(path))
            getMessage(path).send(sender, placeholders);

        return this;
    }


    public MessageCache loadMessage(String path) {
        this.messages.put(path.toLowerCase(), new Message(this.config.getConfig(), path));
        return this;
    }

    public boolean hasMessage(String path) {
        return this.messages.containsKey(path.toLowerCase());
    }

    public Message getMessage(String path) {
        return this.messages.get(path.toLowerCase());
    }
}

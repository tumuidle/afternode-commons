package cn.afternode.commons.bukkit.message;

import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

public record RegisteredClickCallback(
        UUID id,
        boolean once,
        Consumer<Player> callback
) {
}

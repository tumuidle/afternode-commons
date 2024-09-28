package cn.afternode.commons.bukkit.message;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class CallbackCommand extends Command {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private final Map<UUID, RegisteredClickCallback> registry = new HashMap<>();

    public CallbackCommand() {
        super("callback-" + counter.incrementAndGet());
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player player) {
            try {
                UUID id = UUID.fromString(args[0]);
                if (registry.containsKey(id)) {
                    RegisteredClickCallback reg = this.registry.get(id);
                    reg.callback().accept(player);
                    if (reg.once())
                        this.registry.remove(id);
                }
            } catch (IllegalArgumentException ignored) {}
        }

        return true;
    }

    /**
     * Register callback
     * @param once If the callback can only be triggered once
     * @param callback Callback function
     * @return Registered callback instance
     */
    public RegisteredClickCallback register(boolean once, Consumer<Player> callback) {
        UUID id = UUID.randomUUID();
        RegisteredClickCallback reg = new RegisteredClickCallback(id, once, callback);
        this.registry.put(id, reg);
        return reg;
    }

    /**
     * Register a callback that can only be triggered once
     * @param callback Callback function
     * @return Registered callback instance
     */
    public RegisteredClickCallback register(Consumer<Player> callback) {
        return this.register(true, callback);
    }

    /**
     * Remove a registered callback
     * @param id Callback ID
     */
    public void unregister(@NotNull UUID id) {
        this.registry.remove(id);
    }
}

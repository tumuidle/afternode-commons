package cn.afternode.commons.velocity.message;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;

import java.util.*;
import java.util.function.Predicate;

/**
 * Velocity tab complete builder implementation
 */
public class TabBuilder {
    private ProxyServer server;
    private CommandSource target = null;

    private final List<String> list = new ArrayList<>();

    /**
     * Create with server and specified target
     * @param server Server
     * @param source Target
     */
    public TabBuilder(ProxyServer server, CommandSource source) {
        this.server = server;
        this.target = source;
    }

    /**
     * Create with server
     * @param server server
     */
    public TabBuilder(ProxyServer server) {
        this.server = server;
    }

    /**
     * Create without parameters
     */
    public TabBuilder() {
        this(null, null);
    }

    /**
     * Get current target
     * @return target
     */
    public CommandSource target() {
        return target;
    }

    /**
     * Set current target
     * @param target target
     * @return this
     */
    public TabBuilder target(CommandSource target) {
        this.target = target;
        return this;
    }

    /**
     * Get current server
     * @return server
     */
    public ProxyServer server() {
        return this.server;
    }

    /**
     * Set current server
     * @param server server
     * @return this
     */
    public TabBuilder server(ProxyServer server) {
        this.server = server;
        return this;
    }

    /**
     * Add items
     * @param items items
     * @return this
     */
    public TabBuilder add(String... items) {
        list.addAll(Arrays.asList(items));
        return this;
    }

    /**
     * Add items with prefix
     * @param prefix prefix
     * @param items items
     * @return this
     */
    public TabBuilder add(String prefix, String... items) {
        for (String item : items) {
            if (item.startsWith(prefix)) {
                list.add(item);
            }
        }
        return this;
    }

    /**
     * Add all online players' names
     * @return this
     */
    public TabBuilder players() {
        if (server == null)
            throw new NullPointerException("No proxy server instance provided");
        for (Player p: this.server.getAllPlayers()) {
             this.list.add(p.getUsername());
        }
        return this;
    }

    /**
     * Add all online players in a server
     * @param server server
     * @return this
     */
    public TabBuilder players(ServerConnection server) {
        if (server == null)
            throw new NullPointerException("No proxy server instance provided");
        this.server.getAllPlayers()
                .parallelStream()
                .filter(p -> Objects.equals(p.getCurrentServer().orElse(null), server))
                .forEach(p -> this.list.add(p.getUsername()));
        return this;
    }

    /**
     * Filter all online players' names and add
     * @param prefix prefix
     * @return this
     */
    public TabBuilder players(String prefix) {
        if (server == null)
            throw new NullPointerException("No proxy server instance provided");
        for (Player p : this.server.getAllPlayers()) {
            if (p.getUsername().startsWith(prefix)) {
                this.list.add(p.getUsername());
            }
        }
        return this;
    }

    /**
     * Add all online players' names with prefix and server
     * @param server server
     * @param prefix prefix
     * @return this
     */
    public TabBuilder players(ProxyServer server, String prefix) {
        if (server == null)
            throw new NullPointerException("No proxy server instance provided");
        this.list.addAll(this.server.getAllPlayers()
                        .parallelStream()
                        .filter(p -> Objects.equals(p.getCurrentServer().orElse(null), server) && p.getUsername().startsWith(prefix))
                        .map(Player::getUsername)
                        .toList());
        return this;
    }

    /**
     * Add all online players' names with custom filter
     * @param filter filter
     * @return this
     */
    public TabBuilder players(Predicate<Player> filter) {
        if (server == null)
            throw new NullPointerException("No proxy server instance provided");
        this.list.addAll(this.server.getAllPlayers()
                .stream()
                .filter(filter)
                .map(Player::getUsername)
                .toList());
        return this;
    }

    /**
     * Build to string list (Create new list object)
     * @return result
     */
    public List<String> build() {
        return new ArrayList<>(list);
    }
}

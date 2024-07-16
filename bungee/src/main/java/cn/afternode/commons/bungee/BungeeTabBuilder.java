package cn.afternode.commons.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * TabBuilder implementation for BungeeCord
 */
public class BungeeTabBuilder {
    private final List<String> comp = new ArrayList<>();

    /**
     * Append completions
     * @param prefix Prefix filter
     * @param append Contents
     */
    public BungeeTabBuilder append(String prefix, String... append) {
        for (String a : append) {
            if (a.startsWith(prefix))
                comp.add(a);
        }
        return this;
    }

    /**
     * Append completions
     * @param append Contents
     */
    public BungeeTabBuilder append(String... append) {
        comp.addAll(Arrays.asList(append));
        return this;
    }

    /**
     * Append players in specified server
     * @param prefix Name prefix filter
     * @param server Server
     */
    public BungeeTabBuilder players(String prefix, ServerInfo server) {
        for (ProxiedPlayer player : server.getPlayers()) {
            String name = player.getName();
            if (name.startsWith(prefix))
                comp.add(name);
        }
        return this;
    }

    /**
     * Append players in specified server
     * @param filter Player filter
     * @param server Server
     */
    public BungeeTabBuilder players(Predicate<ProxiedPlayer> filter, ServerInfo server) {
        for (ProxiedPlayer player : server.getPlayers()) {
            if (filter.test(player))
                comp.add(player.getName());
        }
        return this;
    }

    /**
     * Append players in specified server
     * @param server Server
     */
    public BungeeTabBuilder players(ServerInfo server) {
        for (ProxiedPlayer player : server.getPlayers()) {
            comp.add(player.getName());
        }
        return this;
    }

    /**
     * Append players in all servers
     * @param filter Player filter
     */
    public BungeeTabBuilder players(Predicate<ProxiedPlayer> filter) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (filter.test(player))
                comp.add(player.getName());
        }
        return this;
    }

    /**
     * Append players in all servers
     * @param prefix Name prefix filter
     */
    public BungeeTabBuilder players(String prefix) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            String name = player.getName();
            if (name.startsWith(prefix))
                comp.add(name);
        }
        return this;
    }

    /**
     * Append players in all servers
     */
    public BungeeTabBuilder players() {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            comp.add(player.getName());
        }
        return this;
    }

    /**
     * Append server names
     */
    public BungeeTabBuilder servers() {
        for (ServerInfo si: ProxyServer.getInstance().getServers().values())
            comp.add(si.getName());
        return this;
    }

    /**
     * Append server names
     * @param prefix Prefix filter
     */
    public BungeeTabBuilder servers(String prefix) {
        for (ServerInfo si: ProxyServer.getInstance().getServers().values()) {
            String name = si.getName();
            if (name.startsWith(prefix))
                comp.add(name);
        }
        return this;
    }

    /**
     * Append server names
     * @param filter Server filter
     */
    public BungeeTabBuilder servers(Predicate<ServerInfo> filter) {
        for (ServerInfo si: ProxyServer.getInstance().getServers().values()) {
            if (filter.test(si))
                comp.add(si.getName());
        }
        return this;
    }

    /**
     * Build to list
     * @return Result
     */
    public List<String> build() {
        return new ArrayList<>(comp);
    }
}

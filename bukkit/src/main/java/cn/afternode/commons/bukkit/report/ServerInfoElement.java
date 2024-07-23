package cn.afternode.commons.bukkit.report;

import org.bukkit.Server;

/**
 * Server information element with version, name and online-mode state
 */
public class ServerInfoElement implements IPluginReportElement {
    private final Server server;

    public ServerInfoElement(Server server) {
        this.server = server;
    }

    @Override
    public String title() {
        return "Server Info";
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append("Minecraft: ").append(server.getMinecraftVersion()).append("\n");
        sb.append("Bukkit: ").append(server.getBukkitVersion()).append("\n");
        sb.append("Server Name: ").append(server.getName()).append("\n");
        sb.append("Online Mode: ").append(server.getOnlineMode()).append("\n");
        return sb.toString();
    }
}

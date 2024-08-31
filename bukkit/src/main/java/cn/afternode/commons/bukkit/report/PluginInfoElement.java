package cn.afternode.commons.bukkit.report;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import java.util.List;

/**
 * Plugin information (from plugin.yml/PluginDescriptionFile)
 * @see PluginDescriptionFile
 */
public class PluginInfoElement implements IPluginReportElement {
    private final Plugin plugin;

    private boolean withWebsite = true;
    private boolean withLibraries = false;
    private boolean withDepends = true;
    private boolean withSoftDepends = true;
    private boolean withLoadBefore = false;

    private boolean withPluginNotFound = false;

    /**
     * Primary constructor
     * @param plugin Plugin
     */
    public PluginInfoElement(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String title() {
        return "Plugin Info of " + plugin.getName();
    }

    /**
     * Append website in plugin.yml
     * @param state State
     * @return This element
     * @see PluginDescriptionFile#getWebsite()
     */
    public PluginInfoElement withWebsite(boolean state) {
        this.withWebsite = state;
        return this;
    }

    /**
     * Append libraries in plugin.yml
     * @param state State
     * @return This element
     * @see PluginDescriptionFile#getLibraries()
     */
    public PluginInfoElement withLibraries(boolean state) {
        this.withLibraries = state;
        return this;
    }

    /**
     * Append dependencies names in plugin.yml
     * @param state State
     * @return This element
     * @see PluginDescriptionFile#getDepend()
     */
    public PluginInfoElement withDepends(boolean state) {
        this.withDepends = state;
        return this;
    }

    /**
     * Append soft dependencies names in plugin.yml
     * @param state State
     * @return This element
     * @see PluginDescriptionFile#getSoftDepend()
     */
    public PluginInfoElement withSoftDepends(boolean state) {
        this.withSoftDepends = state;
        return this;
    }

    /**
     * Append load-before names in plugin.yml
     * @param state State
     * @return This element
     * @see PluginDescriptionFile#getLoadBefore()
     */
    public PluginInfoElement withLoadBefore(boolean state) {
        this.withLoadBefore = state;
        return this;
    }

    /**
     * Append if dependencies/soft-dependencies/load-before plugins not found
     * @param state State
     * @return This element
     */
    public PluginInfoElement withPluginNotFound(boolean state) {
        this.withPluginNotFound = state;
        return this;
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();

        PluginDescriptionFile desc = plugin.getDescription();
        sb.append("Version: ").append(desc.getVersion()).append("\n");
        sb.append("API-Version: ").append(desc.getAPIVersion()).append("\n");
        if (withWebsite && desc.getWebsite() != null)
            sb.append("Website: ").append(desc.getWebsite()).append("\n");

        PluginManager mgr = Bukkit.getPluginManager();

        if (withDepends && !desc.getDepend().isEmpty()) {
            sb.append("Depends: \n");
            for (String depend : desc.getDepend()) {
                sb.append("    ").append(depend);
                if (withPluginNotFound && mgr.getPlugin(depend) == null)
                    sb.append(" [NOT FOUND]");
                sb.append("\n");
            }
        }

        if (withSoftDepends && !desc.getSoftDepend().isEmpty()) {
            sb.append("Soft Depends: \n");
            for (String depend : desc.getSoftDepend()) {
                sb.append("    ").append(depend);
                if (withPluginNotFound && mgr.getPlugin(depend) == null)
                    sb.append(" [NOT FOUND]");
                sb.append("\n");
            }
        }

        if (withLoadBefore && !desc.getLoadBefore().isEmpty()) {
            sb.append("Load Before: \n");
            for (String depend : desc.getLoadBefore()) {
                sb.append("    ").append(depend);
                if (withPluginNotFound && mgr.getPlugin(depend) == null)
                    sb.append(" [NOT FOUND]");
                sb.append("\n");
            }
        }

        if (withLibraries && !desc.getLibraries().isEmpty()) {
            sb.append("Libraries: \n");
            for (String library : desc.getLibraries()) {
                sb.append("    ").append(library);
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}

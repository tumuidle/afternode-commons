package cn.afternode.commons.bukkit.report;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Plugin (name and version) list
 */
public class PluginListElement implements IPluginReportElement {
    private boolean withState = false;

    /**
     * Append enable/disable state after each plugin name
     * @param withState State
     * @return This element
     */
    public PluginListElement withState(boolean withState) {
        this.withState = withState;
        return this;
    }

    @Override
    public String title() {
        return "Plugin List";
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            sb.append(" - ").append(plugin.getDescription().getFullName());
            if (withState) {
                if (plugin.isEnabled()) {
                    sb.append(" [ENABLED]");
                } else {
                    sb.append(" [DISABLED]");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}

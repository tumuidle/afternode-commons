package cn.afternode.commons.bukkit.report;

public interface IPluginReportElement {
    /**
     * Title of this element (append to report)
     * @return Title
     */
    String title();

    /**
     * Build to string
     * @return Body of this element
     */
    String build();
}

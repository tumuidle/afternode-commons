package cn.afternode.commons.bukkit.report;

public record CustomElement(String title, String content) implements IPluginReportElement {
    @Override
    public String title() {
        return title;
    }

    @Override
    public String build() {
        return content;
    }
}

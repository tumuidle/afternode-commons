package cn.afternode.commons.bukkit.report;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Exception (stacktrace) element
 */
public class ExceptionElement implements IPluginReportElement{
    private final String name;
    private final Throwable throwable;

    /**
     * Primary constructor
     * @param name Title
     * @param throwable Throwable
     */
    public ExceptionElement(String name, Throwable throwable) {
        this.name = name;
        this.throwable = throwable;
    }

    @Override
    public String title() {
        return "Stack Trace / " + name;
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);

        sb.append(sw);

        return sb.toString();
    }
}

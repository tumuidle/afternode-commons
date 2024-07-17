package cn.afternode.commons.bungee;

import net.md_5.bungee.api.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Utilities for specified plugin
 */
public class BungeePluginContext {
    private final Plugin plugin;

    /**
     * Constructor
     * @param plugin Plugin
     */
    public BungeePluginContext(final Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Getter
     * @return Plugin
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Create BungeeTabBuilder
     */
    public BungeeTabBuilder tab() {
        return new BungeeTabBuilder();
    }

    /**
     * Save resource to specified path
     * @param path Resource path
     * @param out Output path
     * @param overwrite Overwrite existing
     * @throws IOException IOException
     * @throws NullPointerException Resource not exist
     */
    public void saveResource(String path, Path out, boolean overwrite) throws IOException, NullPointerException {
        if (Files.exists(out))
            return;

        try (InputStream in = Objects.requireNonNull(plugin.getClass().getClassLoader().getResourceAsStream(path), () -> "%s @ resources".formatted(path))) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[4096];

            while ((nRead = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            Files.write(out, buffer.toByteArray());
        }
    }
}

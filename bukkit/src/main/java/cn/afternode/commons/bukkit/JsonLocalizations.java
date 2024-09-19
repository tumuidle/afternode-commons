package cn.afternode.commons.bukkit;

import cn.afternode.commons.localizations.ILocalizations;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;

/**
 * Minecraft style *.json language file
 */
public class JsonLocalizations implements ILocalizations {
    private final JsonObject lang;

    /**
     * Load from JsonObject
     * @param obj Json
     * @see com.google.gson.Gson#fromJson(String, Class)
     */
    public JsonLocalizations(JsonObject obj) {
        this.lang = obj;
    }

    /**
     * Load from ClassLoader resources with raw path
     * @param loader Loader
     * @param path Raw resource path
     */
    public JsonLocalizations(ClassLoader loader, String path) {
        this(new Gson().fromJson(
                new InputStreamReader(
                        Objects.requireNonNull(
                                loader.getResourceAsStream(path)
                        )
                ),
                JsonObject.class
        ));
    }

    /**
     * Load from resources with raw path
     * @param path Raw resource path
     */
    public JsonLocalizations(String path) {
        this(JsonLocalizations.class.getClassLoader(), path);
    }

    /**
     * Load from ClassLoader resources, at <code>/assets/(namespace)/lang/(langCode).json</code>
     * @param loader Loader
     * @param namespace Namespace in path
     * @param langCode Language file name
     * @see JsonLocalizations#JsonLocalizations(ClassLoader, String)
     */
    public JsonLocalizations(ClassLoader loader, String namespace, String langCode) {
        this(loader, getResourcePath(namespace, langCode));
    }

    /**
     * Load from resources
     * @param namespace Namespace in path
     * @param langCode Language file name
     * @see JsonLocalizations#JsonLocalizations(ClassLoader, String, String)
     */
    public JsonLocalizations(String namespace, String langCode) {
        this(JsonLocalizations.class.getClassLoader(), namespace, langCode);
    }

    @Override
    public String get(String key) {
        JsonElement el = lang.get(key);
        return el.isJsonPrimitive() ? el.getAsString() : key;
    }

    @Override
    public String get(String key, Map<String, Object> placeholders) {
        String get = this.get(key);
        for (String k : placeholders.keySet()) {
            get = get.replace("%" + k + "%", String.valueOf(placeholders.get(k)));
        }
        return get;
    }

    @Override
    public String get(String key, String... args) {
        return this.get(key).formatted((Object[]) args);
    }

    public static String getResourcePath(String namespace, String langCode) {
        return "assets/%s/lang/%s.json".formatted(namespace, langCode);
    }
}

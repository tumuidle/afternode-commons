package cn.afternode.commons.bukkit;

import cn.afternode.commons.localizations.ILocalizations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.MemorySection;

import java.util.Map;
import java.util.Set;

public class ConfigurationLocalizations implements IAdventureLocalizations {
    private final ConfigurationSection config;
    private LegacyComponentSerializer legacy;
    private MiniMessage mini;

    public ConfigurationLocalizations(ConfigurationSection conf, LegacyComponentSerializer legacy, MiniMessage mini) {
        this.config = conf;
        this.legacy = legacy;
        this.mini = mini;
    }

    public ConfigurationLocalizations(ConfigurationSection conf, MiniMessage mini) {
        this(conf, LegacyComponentSerializer.legacySection(), mini);
    }

    public ConfigurationLocalizations(ConfigurationSection conf, LegacyComponentSerializer legacy) {
        this(conf, legacy, MiniMessage.miniMessage());
    }

    public ConfigurationLocalizations(ConfigurationSection conf) {
        this(conf, LegacyComponentSerializer.legacySection(), MiniMessage.miniMessage());
    }

    @Override
    public String get(String key) {
        return String.valueOf(config.get(key, key));
    }

    @Override
    public String get(String key, Map<String, Object> placeholders) {
        String base = get(key);
        for (String s : placeholders.keySet()) {
            base = base.replace("%" + s + "%", String.valueOf(placeholders.get(s)));
        }
        return base;
    }

    @Override
    public String get(String key, String... args) {
        return this.get(key).formatted((Object[]) args);
    }

    /**
     * Get keys in current localizations
     * @return Keys
     */
    @Override
    public Set<String> keys() {
        return config.getKeys(true);
    }

    @Override
    public TextComponent legacy(String key) {
        return this.legacy.deserialize(this.get(key));
    }

    @Override
    public TextComponent legacy(String key, Map<String, Object> placeholders) {
        return this.legacy.deserialize(this.get(key, placeholders));
    }

    @Override
    public TextComponent legacy(String key, String... args) {
        return this.legacy.deserialize(this.get(key, args));
    }

    @Override
    public Component mini(String key) {
        return this.mini.deserialize(this.get(key));
    }

    @Override
    public Component mini(String key, Map<String, Object> placeholders) {
        return this.mini.deserialize(this.get(key, placeholders));
    }

    @Override
    public Component mini(String key, String... args) {
        return this.mini.deserialize(this.get(key, args));
    }

    @Override
    public void withLegacySerializer(LegacyComponentSerializer serializer) {
        this.legacy = serializer;
    }

    @Override
    public void withMiniMessage(MiniMessage mm) {
        this.mini = mm;
    }

    @Override
    public ConfigurationLocalizations withFallback(ILocalizations fallback) {
        MemorySection sec = new MemoryConfiguration();
        for (String key : fallback.keys()) {
            sec.set(key, fallback.get(key));
        }
        for (String key : this.config.getKeys(true)) {
            sec.set(key, this.config.get(key));
        }
        return new ConfigurationLocalizations(sec);
    }
}

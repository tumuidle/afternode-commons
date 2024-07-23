package cn.afternode.commons.bukkit;

import cn.afternode.commons.localizations.ILocalizations;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.Set;

public class ConfigurationLocalizations implements ILocalizations {
    private final ConfigurationSection config;

    public ConfigurationLocalizations(ConfigurationSection conf) {
        this.config = conf;
    }

    @Override
    public String get(String key) {
        return String.valueOf(config.get(key, key));
    }

    @Override
    public String get(String key, Map<String, Object> placeholders) {
        String base = get(key);
        for (String s : placeholders.keySet()) {
            base.replace("%" + s + "%", String.valueOf(placeholders.get(s)));
        }
        return base;
    }

    /**
     * Get keys in current localizations
     * @return Keys
     */
    public Set<String> keys() {
        return config.getKeys(true);
    }
}

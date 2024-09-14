package cn.afternode.commons.bukkit.configurations;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurationMerger {
    /**
     * Create a new YamlConfiguration, write src and dest into it
     * <br>
     * The contents of src are retained when the key is duplicated
     * @param src Source configurations
     * @param dest Destination configurations
     * @return Merged
     */
    public static YamlConfiguration migrate(ConfigurationSection src, ConfigurationSection dest) {
        YamlConfiguration conf = new YamlConfiguration();

        Object get;
        for (String k: dest.getKeys(false))
            conf.set(k, dest.get(k));
        for (String k: src.getKeys(true)) {
            get = src.get(k);
            if (!(get instanceof ConfigurationSection))
                conf.set(k, get);
        }

        return conf;
    }
}

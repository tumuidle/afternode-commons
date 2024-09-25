package cn.afternode.commons.bukkit;

import cn.afternode.commons.localizations.ILocalizations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Map;

public interface IAdventureLocalizations extends ILocalizations {
    /**
     * Get localizations without any placeholder and deserialize with legacy serializer
     * @param key Localization key
     * @return Result localization or provided key if not found
     */
    TextComponent legacy(String key);

    /**
     * Get localizations with specified placeholders and deserialize with legacy serializer
     * @param key Localization key
     * @param placeholders Placeholders
     * @return Result localizations with placeholders applied, or provided key if not found
     */
    TextComponent legacy(String key, Map<String, Object> placeholders);

    /**
     * Get localizations with java formatter and deserialize with legacy serializer
     * @param key Localization key
     * @param args Formater replacements
     * @return Result localizations with String#format
     * @see String#formatted(Object...)
     */
    TextComponent legacy(String key, String... args);

    /**
     * Get localizations without any placeholder and deserialize with MiniMessage
     * @param key Localization key
     * @return Result localization or provided key if not found
     */
    Component mini(String key);

    /**
     * Get localizations with specified placeholders and deserialize with MiniMessage
     * @param key Localization key
     * @param placeholders Placeholders
     * @return Result localizations with placeholders applied, or provided key if not found
     */
    Component mini(String key, Map<String, Object> placeholders);

    /**
     * Get localizations, format with Java formatter and deserialize with MiniMessage
     * @param key Localization key
     * @param args Formater replacements
     * @return Result localizations with String#format
     * @see String#formatted(Object...)
     */
    Component mini(String key, String... args);

    /**
     * Set current legacy serializer
     * @param serializer Serializer
     */
    void withLegacySerializer(LegacyComponentSerializer serializer);

    /**
     * Set current MiniMessage
     * @param mm instance
     */
    void withMiniMessage(MiniMessage mm);
}

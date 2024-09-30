package cn.afternode.commons.localizations;

import java.util.Map;
import java.util.Set;

public interface ILocalizations {
    /**
     * Get localization without any placeholder
     * @param key Localization key
     * @return Result localizations, or provided key if not found
     */
    String get(String key);

    /**
     * Get localizations with specified placeholders
     * @param key Localization key
     * @param placeholders Placeholders
     * @return Result localizations with placeholders applied, or provided key if not found
     */
    String get(String key, Map<String, Object> placeholders);

    /**
     * Get localizations with java formatter
     * @param key Localization key
     * @param args Formater replacements
     * @return Result localizations with String#format
     * @see String#formatted(Object...)
     */
    String get(String key, String... args);

    /**
     * Get keys from localizations
     * @return Keys
     */
    Set<String> keys();

    /**
     * Create new localizations instance and merge fallback
     * @param fallback fallback
     * @return new instance
     */
    default ILocalizations withFallback(ILocalizations fallback) { throw new UnsupportedOperationException("This localizations does not support fallback"); }
}

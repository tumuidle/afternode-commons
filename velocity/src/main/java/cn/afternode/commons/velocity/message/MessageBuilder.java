package cn.afternode.commons.velocity.message;

import cn.afternode.commons.localizations.ILocalizations;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.awt.*;
import java.util.Map;

/**
 * Velocity message builder implementation
 */
public class MessageBuilder {
    private ILocalizations localizations;
    private ComponentLike linePrefix = Component.text();
    private CommandSource target;

    private final TextComponent.Builder component = Component.text();

    /**
     * Primary constructor
     * @param localizations Localizations provider
     * @param linePrefix Prefix of each line
     * @param target Message target
     */
    public MessageBuilder(
            ILocalizations localizations,
            ComponentLike linePrefix,
            CommandSource target
    ) {
        this.localizations = localizations;
        this.linePrefix = linePrefix;
        this.target = target;
        if (linePrefix != null)
            this.component.append(linePrefix);
    }

    /**
     * Create a basic message builder
     */
    public MessageBuilder() {
        this(null, null, null);
    }

    /**
     * Append raw text
     * @param text Raw text
     * @return This builder
     */
    public MessageBuilder text(String text) {
        this.component.append(Component.text(text));
        return this;
    }

    /**
     * Append colored raw text (overwrites style)
     * @param text Raw text
     * @param color AWT color
     * @return This builder
     * @see java.awt.Color
     */
    public MessageBuilder text(String text, Color color) {
        this.component.append(Component.text(text).color(TextColor.color(color.getRGB())));
        return this;
    }

    /**
     * Append localized text with placeholder, a localizations object must be passed to this builder
     * @param key Localization key
     * @param placeholders Localization placeholder
     * @return This builder
     * @see #localizations
     * @see #localizations(ILocalizations)
     * @see ILocalizations#get(String, Map)
     */
    public MessageBuilder localize(String key, Map<String, Object> placeholders) {
        if (localizations == null)
            throw new NullPointerException("No localizations passed to this builder");
        return this.text(this.localizations.get(key, placeholders));
    }

    /**
     * Append localized text, a localizations object must be passed to this builder
     * @param key Localization key
     * @return This builder
     * @see #localizations
     * @see #localizations(ILocalizations)
     * @see ILocalizations#get(String, Map)
     */
    public MessageBuilder localize(String key) {
        if (localizations == null)
            throw new NullPointerException("No localizations passed to this builder");
        return this.text(this.localizations.get(key));
    }

    /**
     * Append MiniMessage (overwrites style)
     * <br>
     * <a href="https://docs.advntr.dev/minimessage/index.html">MiniMessage docs</a>
     *
     * @param mini MiniMessage string
     * @return This builder
     */
    public MessageBuilder mini(String mini) {
        this.component.append(MiniMessage.miniMessage().deserialize(mini));
        return this;
    }

    /**
     * Append HoverEvent
     * @param source Event source
     * @return This builder
     * @see net.kyori.adventure.text.event.HoverEvent
     */
    public MessageBuilder hover(HoverEventSource<?> source) {
        this.component.hoverEvent(source);
        return this;
    }

    /**
     * Append click event
     * @param event Event
     * @return This builder
     * @see ClickEvent
     */
    public MessageBuilder click(ClickEvent event) {
        this.component.clickEvent(event);
        return this;
    }

    /**
     * Append adventure component
     * @param componentLike Component
     * @return This builder
     * @see Component
     */
    public MessageBuilder append(ComponentLike componentLike) {
        this.component.append(componentLike);
        return this;
    }

    /**
     * Append new line with prefix
     * @return This builder
     * @see #linePrefix
     * @see #linePrefix(ComponentLike)
     */
    public MessageBuilder line() {
        this.component.append(Component.newline());
        this.component.append(linePrefix);
        return this;
    }

    /**
     * Append empty line
     * @return This builder
     */
    public MessageBuilder emptyLine() {
        this.component.append(Component.newline());
        return this;
    }

    /**
     * Check sender permission and append adventure component, a CommandSender must be passed to this builder
     * @param permission Permission to check
     * @param componentLike Adventure component
     * @return This builder
     */
    public MessageBuilder permission(String permission, ComponentLike componentLike) {
        if (this.target.hasPermission(permission))
            this.component.append(componentLike);
        return this;
    }

    public ILocalizations localizations() {
        return localizations;
    }

    public MessageBuilder localizations(ILocalizations localizations) {
        this.localizations = localizations;
        return this;
    }

    public ComponentLike linePrefix() {
        return linePrefix;
    }

    public MessageBuilder linePrefix(ComponentLike linePrefix) {
        this.linePrefix = linePrefix;
        return this;
    }

    public CommandSource target() {
        return target;
    }

    public MessageBuilder target(CommandSource target) {
        this.target = target;
        return this;
    }

    /**
     * Convert this builder to an Adventure component
     * @return Adventure component
     */
    public TextComponent build() {
        return this.component.build();
    }

    /**
     * Convert this builder to Adventure component and send it to target of this builder
     * <br>
     * A sender must be passed to this builder
     * @see #target(CommandSource)
     * @see CommandSource#sendMessage(Component)
     * @see #build()
     */
    public void send() {
        if (this.target == null)
            throw new NullPointerException("No sender passed to this builder");
        this.target.sendMessage(this.build());
    }

    /**
     * Convert this builder to Adventure component and send it to target
     * @param target Bukkit CommandSender
     * @see #send()
     * @see #build()
     * @see CommandSource#sendMessage(Component)
     */
    public void send(CommandSource target) {
        target.sendMessage(this.build());
    }
}

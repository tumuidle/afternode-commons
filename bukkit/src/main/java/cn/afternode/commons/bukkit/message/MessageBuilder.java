package cn.afternode.commons.bukkit.message;

import cn.afternode.commons.localizations.ILocalizations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Map;
import java.util.Stack;

public class MessageBuilder {
    private ILocalizations localizations;
    private ComponentLike linePrefix = Component.text();
    private CommandSender sender;

    private Stack<ComponentStyle> styleStack = null;

    private final TextComponent.Builder component = Component.text();

    public MessageBuilder(
            @Nullable ILocalizations locale,
            @Nullable ComponentLike linePrefix,
            @Nullable CommandSender sender,
            boolean styleStack
            ) {
        this.localizations = locale;
        if (linePrefix != null) this.linePrefix = linePrefix;
        this.sender = sender;
        if (styleStack)
            useStyleStack();

        component.append(this.linePrefix);
    }

    public MessageBuilder(@Nullable ILocalizations locale) {
        this(locale, null, null, false);
    }

    public MessageBuilder() {
        this(null, null, null, false);
    }

    public MessageBuilder(boolean styleStack) {
        this(null, null, null, styleStack);
    }

    /**
     * Push a default style to the stack
     */
    public void pushStyle() {
        styleStack.push(new ComponentStyle());
    }

    /**
     * Get style at top of stack
     * @return Style, or null if style stack not enabled
     */
    public ComponentStyle style() {
        if (styleStack != null)
            return styleStack.peek();
        else return null;
    }

    /**
     * Pop style stack
     */
    public void popStyle() {
        styleStack.pop();
    }

    /**
     * Clear style stack and push a default style
     */
    public void clearStyle() {
        styleStack.clear();
        styleStack.push(new ComponentStyle());
    }

    /**
     * Enable style stack and push a default style
     */
    public void useStyleStack() {
        this.styleStack = new Stack<>();
        this.styleStack.push(new ComponentStyle());
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
        ComponentStyle style = style();
        component.append(style == null ? Component.text(this.localizations.get(key, placeholders)) : Component.text(this.localizations.get(key, placeholders), style.build()));
        return this;
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
        ComponentStyle style = style();
        component.append(style == null ? Component.text(this.localizations.get(key)) : Component.text(this.localizations.get(key), style.build()));
        return this;
    }

    /**
     * Append raw text
     * @param text Raw text
     * @return This builder
     */
    public MessageBuilder text(String text) {
        ComponentStyle style = style();
        this.component.append(style == null ? Component.text(text) : Component.text(text, style.build()));
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
        this.component.appendNewline();
        this.component.append(linePrefix);
        return this;
    }

    /**
     * Append empty line
     * @return This builder
     */
    public MessageBuilder emptyLine() {
        this.component.appendNewline();
        return this;
    }

    /**
     * Check sender permission and append adventure component, a CommandSender must be passed to this builder
     * @param permission Permission to check
     * @param componentLike Adventure component
     * @return This builder
     * @see #sender
     * @see #sender(CommandSender)
     */
    public MessageBuilder permission(String permission, ComponentLike componentLike) {
        if (sender.hasPermission(permission))
            this.component.append(componentLike);
        return this;
    }

    /**
     * Check sender permission and append adventure component, a CommandSender must be passed to this builder
     * @param permission Permission to check
     * @param component Adventure component
     * @return This builder
     * @see #sender
     * @see #sender(CommandSender)
     */
    public MessageBuilder permission(Permission permission, ComponentLike component) {
        if (sender.hasPermission(permission))
            this.component.append(component);
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
     * Convert this builder to Adventure component and send it to sender of this builder
     * <br>
     * A sender must be passed to this builder
     * @see #sender(CommandSender)
     * @see #sender
     * @see #build()
     */
    public void send() {
        if (this.sender == null)
            throw new NullPointerException("No sender passed to this builder");
        this.sender.sendMessage(this.build());
    }

    /**
     * Convert this builder to Adventure component and send it to sender
     * @param sender Bukkit CommandSender
     * @see CommandSender#sendMessage(Component)
     * @see #send()
     * @see #build()
     */
    public void send(CommandSender sender) {
        sender.sendMessage(this.build());
    }

    public ILocalizations getLocalizations() {
        return localizations;
    }

    public MessageBuilder localizations(ILocalizations localizations) {
        this.localizations = localizations;
        return this;
    }

    public ComponentLike getLinePrefix() {
        return linePrefix;
    }

    public MessageBuilder linePrefix(ComponentLike prefix) {
        this.linePrefix = prefix;
        return this;
    }

    public CommandSender getSender() {
        return sender;
    }

    public MessageBuilder sender(CommandSender sender) {
        this.sender = sender;
        return this;
    }
}

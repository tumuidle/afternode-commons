package cn.afternode.commons.advntr;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.awt.*;

/**
 * Adventure-API implementation of MessageBuilder
 */
public class AdvntrMessageBuilder {
    private ComponentLike linePrefix = Component.text();
    private Audience audience;

    private final TextComponent.Builder component = Component.text();

    /**
     * Append raw text
     * @param text Raw text
     * @return This builder
     */
    public AdvntrMessageBuilder text(String text) {
        this.component.append(Component.text(text));
        return this;
    }

    /**
     * Append colored raw text
     * @param text Raw text
     * @param color AWT color
     * @return This builder
     * @see java.awt.Color
     */
    public AdvntrMessageBuilder text(String text, Color color) {
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
    public AdvntrMessageBuilder mini(String mini) {
        this.component.append(MiniMessage.miniMessage().deserialize(mini));
        return this;
    }

    /**
     * Append HoverEvent
     * @param source Event source
     * @return This builder
     * @see net.kyori.adventure.text.event.HoverEvent
     */
    public AdvntrMessageBuilder hover(HoverEventSource<?> source) {
        this.component.hoverEvent(source);
        return this;
    }

    /**
     * Append click event
     * @param event Event
     * @return This builder
     * @see ClickEvent
     */
    public AdvntrMessageBuilder click(ClickEvent event) {
        this.component.clickEvent(event);
        return this;
    }

    /**
     * Append adventure component
     * @param componentLike Component
     * @return This builder
     * @see Component
     */
    public AdvntrMessageBuilder append(ComponentLike componentLike) {
        this.component.append(componentLike);
        return this;
    }

    /**
     * Append new line with prefix
     * @return This builder
     * @see #linePrefix
     * @see #linePrefix(ComponentLike)
     */
    public AdvntrMessageBuilder line() {
        this.component.append(Component.newline());
        this.component.append(linePrefix);
        return this;
    }

    /**
     * Append empty line
     * @return This builder
     */
    public AdvntrMessageBuilder emptyLine() {
        this.component.append(Component.newline());
        return this;
    }

    /**
     * <STRONG>UNSAFE</STRONG>
     * <br>
     * Append gradient text, this method uses MiniMessage, may cause injection, NEVER insert player messages with this
     * <br>
     * <a href="https://docs.advntr.dev/minimessage/format.html#gradient">MiniMessage docs</a>
     * @param text Text
     * @param colors Colors
     * @return This builder
     */
    public AdvntrMessageBuilder gradient(String text, Color... colors) {
        StringBuilder mini = new StringBuilder();
        mini.append("<gradient");
        for (Color color : colors) {
            mini.append(":").append("#%02x%02x%02x".formatted(color.getRed(), color.getGreen(), color.getBlue()));
        }
        mini.append(">").append(text).append("</gradient>");
        return this.mini(mini.toString());
    }

    /**
     * <STRONG>UNSAFE</STRONG>
     * <br>
     * Append gradient text, this method uses MiniMessage, may cause injection, NEVER insert player messages with this
     * <br>
     * <a href="https://docs.advntr.dev/minimessage/format.html#gradient">MiniMessage docs</a>
     * @param text Text
     * @param colors Colors
     * @return This builder
     */
    public AdvntrMessageBuilder gradient(String text, int... colors) {
        StringBuilder mini = new StringBuilder();
        mini.append("<gradient");
        for (int color : colors) {
            mini.append(":").append("#%06x".formatted(color));
        }
        mini.append(">").append(text).append("</gradient>");
        return this.mini(mini.toString());
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
     * @see #audience(Audience)
     * @see #audience
     * @see #build()
     */
    public void send() {
        if (this.audience == null)
            throw new NullPointerException("No audience passed to this builder");
        this.audience.sendMessage(this.build());
    }

    /**
     * Convert this builder to Adventure component and send it to sender
     * @param audience Adventure audience
     * @see Audience#sendMessage(Component)
     * @see #send()
     * @see #build()
     */
    public void send(Audience audience) {
        audience.sendMessage(this.build());
    }

    /**
     * Get line prefix passed to this builder
     * @return Line prefix
     */
    public ComponentLike getLinePrefix() {
        return linePrefix;
    }

    /**
     * Set line prefix
     * @param prefix Adventure component
     * @return This builder
     */
    public AdvntrMessageBuilder linePrefix(ComponentLike prefix) {
        this.linePrefix = prefix;
        return this;
    }

    /**
     * Get Adventure audience passed to this builder
     * @return Adventure audience
     */
    public Audience getAudience() {
        return audience;
    }

    /**
     * Set audience for this builder
     * @param audience Adventure audience
     * @return This builder
     */
    public AdvntrMessageBuilder audience(Audience audience) {
        this.audience = audience;
        return this;
    }
}

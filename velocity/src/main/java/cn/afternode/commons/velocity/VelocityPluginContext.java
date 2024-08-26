package cn.afternode.commons.velocity;

import cn.afternode.commons.localizations.ILocalizations;
import cn.afternode.commons.velocity.message.MessageBuilder;
import cn.afternode.commons.velocity.message.TabBuilder;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.ComponentLike;

/**
 * Velocity plugin context implementation
 */
public class VelocityPluginContext {
    private ILocalizations localizations = null;
    private ComponentLike linePrefix = null;

    private final ProxyServer server;

    /**
     * Create context in a server
     * @param server server
     */
    public VelocityPluginContext(ProxyServer server) {
        this.server = server;
    }

    public ProxyServer getServer() {
        return server;
    }

    /**
     * Set localization provider
     * @param localizations param
     */
    public void setLocalizations(ILocalizations localizations) {
        this.localizations = localizations;
    }

    /**
     * Get localization provider
     * @return Localizations
     */
    public ILocalizations getLocalizations() {
        return localizations;
    }

    /**
     * Get line prefix for MessageBuilder
     * @see MessageBuilder#linePrefix()
     * @return Line prefix
     */
    public ComponentLike getLinePrefix() {
        return linePrefix;
    }

    /**
     * Set line prefix for MessageBuilder
     * @param linePrefix prefix
     * @see MessageBuilder#linePrefix(ComponentLike)
     */
    public void setLinePrefix(ComponentLike linePrefix) {
        this.linePrefix = linePrefix;
    }

    /**
     * Create MessageBuilder with localizations and linePrefix in this context
     * @return builder
     */
    public MessageBuilder message() {
        return new MessageBuilder(localizations, linePrefix, null);
    }

    /**
     * Create MessageBuilder with localizations and linePrefix in this context, and pass a message target
     * @param target Message target
     * @return builder
     */
    public MessageBuilder message(CommandSource target) {
        return new MessageBuilder(localizations, linePrefix, target);
    }

    /**
     * Create TabBuilder
     * @return builder
     */
    public TabBuilder tab() {
        return new TabBuilder(this.server);
    }

    /**
     * Create tab builder and pass a CommandSource
     * @param target target
     * @return builder
     */
    public TabBuilder tab(CommandSource target) {
        return new TabBuilder(this.server, target);
    }
}

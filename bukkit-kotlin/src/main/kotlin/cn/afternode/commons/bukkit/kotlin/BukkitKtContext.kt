package cn.afternode.commons.bukkit.kotlin

import cn.afternode.commons.bukkit.message.MessageBuilder
import cn.afternode.commons.bukkit.message.TabBuilder
import cn.afternode.commons.localizations.ILocalizations
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

@Deprecated("Deprecated",
    replaceWith = ReplaceWith("BukkitPluginContext", "cn.afternode.commons.bukkit.BukkitPluginContext"))
class BukkitKtContext(val plugin: Plugin) {
    /**
     * Line prefix for MessageBuilder
     * @see MessageBuilder
     * @see BukkitKtContext.message
     */
    var messageLinePrefix: ComponentLike = Component.text().build()

    /**
     * Localizations for MessageBuilder
     * @see MessageBuilder
     * @see BukkitKtContext.message
     */
    var localization: ILocalizations? = null

    /**
     * Create commandSuggestion
     * @see commandSuggestion
     */
    fun tab(sender: CommandSender? = null, ignoreCase: Boolean = false, block: TabBuilder.() -> Unit) =
        commandSuggestion(sender, block)

    fun tab(sender: CommandSender?) = TabBuilder(sender)

    /**
     * Wrapped message builder using linePrefix and localizations in current context
     * @return Result message
     * @see BukkitKtContext.messageLinePrefix
     * @see BukkitKtContext.localization
     * @see MessageBuilder
     */
    fun message(sender: CommandSender? = null, block: MessageBuilder.() -> Unit) = message(linePrefix = messageLinePrefix, sender = sender, locale = localization, block = block)

    /**
     * Wrapped message builder using linePrefix and localizations in current context
     * @return Result message builder
     * @see BukkitKtContext.messageLinePrefix
     * @see BukkitKtContext.localization
     * @see MessageBuilder
     */
    fun message(sender: CommandSender? = null): MessageBuilder = MessageBuilder().apply {
        sender(sender)
        linePrefix(messageLinePrefix)
        localizations(localization)
    }
}

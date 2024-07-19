package cn.afternode.commons.bukkit.kotlin

import cn.afternode.commons.bukkit.BukkitPluginContext
import cn.afternode.commons.bukkit.message.MessageBuilder
import cn.afternode.commons.bukkit.message.TabBuilder
import net.kyori.adventure.text.TextComponent
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

/**
 * Create BukkitPluginContext of current plugin
 * @see BukkitPluginContext
 */
fun Plugin.createContext() = BukkitPluginContext(this)

/**
 * Create MessageBuilder with localizations and prefix in this context
 * @param sender Sender passed to MessageBuilder
 */
fun BukkitPluginContext.message(sender: CommandSender, block: MessageBuilder.() -> Unit): TextComponent {
    val builder = message(sender)
    block(builder)
    return builder.build()
}

/**
 * Create MessageBuilder with localizations and prefix in this context and send to the sender
 * @param sender Sender passed to MessageBuilder
 */
fun BukkitPluginContext.sendMessage(sender: CommandSender, block: MessageBuilder.() -> Unit) {
    sender.sendMessage(message(sender, block))
}

/**
 * Create MessageBuilder with localizations and prefix in this context
 * @return builder
 */
fun BukkitPluginContext.message(block: MessageBuilder.() -> Unit): TextComponent {
    val builder = message()
    block(builder)
    return builder.build()
}

/**
 * Create tab completion with sender
 */
fun BukkitPluginContext.tab(sender: CommandSender, block: TabBuilder.() -> Unit): MutableList<String> {
    val builder = TabBuilder(sender)
    block(builder)
    return builder.build()
}

/**
 * Create tab completion
 */
fun BukkitPluginContext.tab(block: TabBuilder.() -> Unit): MutableList<String> {
    val builder = TabBuilder()
    block(builder)
    return builder.build()
}

package cn.afternode.commons.bukkit.kotlin

import cn.afternode.commons.bukkit.message.TabBuilder
import org.bukkit.command.CommandSender

/**
 * Create command suggestion (Tab completion)
 */
fun commandSuggestion(sender: CommandSender? = null, block: TabBuilder.() -> Unit): MutableList<String> {
    val builder = TabBuilder(sender)
    block(builder)
    return builder.build()
}

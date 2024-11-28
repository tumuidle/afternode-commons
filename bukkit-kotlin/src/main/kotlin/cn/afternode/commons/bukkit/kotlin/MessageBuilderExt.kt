package cn.afternode.commons.bukkit.kotlin

import cn.afternode.commons.bukkit.message.ComponentStyle
import cn.afternode.commons.bukkit.message.MessageBuilder
import net.kyori.adventure.text.Component
import org.bukkit.permissions.Permission

fun MessageBuilder.localize(key: String, vararg p: Pair<String, Any>): MessageBuilder =
    this.localize(key, p.toMap())

fun MessageBuilder.hover(block: HoverBuilder.() -> Unit): MessageBuilder {
    val builder = HoverBuilder()
    block(builder)
    this.hover(builder.build())
    return this
}

fun MessageBuilder.click(block: ClickBuilder.() -> Unit): MessageBuilder {
    val builder = ClickBuilder()
    block(builder)
    this.click(builder.build())
    return this
}

fun MessageBuilder.append(builder: MessageBuilder): MessageBuilder {
    this.append(builder.build())
    return this
}

fun MessageBuilder.sub(styleStack: Boolean = false, linePrefix: Component? = null, block: MessageBuilder.() -> Unit): MessageBuilder {
    val builder = MessageBuilder(localizations, linePrefix, sender, styleStack)
    block(builder)
    this.append(builder)
    return this
}

fun MessageBuilder.permission(permission: String, styleStack: Boolean = false, block: MessageBuilder.() -> Unit): MessageBuilder {
    if (sender.hasPermission(permission)) {
        val builder = MessageBuilder(localizations, linePrefix, sender, styleStack)
        block(builder)
        this.append(builder)
    }
    return this
}

fun MessageBuilder.permission(permission: Permission, styleStack: Boolean = false, block: MessageBuilder.() -> Unit): MessageBuilder {
    if (sender.hasPermission(permission)) {
        val builder = MessageBuilder(localizations, linePrefix, sender, styleStack)
        block(builder)
        this.append(builder)
    }
    return this
}

fun MessageBuilder.pushStyle(block: ComponentStyle.() -> Unit): MessageBuilder {
    this.pushStyle()
    block(this.style())
    return this
}

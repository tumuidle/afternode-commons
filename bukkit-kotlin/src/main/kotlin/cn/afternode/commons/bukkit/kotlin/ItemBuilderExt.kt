package cn.afternode.commons.bukkit.kotlin

import cn.afternode.commons.bukkit.gui.ItemBuilder
import cn.afternode.commons.bukkit.message.MessageBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun item(material: Material): ItemBuilder = ItemBuilder(material)

fun item(material: Material, block: ItemBuilder.() -> Unit): ItemStack {
    val builder = ItemBuilder(material)
    block.invoke(builder)
    return builder.build()
}

fun ItemBuilder.name(name: MessageBuilder.() -> Unit): ItemBuilder =
    this.name(message(block = name))

fun ItemBuilder.lore(lore: MessageBuilder.() -> Unit): ItemBuilder =
    this.lore(message(block = lore))

package cn.afternode.commons.bukkit.kotlin

import cn.afternode.commons.bukkit.messaging.MessageChannelContext
import cn.afternode.commons.bukkit.messaging.NBukkitByteBuf
import org.bukkit.entity.Player

fun MessageChannelContext.send(player: Player, data: NBukkitByteBuf.() -> Unit) {
    val buf = NBukkitByteBuf()
    data(buf)
    this.send(player, buf)
}

fun Player.sendPluginMessage(channel: MessageChannelContext, data: NBukkitByteBuf.() -> Unit) {
    channel.send(this, data)
}

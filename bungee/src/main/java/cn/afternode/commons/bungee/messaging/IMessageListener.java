package cn.afternode.commons.bungee.messaging;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public interface IMessageListener {
    void onMessage(String channel, ProxiedPlayer player, NBungeeByteBuf data);
}

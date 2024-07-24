package cn.afternode.commons.bukkit.messaging;

import org.bukkit.entity.Player;

public interface IMessageListener {
    void onMessage(String channel, Player player, NBukkitByteBuf data);
}

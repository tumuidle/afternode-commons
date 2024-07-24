package cn.afternode.commons.bukkit.messaging;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Channel context and message listener
 */
public class MessageChannelContext implements PluginMessageListener, Closeable {
    private final String channel;
    private final BukkitMessagingHelper helper;
    private final IMessageListener listener;

    private boolean closed = false;

    MessageChannelContext(String channel, BukkitMessagingHelper helper, IMessageListener listener) {
        this.channel = channel;
        this.helper = helper;
        this.listener = listener;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (!this.channel.equals(channel))
            return;
        if (closed)
            return;

        try {
            NBukkitByteBuf buf = new NBukkitByteBuf(message);
            if (helper.signingAvailable())
                buf = helper.validateCombined(buf);

            listener.onMessage(channel, player, buf);
        } catch (Throwable t) {
            throw new RuntimeException("(%s) Error handling plugin message from %s".formatted(channel, player.getName()));
        }
    }

    /**
     * Send message to channel through specified player
     * @param player Player
     * @param buf Data
     * @throws NoSuchAlgorithmException Signing error
     * @throws InvalidKeyException Signing error
     * @throws IOException Using a closed channel
     */
    public void send(Player player, NBukkitByteBuf buf) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        if (closed)
            throw new IOException("Using a closed channel");

        byte[] data;
        if (helper.signingAvailable()) {
            data = helper.combineSign(buf);
        } else {
            data = buf.toArray();
        }
        player.sendPluginMessage(helper.getPlugin(), channel, data);
    }

    @Override
    public void close() throws IOException {
        if (closed)
            return;

        Messenger m = Bukkit.getMessenger();
        m.unregisterIncomingPluginChannel(helper.getPlugin(), this.channel);
        m.unregisterOutgoingPluginChannel(helper.getPlugin());
        closed = true;
    }
}

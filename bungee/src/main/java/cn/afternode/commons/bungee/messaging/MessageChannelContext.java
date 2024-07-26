package cn.afternode.commons.bungee.messaging;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.Closeable;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MessageChannelContext implements Listener, Closeable {
    private final String channel;
    private final BungeeMessagingHelper helper;
    private final IMessageListener listener;

    private boolean closed = false;

    MessageChannelContext(String channel, BungeeMessagingHelper helper, IMessageListener listener) {
        this.channel = channel;
        this.helper = helper;
        this.listener = listener;
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!this.channel.equals(event.getTag()))
            return;
        if (closed)
            return;

        Connection receiver = event.getReceiver();
        if (!(receiver instanceof ProxiedPlayer player))
            return;

        try {
            NBungeeByteBuf buf = new NBungeeByteBuf(event.getData());
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
    public void send(ProxiedPlayer player, NBungeeByteBuf buf) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        if (closed)
            throw new IOException("Using a closed channel");

        byte[] data;
        if (helper.signingAvailable()) {
            data = helper.combineSign(buf);
        } else {
            data = buf.toArray();
        }
        player.sendData(this.channel, data);
    }

    /**
     * Send message to all servers with at least 1 player online
     * @param buf Data
     * @throws IOException Using a closed channel
     * @throws NoSuchAlgorithmException Signing error
     * @throws InvalidKeyException Signing error
     */
    public void sendAll(NBungeeByteBuf buf) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        if (closed)
            throw new IOException("Using a closed channel");

        byte[] data;
        if (helper.signingAvailable()) {
            data = helper.combineSign(buf);
        } else {
            data = buf.toArray();
        }

        for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
            if (!server.getPlayers().isEmpty())
                server.sendData(this.channel, data);
        }
    }

    @Override
    public void close() throws IOException {
        if (closed)
            return;

        ProxyServer proxy = ProxyServer.getInstance();
        proxy.unregisterChannel(this.channel);
        proxy.getPluginManager().unregisterListener(this);
        closed = true;
    }
}

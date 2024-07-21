package cn.afternode.commons.bungee;

import cn.afternode.commons.binary.WrappedByteBuffer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.nio.ByteBuffer;

public class BungeeByteBuffer extends WrappedByteBuffer {
    public BungeeByteBuffer(byte[] data) {
        super(ByteBuffer.wrap(data));
    }

    public void writePlayer(ProxiedPlayer player) {
        this.writeUUID(player.getUniqueId());
    }

    public ProxiedPlayer readPlayer() {
        return ProxyServer.getInstance().getPlayer(this.readUUID());
    }

    public byte[] toArray() {
        ByteBuffer src = this.src();
        int off = src.position();
        src.position(0);
        byte[] out = new byte[src.remaining()];
        src.get(out);
        src.position(off);
        return out;
    }
}

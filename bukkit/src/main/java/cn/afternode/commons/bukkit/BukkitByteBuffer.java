package cn.afternode.commons.bukkit;

import cn.afternode.commons.binary.WrappedByteBuffer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.nio.ByteBuffer;

public class BukkitByteBuffer extends WrappedByteBuffer {
    public BukkitByteBuffer(byte[] data) {
        super(ByteBuffer.wrap(data));
    }

    public void writePlayer(Player player) {
        this.writeUUID(player.getUniqueId());
    }

    public Player readPlayer() {
        return Bukkit.getPlayer(this.readUUID());
    }

    public OfflinePlayer readPlayerOffline() {
        return Bukkit.getOfflinePlayer(this.readUUID());
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

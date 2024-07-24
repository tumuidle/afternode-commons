package cn.afternode.commons.bukkit.messaging;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Netty version of BukkitByteBuffer
 * <br>
 * No need to shade netty, it's provided by server
 */
public class NBukkitByteBuf {
    private final ByteBuf src;

    public NBukkitByteBuf(byte[] data) {
        this.src = Unpooled.copiedBuffer(data);
    }

    public NBukkitByteBuf() {
        this.src = Unpooled.buffer();
    }

    // Bytes start
    public void read(byte[] dest) {
        src.writeBytes(dest);
    }

    public void write(byte[] data) {
        src.writeBytes(data);
    }

    public byte[] read(int size) {
        byte[] data = new byte[size];
        src.readBytes(data);
        return data;
    }

    public byte[] readBlock() {
        return read(this.src.readShortLE());
    }

    public void writeBlock(byte[] data) {
        this.src.writeShortLE(data.length);
        this.src.writeBytes(data);
    }

    public byte[] readBlockL() {
        return this.read(this.src.readIntLE());
    }

    public void writeBlockL(byte[] data) {
        this.src.writeIntLE(data.length);
        this.src.writeBytes(data);
    }
    // Bytes end

    // Short start
    public short readShort() {
        return this.src.readShortLE();
    }

    public void writeShort(short data) {
        this.src.writeShortLE(data);
    }
    // Short end

    // Integer start
    public int readInt() {
        return this.src.readIntLE();
    }

    public void writeInt(int data) {
        this.src.writeIntLE(data);
    }
    // Integer end

    // Long start
    public long readLong() {
        return this.src.readLong();
    }

    public void writeLong(long data) {
        this.src.writeLong(data);
    }
    // Long end

    // String start
    public String readString() {
        return new String(this.readBlock(), StandardCharsets.UTF_8);
    }

    public void writeString(String data) {
        this.writeBlock(data.getBytes(StandardCharsets.UTF_8));
    }
    // String end

    // Enum start
    public void writeEnum(Enum<?> e) {
        this.src.writeShortLE(e.ordinal());
    }

    public <T extends Enum<?>> T readEnum(Class<T> clazz) {
        return clazz.getEnumConstants()[this.readShort()];
    }
    // Enum end

    // UUID start
    public UUID readUUID() {
        return new UUID(this.readLong(), this.readLong());
    }

    public void writeUUID(UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
    }
    // UUID end

    // Players start
    public Player readPlayer() {
        return Bukkit.getPlayer(this.readUUID());
    }

    public void writePlayer(Player player) {
        this.writeUUID(player.getUniqueId());
    }
    // Players end

    // Items start
    public ItemStack readItemStack() throws IOException, ClassNotFoundException {
        byte[] data = readBlock();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        BukkitObjectInputStream in = new BukkitObjectInputStream(bais);
        return (ItemStack) in.readObject();
    }

    public void writeItemStack(ItemStack item) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BukkitObjectOutputStream boos = new BukkitObjectOutputStream(baos);
        boos.writeObject(item);
        this.writeBlock(baos.toByteArray());
    }
    // Items end

    public ByteBuf direct() {
        return src;
    }

    public byte[] toArray() {
        return src.array();
    }
}

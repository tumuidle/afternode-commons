package cn.afternode.commons.bukkit.messaging;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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

    /**
     * Read byte array
     * @param dest Destination
     */
    public void read(byte[] dest) {
        src.writeBytes(dest);
    }

    /**
     * Write byte array
     * @param data Data
     */
    public void write(byte[] data) {
        src.writeBytes(data);
    }

    /**
     * Read byte array with specified size
     * @param size Size
     * @return Result
     */
    public byte[] read(int size) {
        byte[] data = new byte[size];
        src.readBytes(data);
        return data;
    }

    /**
     * Read byte array in a block
     * @return Result
     */
    public byte[] readBlock() {
        return read(this.src.readShortLE());
    }

    /**
     * Write byte array in a block
     * <br>
     * @param data Block
     */
    public void writeBlock(byte[] data) {
        this.src.writeShortLE(data.length);
        this.src.writeBytes(data);
    }

    /**
     * Read byte array in a large block
     * @return Result
     */
    public byte[] readBlockL() {
        return this.read(this.src.readIntLE());
    }

    /**
     * Write byte array in a large block
     * @param data Data
     */
    public void writeBlockL(byte[] data) {
        this.src.writeIntLE(data.length);
        this.src.writeBytes(data);
    }
    // Bytes end

    // Short start

    /**
     * Read short
     * @return result
     */
    public short readShort() {
        return this.src.readShortLE();
    }

    /**
     * Write short
     * @param data data
     */
    public void writeShort(short data) {
        this.src.writeShortLE(data);
    }
    // Short end

    // Integer start

    /**
     * Read integer
     * @return result
     */
    public int readInt() {
        return this.src.readIntLE();
    }

    /**
     * Write integer
     * @param data result
     */
    public void writeInt(int data) {
        this.src.writeIntLE(data);
    }
    // Integer end

    // Long start

    /**
     * Read long integer
     * @return result
     */
    public long readLong() {
        return this.src.readLong();
    }

    /**
     * Write long integer
     * @param data data
     */
    public void writeLong(long data) {
        this.src.writeLong(data);
    }
    // Long end

    // String start

    /**
     * Read UTF-8 String
     * @return result
     */
    public String readString() {
        return new String(this.readBlock(), StandardCharsets.UTF_8);
    }

    /**
     * Write UTF-8 string
     * @param data data
     */
    public void writeString(String data) {
        this.writeBlock(data.getBytes(StandardCharsets.UTF_8));
    }
    // String end

    // Enum start
    /**
     * Write enumeration object (1 short integer)
     * @param e object
     */
    public void writeEnum(Enum<?> e) {
        this.src.writeShortLE(e.ordinal());
    }

    /**
     * Read enumeration object (1 short integer)
     * @param clazz Type
     * @return result
     * @param <T> Type
     */
    public <T extends Enum<?>> T readEnum(Class<T> clazz) {
        return clazz.getEnumConstants()[this.readShort()];
    }
    // Enum end

    // UUID start

    /**
     * Read UUID (2 long integers)
     * @return result
     */
    public UUID readUUID() {
        return new UUID(this.readLong(), this.readLong());
    }

    /**
     * Write UUID (2 long integers)
     * @param uuid object
     */
    public void writeUUID(UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
    }
    // UUID end

    // Players start
    /**
     * Read player (UUID)
     * @return result (online only)
     */
    public Player readPlayer() {
        return Bukkit.getPlayer(this.readUUID());
    }

    /**
     * Read player (UUID)
     * @return result (online or offline)
     */
    public OfflinePlayer readPlayerOffline() {
        return Bukkit.getOfflinePlayer(this.readUUID());
    }

    /**
     * Write player (UUID)
     * @param player result
     */
    public void writePlayer(OfflinePlayer player) {
        this.writeUUID(player.getUniqueId());
    }
    // Players end

    // Items start

    /**
     * Read item stack with BukkitObjectInputStream
     * @return result
     * @throws IOException ObjectInputStream error
     * @throws ClassNotFoundException ObjectInputStream error
     * @see BukkitObjectInputStream
     */
    public ItemStack readItemStack() throws IOException, ClassNotFoundException {
        byte[] data = readBlock();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        BukkitObjectInputStream in = new BukkitObjectInputStream(bais);
        return (ItemStack) in.readObject();
    }

    /**
     * Write item stack with BukkitObjectOutputStream
     * @param item item
     * @throws IOException ObjectInputStream error
     * @see BukkitObjectInputStream
     */
    public void writeItemStack(ItemStack item) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BukkitObjectOutputStream boos = new BukkitObjectOutputStream(baos);
        boos.writeObject(item);
        this.writeBlock(baos.toByteArray());
    }
    // Items end

    /**
     * Access to source netty ByteBuf directly
     * @return source
     */
    public ByteBuf direct() {
        return src;
    }

    /**
     * Convert to byte array
     * @return result
     */
    public byte[] toArray() {
        return src.array();
    }
}

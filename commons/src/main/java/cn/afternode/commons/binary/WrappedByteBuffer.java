package cn.afternode.commons.binary;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class WrappedByteBuffer {
    public static final int DEFAULT_SIZE = 65535;

    private final ByteBuffer src;
    
    private int readOff = 0;
    private int writeOff = 0;

    /**
     * Wrap byte buffer
     * @param src ByteBuffer
     */
    public WrappedByteBuffer(ByteBuffer src) {
        this.src = src;
    }

    /**
     * Create with specified size
     * @param size size
     * @see ByteBuffer#allocate(int) 
     */
    public WrappedByteBuffer(int size) {
        this(ByteBuffer.allocate(size));
    }

    /**
     * Create with default size
     * @see #DEFAULT_SIZE
     */
    public WrappedByteBuffer() {
        this(DEFAULT_SIZE);
    }

    // Bytes START
    /**
     * Write provided bytes
     * @param buf Source bytes
     * @return This wrapper
     */
    public WrappedByteBuffer writeBytes(byte[] buf) {
        src.put(buf, writeOff, buf.length);
        writeOff += buf.length;
        return this;
    }

    /**
     * Write provided bytes with specified offset and length
     * @param buf Source bytes
     * @param off Target offset
     * @param len Write length
     * @return This wrapper
     */
    public WrappedByteBuffer writeBytes(byte[] buf, int off, int len) {
        src.put(buf, off, len);
        return this;
    }

    /**
     * Transfer bytes from wrapped ByteBuffer to byte[]
     * @param buf Target byte[]
     * @param off Offset
     * @param len Length
     */
    public void readBytes(byte[] buf, int off, int len) {
        src.get(buf, off, len);
    }

    /**
     * Transfer bytes from wrapped ByteBuffer to byte[] with length of target
     * @param buf Target byte[]
     */
    public void readBytes(byte[] buf) {
        src.get(buf, readOff, buf.length);
        readOff += buf.length;
    }
    // Bytes END

    // Block START
    public byte[] readBlock(int off) {
        byte[] data = new byte[this.readShort(off)];
        int nOff = off + 4;
        src.get(data, nOff, data.length);
        return data;
    }

    public byte[] readBlock() {
        return this.readBlock(this.readOff);
    }

    public void writeBlock(byte[] data, int off) {
        if (data.length >= Short.MAX_VALUE)
            throw new ArrayIndexOutOfBoundsException("Byte array too large");

        this.writeShort((short) data.length, off);
        this.writeBytes(data, off, data.length);
    }

    public void writeBlock(byte[] data) {
        this.writeBlock(data, this.writeOff);
    }
    // Block END
    
    // Integer START

    /**
     * Write integer with specified offset
     * @param src Source integer
     * @param off Target offset
     * @return This wrapper
     */
    public WrappedByteBuffer writeInt(int src, int off) {
        this.src.putInt(off, src);
        return this;
    }

    /**
     * Write integer
     * @param src Source integer
     * @return This wrapper
     */
    public WrappedByteBuffer writeInt(int src) {
        this.src.putInt(src);
        this.writeOff += 4;
        return this;
    }

    /**
     * Read integer with specified offset
     * @param off Target offset
     * @return Result integer
     */
    public int readInt(int off) {
        return this.src.getInt(off);
    }

    /**
     * Read integer
     * @return Result integer
     */
    public int readInt() {
        int r = this.src.getInt(readOff);
        readOff += 4;
        return r;
    }
    // Integer END

    // Short START

    /**
     * Write short with specified offset
     * @param src Source short
     * @param off Target offset
     * @return This wrapper
     */
    public WrappedByteBuffer writeShort(short src, int off) {
        this.src.putShort(off, src);
        return this;
    }

    /**
     * Write short
     * @param src Source short
     * @return This wrapper
     */
    public WrappedByteBuffer writeShort(short src) {
        writeShort(src, this.writeOff);
        this.writeOff += 2;
        return this;
    }

    /**
     * Read short with specified offset
     * @param off Target offset
     * @return Result short
     */
    public short readShort(int off) {
        return this.src.getShort(off);
    }

    /**
     * Read short
     * @return Result short
     */
    public short readShort() {
        short r = readShort(this.readOff);
        this.readOff += 2;
        return r;
    }
    // Short END

    // Long start
    public void writeLong(long v) {
        this.src.putLong(v);
    }

    public long readLong() {
        return this.src.getLong();
    }
    // Long end

    // UTF START
    /**
     * Write UTF-8 encoded string with specified offset
     * @param src Source string
     * @param off Target offset
     * @return This wrapper
     */
    public WrappedByteBuffer writeUtf(String src, int off) {
        byte[] bytes = src.getBytes(StandardCharsets.UTF_8);
        this.writeInt(bytes.length, off);
        off += 4;
        this.writeBytes(bytes, off, bytes.length);
        return this;
    }

    /**
     * Write UTF-8 encoded string
     * @param src Source string
     * @return This wrapper
     */
    public WrappedByteBuffer writeUtf(String src) {
        byte[] bytes = src.getBytes(StandardCharsets.UTF_8);
        this.writeInt(bytes.length);
        this.writeBytes(bytes);
        return this;
    }

    /**
     * Read UTF-8 encoded string with specified offset
     * @param off Target offset
     * @return Result string
     */
    public String readUtf(int off) {
        byte[] bytes = new byte[this.readInt(off)];
        off += 4;
        this.readBytes(bytes, off, bytes.length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Read UTF-8 encoded string
     * @return Result string
     */
    public String readUtf() {
        byte[] bytes = new byte[this.readInt(this.readOff)];
        this.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
    // UTF END

    // Enum START

    /**
     * Write enum (ordinal) with specified offset
     * @param e Source enum
     * @param off Target offset
     * @return This wrapper
     */
    public WrappedByteBuffer writeEnum(Enum<?> e, int off) {
        this.writeShort((short) e.ordinal(), off);
        return this;
    }

    /**
     * Write enum (ordinal)
     * @param e Source enum
     * @return This wrapper
     */
    public WrappedByteBuffer writeEnum(Enum<?> e) {
        this.writeShort((short) e.ordinal());
        return this;
    }

    /**
     * Read enum with specified offset
     * @param type Enum type
     * @param off Target offset
     * @return Result enum
     * @param <T> Enum type
     */
    public <T extends Enum<T>> T readEnum(Class<T> type, int off) {
        int index = this.readShort();
        return type.getEnumConstants()[index];
    }

    /**
     * Read enum
     * @param type Enum type
     * @return Result enum
     * @param <T> Enum type
     */
    public <T extends Enum<T>> T readEnum(Class<T> type) {
        T e = readEnum(type, this.readOff);
        this.readOff += 2;
        return e;
    }

    /**
     * Try to read enum
     * @param type Enum type
     * @return Result enum, or null if the type is not representing an enum class
     * @param <T> Enum type
     */
    public <T> T tryReadEnum(Class<T> type) {
        try {
            int index = this.readShort();
            return type.getEnumConstants()[index];
        } catch (NullPointerException ex) {
            return null;
        }
    }
    // Enum END

    // Iterable start

    /**
     * Write collections
     * @param col Source
     * @param writer Serializer
     * @param <T> Type
     */
    public <T> void writeCollection(Collection<T> col, BiConsumer<T, WrappedByteBuffer> writer) {
        this.writeInt(col.size());
        for (T t : col) {
            writer.accept(t, this);
        }
    }

    /**
     * Write string collection
     * @param col Source
     */
    public void writeStringCollection(Collection<String> col) {
        this.writeCollection(col, WrappedByteBuffer::stringIterableWriter);
    }

    /**
     * Read to ArrayList
     * @param reader Deserializer
     * @return Result
     * @param <T> Type
     */
    public <T> List<T> readList(Function<WrappedByteBuffer, T> reader) {
        int size = this.readInt();
        List<T> target = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            target.add(reader.apply(this));
        }
        return target;
    }

    /**
     * Read string to array list
     * @return Result
     */
    public List<String> readStringList() {
        return this.readList(WrappedByteBuffer::stringIterableReader);
    }

    /**
     * Read to HashSet
     * @param reader Deserializer
     * @return Result
     * @param <T> Type
     */
    public <T> Set<T> readSet(Function<WrappedByteBuffer, T> reader) {
        int size = this.readInt();
        Set<T> target = new HashSet<>(size);
        for (int i = 0; i < size; i++) {
            target.add(reader.apply(this));
        }
        return target;
    }

    /**
     * Read string to HashSet
     * @return Result
     */
    public Set<String> readStringSet() {
        return this.readSet(WrappedByteBuffer::stringIterableReader);
    }

    private static String stringIterableReader(WrappedByteBuffer wbb) {
        return wbb.readUtf();
    }

    private static void stringIterableWriter(String str, WrappedByteBuffer wbb) {
        wbb.writeUtf(str);
    }
    // Iterable end

    // UUID start
    public void writeUUID(UUID id) {
        this.writeLong(id.getMostSignificantBits());
        this.writeLong(id.getLeastSignificantBits());
    }

    public UUID readUUID() {
        return new UUID(this.readLong(), this.readLong());
    }
    // UUID end

    /**
     * Reset reader offset
     */
    public void resetReader() {
        this.readOff = 0;
    }

    /**
     * @return Reader offset
     */
    public int readerOffset() {
        return this.readOff;
    }

    /**
     * Reset ByteBuffer position
     */
    public void resetWriter() {
        this.src.position(0);
    }

    /**
     * @return ByteBuffer position
     */
    public int writeOffset() {
        return this.src.position();
    }

    public byte[] array() {
        return this.src.array();
    }

    public ByteBuffer src() {
        return this.src;
    }
}

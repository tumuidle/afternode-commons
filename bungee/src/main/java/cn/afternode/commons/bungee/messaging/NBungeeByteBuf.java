package cn.afternode.commons.bungee.messaging;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Netty version of BungeeByteBuffer
 * <br>
 * No need to shade netty, it's provided by proxy server
 */
public class NBungeeByteBuf {
    private final ByteBuf src;

    public NBungeeByteBuf(byte[] data) {
        this.src = Unpooled.copiedBuffer(data);
    }

    public NBungeeByteBuf() {
        this.src = Unpooled.buffer();
    }


}

package cn.afternode.commons.bungee.messaging;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Messaging helper with signing support
 */
public class BungeeMessagingHelper {
    private final Plugin plugin;
    private SecretKeySpec key;

    /**
     * Primary constructor
     * @param plugin Context plugin
     */
    public BungeeMessagingHelper(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Register an incoming and outgoing channel
     * @param channel Channel name
     * @param listener Message listener
     * @return Channel context
     */
    public MessageChannelContext register(String channel, IMessageListener listener) {
        MessageChannelContext context = new MessageChannelContext(channel, this, listener);
        ProxyServer proxy = ProxyServer.getInstance();
        proxy.registerChannel(channel);
        proxy.getPluginManager().registerListener(plugin, context);
        return context;
    }

    /**
     * Check if signing is available
     * @return Is signing available
     */
    public boolean signingAvailable() {
        return key != null && !key.isDestroyed();
    }

    /**
     * Set signing key (HmacSHA256)
     * @param key HmacSHA256 key, the recommended size is 64 bytes
     */
    public void setKey(String key) {
        this.key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    /**
     * Generate data sign
     * @param data Data
     * @return Sign
     * @throws NoSuchAlgorithmException Mac.getInstance error
     * @throws InvalidKeyException If the given key is inappropriate for initializing this MAC
     * @throws IllegalStateException Signing not available
     * @see #setKey(String)
     * @see Mac#getInstance(String)
     * @see Mac#init(Key)
     */
    public byte[] sign(byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
        if (!signingAvailable())
            throw new IllegalStateException("Signing not available");

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        return mac.doFinal(data);
    }

    /**
     * Sign data and combine to a byte array
     * @param buf Data
     * @return Combined sign and data
     * @throws NoSuchAlgorithmException Mac.getInstance error
     * @throws InvalidKeyException If the given key is inappropriate for initializing this MAC
     * @see #sign(byte[])
     * @see #validateCombined(NBungeeByteBuf)
     */
    public byte[] combineSign(NBungeeByteBuf buf) throws NoSuchAlgorithmException, InvalidKeyException {
        NBungeeByteBuf nBuf = new NBungeeByteBuf();

        byte[] data = buf.toArray();
        nBuf.writeBlock(sign(data));
        nBuf.writeBlock(data);
        return nBuf.toArray();
    }

    /**
     * Validate a sign
     * @param sign Sign
     * @param data Data
     * @return If sign is valid
     * @throws NoSuchAlgorithmException Mac.getInstance error
     * @throws InvalidKeyException If the given key is inappropriate for initializing this MAC
     */
    public boolean validate(byte[] sign, byte[] data) throws InvalidKeyException, NoSuchAlgorithmException {
        if (!signingAvailable())
            throw new IllegalStateException("Signing not available");

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        return Arrays.equals(sign, mac.doFinal(data));
    }

    /**
     * Validate combined signed data
     * @param buf Data
     * @return Data body, or null if sign is invalid
     * @throws NoSuchAlgorithmException Mac.getInstance error
     * @throws InvalidKeyException If the given key is inappropriate for initializing this MAC
     * @see #combineSign(NBungeeByteBuf)
     * @see #validate(byte[], byte[])
     */
    public NBungeeByteBuf validateCombined(NBungeeByteBuf buf) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] sign = buf.readBlock();
        byte[] data = buf.readBlock();
        if (!validate(sign, data))
            return null;
        return new NBungeeByteBuf(data);
    }

    /**
     * Get context plugin
     * @return Context plugin
     */
    public Plugin getPlugin() {
        return plugin;
    }
}

import cn.afternode.commons.bukkit.messaging.NBukkitByteBuf;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class TestBukkitByteBuf {
    @Test
    public void test() {
        UUID id = new UUID(114514, 1919810);

        NBukkitByteBuf bb = new NBukkitByteBuf();
        bb.writeUUID(id);
        byte[] result = bb.toArray();

        NBukkitByteBuf read = new NBukkitByteBuf(result);
        UUID rid = read.readUUID();
        System.out.printf("%s %s%n", rid.getMostSignificantBits(), rid.getLeastSignificantBits());
    }
}

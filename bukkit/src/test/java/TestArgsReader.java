import cn.afternode.commons.bukkit.message.ArgsReader;
import org.junit.jupiter.api.Test;

public class TestArgsReader {
    @Test
    public void a() {
        String[] args = {"a", "true", "\"hello", "world\"", "1.0", "5"};
        ArgsReader reader = new ArgsReader(args);
        System.out.println(reader.next());
        System.out.println(reader.readBoolean());
        System.out.println(reader.readString());
        System.out.println(reader.readDouble());
        System.out.println(reader.readInt());
        System.out.println(reader.next());
        System.out.println(reader.readString(2));
    }
}

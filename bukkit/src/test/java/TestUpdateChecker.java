import cn.afternode.commons.bukkit.update.ModrinthVersionInfo;
import cn.afternode.commons.bukkit.update.UpdateChecker;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestUpdateChecker {
    @Test
    public void testModrinth() throws IOException {
        ModrinthVersionInfo info = UpdateChecker.getModrinthVersion("general");
        System.out.println(info.getName());
        System.out.println(info.isNewerThanSemver("1.1.0"));
        System.out.println(info.isNewerThanSemver("2.1.0"));
        System.out.println(info.isNewerThanSemver("2.2.4"));
        System.out.println(info.isNewerThanSemver("2.3.0"));
    }
}

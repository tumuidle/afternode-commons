import cn.afternode.commons.bukkit.update.ModrinthVersionInfo;
import cn.afternode.commons.bukkit.update.SemVer;
import cn.afternode.commons.bukkit.update.UpdateChecker;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestUpdateChecker {
    @Test
    public void testModrinth() throws IOException {
        System.out.println("=== Modrinth ===");
        ModrinthVersionInfo info = UpdateChecker.getModrinthVersion("general");
        System.out.println(info.getName());
        System.out.println(info.isNewerThanSemver("1.1.0"));
        System.out.println(info.isNewerThanSemver("2.1.0"));
        System.out.println(info.isNewerThanSemver("2.2.1"));
        System.out.println(info.isNewerThanSemver("2.2.4"));
        System.out.println(info.isNewerThanSemver("2.3.0"));
    }

    @Test
    public void testHangar() throws IOException {
        System.out.println("=== Hangar ===");
        String ver = UpdateChecker.getHangarLatestVersion("ViaVersion");
        System.out.println(SemVer.isNewerThan("4.9.0", ver));
        System.out.println(SemVer.isNewerThan("v5.0.0", ver));
        System.out.println(SemVer.isNewerThan("5.0.2", ver));
        System.out.println(SemVer.isNewerThan("5.2.0", ver));
        System.out.println(SemVer.isNewerThan("6.2.1", ver));
        System.out.println(new SemVer(ver));
    }

    @Test
    public void testSpiget() throws IOException {
        System.out.println("=== Spiget ===");
        System.out.println(UpdateChecker.getSpigetLatestVersion("111381").getName());
    }
}

package cn.afternode.commons.bukkit.update;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Pruned modrinth version model
 * <br>
 * <a href="https://docs.modrinth.com/#tag/version_model">Modrinth Documentation</a>
 */
public class ModrinthVersionInfo {
    @SerializedName("game_versions")
    private List<String> gameVersions;
    @SerializedName("version_number")
    private String versionNumber;
    private String name;
    private String type;

    /**
     * Primary constructor
     * @param gameVersions Json "game_versions"
     * @param versionNumber Json "version_number"
     * @param name Json "name"
     * @param type Json "type"
     */
    public ModrinthVersionInfo(List<String> gameVersions, String versionNumber, String name, String type) {
        this.gameVersions = gameVersions;
        this.versionNumber = versionNumber;
        this.name = name;
        this.type = type;
    }

    /**
     * Get supported game versions
     * @return Versions
     */
    public List<String> getGameVersions() {
        return gameVersions;
    }

    /**
     * Get version number
     * @return version number
     */
    public String getVersionNumber() {
        return versionNumber;
    }

    /**
     * Get version name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get version type
     * @return version type (release/beta/alpha)
     */
    public String getType() {
        return type;
    }

    /**
     * Compare with simple number version (float)
     * @param number Target
     * @return If float parse result of versionNumber is newer than provided number
     * @see ModrinthVersionInfo#versionNumber
     */
    public boolean isNewerThanNumber(float number) {
        return Float.parseFloat(versionNumber) > number;
    }

    /**
     * Convert versionNumber to SemVer
     * @return SemVer
     * @see SemVer
     */
    public SemVer toSemVer() {
        return new SemVer(this.versionNumber);
    }

    /**
     * Compare with SemVer
     * @param version Target
     * @return If SemVer parse result of versionNumber is newer that provided number
     * @see SemVer
     */
    public boolean isNewerThanSemver(String version) {
        SemVer target = new SemVer(version);
        SemVer self = new SemVer(this.versionNumber);

        return self.getMajor() > target.getMajor() || self.getMinor() > target.getMinor() || self.getPatch() > target.getPatch();
    }
}

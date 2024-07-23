package cn.afternode.commons.bukkit.update;

/**
 * Pruned Spiget version information model
 * <br>
 * <a href="https://rest.wiki/?https://raw.githubusercontent.com/SpiGetOrg/Documentation/master/swagger.yml">Official documentation</a>
 */
public class SpigetVersionInfo {
    private String name;
    private long releaseDate;

    /**
     * Get version name
     * @return Version name (e.g. v1.0).
     */
    public String getName() {
        return name;
    }

    /**
     * Get release date time stamp
     * @return Timestamp of the version's release date.
     */
    public long getReleaseDate() {
        return releaseDate;
    }

    /**
     * Compare with simple number version (float)
     * @param number Target
     * @return If float parse result of versionNumber is newer than provided number
     * @see SpigetVersionInfo#name
     */
    public boolean isNewerThanNumber(float number) {
        return Float.parseFloat(name) > number;
    }

    /**
     * Convert name to SemVer
     * @return SemVer
     * @see SemVer
     */
    public SemVer toSemVer() {
        return new SemVer(this.name);
    }

    /**
     * Compare with SemVer
     * @param version Target
     * @return If SemVer parse result of name is newer that provided number
     * @see SemVer
     */
    public boolean isNewerThanSemver(String version) {
        return SemVer.isNewerThan(version, this.name);
    }
}

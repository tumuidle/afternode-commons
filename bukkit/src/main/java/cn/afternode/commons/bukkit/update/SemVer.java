package cn.afternode.commons.bukkit.update;

/**
 * Simple SemVer parser
 */
public class SemVer {
    private final int major;
    private final int minor;
    private final int patch;

    private final String extra;

    /**
     * Parse SemVer
     * @param version Version
     */
    public SemVer(String version) {
        String[] parts = version.split("-");
        String[] vParts = parts[0].split("\\.");
        major = Integer.parseInt(vParts[0]);
        minor = Integer.parseInt(vParts[1]);
        if (parts.length >= 3) {
            patch = Integer.parseInt(vParts[2]);
        } else {
            patch = 0;
        }

        StringBuilder ex = new StringBuilder();
        for (int i = 1; i < parts.length; i++) {
            if (!ex.isEmpty())
                ex.append("-");
            ex.append(parts[i]);
        }
        extra = ex.toString();
    }

    /**
     * Get major number
     * @return Major
     */
    public int getMajor() {
        return major;
    }

    /**
     * Get minor number
     * @return Minor
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Get patch number
     * @return Patch
     */
    public int getPatch() {
        return patch;
    }

    /**
     * Get extra string in version (after MAJOR.MINOR.PATCH-)
     * @return Extra string
     */
    public String getExtra() {
        return extra;
    }
}

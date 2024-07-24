package cn.afternode.commons.bungee.update;

/**
 * Simple SemVer parser
 */
public class SemVer {
    private final String src;

    private final int major;
    private final int minor;
    private final int patch;

    private final String extra;

    /**
     * Parse SemVer
     * @param version Version
     */
    public SemVer(String version) {
        int numIndex = -1;
        char[] cs = version.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            try {
                Integer.parseInt(String.valueOf(cs[i]));
                numIndex = i;
                break;
            } catch (NumberFormatException ignored) {}
        }
        if (numIndex == -1)
            throw new IllegalArgumentException("Cannot parse string %s as SemVer".formatted(version));

        this.src = version.substring(numIndex);

        String[] parts = src.split("-");
        String[] vParts = parts[0].split("\\.");
        major = Integer.parseInt(vParts[0]);
        minor = Integer.parseInt(vParts[1]);
        if (vParts.length >= 3) {
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

    @Override
    public String toString() {
        return "SemVer{" +
                "src='" + src + '\'' +
                ", major=" + major +
                ", minor=" + minor +
                ", patch=" + patch +
                ", extra='" + extra + '\'' +
                '}';
    }

    /**
     * Compare semver
     * @param fromStr From
     * @param targetStr Target
     * @return Is target newer than from
     */
    public static boolean isNewerThan(String fromStr, String targetStr) {
        SemVer target = new SemVer(targetStr);
        SemVer from = new SemVer(fromStr);

        if (target.major != from.major) {
            return target.major > from.major;
        } else if (target.minor != from.minor) {
            return target.minor > from.minor;
        }
        return target.patch > from.patch;
    }
}

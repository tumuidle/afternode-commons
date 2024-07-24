package cn.afternode.commons.bungee.update;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UpdateChecker {
    private static final String URL_ENCODED_LOADERS = URLEncoder.encode("[\"paper\"]", StandardCharsets.UTF_8);

    /**
     * <a href="https://docs.modrinth.com/#tag/versions/operation/getProjectVersions">Modrinth Documentation</a>
     * @param query Project ID or Slug
     * @return Latest version info
     */
    public static ModrinthVersionInfo getModrinthVersion(String query) throws IOException {
        List<ModrinthVersionInfo> version = new Gson().fromJson(
                get("https://api.modrinth.com/v2/project/%s/version?loaders=%s"
                        .formatted(URLEncoder.encode(query, StandardCharsets.UTF_8), URL_ENCODED_LOADERS)),
                new TypeToken<List<ModrinthVersionInfo>>() {}.getType());
        return version.get(0);
    }

    public static String getHangarLatestVersion(String slug) throws IOException {
        return get("https://hangar.papermc.io/api/v1/projects/%s/latestrelease".formatted(slug));
    }

    public static SpigetVersionInfo getSpigetLatestVersion(String resourceId) throws IOException {
        return new Gson().fromJson(get("https://api.spiget.org/v2/resources/%s/versions/latest".formatted(resourceId)), SpigetVersionInfo.class);
    }

    private static String get(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();

        if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
            throw new IOException("Http GET failed: " + con.getResponseMessage());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream in = con.getInputStream()) {
            int nRead;
            byte[] data = new byte[4096];

            while ((nRead = in.read(data, 0, data.length)) != -1) {
                baos.write(data, 0, nRead);
            }
        }

        con.disconnect();
        return baos.toString(StandardCharsets.UTF_8);
    }
}

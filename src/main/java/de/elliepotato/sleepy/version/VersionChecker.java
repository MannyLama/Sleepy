package de.elliepotato.sleepy.version;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.elliepotato.sleepy.Sleepy;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author Ellie :: 27/07/2019
 */
public class VersionChecker {

    private static final String DOMAIN = "elliepotato.de";
    private static final String API_VERSION = "v1";

    private static final String ENDPOINT = "plugin";
    private static final String METHOD = "GetVersion";

    private static final String PLUGIN_ID = "sleepy";

    private final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public VersionChecker(Sleepy sleepy) {

        sleepy.getServer().getScheduler().runTaskAsynchronously(sleepy, () -> {
            Thread.currentThread().setName("sleepy-version-checker");

            VersionParser selfVersion = new VersionParser(sleepy.getDescription().getVersion());
            if (selfVersion.getClassifier().equals("DEV")) {
                Sleepy.debug("This is a developer build so a version check will not be carried out");
                return;
            }

            VersionParser latestVersion = new VersionParser(getLatestVersion(sleepy));

            if (latestVersion.getFullVersion() == null) return;

            if (latestVersion.getVersion() > selfVersion.getVersion()
                    || latestVersion.getSubVersion() > selfVersion.getSubVersion()) {
                sleepy.setUpdateMeMessage(ChatColor.BOLD + "Plugin outdated!" + ChatColor.RESET + " New version: " + ChatColor.RED + latestVersion.getFullVersion()
                        + ChatColor.WHITE + " (Your version: " + ChatColor.RED + selfVersion.getFullVersion() + ChatColor.WHITE + ").");
                sleepy.logInfo(ChatColor.stripColor(sleepy.getUpdateMeMessage()));
                return;
            }

            // If they have a snapshot build
            // And the latest version is BIGGER OR SAME
            // And SUB build is BIGGER

            if (latestVersion.getClassifier().equals("RELEASE") && selfVersion.getClassifier().equals("SNAPSHOT")
                    && selfVersion.getVersion() <= latestVersion.getVersion()
                    && selfVersion.getSubVersion() <= latestVersion.getSubVersion()) {

                sleepy.setUpdateMeMessage(ChatColor.BOLD + "Plugin outdated!" + ChatColor.RESET + " A stable version was released: " + ChatColor.RED + latestVersion.getFullVersion()
                        + ChatColor.WHITE + " (Your version: " + ChatColor.RED + selfVersion.getFullVersion() + ChatColor.WHITE + ").");
                sleepy.logInfo(ChatColor.stripColor(sleepy.getUpdateMeMessage()));
                return;
            }

            Sleepy.debug("Version up to date.");

        });


    }

    private String getLatestVersion(Sleepy sleepy) {
        try {
            URL url = new URL("http://" + DOMAIN + "/api/" + API_VERSION);
            // URL url = new URL("http://localhost:9489/api/" + API_VERSION);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);

            Sleepy.debug("ready for write");

            // Write
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            writer.write(gson.toJson(new PayloadWrapper()
                    .setEndpoint(ENDPOINT)
                    .setMethod(METHOD)
                    .addPayload("plugin-id", PLUGIN_ID)));

            Sleepy.debug(gson.toJson(new PayloadWrapper()
                    .setEndpoint(ENDPOINT)
                    .setMethod(METHOD)
                    .addPayload("plugin-id", PLUGIN_ID)));

            writer.close();

            // Read
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            final String rawOutput = reader.readLine();

            Sleepy.debug("raw out :" + rawOutput);

            final PayloadWrapper response = gson.fromJson(rawOutput, new TypeToken<PayloadWrapper>() {
            }.getType());

            if (response.getResponseCode() != 200) {
                sleepy.logError("API server returned code " + response.getResponseCode() + ": " + response.getResponseMessage() + " when checking version.");
                return null;
            }


            reader.close();
            connection.disconnect();

            return response.getResponseMessage();

        } catch (IOException e) {
            sleepy.logError("Failed to check for an update! Ensure '" + DOMAIN + "' is whitelisted! Maybe server is down?");
            e.printStackTrace();
        }

        return "";
    }


}

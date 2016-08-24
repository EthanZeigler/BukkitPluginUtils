package com.ethanzeigler.bukkitpluginutils.update;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ethan on 8/13/16.
 */
public class RemoteUpdateChecker {

    /**
     * Runs an update check on the plugin. This connects to the bukget API, which updates every 6 hours. Data will
     * not be immediately accurate. <p>
     * If the returned data seems completely inaccurate (as in the plugin is being mistaken for another), bukkitdev
     * knows your plugin by another name. Load bukkit.org and open the plugin page. Instead, use
     * {@link RemoteUpdateChecker#updateCheck(String, String)} with the name of the plugin as listed in the page URL.
     * </p>
     *
     * @param plugin The plugin to check for updates
     * @return whether or not there
     */
    public static UpdateCallback updateCheck(JavaPlugin plugin) {
        return updateCheck(plugin.getName().toLowerCase().replace(" ", ""), plugin.getDescription().getVersion());
    }

    /**
     * Runs an update check on the plugin. This connects to the bukget API, which updates every 6 hours. Data will
     * not be immediately accurate. <p>
     * This overloaded method allows the user to override the automatic slug generation done with
     * {@link RemoteUpdateChecker#updateCheck(JavaPlugin)} with the name of the plugin as listed in the page URL.
     * </p>
     *
     * @param slug               the plugin's name as defined in the URL in it's bukkitdev page.
     * @param currentVersionName the exact version found in the plugin yml, which can be found with
     *                           {@link PluginDescriptionFile#getVersion()} from {@link JavaPlugin#getDescription()}
     * @return Update information in the form of an UpdateCallback Object
     */
    public static UpdateCallback updateCheck(String slug, String currentVersionName) {
        VersionSet versionSet = getVersionData(slug);

        // get this version and compare it to the existing one
        if (!versionSet.getRelease().getVersionNumber().equals(currentVersionName)) {
            // there's an update
            return new UpdateCallback(versionSet.getRelease(), true);
        } else {
            return new UpdateCallback(versionSet.getRelease(), false);
        }
    }

    /**
     * Connects to Bukget using the given slug and returns the version data provided.
     * @param slug plugin name as stated in it's BukkitDev URL
     * @return VersionSet of data returned by BukkitDev
     */
    public static VersionSet getVersionData(String slug) {
        // contstruct URL to connect to at the Bukget API
        String stringUrl = "http://api.bukget.org/3/updates?slugs=" + slug.toLowerCase().replace(" ", "");

        //Connect to the Bukget API and write the received data to rawBukgetResponse if conenction is valid
        JsonArray rawBukgetResponse;
        try {
            HttpURLConnection request = null;
            URL url = new URL(stringUrl);
            request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser parser = new JsonParser();
            JsonElement root = parser.parse(new InputStreamReader((InputStream) request.getContent()));
            rawBukgetResponse = root.getAsJsonArray();
        } catch (IOException e) {
            System.out.println("Could not connect to the Bukget API for an update check. The site could be down or " +
                    "there is no internet connection.");
            return null;
        }

        // check number of plugins that match the slug. if not one, take appropriate action
        if (rawBukgetResponse.size() > 1) {
            System.out.println("Multiple plugins found under the slug: " + slug + ". The " +
                    "update check has failed.");
            return null;
        } else if (rawBukgetResponse.size() == 0) {
            System.out.println("No data was found for the plugin slug: " + slug + ". If this is a new plugin, this " +
                    "is expected as Bukget only updates once every 6 hours. The update check has failed.");
            return null;
        } else {
            // one result from plugin slug. Take JSON data and make it into a developer friendly format
            JsonObject jsonPluginData = rawBukgetResponse.get(0).getAsJsonObject();
            System.out.println("Performing update check for " + jsonPluginData.get("plugin_name") + " with the slug " +
                    jsonPluginData.get("slug"));

            JsonObject jsonVersionData = jsonPluginData.get("versions").getAsJsonObject();

            VersionSet versionSet;
            PluginVersion alpha = null, beta = null, release = null, latest = null;

            // read each version in the JSON response. If there is no data, the version will be null.
            for (ReleaseTag currentTag : ReleaseTag.values()) {
                JsonObject currentVersion;
                PluginVersion version;
                String versionNumber;

                if (jsonVersionData.get(currentTag.getLowerCase()) == null) {
                    continue; // there is data for this version tag
                }

                currentVersion = jsonVersionData.get(currentTag.getLowerCase()).getAsJsonObject();
                version = new PluginVersion(currentVersion.get("version").getAsString(), currentTag);

                switch (currentTag) {
                    case ALPHA:
                        alpha = version;
                        break;
                    case BETA:
                        beta = version;
                        break;
                    case RELEASE:
                        release = version;
                        break;
                    case LATEST:
                        latest = version;
                }
            }

            // combine data
            versionSet = new VersionSet(alpha, beta, release, latest);
            return versionSet;
        }
    }
}

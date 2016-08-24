package com.ethanzeigler.bukkitpluginutils;

import com.ethanzeigler.bukkitpluginutils.language.LanguageManager;
import com.ethanzeigler.bukkitpluginutils.update.UpdateNotifier;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.*;

/**
 * A extended plugin that adds lots of features that Bukkit Developers used to have to write over and over.
 */
public abstract class BukkitUtilPlugin extends JavaPlugin {
    private static final String UTILS_FILE_FOLDER = "BukkitPluginUtils/";
    private static final String UTILS_FILE_NAME = "BPUData.yml";
    public static final String BPU_VERSION = "1";


    private FileConfiguration utilsFile;
    private boolean isProperlyLoaded = false;
    private String pluginDirectoryPath;
    private UpdateNotifier updateNotifier;
    private BPUOptions options;

    public BukkitUtilPlugin() {
        initializeOptions();
    }

    /**
     * @param loader
     * @param server
     * @param description
     * @param dataFolder
     * @param file
     * @deprecated
     */
    public BukkitUtilPlugin(PluginLoader loader, Server server, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, server, description, dataFolder, file);
        initializeOptions();
    }

    public BukkitUtilPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        initializeOptions();
    }

    /**
     * Loads a file from the plugin folder with the given name. If the file does not exist, it will be created.
     * An IOException will occur if the file cannot be created or if loading fails.
     *
     * @param path the name of the file and it's path in the plugin folder
     * @return The {@link YamlConfiguration} specified by the path.
     * @throws IOException if the file fails to load or the file does not exist and cannot be created.
     */
    public FileConfiguration getFile(String path) {
        validateState();
        File file = null;
        try {
            file = new File(pluginDirectoryPath + path);
            file.createNewFile();

            return YamlConfiguration.loadConfiguration(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(new IOException(
                    "Could not load file:" + file.getPath()));
        }
    }

    /**
     * Logs the given message to the console along with the plugin's name.
     * @param msg the message to log
     */
    public void logToConsole(String msg) {
        logToConsole(msg, null);
    }

    /**
     * Logs the given message to the console along with the plugin's name.
     * @param msg the message to log
     * @param startColor the color to start the message with. Most consoles do not support this.
     */
    public void logToConsole(String msg, ChatColor startColor) {
        this.getLogger().info(LanguageManager.getBeautifiedMessage(startColor, msg, options.getPluginPrefix()));
    }

    /**
     * Called when the plugin is loaded by Bukkit to initialize the settings so users can manipulate it in the onEnable
     * method
     */
    private void initializeOptions() {
        /*
        load extra data from plugin.yml
        utils:
          prefix:
          update:
            enabled: true
            changelog:
            permission_node:
        - default plugin prefix
        - changelog
        - update notification permission
         */
        BPUOptions options = new BPUOptions(this.getDescription().getFullName());

        try {
            FileConfiguration yml = YamlConfiguration.loadConfiguration(
                    new BufferedReader(new FileReader(getClass().getResource("plugin.yml").getFile())));
            // load extra data
            // null check is part of the options class
            options.setPluginPrefix((String) yml.get("utils.prefix"));
            options.setUpdateCheckEnabled((Boolean) yml.get("utils.update.enabled"));
            options.setChangelog((String) yml.get("utils.update.changelog"));
            options.setUpdateAlertPermission((String) yml.get("utils.update.permission_node"));
        } catch (FileNotFoundException | ClassCastException e) {
            e.printStackTrace();
        }

        // initialize other data that can be done before the second load
        pluginDirectoryPath = getDataFolder().getPath() + "/";

        // create data folder
        new File(pluginDirectoryPath).mkdirs();
    }


    /**
     * Loads the BPU resources that must be loaded after the options are set. Not running this method can cause errors
     * later and without warning.
     */
    public void initializeBPUResources() {
        updateNotifier = new UpdateNotifier(this);

        // all loaded
        isProperlyLoaded = true;
    }

    /**
     * Validates that all resources are properly loaded. If not, a FailedInitializationError will be thrown.
     */
    private void validateState() {
        if (!isProperlyLoaded) {
            throw new RuntimeException(new FailedInitializationError());
        }
    }

    /*
    ===================== Data File =========================
     */

    /**
     * Gets the API's utility file
     * @return the API's utility file
     */
    public FileConfiguration getUtilsFile() {
        if (utilsFile != null) {
            return utilsFile;
        } else {
            try {
                new File(pluginDirectoryPath + UTILS_FILE_FOLDER).mkdirs();
                File configFile = new File(pluginDirectoryPath + UTILS_FILE_FOLDER + UTILS_FILE_NAME);
                configFile.createNewFile();
                utilsFile = YamlConfiguration.loadConfiguration(configFile);
                utilsFile.options().header(options.getFileHeader());
                saveUtilsFile();
                return utilsFile;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Saves the API's utility file
     */
    public void saveUtilsFile() {
        try {
            utilsFile.save(pluginDirectoryPath + UTILS_FILE_FOLDER + UTILS_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the player's yml file from the Player Files directory of the plugin folder. If it does not exist, it will
     * be created.
     * @param player the player who's file should be loaded.
     * @return the player's file.
     */
    public FileConfiguration getPlayerFile(OfflinePlayer player) {
        try {
            File file = new File(String.format(pluginDirectoryPath + "Player Files/%s.txt", player.getUniqueId()));
            file.createNewFile();

            return YamlConfiguration.loadConfiguration(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(new IOException(
                    String.format("Could not load player file: %s/%s", player.getName(), player.getUniqueId())));
        }
    }

    /**
     * Saves the player's file.
     * @param file the player's file
     * @param player the player who's file is being saved.
     */
    public void savePlayerFile(FileConfiguration file, OfflinePlayer player) {
        try {
            file.save(new File(String.format(pluginDirectoryPath + "Player Files/%s.txt", player.getUniqueId())));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(new IOException(
                    String.format("Could not save player file: %s/%s", player.getName(), player.getUniqueId())));
        }
    }

    /**
     * Returns the plugin's file directory as a String
     * @return the plugin's file directory as a String
     */
    public String getPluginFileDirectory() {
        return pluginDirectoryPath;
    }

    /**
     * Gets the API's options. Changing these settings before calling {@link BukkitUtilPlugin#initializeBPUResources()}
     * @return the API's options
     */
    public BPUOptions getBPUOptions() {
        return options;
    }

    /**
     * Runs a runnable synchronously. Simply a convenience method.
     * @param runnable the runnable to run synchronously.
     */
    public void runSynchronously(Runnable runnable) {
        getServer().getScheduler().runTask(this, runnable);
    }

    /**
     * Runs a runnable asynchronously. Simply a convenience method.
     * @param runnable the runnable to run asynchronously.
     */
    public void runAsynchronously(Runnable runnable) {
        getServer().getScheduler().runTaskAsynchronously(this, runnable);
    }
}

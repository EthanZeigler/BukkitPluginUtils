package com.ethanzeigler.bukkitpluginutils.update;

import com.ethanzeigler.bukkitpluginutils.BukkitUtilPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A library of systems checking for updates within a BukkitUtilPlugin.
 */
public class UpdateNotifier implements Listener {
    private static final String LAST_VERSION_PATH = "last_version";
    private static final String BPU_FILE_VERSION_PATH = "BPU_version";

    private BukkitUtilPlugin plugin;
    private boolean isARemoteUpdate = false;
    private boolean hasFileUpdated = false;
    private int taskId;
    private List<UUID> notifiedOfFileUpdate = new ArrayList<>();
    private List<UUID> notifiedOfRemoteUpdate = new ArrayList<>();

    /**
     * A library of systems checking for updates within a BukkitUtilPlugin.
     * @param plugin the BukkitUtilPlugin being used

     */
    public UpdateNotifier(BukkitUtilPlugin plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // remote update, if enabled
        if (plugin.getBPUOptions().isUpdateCheckOn()) {
            taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(
                    plugin, () -> checkForRemoteUpdates(), 0, plugin.getBPUOptions().getTicksBetweenUpdateChecks());
        }


        // file update
        hasFileUpdated = hasFileUpdated();
        if (hasFileUpdated) {
            Bukkit.broadcast(plugin.getBPUOptions().getUpdateInstalledMessage() + plugin.getBPUOptions().getChangelog()
                    , plugin.getBPUOptions().getUpdateAlertPermission());
        }
    }

    /**
     * Gets whether the plugin has updated since it's last use. This is dependent on the data file set
     * in the BukkitPluginUtils folder of the plugin. The last version as specified by the plugin.yml is saved here.
     * When the plugin loads, if the version in the YAML does not match the version saved by BukkitPluginUtils,
     * true will be returned, and the value in the file will be changed to the new version.
     * @return whether or not the plugin has updated.
     */
    public boolean hasFileUpdated() {
        FileConfiguration file = plugin.getUtilsFile();
        String version = (String) file.get(LAST_VERSION_PATH);
        boolean isFileInitialized = file.get(LAST_VERSION_PATH) != null;

        // if there is no version data set or it does not match the recorded version, the file updated
        if (!isFileInitialized) {
            file.set(LAST_VERSION_PATH, plugin.getDescription().getVersion());
            file.set(BPU_FILE_VERSION_PATH, BukkitUtilPlugin.BPU_VERSION);
            plugin.saveUtilsFile();
        } else if (version == null || !version.equals(plugin.getDescription().getVersion())) {
            file.set(LAST_VERSION_PATH, plugin.getDescription().getVersion());
            file.set(BPU_FILE_VERSION_PATH, BukkitUtilPlugin.BPU_VERSION);
            plugin.saveUtilsFile();
            return true;
        }
        return false;
    }

    /**
     * Checks for updates to the plugin using {@link RemoteUpdateChecker} asynchronously. If an update is found, moderators
     * will be alerted.
     */
    public void checkForRemoteUpdates() {
        plugin.runAsynchronously(() -> {
            UpdateCallback callback = RemoteUpdateChecker.updateCheck(plugin);

            if (callback.isAnUpdate()) {
                plugin.getServer().getScheduler().cancelTask(taskId);
                plugin.runSynchronously(() -> {
                    Bukkit.broadcast(plugin.getBPUOptions().getUpdateAvailableMessage(),
                            plugin.getBPUOptions().getUpdateAlertPermission());
                });
            }
        });
    }


    /**
     * Listener for player joins so update messages can be sent to moderators.
     * @param e
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (isARemoteUpdate && e.getPlayer().hasPermission(plugin.getBPUOptions().getUpdateAlertPermission())) {
            if (!notifiedOfRemoteUpdate.contains(e.getPlayer().getUniqueId())) {
                e.getPlayer().sendMessage(plugin.getBPUOptions().getUpdateAvailableMessage());
                notifiedOfRemoteUpdate.add(e.getPlayer().getUniqueId());
            }
        }

        if (hasFileUpdated && e.getPlayer().hasPermission(plugin.getBPUOptions().getUpdateAlertPermission())) {
            if (!notifiedOfFileUpdate.contains(e.getPlayer().getUniqueId())) {
                e.getPlayer().sendMessage(plugin.getBPUOptions().getUpdateInstalledMessage() +
                        plugin.getBPUOptions().getChangelog());
                notifiedOfFileUpdate.add(e.getPlayer().getUniqueId());
            }
        }
    }
}

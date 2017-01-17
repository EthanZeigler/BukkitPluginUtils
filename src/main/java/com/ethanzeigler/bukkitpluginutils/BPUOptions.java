package com.ethanzeigler.bukkitpluginutils;

/**
 * Holds the optins of the API
 */
public class BPUOptions {
    private boolean isUpdateCheckOn = true;
    private String updateAvailableMessage = "There is an update available for ";
    private String updateInstalledMessage;
    private String fileHeader = "This is a data file from the BukkitPluginsUtils API. Please leave this alone. Thanks!";
    private String updateAlertPermission = "Unassigned";
    private String pluginPrefix = "";
    private String changelog;
    private long ticksBetweenUpdateChecks = 864000L;
    private boolean isUpdateInstalledMessageOn = true;

    public BPUOptions(String pluginName ) {
        updateInstalledMessage = pluginName + " has updated. Here's what changed: ";
    }

    /**
     *  Whether BPU is currently checking for updates.
     * @return
     */
    public boolean isUpdateCheckOn() {
        return isUpdateCheckOn;
    }

    /**
     * Sets whether BPU should check for updates.
     * @param updateCheckOn
     */
    public void setUpdateCheckEnabled(Boolean updateCheckOn) {
        if (!isNull(updateCheckOn)) {
            isUpdateCheckOn = updateCheckOn;
        }
    }

    /**
     * Gets the message that will be sent to players with the update alert permission when there is a remote update
     * available
     * @return the message sent to players when there is an update
     */
    public String getUpdateAvailableMessage() {
        return updateAvailableMessage;
    }

    /**
     * Sets the message that will be sent to players with the update alert permission when there is a remote update
     * available
     * @param updateAvailableMessage the message sent to players when there is an update
     */
    public void setUpdateAvailableMessage(String updateAvailableMessage) {
        if (!isNull(updateAvailableMessage)) {
            this.updateAvailableMessage = updateAvailableMessage;
        }
    }

    /**
     * Gets the file header of the BukkitPluginUtils file
     * @return the file header of the BukkitPluginUtils file
     */
    public String getFileHeader() {
        return fileHeader;
    }

    /**
     * Sets the file header of the BukkitPluginUtils file
     * @param fileHeader the file header of the BukkitPluginUtils file
     */
    public void setFileHeader(String fileHeader) {
        if (!isNull(fileHeader)) {
            this.fileHeader = fileHeader;
        }
    }

    /**
     * Gets the update alert permission needed to get a message when there is a remote plugin update or the plugin
     * was updated
     * @return the update alert permission needed to get a message when there is a remote plugin update or the plugin
     * was updated
     */
    public String getUpdateAlertPermission() {
        return updateAlertPermission;
    }

    /**
     * Sets the update alert permission needed to get a message when there is a remote plugin update or the plugin
     * was updated. This is already set if it is defined in the plugin.yml, but will be overriden if changed here.
     * @return the update alert permission needed to get a message when there is a remote plugin update or the plugin
     * was updated
     */
    public void setUpdateAlertPermission(String updateAlertPermission) {
        if (!isNull(updateAlertPermission)) {
            this.updateAlertPermission = updateAlertPermission;
        }
    }

    /**
     * Gets the prefix the plugin uses when sending messages
     * @return the prefix the plugin uses when sending messages
     */
    public String getPluginPrefix() {
        return pluginPrefix;
    }

    /**
     * Sets the prefix the plugin uses when sending messages. If this is defined in the plugin.yml, this will override
     * that setting.
     * @return the prefix the plugin uses when sending messages
     */
    public void setPluginPrefix(String pluginPrefix) {
        if (!isNull(pluginPrefix)) {
            this.pluginPrefix = pluginPrefix;
        }
    }

    /**
     * Gets the number of ticks between remote update checks
     * @return the number of ticks between remote update checks
     */
    public long getTicksBetweenUpdateChecks() {
        return ticksBetweenUpdateChecks;
    }

    /**
     * Gets the number of ticks between remote update checks. If this is already defined in the plugin.yml, it will be
     * overridden by the new setting.
     * @return the number of ticks between remote update checks
     */
    public void setTicksBetweenUpdateChecks(Long ticksBetweenUpdateChecks) {
        if (!isNull(ticksBetweenUpdateChecks)) {
            this.ticksBetweenUpdateChecks = ticksBetweenUpdateChecks;
        }
    }

    /**
     * Gets the message sent when an update is installed
     * @return the message sent when an update is installed
     */
    public String getUpdateInstalledMessage() {
        return updateInstalledMessage;
    }

    /**
     * Sets the message sent when an update is installed. If this is already defined in the plugin.yml, it will be
     * overridden with the new setting.
     * @param updateInstalledMessage the message sent when an update is installed
     */
    public void setUpdateInstalledMessage(String updateInstalledMessage) {
        if (!isNull(updateInstalledMessage)) {
            this.updateInstalledMessage = updateInstalledMessage;
        }
    }

    /**
     * Gets this verison's plugin changelog.
     * @return this version's plugin changelog.
     */
    public String getChangelog() {
        return changelog;
    }

    /**
     * Sets this verison's plugin changelog. If it is already defined in the plugin.yml, it will be overridden with
     * the new setting.
     * @return this version's plugin changelog.
     */
    public void setChangelog(String changelog) {
        if (!isNull(changelog)) {
            this.changelog = changelog;
        }
    }

    /**
     * Gets whether or not the argument is null.
     * @param obj the argument to check
     * @return whether or not the argument is null
     */
    private boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * Gets whether or not the local update message is enabled.
     * @return whether or not the local update message is enabled.
     */
    public boolean isUpdateInstalledMessageOn() {
        return isUpdateInstalledMessageOn;
    }

    /**
     * Sets whether or not the local update message is enabled.
     */
    public void setUpdateInstalledMessageOn(Boolean updateInstalledMessageOn) {
        if (!isNull(updateInstalledMessageOn)) {
            isUpdateInstalledMessageOn = updateInstalledMessageOn;
        }
    }
}

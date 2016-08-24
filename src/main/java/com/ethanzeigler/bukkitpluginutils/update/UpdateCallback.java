package com.ethanzeigler.bukkitpluginutils.update;

/**
 * Created by Ethan on 8/13/16.
 */
public class UpdateCallback {
    private PluginVersion pluginVersion;
    private boolean isAnUpdate;

    public UpdateCallback(PluginVersion pluginVersion, boolean isAnUpdate) {
        this.pluginVersion = pluginVersion;
        this.isAnUpdate = isAnUpdate;
    }

    public PluginVersion getPluginVersion() {
        return pluginVersion;
    }

    public boolean isAnUpdate() {
        return isAnUpdate;
    }
}

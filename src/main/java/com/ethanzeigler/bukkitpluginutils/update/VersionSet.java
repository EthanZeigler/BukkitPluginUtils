package com.ethanzeigler.bukkitpluginutils.update;

/**
 * Created by Ethan on 8/14/16.
 */
public class VersionSet {
    private final PluginVersion alpha;
    private final PluginVersion beta;
    private final PluginVersion release;
    private final PluginVersion latest;

    public VersionSet(PluginVersion alpha, PluginVersion beta, PluginVersion release, PluginVersion latest) {
        this.alpha = alpha;
        this.beta = beta;
        this.release = release;
        this.latest = latest;
    }

    /**
     * Gets the last alpha version of the plugin. This does not necessarily mean the latest.
     * @return the last alpha version of the plugin.
     */
    public PluginVersion getAlpha() {
        return alpha;
    }

    /**
     * Gets the last beta version of the plugin. This does not necessarily mean the latest.
     * @return the last beta version of the plugin.
     */
    public PluginVersion getBeta() {
        return beta;
    }

    /**
     * Gets the last release of the plugin as in the "release" state such as beta and alpha.
     * This does not necessarily mean the latest
     * @return the latest release version of the plugin.
     */
    public PluginVersion getRelease() {
        return release;
    }

    /**
     * Gets the latest version of the plugin.
     * @return the latest version of the plugin.
     */
    public PluginVersion getLatest() {
        return latest;
    }
}

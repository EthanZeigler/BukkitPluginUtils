package com.ethanzeigler.bukkitpluginutils.update;

/**
 * Created by Ethan on 8/13/16.
 */
public class PluginVersion {
    public final String versionNumber;
    public final ReleaseTag releaseTag;

    public PluginVersion(String versionString, ReleaseTag releaseTag) {
        this.versionNumber = versionString;
        this.releaseTag = releaseTag;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public ReleaseTag getReleaseTag() {
        return releaseTag;
    }
}

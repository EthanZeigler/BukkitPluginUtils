package com.ethanzeigler.bukkitpluginutils.update;

/**
 * Created by Ethan on 8/13/16.
 */
public enum ReleaseTag {
    ALPHA,
    BETA,
    RELEASE,
    LATEST;

    public String getLowerCase() {
        return toString().toLowerCase();
    }
}

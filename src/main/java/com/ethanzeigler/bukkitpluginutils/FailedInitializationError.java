package com.ethanzeigler.bukkitpluginutils;

/**
 * Created by Ethan on 8/13/16.
 */
public class FailedInitializationError extends Exception {
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public FailedInitializationError() {
        super("BukkitPluginUtils was not loaded properly. You must call BukkitUtilPlugin#initializeBPUResources " +
                "before using this function.");
    }


}

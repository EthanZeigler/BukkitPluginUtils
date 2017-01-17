package com.ethanzeigler.bukkitpluginutils.language;

import javafx.util.Pair;

/**
 * Represents a class that can provide messages based on a given key
 */
public interface MessageProvider {
    /**
     * Returns the message connected to the key
     * @param key the key of the message mapping
     * @return the message mapped to the key
     */
    public String get(String key);
}

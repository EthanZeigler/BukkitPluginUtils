package com.ethanzeigler.bukkitpluginutils.language;

import java.util.Locale;

/**
 * Created by Ethan on 8/13/16.
 */
public interface Language {
    /**
     * Gets a language's locale.
     * @return the language's locale
     */
    public Locale getLocale();

    /**
     * Gets the resource bundle base name for the language.
     * @return the resource bundle base
     */
    public String getResourceBundleBase();
}

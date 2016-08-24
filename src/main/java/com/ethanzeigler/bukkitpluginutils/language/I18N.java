package com.ethanzeigler.bukkitpluginutils.language;

import org.bukkit.ChatColor;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static sun.print.ServiceDialog.getMsg;

public class I18N implements MessageProvider {
    private ResourceBundle messages;
    private Language lang;
    private MessageFormat formatter;

    /**
     * Loads the given language file using the given classloader.
     * @param classLoader classloader to load from. This is most needed when not loading from the plugin jar.
     *                    A {@link com.ethanzeigler.bukkitpluginutils.FileClassLoader} is provided for this.
     * @param lang the language that is being loaded
     * @throws MissingResourceException is the file cannot be found
     */
    public I18N(ClassLoader classLoader, Language lang) throws MissingResourceException {
        this.lang = lang;
        this.formatter = new MessageFormat("", lang.getLocale());

        this.messages = ResourceBundle.getBundle(lang.getResourceBundleBase(), lang.getLocale(), classLoader);
    }

    /**
     * Loads the given language file from the base of the plugin jar.
     * @param lang the language that us being loaded
     * @throws MissingResourceException is the file cannot be found.
     */
    public I18N(Language lang) throws MissingResourceException {
        this.lang = lang;
        this.formatter = new MessageFormat("", lang.getLocale());

        this.messages = ResourceBundle.getBundle(lang.getResourceBundleBase(), lang.getLocale());
    }

    /**
     * Gets the message specified by the key, uses {@link ChatColor#translateAlternateColorCodes(char, String)} with
     * the & symbol, and returns it.
     * @param key the message key
     * @return the message mapped to the key
     */
    @Override
    public String get(String key) {
        return ChatColor.translateAlternateColorCodes('&', messages.getString(key));
    }

    /**
     * Gets and formats a message using the {@link com.sun.org.apache.xerces.internal.util.MessageFormatter}. For more
     * information, look up Java's MessageFormatter class.
     * @param key the message key
     * @param replacements replacement args
     * @see com.sun.org.apache.xerces.internal.util.MessageFormatter#formatMessage(Locale, String, Object[])
     * @return the formatted message
     */
    public String getAndFormat(String key, Object... replacements) {
        return format(getMsg(key), replacements);
    }

    /**
     * Formats a message using the {@link com.sun.org.apache.xerces.internal.util.MessageFormatter}. For more
     * information, look up Java's MessageFormatter class.
     * @param replacements replacement args
     * @see com.sun.org.apache.xerces.internal.util.MessageFormatter#formatMessage(Locale, String, Object[])
     * @return the formatted message
     */
    private String format(String input, Object... replacements) {
        formatter.applyPattern(input);
        return formatter.format(replacements);
    }

    /**
     * Gets the loaded language
     * @return
     */
    public Language getLanguage() {
        return lang;
    }
}

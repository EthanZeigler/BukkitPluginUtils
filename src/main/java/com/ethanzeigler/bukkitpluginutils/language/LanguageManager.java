package com.ethanzeigler.bukkitpluginutils.language;
import javafx.util.Pair;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A language manager for Bukkit plugins
 */
public class LanguageManager {
    private JavaPlugin plugin;
    private String pluginPrefix;
    private MessageProvider messageProvider;

    /**
     * A new LanguageManager
     * @param plugin the JavaPlugin. This is used for sending synchronous messages to be thread safe.
     * @param provider the message provider being used.
     * @param pluginPrefix the prefix the plugin is using for beautified messages
     */
    public LanguageManager(JavaPlugin plugin, MessageProvider provider, String pluginPrefix) {
        this.plugin = plugin;
        this.pluginPrefix = pluginPrefix;
        this.messageProvider = provider;
    }


    /**
     * Gets the message template if it exists from the current language file.
     *
     * @param msg the message template to get
     * @return the message of that template
     */
    public String getMessage(String msg) {
        try {
            return (String) messageProvider.get(msg);
        } catch (Exception e) {
            return "Something is wrong with this plugin. Tell an administrator. ERROR: Key pair \"" + msg + "\" could not be found.";
        }
    }

    public void getAndSendMessage(CommandSender player, String key) {
        sendMessage(player, getMessage(key));
    }

    /**
     * Sends a beautified message to the player
     *
     * @param player     player to send the message to
     * @param startColor color to start the message with. If null, will be {@link ChatColor#RESET}.
     * @param message    the message to send
     */
    public void sendMessage(CommandSender player, ChatColor startColor, String message) {
        player.sendMessage(getBeautifiedMessage(startColor, message));
    }

    public void sendMessage(CommandSender player, String message) {
        sendMessage(player, null, message);
    }

    /**
     * Sends a message to the player on the next tick synchronously for when a message needs to be sent during an
     * async action.
     *
     * @param player     player to send the message to
     * @param startColor color to start the message with. If null, will be {@link ChatColor#RESET}
     * @param message    the message to send
     */
    public void sendSyncBeautifiedMessage(CommandSender player, ChatColor startColor, String message) {
        plugin.getServer().getScheduler().runTask(plugin, () -> sendMessage(player, startColor, message));
    }

    public void sendSyncBeautifiedMessage(CommandSender player, String message) {
        sendSyncBeautifiedMessage(player, null, message);
    }

    public String getBeautifiedMessage(String msg) {
        return getBeautifiedMessage(null, msg);
    }

    public String getBeautifiedMessage(ChatColor startColor, String msg) {
        return getBeautifiedMessage(startColor, msg, pluginPrefix);
    }

    public static String getBeautifiedMessage(ChatColor startColor, String msg, String pluginPrefix) {
        return String.format("%s[%s] %s", startColor == null ? ChatColor.RESET : startColor, pluginPrefix, msg);
    }

    /**
     * Returns the message with the given key pairs replaced, if found.
     *
     * <p>The key pairs replace the given String identified by {@link String#replace(CharSequence, CharSequence)}
     * with the value's {@link Object#toString()} output.</p>
     * @param key the message key as found in the current localization method
     * @param replacements key pairs that will be replaced in the String
     * @return the message with the the given replacements
     */
    public String getMessageAndReplace(String key, Pair<String, Object>... replacements) {
        String msg = getMessage(key);

        for (Pair<String, Object> replacement : replacements) {
            msg = msg.replace(replacement.getKey(), replacement.getValue().toString());
        }

        return msg;
    }

    public MessageProvider getMessageProvider() {
        return messageProvider;
    }
}

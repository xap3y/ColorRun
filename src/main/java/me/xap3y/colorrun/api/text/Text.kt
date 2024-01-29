package me.xap3y.colorrun.api.text

import org.bukkit.Bukkit
import org.bukkit.ChatColor

class Text {
    companion object {
        private const val prefix = "&7[&6ColorRun&7] &r"
        fun colored(message: String, withPrefix: Boolean = false): String =
            ChatColor.translateAlternateColorCodes('&', (if (withPrefix) prefix else "") + message)

        fun debugMsg(message: String) = colored("&8[D] &f$message", false)

        fun console(message: String) {
            Bukkit.getServer().consoleSender.sendMessage(colored(message, false))
        }
    }
}
package me.xap3y.colorrun.api.text

import org.bukkit.ChatColor

class Text {
    companion object {
        private const val prefix = "&7[&6ColorRun&7] &r"
        fun colored(message: String, withPrefix: Boolean = false): String {
            return ChatColor.translateAlternateColorCodes('&', prefix + message)
        }
    }
}
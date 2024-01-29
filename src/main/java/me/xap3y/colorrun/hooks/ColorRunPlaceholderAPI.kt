package me.xap3y.colorrun.hooks

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.xap3y.colorrun.Main
import org.bukkit.OfflinePlayer

class ColorRunPlaceholderAPI(private val plugin: Main): PlaceholderExpansion() {
    override fun getIdentifier(): String = "colorrun"

    override fun getAuthor(): String = "XAP3Y"

    override fun getVersion(): String = "2.0.5"

    override fun onRequest(player: OfflinePlayer?, identifier: String): String? {
        if (player == null) return null
        //val args = identifier.split("_")

        return when (identifier) {
            "arenas" -> plugin.arenasDb.arenasSize().toString()
            else -> null
        }
    }
}
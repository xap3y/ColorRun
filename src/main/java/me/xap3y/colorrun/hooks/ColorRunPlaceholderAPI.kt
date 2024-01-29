package me.xap3y.colorrun.hooks

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.xap3y.colorrun.Main
import me.xap3y.colorrun.api.enums.PlayerCollectionEnums
import org.bukkit.OfflinePlayer

class ColorRunPlaceholderAPI(private val plugin: Main): PlaceholderExpansion() {  // Support for PAPI
    override fun getIdentifier(): String = "colorrun"

    override fun getAuthor(): String = "XAP3Y"

    override fun getVersion(): String = "2.0.5"

    override fun onRequest(player: OfflinePlayer?, identifier: String): String? {
        if (player == null) return null
        //val args = identifier.split("_")

        return when (identifier) {
            "arenas" -> plugin.arenasDb.arenasSize().toString()
            "wins" -> 0.toString() // TODO
            "loses" -> 0.toString() // TODO
            "is_ingame" -> plugin.playerDb.getSetting(player.uniqueId.toString(), PlayerCollectionEnums.IN_GAME).toString()
            "debug" -> plugin.debug.toString()
            else -> null
        }
    }
}
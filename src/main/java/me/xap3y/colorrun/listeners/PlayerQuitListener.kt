package me.xap3y.colorrun.listeners

import me.xap3y.colorrun.Main
import me.xap3y.colorrun.api.Arena
import me.xap3y.colorrun.api.enums.PlayerCollectionEnums
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener(private val plugin: Main): Listener {

    @EventHandler
    fun onPlayerQuitEvent(e: PlayerQuitEvent) {

        // Remove player from arena and set in_game to false

        val uuid: String = e.player.uniqueId.toString()

        if (plugin.playerDb.getSetting(uuid, PlayerCollectionEnums.IN_GAME) != true) return

        plugin.playerDb.setSetting(uuid, PlayerCollectionEnums.IN_GAME, false)

        val arena: Arena = plugin.playerDb.getArena(uuid) ?: return

        arena.removePlayer(e.player)

        plugin.playerDb.clearArena(uuid)
    }
}
package me.xap3y.colorrun.util

import me.xap3y.colorrun.Main
import me.xap3y.colorrun.api.Arena
import me.xap3y.colorrun.api.enums.PlayerCollectionEnums
import me.xap3y.colorrun.api.text.Text
import org.bukkit.entity.Player

class ProcessPlayerJoin {
    companion object {
        @JvmStatic
        fun processPlayerJoin(player: Player, arena: Arena, plugin: Main) {
            player.sendMessage(Text.colored("&fYou have joined the arena! &7(&c${arena.getPlayers().size + 1}&7/&c${arena.getMaxPlayers()}&7)", true))
            arena.addPlayer(player)
            plugin.playerDb.setSetting(player.uniqueId.toString(), PlayerCollectionEnums.IN_GAME, true)
            plugin.playerDb.setArena(player.uniqueId.toString(), arena)
        }
    }
}
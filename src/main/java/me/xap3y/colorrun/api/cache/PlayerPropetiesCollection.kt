package me.xap3y.colorrun.api.cache

import me.xap3y.colorrun.api.Arena
import me.xap3y.colorrun.api.enums.PlayerCollectionEnums

class PlayerPropetiesCollection {

    private val playerSettings: HashMap<String, HashMap<PlayerCollectionEnums, Boolean>> = HashMap()

    private val playerArenas: HashMap<String, Arena> = HashMap()

    fun setSetting(player: String, enum: PlayerCollectionEnums, value: Boolean) {
        if (!playerSettings.containsKey(player)) {
            playerSettings[player] = HashMap()
        }
        playerSettings[player]!![enum] = value
    }
    fun getSetting(player: String, enum: PlayerCollectionEnums): Boolean? {
        if (!playerSettings.containsKey(player)) return null
        return playerSettings[player]?.get(enum)
    }

    fun hasSetting(player: String): Boolean {
        return playerSettings.containsKey(player)
    }

    fun setArena(uuid: String, arena: Arena) {
        playerArenas[uuid] = arena
    }

    fun clearArena(uuid: String){
        playerArenas.remove(uuid)
    }

    fun getArena(uuid: String): Arena? {
        return playerArenas[uuid]
    }
}
package me.xap3y.colorrun.api.cache

import me.xap3y.colorrun.api.ArenaPropeties
import me.xap3y.colorrun.api.enums.ArenaStatesEnums

class ArenasCollection {


    private val arenas = HashMap<String, ArenaPropeties>()

    fun getArenas(): HashMap<String, ArenaPropeties> {
        return arenas
    }

    fun arenasSize(): Int {
        return arenas.size
    }

    fun addArena(name: String, arenaPropeties: ArenaPropeties) {
        arenas[name] = arenaPropeties
    }

    fun removeArena(name: String) {
        if(hasArena(name)) arenas.remove(name)
    }

    fun hasArena(name: String): Boolean {
        return arenas.containsKey(name)
    }

    fun getState(name: String): ArenaStatesEnums {
        return if(hasArena(name)) arenas[name]?.state!! else ArenaStatesEnums.NOT_FOUND
    }

    fun setState(name: String, state: ArenaStatesEnums) {
        if(hasArena(name)) arenas[name]?.state = state
    }

}
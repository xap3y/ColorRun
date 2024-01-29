package me.xap3y.colorrun.api.cache

import me.xap3y.colorrun.api.Arena
import me.xap3y.colorrun.api.ArenaPropeties
import me.xap3y.colorrun.api.events.arena.ArenaCreatedEvent

class ArenasCollection {


    private val arenas: Set<Arena> = setOf()

    fun getArenas(): Set<Arena>{
        return arenas
    }

    fun arenasSize(): Int {
        return arenas.size
    }

    fun addArena(name: String, propeties: ArenaPropeties) {
        ArenaCreatedEvent(Arena(name, propeties)).callEvent()
        arenas.plus(Arena(name, propeties))
    }

    fun removeArena(name: String) {
        if(hasArena(name)) arenas.minus( getArena(name) )
    }

    fun getArena(name: String): Arena? {
        return arenas.find { it.getName() == name }
    }

    fun hasArena(name: String): Boolean {
        return arenas.find { it.getName() == name } != null
    }
}
package me.xap3y.colorrun.api.cache

import me.xap3y.colorrun.Main
import me.xap3y.colorrun.api.Arena
import me.xap3y.colorrun.api.ArenaPropeties
import me.xap3y.colorrun.api.events.arena.ArenaCreatedEvent
import org.bukkit.Bukkit


class ArenasCollection(private val plugin: Main) {


    private val arenas: MutableSet<Arena> = mutableSetOf()

    fun getArenas(): Set<Arena>{
        return arenas
    }

    fun arenasSize(): Int {
        return arenas.size
    }

    fun addArena(name: String, propeties: ArenaPropeties) {

        Bukkit.getScheduler().runTask(plugin, Runnable {
            val event = ArenaCreatedEvent(Arena(name, propeties))
            event.callEvent()
            if (!event.isCancelled) arenas.add(Arena(name, propeties))
        })

        //plugin.server.pluginManager.callEvent(ArenaCreatedEvent(Arena(name, propeties)))
        //ArenaCreatedEvent(Arena(name, propeties)).callEvent() WTF?
    }

    fun removeArena(name: String) {
        if(hasArena(name)) arenas.remove( getArena(name) )
    }

    fun getArena(name: String): Arena? {
        return arenas.find { it.getName() == name }
    }

    fun hasArena(name: String): Boolean {
        return arenas.find { it.getName() == name } != null
    }
}
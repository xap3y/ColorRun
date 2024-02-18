package me.xap3y.colorrun.api.cache

import me.xap3y.colorrun.Main
import me.xap3y.colorrun.api.Arena
import me.xap3y.colorrun.api.ArenaPropeties


class ArenasCollection(private val plugin: Main) {


    private val arenas: MutableSet<Arena> = mutableSetOf()

    fun getArenas(): Set<Arena>{
        return arenas
    }

    fun arenasSize(): Int {
        return arenas.size
    }

    fun addArena(name: String, propeties: ArenaPropeties, registerJson: Boolean = false) : Boolean {


        if (arenas.any { it.getName() == name }) return false

        /*try {
            Bukkit.getScheduler().runTask(plugin, Runnable {
                val event = ArenaCreatedEvent(Arena(name, propeties))
                event.callEvent()
                if (!event.isCancelled) arenas.add(Arena(name, propeties))
            })
        } catch (e: Exception) {
            //arenas.add(Arena(name, propeties))
            try {
                val event = ArenaCreatedEvent(Arena(name, propeties))
                event.callEvent()
                if (!event.isCancelled) arenas.add(Arena(name, propeties))
            } catch (e: Exception) {
                // TODO
            }
        }*/

        arenas.add(Arena(name, propeties, plugin))

        if (registerJson) {
            plugin.configManager.addArena(me.xap3y.colorrun.util.Arena(
                name,
                propeties.minPlayers,
                propeties.maxPlayers,
                me.xap3y.colorrun.util.Location(
                    propeties.spawn.x,
                    propeties.spawn.y,
                    propeties.spawn.z,
                    propeties.spawn.world.name
                )
            ))
        }

        return true
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
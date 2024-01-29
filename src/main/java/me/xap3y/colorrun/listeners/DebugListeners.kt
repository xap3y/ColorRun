package me.xap3y.colorrun.listeners

import me.xap3y.colorrun.api.events.arena.ArenaCreatedEvent
import me.xap3y.colorrun.api.text.Text
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class DebugListeners: Listener {
    @EventHandler
    fun onArenaCreatedEvent(e: ArenaCreatedEvent) {
        Text.console("[onArenaCreatedEvent] Arena created: ${e.arena.getName()}")
        //println("Arena created: ${e.arena.getName()}")
        //e.isCancelled = true
    }
}
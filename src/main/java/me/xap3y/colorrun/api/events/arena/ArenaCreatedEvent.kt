package me.xap3y.colorrun.api.events.arena

import me.xap3y.colorrun.api.Arena
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class ArenaCreatedEvent(val arena: Arena): Event(), Cancellable {

    private var isCancelled: Boolean = false

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    override fun isCancelled(): Boolean {
        return isCancelled
    }

    override fun setCancelled(cancel: Boolean) {
        this.isCancelled = cancel
    }

    companion object {
        private val HANDLERS = HandlerList()
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }
}
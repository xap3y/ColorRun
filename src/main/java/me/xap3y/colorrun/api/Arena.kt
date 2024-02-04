package me.xap3y.colorrun.api

import me.xap3y.colorrun.api.enums.ArenaStatesEnums
import me.xap3y.colorrun.api.events.arena.ArenaStateChangedEvent
import me.xap3y.colorrun.api.text.Text
import org.bukkit.Location
import org.bukkit.entity.Player
import java.time.LocalDateTime

class Arena(private val name: String, private var propeties: ArenaPropeties) {
    fun getName(): String {
        return name
    }
    fun getPropeties(): ArenaPropeties {
        return propeties
    }
    fun getPlayers(): MutableSet<Player> {
        return propeties.players
    }
    fun addPlayer(player: Player, notify: Boolean = true) {

        if (notify) propeties.players.forEach { it.sendMessage(Text.colored("&7[&a+&7] &e${player.name}", true))}

        propeties.players.add(player)

        notifyPlayersChange()
    }

    fun removePlayer(player: Player, notify: Boolean = true) {

        propeties.players.remove(player)

        if (notify) propeties.players.forEach { it.sendMessage(Text.colored("&7[&c-&7] &e${player.name}", true))}

        notifyPlayersChange()
    }

    private fun notifyPlayersChange() {

        val state: ArenaStatesEnums = getState()
        val playersSize: Int = getPlayers().size

        /*if (playersSize < 2 && false) {

            if (state == ArenaStatesEnums.STARTING || state == ArenaStatesEnums.STARTING_FULL) {
                setState(ArenaStatesEnums.WAITING)
            } else if (state == ArenaStatesEnums.INGAME) {
                setState(ArenaStatesEnums.ENDING)

                //TODO("IMPLEMENT ENDING")
            }

        } else */
        if (playersSize >= getMinPlayers() && (state == ArenaStatesEnums.WAITING || state == ArenaStatesEnums.STARTING)) {

            //Text.console("Starting countdown...")
            if (playersSize > getMaxPlayers()) Text.console("how?")

            if (playersSize == getMaxPlayers()) {
                setState(ArenaStatesEnums.STARTING_FULL)
                getPlayers().forEach { it.sendMessage(Text.colored("&fStarting with full player count")) }
            }

            else {
                if (state == ArenaStatesEnums.STARTING) return
                setState(ArenaStatesEnums.STARTING)
                getPlayers().forEach { it.sendMessage(Text.colored("&fCountdown starting..")) }
                //Text.console("STARTING")
            }

            //TODO("IMPLEMENT STARTING")

        } else if (playersSize < getMaxPlayers() && state == ArenaStatesEnums.STARTING_FULL && playersSize >= getMinPlayers()) {

            setState(ArenaStatesEnums.STARTING)
            getPlayers().forEach { it.sendMessage(Text.colored("&fNot starting full, but still starting..")) }

        } else if (playersSize < getMinPlayers() && (state == ArenaStatesEnums.STARTING || state == ArenaStatesEnums.STARTING_FULL)) {

            setState(ArenaStatesEnums.WAITING)
            getPlayers().forEach { it.sendMessage(Text.colored("&fStarting, but player left so not starting anymore..")) }

        }
    }

    fun getMinPlayers(): Int {
        return propeties.minPlayers
    }
    fun getMaxPlayers(): Int {
        return propeties.maxPlayers
    }
    fun getSpawn(): Location {
        return propeties.spawn
    }
    fun getStartTimeout(): Int {
        return propeties.startTimeout
    }
    fun getState(): ArenaStatesEnums {
        return propeties.state
    }
    fun setState(state: ArenaStatesEnums) {
        propeties.state = state
        //ArenaStateChangedEvent(name, state).callEvent()
        // TODO
    }
    fun isReady(): Boolean {
        return propeties.isReady
    }
    fun setReady(ready: Boolean) {
        propeties.isReady = ready
    }
    fun getCreatedAt(): LocalDateTime {
        return propeties.createdAt
    }
    fun setPropeties(propeties: ArenaPropeties) {
        this.propeties = propeties
    }
    fun setMinPlayers(minPlayers: Int) {
        propeties.minPlayers = minPlayers
    }
    fun setMaxPlayers(maxPlayers: Int) {
        propeties.maxPlayers = maxPlayers
    }
    fun setStartTimeout(startTimeout: Int) {
        propeties.startTimeout = startTimeout
    }
    fun setSpawn(spawn: Location) {
        propeties.spawn = spawn
    }
    fun setPlayers(players: MutableSet<Player>) {
        propeties.players = players
    }
    fun setCreatedAt(createdAt: LocalDateTime) {
        propeties.createdAt = createdAt
    }
    fun getMaintenanceStatus(): Boolean {
        return propeties.maintenance
    }
    fun setMaintenanceStatus(status: Boolean) {
        propeties.maintenance = status
    }
}
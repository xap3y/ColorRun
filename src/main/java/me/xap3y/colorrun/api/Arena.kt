package me.xap3y.colorrun.api

import me.xap3y.colorrun.api.enums.ArenaStatesEnums
import me.xap3y.colorrun.api.events.arena.ArenaStateChangedEvent
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
    fun addPlayer(player: Player) {
        propeties.players.add(player)
    }
    fun removePlayer(player: Player) {
        propeties.players.remove(player)
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
        ArenaStateChangedEvent(name, state).callEvent()
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
}
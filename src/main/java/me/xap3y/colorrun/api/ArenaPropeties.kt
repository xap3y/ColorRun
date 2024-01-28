package me.xap3y.colorrun.api

import me.xap3y.colorrun.api.enums.ArenaStatesEnums
import org.bukkit.Location
import org.bukkit.entity.Player
import java.time.LocalDateTime
import java.util.Date

data class ArenaPropeties(
    var isReady: Boolean,
    var state: ArenaStatesEnums,
    val name: String,
    val createdAt: LocalDateTime,
    val players: MutableSet<Player>,
    var maxPlayers: Int,
    var minPlayers: Int,
    var startTimeout: Int,
    var spawn: Location
)
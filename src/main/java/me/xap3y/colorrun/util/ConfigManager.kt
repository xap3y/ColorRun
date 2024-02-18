package me.xap3y.colorrun.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileReader
import java.io.FileWriter


class ConfigManager(private val file: File) {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val arenas: MutableList<Arena> = loadArenas()

    private fun loadArenas(): MutableList<Arena> {
        val configFile = file
        return if (configFile.exists()) {
            FileReader(configFile).use { reader ->
                gson.fromJson(reader, Array<Arena>::class.java).toMutableList()
            }
        } else {
            mutableListOf()
        }
    }

    private fun saveArenas() {
        FileWriter(file).use { writer ->
            gson.toJson(arenas, writer)
        }
    }

    fun addArena(arena: Arena) {
        arenas.add(arena)
        saveArenas()
    }

    fun getArenas(): List<Arena> {
        return arenas.toList()
    }

    fun updateArena(arenaName: String, fieldName: String, newValue: Any) {
        val arena = arenas.find { it.arenaName == arenaName }
        if (arena != null) {
            when (fieldName) {
                "arenaName" -> error("Cannot update arena name")
                "minPlayers" -> arena.minPlayers = newValue as Int
                "maxPlayers" -> arena.maxPlayers = newValue as Int
                "spawnLocation" -> arena.spawnLocation = newValue as Location
                else -> {}
            }
            saveArenas()
        }
    }
}

data class Arena(
    val arenaName: String,
    var minPlayers: Int,
    var maxPlayers: Int,
    var spawnLocation: Location
)

data class Location(
    val x: Double,
    val y: Double,
    val z: Double,
    val world: String
)
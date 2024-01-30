package me.xap3y.colorrun.api

import org.bukkit.Material

data class Block(
    val block: Material,
    val item: Material,
    val power: Double,
    val yPower: Double = 1.0
)

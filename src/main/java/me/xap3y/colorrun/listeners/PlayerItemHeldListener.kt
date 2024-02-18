package me.xap3y.colorrun.listeners

import me.xap3y.colorrun.Main
import me.xap3y.colorrun.util.ProcessPlayerLaunch
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent

class PlayerItemHeldListener(private val plugin: Main): Listener {

    @EventHandler
    fun onPlayerItemHeldEvent(e: PlayerItemHeldEvent) {

        val player = e.player
        val item = player.inventory.getItem(e.newSlot)?.type ?: Material.AIR
        val block = player.location.block.getRelative(BlockFace.DOWN).type

        ProcessPlayerLaunch.processPlayerLaunch(player, block, plugin, item)
    }
}
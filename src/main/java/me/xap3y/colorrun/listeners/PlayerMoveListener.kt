package me.xap3y.colorrun.listeners

import me.xap3y.colorrun.Main
import me.xap3y.colorrun.api.enums.PlayerCollectionEnums
import me.xap3y.colorrun.api.text.Text
import me.xap3y.colorrun.util.ProcessPlayerLaunch.Companion.processPlayerLaunch
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class PlayerMoveListener(private val plugin: Main): Listener {

    @EventHandler
    fun onPlayerMoveEvent(e: PlayerMoveEvent) {

        val player = e.player
        val block = e.to.block.getRelative(BlockFace.DOWN).type

        processPlayerLaunch(player, block)


        if (plugin.playerDb.getSetting(player.uniqueId.toString(), PlayerCollectionEnums.EXTENDED_DEBUG_MODE) == true) {
            val handItem = player.inventory.itemInMainHand.type

            val velocity = player.eyeLocation.direction.clone()

            player.sendMessage(Text.debugMsg("V: %.3f, %.3f, %.3f".format(velocity.x, velocity.y, velocity.z)))
            player.sendActionBar(Text.debugMsg("B: $block || I: $handItem || V: $velocity"))

        }

        else if (plugin.playerDb.getSetting(player.uniqueId.toString(), PlayerCollectionEnums.DEBUG_MODE) == true) {
            player.sendActionBar(Text.debugMsg(block.toString()))
        }


    }
}
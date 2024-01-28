package me.xap3y.colorrun.listeners

import me.xap3y.colorrun.Main
import me.xap3y.colorrun.api.enums.PlayerCollectionEnums
import me.xap3y.colorrun.api.text.Text
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class PlayerMoveListener(private val plugin: Main): Listener {

    @EventHandler
    fun onPlayerMoveEvent(e: PlayerMoveEvent) {

        val player = e.player
        val block = e.to.block.getRelative(BlockFace.DOWN).type


        if (plugin.playerDb.getSetting(player.uniqueId.toString(), PlayerCollectionEnums.EXTENDED_DEBUG_MODE) == true) {
            val handItem = player.inventory.itemInMainHand.type

            val velocity = player.eyeLocation.direction.clone()
            velocity.x *= 1.2
            velocity.z *= 1.2
            velocity.y = 1.0

            player.sendMessage(Text.debugMsg("V: %.3f, %.3f, %.3f".format(velocity.x, velocity.y, velocity.z)))
            player.sendActionBar(Text.debugMsg("B: $block || I: $handItem || V: $velocity"))

            if (block == Material.LAPIS_BLOCK && handItem == Material.LAPIS_LAZULI) {
                player.velocity = velocity


                /*
                player.sendMessage(Text.debugMsg("================================================================"))
                player.sendMessage(Text.debugMsg("OLD V: &b$velocity"))
                player.sendMessage(Text.debugMsg("Launched player with Velocity: &b${velocity.multiply(2)}"))
                player.sendMessage(Text.debugMsg("================================================================"))
                 */

            }

        }

        else if (plugin.playerDb.getSetting(player.uniqueId.toString(), PlayerCollectionEnums.DEBUG_MODE) == true) {
            //player.sendMessage(Text.debugMsg(block))
            player.sendActionBar(Text.debugMsg(block.toString()))
        }


    }
}
package me.xap3y.colorrun.util

import com.cryptomorin.xseries.XMaterial
import me.xap3y.colorrun.api.Block
import org.bukkit.Material
import org.bukkit.entity.Player


class ProcessPlayerLaunch {

    companion object {

        @JvmStatic
        private val actionMap: Set<Block> = setOf(
            Block(Material.LAPIS_BLOCK, XMaterial.LAPIS_LAZULI.parseMaterial(), 1.2, 1.1),
            Block(Material.GOLD_BLOCK, XMaterial.GOLD_INGOT.parseMaterial(), 1.15, 1.15),
            Block(Material.IRON_BLOCK, XMaterial.IRON_INGOT.parseMaterial(), 1.1, 1.05),
            Block(Material.REDSTONE_BLOCK, XMaterial.REDSTONE.parseMaterial(), 1.3, 1.5),
            Block(Material.QUARTZ_BLOCK, XMaterial.QUARTZ.parseMaterial(), 1.4, 0.9),
            Block(Material.COAL_BLOCK, XMaterial.COAL.parseMaterial(), 1.2, .05),
            Block(Material.DIAMOND_BLOCK, XMaterial.DIAMOND.parseMaterial(), 1.2, 0.15),
            Block(Material.EMERALD_BLOCK, XMaterial.EMERALD.parseMaterial(), 3.0, 0.25),
            Block(Material.BRICKS, XMaterial.BRICK.parseMaterial(), 1.0, 0.3)

        )

        @JvmStatic
        fun processPlayerLaunch(player: Player, block: Material, item: Material? = null) {
            val handItem = item ?: player.itemInHand.type

            actionMap.any{
                if (it.block == block && it.item == handItem) {
                    launchPlayer(player, it.power, it.yPower)
                    return@any true
                }
                return@any false
            }

            //launchPlayer(player, 1.2)
        }

        @JvmStatic
        private fun launchPlayer(player: Player, power: Double, y: Double = 1.0) {

            val velocity = player.eyeLocation.direction.clone()
            velocity.x *= power
            velocity.z *= power
            velocity.y = y

            player.velocity = velocity
        }
    }
}
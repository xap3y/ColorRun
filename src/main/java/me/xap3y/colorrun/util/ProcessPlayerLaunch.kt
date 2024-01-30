package me.xap3y.colorrun.util

import me.xap3y.colorrun.api.Block
import org.bukkit.Material
import org.bukkit.entity.Player


class ProcessPlayerLaunch {

    companion object {

        private val actionMap: Set<Block> = setOf(
            Block(Material.LAPIS_BLOCK, Material.LAPIS_LAZULI, 1.2, 1.1),
            Block(Material.GOLD_BLOCK, Material.GOLD_INGOT, 1.15, 1.15),
            Block(Material.IRON_BLOCK, Material.IRON_INGOT, 1.1, 1.05),
            Block(Material.REDSTONE_BLOCK, Material.REDSTONE, 1.3, 1.5),
            Block(Material.QUARTZ_BLOCK, Material.QUARTZ, 1.4, 0.9),
            Block(Material.COAL_BLOCK, Material.COAL, 1.2, .05),
            Block(Material.DIAMOND_BLOCK, Material.DIAMOND, 1.2, 0.15),
            Block(Material.EMERALD_BLOCK, Material.EMERALD, 3.0, 0.10)

        )

        fun processPlayerLaunch(player: Player, block: Material, item: Material? = null) {
            val handItem = item ?: player.inventory.itemInMainHand.type

            actionMap.any{
                if (it.block == block && it.item == handItem) {
                    launchPlayer(player, it.power, it.yPower)
                    return@any true
                }
                return@any false
            }

            //launchPlayer(player, 1.2)
        }

        private fun launchPlayer(player: Player, power: Double, y: Double = 1.0) {

            val velocity = player.eyeLocation.direction.clone()
            velocity.x *= power
            velocity.z *= power
            velocity.y = y

            player.velocity = velocity
        }
    }
}
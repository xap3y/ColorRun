package me.xap3y.colorrun.commands.subcommands

import me.xap3y.colorrun.Main
import me.xap3y.colorrun.api.Arena
import me.xap3y.colorrun.api.enums.ArenaStatesEnums
import me.xap3y.colorrun.api.enums.PlayerCollectionEnums
import me.xap3y.colorrun.api.text.Text
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ArenaForceStart {
    companion object {
        fun forceStart(plugin: Main, sender: CommandSender, arenaName: String? = null) {
            // Force start the arena

            if (arenaName == null) {
                if (sender !is Player) {
                    sender.sendMessage("You must be a player to use this command")
                    return
                }

                val player: Player = sender

                if (plugin.playerDb.getSetting(player.uniqueId.toString(), PlayerCollectionEnums.IN_GAME) != true)
                    return player.sendMessage(Text.colored("&cYou are not in a game"))

                val arena: Arena = plugin.playerDb.getArena(player.uniqueId.toString())
                    ?: return player.sendMessage(Text.colored("&cInvalid arena", true))

                val msg: String? = arena.startGame()

                if (msg != null)
                    player.sendMessage(Text.colored("&c$msg"))
            }
        }
    }
}
package me.xap3y.colorrun.commands

import me.xap3y.colorrun.Main
import me.xap3y.colorrun.api.Arena
import me.xap3y.colorrun.api.ArenaPropeties
import me.xap3y.colorrun.api.enums.ArenaCommandOperations
import me.xap3y.colorrun.api.enums.ArenaStatesEnums
import me.xap3y.colorrun.api.enums.PlayerCollectionEnums
import me.xap3y.colorrun.api.permissions.CommandPermissions
import me.xap3y.colorrun.api.text.CreateAboutMenu
import me.xap3y.colorrun.api.text.CreateHelpMenu
import me.xap3y.colorrun.api.text.Text
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import java.time.LocalDateTime

@CommandDescription("Main command /cr")
class ColorRunCMD(private val plugin: Main){
    @Command("colorrun|cr")
    fun rootCommand(commandSender: CommandSender) {

        val errorMsg: String = "&cWrong usage! &8(&7/cr help&8)"

        commandSender.sendMessage(Text.colored(errorMsg, commandSender is Player))
    }

    @Command("colorrun|cr help")
    @Permission(CommandPermissions.helpCommand)
    fun normalHelp(commandSender: CommandSender) {
        val helpMenu: String = CreateHelpMenu()
            .addCommand("help", "display this menu")
            .addCommand("join <arena>", "join an arena")
            .addCommand("quickjoin", "join an random arena")
            .addCommand("leave", "quit from game")
            .addCommand("about", "show info")
            .build()

        commandSender.sendMessage(Text.colored(helpMenu, commandSender is Player))
    }

    @Command("colorrun|cr about")
    @Permission(CommandPermissions.aboutCommand)
    fun aboutPlugin(commandSender: CommandSender) {
        val aboutMenu: String = CreateAboutMenu()
            .setVersion(plugin.version)
            .addDevelopers(arrayOf("XAP3Y"))
            .addField("debug", plugin.debug.toString())
            .build()

        if (commandSender !is Player) Bukkit.getServer().consoleSender.sendMessage(Text.colored("\n" + aboutMenu, false))
        else commandSender.sendMessage(Text.colored(aboutMenu))
    }

    @Command("colorrun|cr debug")
    @Permission(CommandPermissions.debugCommand)
    fun debugEnable(commandSender: CommandSender) {

        if (commandSender !is Player) return commandSender.sendMessage(Text.colored("&cThis can be only executed as player!", false))

        val uuid: String = commandSender.uniqueId.toString()
        var isEnabled: Boolean = plugin.playerDb.getSetting(uuid, PlayerCollectionEnums.DEBUG_MODE) ?: false
        isEnabled = !isEnabled // Flipping the value

        plugin.playerDb.setSetting(commandSender.uniqueId.toString(), PlayerCollectionEnums.DEBUG_MODE, isEnabled)

        val newValue: String = if (isEnabled) "&aEnabled" else "&cDisabled"

        commandSender.sendMessage(Text.colored("&fDebug mode has been $newValue"))
    }

    @Command("colorrun|cr edebug")
    @Permission(CommandPermissions.debugCommand)
    fun extendedDebugEnable(commandSender: CommandSender) {

        if (commandSender !is Player) return commandSender.sendMessage(Text.colored("&cThis can be only executed as player!", false))

        val uuid: String = commandSender.uniqueId.toString()
        var isEnabled: Boolean = plugin.playerDb.getSetting(uuid, PlayerCollectionEnums.EXTENDED_DEBUG_MODE) ?: false
        isEnabled = !isEnabled // Flipping the value

        plugin.playerDb.setSetting(commandSender.uniqueId.toString(), PlayerCollectionEnums.EXTENDED_DEBUG_MODE, isEnabled)

        val newValue: String = if (isEnabled) "&aEnabled" else "&cDisabled"

        commandSender.sendMessage(Text.colored("&fExtended debug mode has been $newValue"))
    }

    @Command("colorrun|cr arenas")
    @Permission(CommandPermissions.arenasCommands)
    fun arenaList(commandSender: CommandSender) {

        val arenas: Set<String> = plugin.arenasDb.getArenas().map { it.getName() }.toSet() // Converting Set<Arena> to Set<String>
        val size: Int = plugin.arenasDb.arenasSize()

        commandSender.sendMessage(Text.colored("&fList (&9$size&f): &6${arenas.joinToString("&f, &6")}"))
        //commandSender.sendMessage(Text.colored("&fList2: &b$arenas"))
    }

    @Command("colorrun|cr arena <operation> [name] [arg2]")
    @Permission(CommandPermissions.arenasCommands)
    fun arenaCommand(
        commandSender: CommandSender,
        @Argument("operation") operation: ArenaCommandOperations,
        @Argument("name") arg1: String? = null,
        @Argument("arg2") arg2: Int? = null,
    ) {

        val isPlayer = commandSender is Player

        if (arg1 == null) return commandSender.sendMessage(Text.colored("&cYou must specify the name of the arena!", isPlayer))

        if (operation == ArenaCommandOperations.CREATE) {
            //if (param == null) return commandSender.sendMessage(Text.colored("&cYou must specify the name of the arena!", commandSender is Player))

            val location =
                if (commandSender is Player) commandSender.location else Bukkit.getWorld("world")!!.spawnLocation

            if (plugin.arenasDb.getArena(arg1) != null) return commandSender.sendMessage(Text.colored("&cThis arena already exists!", isPlayer))
            if (plugin.arenasDb.addArena(
                arg1, ArenaPropeties(  // Development only
                    false,
                    ArenaStatesEnums.CLOSED,
                    LocalDateTime.now(),
                    mutableSetOf(),
                    4,
                    1,
                    15,
                    location
                )
            )) commandSender.sendMessage(Text.colored("&aArena has been created!", isPlayer))
            else commandSender.sendMessage(Text.colored("&cCannot create arena!", isPlayer))
        }

        val arena: Arena = plugin.arenasDb.getArena(arg1)
            ?: return commandSender.sendMessage(Text.colored("&cThis arena doesn't exists!", isPlayer))

        if (operation == ArenaCommandOperations.REMOVE || operation == ArenaCommandOperations.SETSPAWN) {

            if (operation == ArenaCommandOperations.REMOVE) {

                if (!plugin.arenasDb.hasArena(arg1)) return commandSender.sendMessage(Text.colored("&cThis arena doesn't exists!", isPlayer))
                plugin.arenasDb.removeArena(arg1)
                commandSender.sendMessage(Text.colored("&aArena has been removed!", isPlayer))

            } else {

                val location = if (commandSender is Player) commandSender.location else Bukkit.getWorld("world")!!.spawnLocation
                arena.setSpawn(location)
                commandSender.sendMessage(Text.colored("&aSpawn has been set!", isPlayer))

            }
        } else if (operation == ArenaCommandOperations.SETMAXPLAYERS || operation == ArenaCommandOperations.SETMINPLAYERS || operation == ArenaCommandOperations.SETSTARTTIMEOUT) {

            if (arg2 == null) return commandSender.sendMessage(Text.colored("&cYou must specify the number! &7(/cr arena <name> <number>)", isPlayer))

            if (!plugin.arenasDb.hasArena(arg1)) return commandSender.sendMessage(Text.colored("&cThis arena doesn't exists!", isPlayer))

            if ((arg2 < 1) || (arg2 > 999)) {
                return commandSender.sendMessage(Text.colored("&cThe number must be between 1 and 999!", isPlayer))
            }

            if (operation == ArenaCommandOperations.SETMAXPLAYERS) {

                if (arg2 <= arena.getMinPlayers()) return commandSender.sendMessage(Text.colored("&cMax players must be higher than min players!", isPlayer))
                arena.setMaxPlayers(arg2)
                commandSender.sendMessage(Text.colored("&aMax players has been set to &e$arg2", isPlayer))

            } else if (operation == ArenaCommandOperations.SETMINPLAYERS) {

                if (arg2 >= arena.getMaxPlayers()) return commandSender.sendMessage(Text.colored("&cMin players must be lower than max players!", isPlayer))
                arena.setMinPlayers(arg2)
                commandSender.sendMessage(Text.colored("&aMin players has been set to &e$arg2", isPlayer))
            } else {
                arena.setStartTimeout(arg2)
                commandSender.sendMessage(Text.colored("&aStart timeout has been set to &e$arg2 &aseconds", isPlayer))
            }
        } else if (operation == ArenaCommandOperations.SETMAINTENANCE) {
            val maintenanceStatus: Boolean = arena.getMaintenanceStatus()
            arena.setMaintenanceStatus(!maintenanceStatus)
            commandSender.sendMessage(Text.colored("&fMaintenance status was turned ${if (maintenanceStatus) "&cOFF" else "&aON"}", isPlayer))
        }
    }


}
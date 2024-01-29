package me.xap3y.colorrun.commands

import me.xap3y.colorrun.Main
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

        commandSender.sendMessage(Text.colored("&fList1: &6${arenas.joinToString("&f, &6")}"))
        //commandSender.sendMessage(Text.colored("&fList2: &b$arenas"))
    }

    @Command("colorrun|cr arena <operation> [name]")
    @Permission(CommandPermissions.arenasCommands)
    fun arenaCommand(
        commandSender: CommandSender,
        @Argument("operation") operation: ArenaCommandOperations,
        @Argument("name") param: String? = null,
    ) {

        if (operation == ArenaCommandOperations.CREATE) {
            if (param == null) return commandSender.sendMessage(Text.colored("&cYou must specify the name of the arena!", commandSender is Player))
            if (plugin.arenasDb.hasArena(param)) return commandSender.sendMessage(Text.colored("&cThis arena already exists!", commandSender is Player))

            val location = if (commandSender is Player) commandSender.location else Bukkit.getWorld("world")!!.spawnLocation



            plugin.arenasDb.addArena(param, ArenaPropeties(  // Development only
                false,
                ArenaStatesEnums.CLOSED,
                LocalDateTime.now(),
                mutableSetOf(),
                4,
                1,
                15,
                location
            ))

            commandSender.sendMessage(Text.colored("&aArena has been created!", commandSender is Player))
        } else if (operation == ArenaCommandOperations.REMOVE) {
            if (param == null) return commandSender.sendMessage(Text.colored("&cYou must specify the name of the arena!", commandSender is Player))
            if (!plugin.arenasDb.hasArena(param)) return commandSender.sendMessage(Text.colored("&cThis arena doesn't exists!", commandSender is Player))
            plugin.arenasDb.removeArena(param)
            commandSender.sendMessage(Text.colored("&aArena has been removed!", commandSender is Player))
        }
        TODO("Be able to edit arenas, set their spawnpoints, etc.")
    }


}
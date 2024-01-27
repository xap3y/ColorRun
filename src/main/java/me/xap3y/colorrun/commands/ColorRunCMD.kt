package me.xap3y.colorrun.commands

import me.xap3y.colorrun.Main
import me.xap3y.colorrun.api.text.CreateAboutMenu
import me.xap3y.colorrun.api.text.CreateHelpMenu
import me.xap3y.colorrun.api.text.Text
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission

@CommandDescription("Main command /cr")
class ColorRunCMD(private val plugin: Main){


    @Command("colorrun|cr help")
    @Permission("colorrun.help")
    fun normalHelp(commandSender: CommandSender) {
        val helpMenu: String = CreateHelpMenu()
            .addCommand("help", "display this menu")
            .addCommand("join <arena>", "join an arena")
            .addCommand("quickjoin", "join an random arena")
            .addCommand("leave", "quit from game")
            .addCommand("about", "show info")
            .build()

        commandSender.sendMessage(Text.colored(helpMenu))
    }

    @Command("colorrun|cr about")
    @Permission("colorrun.about")
    fun aboutPlugin(commandSender: CommandSender) {
        val aboutMenu: String = CreateAboutMenu()
            .setVersion(plugin.version)
            .addDevelopers(arrayOf("XAP3Y"))
            .addField("debug", plugin.debug.toString())
            .build()

        if (commandSender !is Player) Bukkit.getServer().consoleSender.sendMessage(Text.colored("\n" + aboutMenu))
        else commandSender.sendMessage(Text.colored(aboutMenu))
    }


}
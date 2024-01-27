package me.xap3y.colorrun

import me.xap3y.colorrun.commands.ColorRunCMD
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.SenderMapper
import org.incendo.cloud.bukkit.CloudBukkitCapabilities
import org.incendo.cloud.execution.ExecutionCoordinator.asyncCoordinator
import org.incendo.cloud.paper.PaperCommandManager
import org.slf4j.Logger


class Main : JavaPlugin() {
    
    val version: String = "1.0.0";

    val debug: Boolean = false;
    
    override fun onEnable() {
        // Plugin startup logic

        val commandManager = createCommandManager()
        val annotationParser = createAnnotationParser(commandManager)
        annotationParser.parse(ColorRunCMD(this))
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun createCommandManager(): PaperCommandManager<CommandSender> {
        val executionCoordinatorFunction = asyncCoordinator<CommandSender>()
        val mapperFunction = SenderMapper.identity<CommandSender>()
        val commandManager = PaperCommandManager(
            this,
            executionCoordinatorFunction,
            mapperFunction
        )
        if (commandManager.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
            commandManager.registerBrigadier()
            commandManager.brigadierManager().setNativeNumberSuggestions(false)
        }
        if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            (commandManager as PaperCommandManager<*>).registerAsynchronousCompletions()
        }
        return commandManager
    }

    private fun createAnnotationParser(commandManager: PaperCommandManager<CommandSender>): org.incendo.cloud.annotations.AnnotationParser<CommandSender> {
        return org.incendo.cloud.annotations.AnnotationParser(
            commandManager,
            CommandSender::class.java
        )
    }

}

package me.xap3y.colorrun

import me.xap3y.colorrun.api.ArenaPropeties
import me.xap3y.colorrun.api.cache.ArenasCollection
import me.xap3y.colorrun.api.cache.PlayerPropetiesCollection
import me.xap3y.colorrun.api.enums.ArenaStatesEnums
import me.xap3y.colorrun.api.text.Text
import me.xap3y.colorrun.commands.ColorRunCMD
import me.xap3y.colorrun.hooks.HookManager
import me.xap3y.colorrun.listeners.DebugListeners
import me.xap3y.colorrun.listeners.PlayerItemHeldListener
import me.xap3y.colorrun.listeners.PlayerMoveListener
import me.xap3y.colorrun.listeners.PlayerQuitListener
import me.xap3y.colorrun.util.ConfigManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.SenderMapper
import org.incendo.cloud.bukkit.CloudBukkitCapabilities
import org.incendo.cloud.execution.ExecutionCoordinator.asyncCoordinator
import org.incendo.cloud.paper.PaperCommandManager
import java.io.File
import java.time.LocalDateTime


class Main : JavaPlugin() {
    
    val version: String = "0.6";

    val playerDb: PlayerPropetiesCollection = PlayerPropetiesCollection()

    val arenasDb: ArenasCollection = ArenasCollection(this)

    private val hookManager: HookManager by lazy { HookManager(this) }

    val debug: Boolean = true

    val configManager: ConfigManager = ConfigManager(File(dataFolder, "config.json"))
    
    override fun onEnable() {
        // Plugin startup logic

        if(!dataFolder.exists()) dataFolder.mkdir()

        val commandManager = createCommandManager()
        val annotationParser = createAnnotationParser(commandManager)
        annotationParser.parse(ColorRunCMD(this))

        Bukkit.getPluginManager().registerEvents(PlayerMoveListener(this), this)
        Bukkit.getPluginManager().registerEvents(PlayerQuitListener(this), this)
        Bukkit.getPluginManager().registerEvents(PlayerItemHeldListener(this), this)

        if (debug) {

            Text.console("Registering debug listeners...")
            Bukkit.getPluginManager().registerEvents(DebugListeners(), this)

            Text.console("Creating default arena...")

            val arenas = configManager.getArenas()
            if (arenas.isNotEmpty()) {
                arenas.forEach {
                    try {
                        arenasDb.addArena(
                            it.arenaName,
                            ArenaPropeties(
                                isReady = true,
                                state = ArenaStatesEnums.WAITING,
                                createdAt = LocalDateTime.now(),
                                players = mutableSetOf(),
                                maxPlayers = it.maxPlayers,
                                minPlayers = it.minPlayers,
                                startTimeout = 15,
                                spawn = Location(Bukkit.getWorld(it.spawnLocation.world), it.spawnLocation.x, it.spawnLocation.y, it.spawnLocation.z, 0.0f, 0.0f),
                                waitingLobby = Location(Bukkit.getWorld(it.spawnLocation.world), it.spawnLocation.x, it.spawnLocation.y, it.spawnLocation.z, 0.0f, 0.0f)
                            )
                        )
                    } catch(e: Exception) {
                        Text.console("&4Error loading arenas from config!")
                        // TODO("Self repair broken config")
                        return@forEach
                    }

                }
            } else {
                arenasDb.addArena("test", ArenaPropeties(
                    isReady = true,
                    state = ArenaStatesEnums.WAITING,
                    createdAt = LocalDateTime.now(),
                    players = mutableSetOf(),
                    maxPlayers = 3,
                    minPlayers = 2,
                    startTimeout = 15,
                    spawn = Bukkit.getWorld("world")?.spawnLocation
                        ?: Location(Bukkit.getWorld("world"), 0.0, 25.0, 0.0, 0.0f, 0.0f),
                    waitingLobby = Bukkit.getWorld("world")?.spawnLocation
                        ?: Location(Bukkit.getWorld("world"), 0.0, 25.0, 0.0, 0.0f, 0.0f)
                ), true)
            }
            Text.console("Hooking PAPI....")
            hookManager.registerHooks()
        }
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

    fun getInstance(): Main {
        return this
    }

}

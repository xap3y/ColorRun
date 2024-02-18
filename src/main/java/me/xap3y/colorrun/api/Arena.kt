package me.xap3y.colorrun.api

import me.xap3y.colorrun.Main
import me.xap3y.colorrun.api.enums.ArenaStatesEnums
import me.xap3y.colorrun.api.enums.PlayerCollectionEnums
import me.xap3y.colorrun.api.text.Text
import me.xap3y.colorrun.util.ActionBar.Companion.sendActionbar
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.time.LocalDateTime
import kotlin.concurrent.thread


class Arena(private val name: String, private var propeties: ArenaPropeties, private val plugin: Main) {
    fun getName(): String {
        return name
    }

    private var timeoutThread: Thread? = null
    private var timeoutRunning = false

    private var state2: ArenaStatesEnums = getState()

    private var ingameCountdownThread: Thread? = null

    fun getPlayers(): MutableSet<Player> {
        return propeties.players
    }

    private fun startTimeout(customTime: Int? = null) {
        var timeout: Int = customTime ?: propeties.startTimeout
        val state: ArenaStatesEnums = getState()

        if ((state == ArenaStatesEnums.WAITING && !timeoutRunning) || (state == ArenaStatesEnums.STARTING && !timeoutRunning)) {

            timeoutThread = thread(start = true) {
                timeoutRunning = true
                getPlayers().forEach { it.sendMessage(Text.colored("&aCountdown starting", true)) }
                while(timeout > 0) {
                    getPlayers().forEach {
                        //it.sendMessage("Starting in: $timeout seconds")
                        //ActionBarAPI.sendActionBar(it, Text.colored("&fStarting in: &e$timeout"));
                        sendActionbar(it, Text.colored("&fStarting in: &e$timeout"));
                        //it.sendTitle("", timeout.toString()) // Subtitle only DEPRECATED idk why
                    }
                    timeout--

                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        return@thread
                    }

                }
                timeoutRunning = false

                startGame()
            }
        } else if (state == ArenaStatesEnums.STARTING && timeoutRunning) {
            stopTimeout()
            startTimeout(3) // Reduce timeout to only 3 seconds, This will not reduce it, but rewrite it
            // TODO("Reduce timeout")
        }
    }

    private fun stopTimeout() {
        timeoutThread?.interrupt()
        timeoutThread = null
        timeoutRunning = false
    }

    fun startGame(): String? {
        val state: ArenaStatesEnums = getState()
        if (state == ArenaStatesEnums.STARTING || state == ArenaStatesEnums.STARTING_FULL || (state == ArenaStatesEnums.WAITING && plugin.debug)) {

            if (timeoutRunning) stopTimeout() // Force-start only

            setState(ArenaStatesEnums.INGAME)

            //getPlayers().forEach { it.sendMessage(Text.colored("&bGame started", true)) }
            // TODO("IMPLEMENT 3 Seconds in-game countdown")

            // Teleport players to spawn
            getPlayers().forEach {
                object : BukkitRunnable() {
                    override fun run() {
                        it.teleport(propeties.spawn)
                    }
                }.runTask(plugin)
            }

            val countDownList: Set<String> = setOf("➂", "➁", "➀")

            ingameCountdownThread = thread(true) {
                countDownList.forEach {
                    getPlayers().forEach { player ->
                        player.sendTitle(Text.colored("&e$it"), "")
                    }
                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        return@thread
                    }
                }

                getPlayers().forEach { it.sendTitle(Text.colored("&eGood luck!"), "") }

                Thread.currentThread().interrupt()
                ingameCountdownThread = null
            }

            return null
        } else {
            return "Cannot start game, wrong state"
        }
    }

    fun endGame(player: Player? = null) {
        val state: ArenaStatesEnums = getState()
        if (state != ArenaStatesEnums.INGAME) return

        if (ingameCountdownThread != null) {
            ingameCountdownThread?.interrupt()
            ingameCountdownThread = null
        }

        setState(ArenaStatesEnums.ENDING)

        if (player != null) getPlayers().forEach {
            it.sendMessage(Text.colored("&fPlayer &e${player.name}&f has won the game", true))
        }

        getPlayers().forEach {
            if (player == it) it.sendTitle(Text.colored("&c&lGame over"), Text.colored("&6&lYou won!"))
            else it.sendTitle(Text.colored("&c&lGame over"), Text.colored("&cYou lost!"))
        }

        thread(true) {
            Thread.sleep(3000)

            setState(ArenaStatesEnums.RESTARTING)

            getPlayers().forEach {
                plugin.playerDb.setSetting(it.uniqueId.toString(), PlayerCollectionEnums.IN_GAME, false)
                plugin.playerDb.clearArena(it.uniqueId.toString())

                object : BukkitRunnable() {
                    override fun run() {
                        it.teleport(propeties.spawn)
                    }
                }.runTask(plugin)
            }

            getPlayers().clear()

            setState(ArenaStatesEnums.WAITING)
        }

    }

    fun addPlayer(player: Player, notify: Boolean = true) {

        if (notify) propeties.players.forEach { it.sendMessage(Text.colored("&fPlayer &e${player.name}&f has joined &7(&c${getPlayers().size}&7/&c${getMaxPlayers()}&7)", true))}

        propeties.players.add(player)

        notifyPlayersChange()
    }

    fun removePlayer(player: Player, notify: Boolean = true) {

        //if (notify) propeties.players.forEach { it.sendMessage(Text.colored("&fPlayer &e${player.name}&f has left &7(&c${getPlayers().size - 1}&7/&c${getMaxPlayers()}&7)", true))}

        val state: ArenaStatesEnums = getState()
        val players: MutableSet<Player> = getPlayers()

        players.remove(player)
        player.resetTitle()
        //ActionBarAPI.sendActionBar(player, " ");
        sendActionbar(player, "");

        if (state == ArenaStatesEnums.INGAME) {
            if (notify) players.forEach {
                it.sendMessage(
                    Text.colored("&fPlayer &e${player.name}&f has left", true)
                )
            }
        }

        else if (state == ArenaStatesEnums.WAITING || state == ArenaStatesEnums.STARTING || state == ArenaStatesEnums.STARTING_FULL) {
            if (notify) players.forEach {
                it.sendMessage(
                    Text.colored("&fPlayer &e${player.name}&f has left &7(&c${players.size}&7/&c${getMaxPlayers()}&7)", true)
                )
            }
        }



        //if (notify) propeties.players.forEach { it.sendMessage(Text.colored("&7[&c-&7] &e${player.name}"))}


        notifyPlayersChange()
    }

    private fun notifyPlayersChange() {

        val state: ArenaStatesEnums = getState()
        val playersSize: Int = getPlayers().size

        /*if (playersSize < 2 && false) {

            if (state == ArenaStatesEnums.STARTING || state == ArenaStatesEnums.STARTING_FULL) {
                setState(ArenaStatesEnums.WAITING)
            } else if (state == ArenaStatesEnums.INGAME) {
                setState(ArenaStatesEnums.ENDING)

                //TODO("IMPLEMENT ENDING")
            }

        } else */
        if (playersSize >= getMinPlayers() && (state == ArenaStatesEnums.WAITING || state == ArenaStatesEnums.STARTING)) {

            //Text.console("Starting countdown...")
            if (playersSize > getMaxPlayers()) Text.console("how?")

            if (playersSize == getMaxPlayers()) {

                startTimeout()
                setState(ArenaStatesEnums.STARTING_FULL)
                getPlayers().forEach { it.sendMessage(Text.colored("&fStarting with full player count")) }

                // Starting with full players (reducing countdown)
                // TODO("Reduce countdown")
            }

            else {
                if (state == ArenaStatesEnums.STARTING) return


                //getPlayers().forEach { it.sendMessage(Text.colored("&fCountdown starting..")) }

                // Starting countdown
                startTimeout()
                //Text.console("STARTING")

                // SET STATE RUNNING AFTER CALLING TIMEOUT!
                setState(ArenaStatesEnums.STARTING)
            }


        } else if (playersSize < getMaxPlayers() && state == ArenaStatesEnums.STARTING_FULL && playersSize >= getMinPlayers()) {

            setState(ArenaStatesEnums.STARTING)
            getPlayers().forEach { it.sendMessage(Text.colored("&fNot starting full, but still starting..")) }

        } else if (playersSize < getMinPlayers() && (state == ArenaStatesEnums.STARTING || state == ArenaStatesEnums.STARTING_FULL)) {

            setState(ArenaStatesEnums.WAITING)
            getPlayers().forEach { it.sendMessage(Text.colored("&cNot enough players, countdown canceled", true)) }

            // Stopping countdown
            stopTimeout()
        } else if (state == ArenaStatesEnums.INGAME && playersSize < 2) {
            endGame()
        }
    }

    fun getMinPlayers(): Int {
        return propeties.minPlayers
    }
    fun getMaxPlayers(): Int {
        return propeties.maxPlayers
    }
    fun getSpawn(): Location {
        return propeties.spawn
    }
    fun getStartTimeout(): Int {
        return propeties.startTimeout
    }
    fun getState(): ArenaStatesEnums {
        return propeties.state
    }
    fun setState(state: ArenaStatesEnums) {
        propeties.state = state
        //ArenaStateChangedEvent(name, state).callEvent()
        // TODO
    }
    fun isReady(): Boolean {
        return propeties.isReady
    }
    fun setReady(ready: Boolean) {
        propeties.isReady = ready
    }
    fun getCreatedAt(): LocalDateTime {
        return propeties.createdAt
    }
    fun setPropeties(propeties: ArenaPropeties) {
        this.propeties = propeties
    }
    fun setMinPlayers(minPlayers: Int, saveJson: Boolean = false) {
        propeties.minPlayers = minPlayers

        if (saveJson) plugin.configManager.updateArena(name, "minPlayers", minPlayers)
    }
    fun setMaxPlayers(maxPlayers: Int, saveJson: Boolean = false) {
        propeties.maxPlayers = maxPlayers

        if (saveJson) plugin.configManager.updateArena(name, "maxPlayers", maxPlayers)
    }
    fun setStartTimeout(startTimeout: Int) {
        propeties.startTimeout = startTimeout
    }
    fun setSpawn(spawn: Location) {
        propeties.spawn = spawn
    }
    fun setPlayers(players: MutableSet<Player>) {
        propeties.players = players
    }
    fun setCreatedAt(createdAt: LocalDateTime) {
        propeties.createdAt = createdAt
    }
    fun getMaintenanceStatus(): Boolean {
        return propeties.maintenance
    }
    fun setMaintenanceStatus(status: Boolean) {
        propeties.maintenance = status
    }


}
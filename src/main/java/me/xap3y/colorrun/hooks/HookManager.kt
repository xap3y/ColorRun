package me.xap3y.colorrun.hooks

import me.xap3y.colorrun.Main
import me.xap3y.colorrun.api.text.Text

class HookManager(private val plugin: Main) {

    fun registerHooks() {
        // PlaceholderAPI
        if (plugin.server.pluginManager.getPlugin("PlaceholderAPI") != null) {
            ColorRunPlaceholderAPI(plugin).register()
            Text.console("PlaceholderAPI found, you can now use placeholders")
        }
        else Text.console("PlaceholderAPI not found")
    }
}
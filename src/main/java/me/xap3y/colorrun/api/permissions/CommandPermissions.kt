package me.xap3y.colorrun.api.permissions

class CommandPermissions {

    companion object {

        const val rootPermission: String = "colorrun."


        const val helpCommand: String = rootPermission + "help"
        const val aboutCommand: String = rootPermission + "about"
        const val debugCommand: String = rootPermission + "debug"

        const val arenasCommands: String = rootPermission + "arenas"
    }
}
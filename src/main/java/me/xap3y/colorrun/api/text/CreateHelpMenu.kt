package me.xap3y.colorrun.api.text

class CreateHelpMenu {
    private val values = mutableListOf<String>(
        "&8=========[&6&lColor Run&8]========="
    )

    fun addCommand(key: String, description: String): CreateHelpMenu {
        values.add("&6/cr $key &7- &b$description")
        return this
    }

    fun build(): String {
        return values.joinToString("\n")
    }
}
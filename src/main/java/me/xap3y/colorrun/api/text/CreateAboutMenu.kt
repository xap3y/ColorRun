package me.xap3y.colorrun.api.text

class CreateAboutMenu {
    private val values = mutableListOf<String>(
        "&8=========[&6&lColor Run&8]========="
    )

    fun addField(key: String, description: String): CreateAboutMenu {
        values.add("&6$key &7- &b$description")
        return this
    }

    fun setVersion(ver: String): CreateAboutMenu {
        values.add("&9version &7- &8v&b$ver")
        return this
    }

    fun addDevelopers(developersList: Array<String>): CreateAboutMenu {
        values.add("&6DEVS &7- &b${developersList.joinToString(",")}")
        return this
    }


    fun build(): String {
        return values.joinToString("\n")
    }
}